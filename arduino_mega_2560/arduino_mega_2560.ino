#include <Wire.h>
#include <LiquidCrystal_I2C.h>
#include "HX711.h"
LiquidCrystal_I2C lcd(0x27, 16, 2);
const int LOADCELL_DOUT_PIN = 12;
const int LOADCELL_SCK_PIN = 13;

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

volatile boolean TurnDetected;
volatile boolean up;
bool doonce = 0;
char screen = 0;
boolean changestate = 0;
long weight;
int pump1ml = 20;
int pump2ml = 20;
int pump3ml = 20;


void isr0 ()  {
  TurnDetected = true;
  up = (digitalRead(clk) == digitalRead(dt));
}

void setup() {
  Serial.begin(9600);
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
          case 0: pump1ml = pump1ml - 10;
          break;
          case 1: pump2ml = pump2ml - 10;
          break;
          case 2: pump3ml = pump3ml - 10;
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
      lcd.print("Wait!");
      delay(2000);
      weight = scale.read();
      delay(100);
      lcd.clear();
      lcd.print("Pump 1 ON");
      
      if(pump1ml > 0){
        digitalWrite(in1, HIGH);
        while (scale.read() - weight < 3000) {}
        delay((pump1ml/10)*2800);
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
        digitalWrite(in3, HIGH);
        while (scale.read() - weight < 3000) {}
      
        delay((pump2ml/10)*2800);
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

      if(pump3ml > 0){
        digitalWrite(in5, HIGH);
        while (scale.read() - weight < 3000) {}
        delay((pump3ml/10)*2800);
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
    }
  }
}
