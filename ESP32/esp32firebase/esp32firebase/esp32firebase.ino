#include <Arduino.h>
#if defined(ESP32)
  #include <WiFi.h>
#elif defined(ESP8266)
  #include <ESP8266WiFi.h>
#endif
#include <Firebase_ESP_Client.h>
#include <ArduinoJson.h>


//Provide the token generation process info.
#include "addons/TokenHelper.h"
//Provide the RTDB payload printing info and other helper functions.
#include "addons/RTDBHelper.h"

// Insert your network credentials
#define WIFI_SSID "liu home"
#define WIFI_PASSWORD "2doixxxi"

// Insert Firebase project API Key
#define API_KEY "AIzaSyDluJtssy73myPtnlukEZkBJhqPNKdBEhw"

// Insert RTDB URLefine the RTDB URL */
#define DATABASE_URL "https://test-b0b69-default-rtdb.firebaseio.com/" 

#define RXp2 16
#define TXp2 17

//Define Firebase Data object
FirebaseData fbdo;

FirebaseAuth auth;
FirebaseConfig config;

unsigned long sendDataPrevMillis = 0;
int startValue, endValue;
float floatValue;
bool signupOK = false;
String stringValue;

void setup() {
  //Serial.begin(9600);
  Serial.begin(115200, SERIAL_8N1, RXp2, TXp2);
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  //Serial.print("Connecting to Wi-Fi");
  while (WiFi.status() != WL_CONNECTED) {
    //Serial.print(".");
    delay(300);
  }
  //Serial.println();
  //Serial.print("Connected with IP: ");
  //Serial.println(WiFi.localIP());
  //Serial.println();

  /* Assign the api key (required) */
  config.api_key = API_KEY;

  /* Assign the RTDB URL (required) */
  config.database_url = DATABASE_URL;

  /* Sign up */
  if (Firebase.signUp(&config, &auth, "", "")) {
    //Serial.println("ok");
    signupOK = true;
  }
  else {
    //Serial.printf("%s\n", config.signer.signupError.message.c_str());
  }

  /* Assign the callback function for the long running token generation task */
  config.token_status_callback = tokenStatusCallback; //see addons/TokenHelper.h

  Firebase.begin(&config, &auth);
  Firebase.reconnectWiFi(true);
}

void loop() {
  char retureDate[250]={0};
  if (Firebase.ready() && signupOK && (millis() - sendDataPrevMillis > 15000 || sendDataPrevMillis == 0)) {
    sendDataPrevMillis = millis();
    
    if (Firebase.RTDB.getInt(&fbdo, "/count/start")) {
      if (fbdo.dataType() == "int") {
        startValue = fbdo.intData();
        //Serial.print("StartValue: ");
        //Serial.println(startValue);
      }
    }
    else {
      //Serial.println(fbdo.errorReason());
    }
    
    if (Firebase.RTDB.getInt(&fbdo, "/count/end")) {
      if (fbdo.dataType() == "int") {
        endValue = fbdo.intData();
        //Serial.print("EndValue: ");
        //Serial.println(endValue);
      }
    }
    else {
      //Serial.println(fbdo.errorReason());
    }
    if(startValue <= endValue){
      for(startValue; startValue <= endValue; startValue++){
        String nowValue = "schedule_" + String(startValue);
        //Serial.print("NowValue: ");
        //Serial.println(nowValue);
        if (Firebase.RTDB.getJSON(&fbdo, "/test/" + nowValue)) {
          if (fbdo.dataType() == "json") {
            stringValue = fbdo.jsonString();
            //Serial.println(stringValue);
            const size_t capacity = JSON_OBJECT_SIZE(6) + 60;
            DynamicJsonDocument doc(capacity);
            deserializeJson(doc, stringValue);
            int drink1 = doc["drink1"].as<int>();
            int drink2 = doc["drink2"].as<int>();
            int drink3 = doc["drink3"].as<int>();
//            Serial.print("drink1 = ");
              Serial.println(drink1);
              delay(100);
//            Serial.print("drink2 = ");
              Serial.println(drink2);
              delay(100);
//            Serial.print("drink3 = ");
              Serial.println(drink3);
//            Serial.print("drink1 = ");Serial.println(drink1);
//            Serial.print("drink2 = ");Serial.println(drink2);
//            Serial.print("drink3 = ");Serial.println(drink3);
              while(!Serial.available()){}
              delay(100);
//              Serial.readBytesUntil('\n',retureDate,250);
//              Serial.println("Next drink ESP32");
          }
        }
        else {
          //Serial.println("no found");
          //Serial.println(fbdo.errorReason());
        }

        if (Firebase.RTDB.getInt(&fbdo, "/count/end")) {
          if (fbdo.dataType() == "int") {
            endValue = fbdo.intData();
          }
        }
      }
      Firebase.RTDB.setInt(&fbdo, "count/start", endValue+1);
    }
    else{
      //Serial.println("----------");
    }
  }
}
