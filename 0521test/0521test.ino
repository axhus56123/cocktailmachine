#include <BluetoothSerial.h>

BluetoothSerial BT;

void setup() {
  Serial.begin(115200);
  BT.begin("cocktailmachine");//藍芽名稱
}

void loop() {
  //檢查序列內是否有資料
  while (Serial.available()) {
    //讀取序列資料
    String Sdata = Serial.readString();
    //傳輸給藍芽
    BT.println(Sdata);
  }


 
  //檢查藍芽內是否有資料
  if (BT.available()) {
    //讀取藍芽資料
    String BTdata = BT.readString();
    //顯示在序列視窗
    //Serial.println(BTdata);

    int sa[3], r=0, t=0;
    //資料拆分
    for (int i=0; i < BTdata.length(); i++){ 
      if(BTdata.charAt(i) == '.') { 
          sa[t] = BTdata.substring(r, i).toInt(); 
          r=(i+1); 
          t++;
      }
    }
    Serial.println(sa[0]);
    Serial.println(sa[1]);
    Serial.println(sa[2]);

    BT.println("已接收到資料");
    BT.print("飲料1: ");
    BT.print(sa[0]);
    BT.println(" ml");
    BT.print("飲料2: ");
    BT.print(sa[1]);
    BT.println(" ml");
    BT.print("飲料3: ");
    BT.print(sa[2]);
    BT.println(" ml");
  }
  delay(1);
}
