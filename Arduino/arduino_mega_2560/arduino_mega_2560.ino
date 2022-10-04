#include <Wire.h>
#include <LiquidCrystal_I2C.h>
#include <HardwareSerial.h>
#include "HX711.h"
LiquidCrystal_I2C lcd(0x27, 16, 2);
const int LOADCELL_DOUT_PIN = 12;
const int LOADCELL_SCK_PIN = 13;

int val1 = 0;
int val2 = 0;
int val3 = 0;

HX711 scale;
#define clk 2
#define dt 3
#define sw 4
#define in1 5
#define in2 6
#define in3 7
#define in4 8
#define in5 9
#define in6 10

#define RXp2 17
#define TXp2 16

volatile boolean TurnDetected;
volatile boolean up;
bool doonce = 0;
char screen = 0;
boolean changestate = 0;
long weight;
long pump1ml = 20;
long pump2ml = 20;
long pump3ml = 20;


void isr0 ()  {
  TurnDetected = true;
  up = (digitalRead(clk) == digitalRead(dt));
//  Serial.print("up: ");
//  Serial.println(up);
}

void setup() {
  Serial.begin(9600);
  Serial2.begin(115200);
  scale.begin(LOADCELL_DOUT_PIN, LOADCELL_SCK_PIN);
  lcd.begin(16, 2);
  lcd.init();
  lcd.backlight();
  pinMode(sw, INPUT_PULLUP);
  pinMode(clk, INPUT);
  pinMode(dt, INPUT);
  pinMode(in1, OUTPUT);
  pinMode(in2, OUTPUT);
  pinMode(in3, OUTPUT);
  pinMode(in4, OUTPUT);
  pinMode(in5, OUTPUT);
  pinMode(in6, OUTPUT);
  digitalWrite(in1, LOW);
  digitalWrite(in2, LOW);
  digitalWrite(in3, LOW);
  digitalWrite(in4, LOW);
  digitalWrite(in5, LOW);
  digitalWrite(in6, LOW);
  attachInterrupt (0, isr0, RISING);
}

void loop() {
//  Serial.print("changestate: ");
//  Serial.println(changestate);
  char data1[250]={0};
  char data2[250]={0};
  char data3[250]={0};
  int espStatus = 0;
  size_t setRxBufferSize(size_t);

  if(Serial2.available()){
    Serial2.readBytesUntil('\n',data1,250);
//    Serial.print("drink1: ");
//    Serial.println(data1);
    val1 = atoi(data1);
//    Serial.println(pump1ml);
    
    Serial2.readBytesUntil('\n',data2,250);
//    Serial.print("drink2: ");
//    Serial.println(data2);
    val2 = atoi(data2);
//    Serial.println(pump2ml);
    
    Serial2.readBytesUntil('\n',data3,250);
//    Serial.print("drink3: ");
//    Serial.println(data3);
    val3 = atoi(data3);
//    Serial.println(pump3ml);
    
    
    delay(100);

    if(val1 != 0 || val2 != 0 || val3 != 0){
      pump1ml = val1;
      pump2ml = val2;
      pump3ml = val3;
      
      espStatus = 1;
      goto espData;
    }
  }
  
  if (TurnDetected) {
    delay(200);
    doonce = 0;
    if (changestate == 0) {
      if (up) {
        screen++;
        if (screen > 3) {
          screen = 3;
        }
      }
      else {
        screen = screen - 1;
        if (screen < 0) {
          screen = 0;
        }
      }
    }
    else {
      if (up) {
        switch (screen) {
          case 0: pump1ml = pump1ml + 10;
          break;
          case 1: pump2ml = pump2ml + 10;
          break;
          case 2: pump3ml = pump3ml + 10;
          break;
        }
      }
      else {
        switch (screen) {
          case 0:
            pump1ml = pump1ml - 10;
            if(pump1ml <= 0)
              pump1ml = 0;
            break;
          case 1:
            pump2ml = pump2ml - 10;
            if(pump2ml <= 0)
              pump2ml = 0;
          break;
          case 2:
            pump3ml = pump3ml - 10;
            if(pump3ml <= 0)
              pump3ml = 0;
          break;
        }
      }
    }
    TurnDetected = false;
  }


  if (digitalRead(sw) == LOW) {
    delay(200);
    changestate = !changestate;
    doonce = 0;
  }

  if (screen == 0 && doonce == 0) {
    lcd.clear();
    lcd.print("pump 1");
    lcd.setCursor(0, 1);
    lcd.print(pump1ml);
    lcd.setCursor(3, 1);
    lcd.print("ml");
    if (changestate == 0) {
      lcd.setCursor(9, 0 );
      lcd.print("Change?");
    }
    doonce = 1;
  }

  if (screen == 1 && doonce == 0) {
    lcd.clear();
    lcd.print("pump 2");
    lcd.setCursor(0, 1);
    lcd.print(pump2ml);
    lcd.setCursor(3, 1);
    lcd.print("ml");
    if (changestate == 0) {
      lcd.setCursor(9, 0 );
      lcd.print("Change?");
    }
    doonce = 1;
  }

  if (screen == 2 && doonce == 0) {
    lcd.clear();
    lcd.print("pump 3");
    lcd.setCursor(0, 1);
    lcd.print(pump3ml);
    lcd.setCursor(3, 1);
    lcd.print("ml");
    if (changestate == 0) {
      lcd.setCursor(9, 0 );
      lcd.print("Change?");
    }
    doonce = 1;
  }

  if (screen == 3 && doonce == 0) {
    lcd.clear();
    if (changestate == 0) {
      lcd.print("Start?");
      doonce = 1;
    }
    else {
      espData:
      lcd.clear();
      lcd.print("Wait!");
      delay(2000);
      weight = scale.read();
      delay(100);
      lcd.clear();
      lcd.print("Pump 1 ON");
//      Serial.print("weight1: ");
//      Serial.println(weight);
//      Serial.print("pump1ml: ");
//      Serial.println(pump1ml);
      if(pump1ml > 0){
        digitalWrite(in1, HIGH);
        long x = pump1ml*1030L;
//        Serial.print("x: ");
//        Serial.println(x);
        while (scale.read() - weight <= x) {
//          Serial.println("1111111111111");
//          Serial.println(scale.read());  
        }
//        delay((pump1ml/10)*1500);
        digitalWrite(in1, LOW);
      }
      else{
        delay(500);
      }
      
      lcd.clear();
      lcd.print(pump1ml);
      lcd.print("ml");
      
      delay(2000);
      weight = scale.read();
      delay(100);
      lcd.clear();
      lcd.print("Pump 2 ON");
//      Serial.print("weight2: ");
//      Serial.println(weight);
//      Serial.print("pump2ml: ");
//      Serial.println(pump2ml);
      if(pump2ml > 0){
        digitalWrite(in3, HIGH);
        long y = pump2ml*1030L;
//        Serial.print("y: ");
//        Serial.println(y);
        while (scale.read() - weight <= y) {
//          Serial.println("222222222222");
//          Serial.println(scale.read());  
        }
      
//        delay((pump2ml/10)*1500);
        digitalWrite(in3, LOW);
      }
      else{
        delay(500);
      }
      
      lcd.clear();
      lcd.print(pump2ml);
      lcd.print("ml");
      
      delay(2000);
      weight = scale.read();
      delay(100);
      lcd.clear();
      lcd.print("Pump 3 ON");
//      Serial.print("weight3: ");
//      Serial.println(weight);
//      Serial.print("pump3ml: ");
//      Serial.println(pump3ml);
      if(pump3ml > 0){
        digitalWrite(in5, HIGH);
        long z = pump3ml*1030L;
//        Serial.print("z: ");
//        Serial.println(z);
        while (scale.read() - weight <= z) {
//          Serial.println("33333333333");
//          Serial.println(scale.read());
        }
//        delay((pump3ml/10)*1500);
        digitalWrite(in5, LOW);
      }
      else{
        delay(500);
      }

      lcd.clear();
      lcd.print(pump3ml);
      lcd.print("ml");
      
      delay(2000);
      changestate = 0;
      screen = 0;

      if(espStatus == 1){
//        Serial.println("Next drink 2560");
        espStatus = 0;
        
        lcd.setCursor(0, 0);
        lcd.print("Push for ");
        lcd.setCursor(0, 1);
        lcd.print("     next drink!");
        while(changestate != 1){
          if (digitalRead(sw) == LOW) {
            delay(200);
            changestate = !changestate;
            doonce = 0;
          }  
        }
        int hhh = 234;
        Serial2.println(hhh);
        lcd.clear();
        changestate = 0;
      }
    }
  }
}
