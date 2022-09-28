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
  char data1[250]={0};
  char data2[250]={0};
  char data3[250]={0};
  size_t setRxBufferSize(size_t);

  while(!Serial2.available()){}
  Serial2.readBytesUntil('\n',data1,250);
  Serial.print("drink1: ");
  Serial.println(data1);

  Serial2.readBytesUntil('\n',data2,250);
  Serial.print("drink2: ");
  Serial.println(data2);

  Serial2.readBytesUntil('\n',data3,250);
  Serial.print("drink3: ");
  Serial.println(data3);
  delay(1000);
  Serial.println("Next drink 2560");
  delay(100);
  Serial2.println(1);
}
