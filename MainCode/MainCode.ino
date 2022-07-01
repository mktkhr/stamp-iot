#include <WiFi.h>
#include "UNIT_ENV.h"

const char* ssid = "myssid";
const char* password = "mypassword";
struct tm timeInfo;
char timeData[20];
int measurementIntervalInMinutes = 1;

SHT3X sht30;
QMP6988 qmp6988;

void setup() {
  Serial.begin(115200);
  Wire.begin(21, 22);
  qmp6988.init();
  connectToAccessPoint(ssid, password);
  configTime(9 * 3600L, 0, "ntp.nict.jp", "time.google.com");
}

void loop() {
  getLocalTime(&timeInfo);
  if ((timeInfo.tm_min % measurementIntervalInMinutes == 0) && (timeInfo.tm_sec == 0)) {
    sprintf(timeData, "%04d/%02d/%02d %02d:%02d:%02d", timeInfo.tm_year + 1900, timeInfo.tm_mon + 1, timeInfo.tm_mday, timeInfo.tm_hour, timeInfo.tm_min, timeInfo.tm_sec);
    Serial.println(timeData);
    measureEnv3();
    delay(1000);
  }
}

void connectToAccessPoint(const char* ssid, const char* password) {
  WiFi.begin(ssid, password);
  int waitCountInSec = 0;
  while (WiFi.status() != WL_CONNECTED) {
    waitCountInSec++;
    delay(1000);
    Serial.print(".");
    if (waitCountInSec == 10) {
      Serial.println("\nWi-Fi Connection failed. Please check the ssid and password.");
      delay(3000);
      ESP.restart();
    }
  }
  Serial.println("\nWiFi connected.");
}

void measureEnv3() {
  float temperature;
  float humidity;
  float pressure = qmp6988.calcPressure();
  if (sht30.get() == 0) {
    temperature = sht30.cTemp;
    humidity = sht30.humidity;
  } else {
    temperature = 0, humidity = 0;
  }
  Serial.printf("Temperatura: %2.2f*C  Humedad: %0.2f%%  Pressure: %0.2fPa\r\n", temperature, humidity, pressure);
}
