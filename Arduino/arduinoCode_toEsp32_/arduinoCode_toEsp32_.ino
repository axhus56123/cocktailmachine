#define RXp2 17
#define TXp2 16
#include <HardwareSerial.h>

int val1;
int val2;
int val3;

void setup() {
  // put your setup code here, to run once:
  Serial.begin(9600);
  Serial2.begin(115200);
}
void loop() {
  char data[100]={0};
  size_t setRxBufferSize(size_t);

   /*
   if(Serial2.read()){
     Serial2.readBytesUntil('\n',data,100);
     Serial.print("Received: ");
     Serial.println(data);
   }*/

  while(!Serial2.available()){}

  if (Serial2.available())
  {
    val1=Serial2.read();
  }

   while(!Serial2.available()){}

   if (Serial2.available())
   {
     val2=Serial2.read();
   }

  while(!Serial2.available()){}

  if (Serial2.available())
  {
    val3=Serial2.read();
  }

  Serial.println(val1);
  Serial.println(val2);
  Serial.println(val3);
}
