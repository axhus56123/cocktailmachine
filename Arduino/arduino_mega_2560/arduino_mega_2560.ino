#include <Wire.h>
#include <LiquidCrystal_I2C.h>
#include <HardwareSerial.h>
#include "HX711.h"
LiquidCrystal_I2C lcd(0x27, 16, 2);
const int LOADCELL_DOUT_PIN = 12;
const int LOADCELL_SCK_PIN = 13;

HX711 scale;
#define clk 2
#define dt 3
#define sw 4
#define in1 6
#define in2 10
#define in3 9
#define in4 5
#define in5 8
#define in6 7

#define RXp2 17
#define TXp2 16

volatile boolean TurnDetected;
volatile boolean up;
bool doonce = 0;
char screen = 0;
boolean changestate = 0;
long weight;

int pump1ml = 20;
int pump2ml = 20;
int pump3ml = 20;
int pump4ml = 20;
int pump5ml = 20;
int pump6ml = 20;

int val1 = 0;
int val2 = 0;
int val3 = 0;
int val4 = 0;
int val5 = 0;
int val6 = 0;

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
  char data4[250]={0};
  char data5[250]={0};
  char data6[250]={0};
  int espStatus = 0;
  size_t setRxBufferSize(size_t);

  if(Serial2.available()){
    Serial2.readBytesUntil('\n',data1,250);
    val1 = atoi(data1);
    
    Serial2.readBytesUntil('\n',data2,250);
    val2 = atoi(data2);
    
    Serial2.readBytesUntil('\n',data3,250);
    val3 = atoi(data3);
    
    Serial2.readBytesUntil('\n',data4,250);
    val4 = atoi(data4);

    Serial2.readBytesUntil('\n',data5,250);
    val5 = atoi(data5);

    Serial2.readBytesUntil('\n',data6,250);
    val6 = atoi(data6);
    
    delay(100);

    if(val1 != 0 || val2 != 0 || val3 != 0 || val4 != 0 || val5 != 0 || val6 != 0){
      pump1ml = val1;
      pump2ml = val2;
      pump3ml = val3;
      pump4ml = val4;
      pump5ml = val5;
      pump6ml = val6;
      
      espStatus = 1;
      goto espData;
    }
  }
  
  if (TurnDetected) {
    delay(20);
    doonce = 0;
    if (changestate == 0) {
      if (up) {
        screen++;
        if (screen > 6) {
          screen = 6;
        }
      }
      else {
        screen = screen - 1;
        if (screen < -1) {
          screen = -1;
        }
      }
    }
    else {
      if (up) {
        switch (screen) {
          case 0: 
            pump1ml = pump1ml + 10;
            if(pump1ml >= 250)
              pump1ml = 250;
            break;
          case 1: 
            pump2ml = pump2ml + 10;
            if(pump2ml >= 250)
              pump2ml = 250;
            break;
          case 2: 
            pump3ml = pump3ml + 10;
            if(pump3ml >= 250)
              pump3ml = 250;
            break;
          case 3: 
            pump4ml = pump4ml + 10;
            if(pump4ml >= 250)
              pump4ml = 250;
            break;
          case 4: 
            pump5ml = pump5ml + 10;
            if(pump5ml >= 250)
              pump5ml = 250;
            break;
          case 5: 
            pump6ml = pump6ml + 10;
            if(pump6ml >= 250)
              pump6ml = 250;
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
          case 3:
            pump4ml = pump4ml - 10;
            if(pump4ml <= 0)
              pump4ml = 0;
            break;
          case 4:
            pump5ml = pump5ml - 10;
            if(pump5ml <= 0)
              pump5ml = 0;
            break;
          case 5:
            pump6ml = pump6ml - 10;
            if(pump6ml <= 0)
              pump6ml = 0;
            break;
        }
      }
    }
    TurnDetected = false;
  }
  
  if (digitalRead(sw) == LOW) {
    delay(20);
    changestate = !changestate;
    doonce = 0;
  }

  if (screen == -1 && doonce == 0){
    if (changestate == 0) {
      lcd.clear();
      lcd.print("WASH MODE");
      lcd.setCursor(10, 1);
      lcd.print("START?");
      doonce = 1;
    }
    else {
      int count = 20;
      lcd.clear();
      lcd.print("PROCESSING!");
      digitalWrite(in1, HIGH);
      digitalWrite(in2, HIGH);
      digitalWrite(in3, HIGH);

      while(count >= 11){
        lcd.setCursor(3, 1);
        lcd.print("REMAIN");
        lcd.setCursor(10, 1);
        lcd.print(count);
        lcd.setCursor(13, 1);
        lcd.print("sec");
        count--;
        delay(1000);
      }
      
      digitalWrite(in1, LOW);
      digitalWrite(in2, LOW);
      digitalWrite(in3, LOW);
      digitalWrite(in4, HIGH);
      digitalWrite(in5, HIGH);
      digitalWrite(in6, HIGH);
      
      while(count >= 1){
        lcd.clear();
        lcd.print("PROCESSING!");
        lcd.setCursor(3, 1);
        lcd.print("REMAIN");
        
        if(count == 10)
          lcd.setCursor(10, 1);
        else
          lcd.setCursor(11, 1);
          
        lcd.print(count);
        lcd.setCursor(13, 1);
        lcd.print("sec");
        count--;
        delay(1000);
      }
      digitalWrite(in4, LOW);
      digitalWrite(in5, LOW);
      digitalWrite(in6, LOW);

      delay(200);
      changestate = 0;
      screen = 0;
    }
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
    lcd.print("pump 4");
    lcd.setCursor(0, 1);
    lcd.print(pump4ml);
    lcd.setCursor(3, 1);
    lcd.print("ml");
    if (changestate == 0) {
      lcd.setCursor(9, 0 );
      lcd.print("Change?");
    }
    doonce = 1;
  }

  if (screen == 4 && doonce == 0) {
    lcd.clear();
    lcd.print("pump 5");
    lcd.setCursor(0, 1);
    lcd.print(pump5ml);
    lcd.setCursor(3, 1);
    lcd.print("ml");
    if (changestate == 0) {
      lcd.setCursor(9, 0 );
      lcd.print("Change?");
    }
    doonce = 1;
  }

  if (screen == 5 && doonce == 0) {
    lcd.clear();
    lcd.print("pump 6");
    lcd.setCursor(0, 1);
    lcd.print(pump6ml);
    lcd.setCursor(3, 1);
    lcd.print("ml");
    if (changestate == 0) {
      lcd.setCursor(9, 0 );
      lcd.print("Change?");
    }
    doonce = 1;
  }

  if (screen == 6 && doonce == 0) {
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
      if(pump1ml > 0){
        digitalWrite(in1, HIGH);
        long x = pump1ml*1030L;
        while (scale.read() - weight <= x) {}

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
      if(pump2ml > 0){
        digitalWrite(in2, HIGH);
        long y = pump2ml*1030L;
        while (scale.read() - weight <= y) {}
        digitalWrite(in2, LOW);
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
      if(pump3ml > 0){
        digitalWrite(in3, HIGH);
        long z = pump3ml*1030L;
        while (scale.read() - weight <= z) {}
        digitalWrite(in3, LOW);
      }
      else{
        delay(500);
      }

      lcd.clear();
      lcd.print(pump3ml);
      lcd.print("ml");

      delay(2000);
      weight = scale.read();
      delay(100);
      lcd.clear();
      lcd.print("Pump 4 ON");
      if(pump4ml > 0){
        digitalWrite(in4, HIGH);
        long z = pump4ml*1030L;
        while (scale.read() - weight <= z) {}
        digitalWrite(in4, LOW);
      }
      else{
        delay(500);
      }

      lcd.clear();
      lcd.print(pump4ml);
      lcd.print("ml");

      delay(2000);
      weight = scale.read();
      delay(100);
      lcd.clear();
      lcd.print("Pump 5 ON");
      if(pump5ml > 0){
        digitalWrite(in5, HIGH);
        long z = pump5ml*1030L;
        while (scale.read() - weight <= z) {}
        digitalWrite(in5, LOW);
      }
      else{
        delay(500);
      }

      lcd.clear();
      lcd.print(pump5ml);
      lcd.print("ml");

      delay(2000);
      weight = scale.read();
      delay(100);
      lcd.clear();
      lcd.print("Pump 6 ON");
      if(pump6ml > 0){
        digitalWrite(in6, HIGH);
        long z = pump6ml*1030L;
        while (scale.read() - weight <= z) {}
        digitalWrite(in6, LOW);
      }
      else{
        delay(500);
      }

      lcd.clear();
      lcd.print(pump6ml);
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
        int response = 234;
        Serial2.println(response);
        lcd.clear();
        changestate = 0;
      }
    }
  }
  
}
