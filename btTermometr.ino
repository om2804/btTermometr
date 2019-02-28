#include <SoftwareSerial.h>

SoftwareSerial mySerial(2, 3);

const double opVoltage = 4.98; // опорное напряжение (напряжение питания)
const byte tmpPin = 0; // номер пина, на котором сидит термодатчик

void setup()
{
  Serial.begin(9600); 
  mySerial.begin(9600);
  pinMode(13, OUTPUT); 
}

void loop()
{  
    double vl = (analogRead(tmpPin)*opVoltage)/1024; // значение напряжения на пине
    int tempK = vl*100; // расчет температуры в кельвинах
    int tempC = tempK - 273; // перевод температуры в градусы цельсия
    byte packet[] = {0xDE, 0xAD, tempC >> 8, tempC & 0xFF }; // формируем пакет
        
    // отправляем пакет
    Serial.write(packet, 4); // на компьютер
    mySerial.write(packet, 4); // блютуз-модулю
	
    // индикация работы - помигаем светодиодом
    digitalWrite(13, HIGH);
    delay(500);
    digitalWrite(13, LOW);
    delay(1500);
}
