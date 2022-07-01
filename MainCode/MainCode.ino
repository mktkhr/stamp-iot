#include <WiFi.h>

const char* ssid = "myssid";
const char* password = "mypassword";
struct tm timeInfo;
char timeData[20];

void setup() {
  Serial.begin(115200);
  connectToAccessPoint(ssid, password);
  configTime(9 * 3600L, 0, "ntp.nict.jp", "time.google.com");
}

void loop() {
  getLocalTime(&timeInfo);
  sprintf(timeData, "%04d/%02d/%02d %02d:%02d:%02d", timeInfo.tm_year + 1900, timeInfo.tm_mon + 1, timeInfo.tm_mday, timeInfo.tm_hour, timeInfo.tm_min, timeInfo.tm_sec);
  Serial.println(timeData);
  delay(1000);
}

void connectToAccessPoint(const char* ssid, const char* password){
  WiFi.begin(ssid, password);
  int waitCountInSec = 0;
  while(WiFi.status() != WL_CONNECTED){
    waitCountInSec++;
    delay(1000);
    Serial.print(".");
    if(waitCountInSec == 10){
      Serial.println("\nWi-Fi Connection failed. Please check the ssid and password.");
      delay(3000);
      ESP.restart();
    }
  }
  Serial.println("\nWiFi connected.");
}
