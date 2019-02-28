#include <SoftwareSerial.h>

SoftwareSerial mySerial(2, 3); // ��������� ���� rx � tx ��������������

void setup()  
{
  Serial.begin(9600);
  mySerial.begin(9600);
}

void loop()
{
  if (mySerial.available())
  {
    int c = mySerial.read(); // ������ �� software-�����
    Serial.write(c); // ����� � hardware-����
  }
  if (Serial.available())
  {
    int c = Serial.read(); // ������ �� hardware-�����
    mySerial.write(c); // ����� � software-����
  }
}
