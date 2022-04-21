#include <Wire.h>
#include <LiquidCrystal_I2C.h>
#include "HX711.h"
LiquidCrystal_I2C lcd(0x27, 2, 1, 0, 4, 5, 6, 7, 3, POSITIVE);
HX711 scale(5, 6);
#define clk 2
#define dt 3
#define sw 4
#define in1 7
volatile boolean TurnDetected;
volatile boolean up;
bool doonce = 0;
char screen = 0;
boolean pumpstate = 0;

void isr0 ()  {
  TurnDetected = true;
  up = (digitalRead(clk) == digitalRead(dt));
}

void setup() {
  lcd.begin(16, 2);
  pinMode(sw, INPUT_PULLUP);
  pinMode(clk, INPUT);
  pinMode(dt, INPUT);
  pinMode(in1, OUTPUT);
  digitalWrite(in1, LOW);
  attachInterrupt (0, isr0, RISING);

}

void loop() {
  if (TurnDetected) {
    delay(200);
    if (pumpstate == 1) {
      TurnDetected = false;
    }
    else {
      doonce = 0;
      if (up) {
        screen++;
        if (screen > 2) {
          screen = 2;
        }
      }
      else {
        screen = screen - 1;
        if (screen < 0) {
          screen = 0;
        }
      }
      TurnDetected = false;
    }
  }
}



