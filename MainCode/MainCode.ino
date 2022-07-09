#include <WiFi.h>
#include <SDI12.h>
#include "UNIT_ENV.h"
#include "Adafruit_SGP30.h"

const char* ssid = "myssid"; //myssid
const char* password = "mypassword"; //mypassword
int measurementIntervalInMinutes = 1;
#define DATA_PIN 19

struct tm timeInfo;
char timeData[20];
SDI12 mySDI12(DATA_PIN);
SHT3X sht30;
QMP6988 qmp6988;
Adafruit_SGP30 sgp;

float temperature, humidity;

void setup() {
  Serial.begin(115200);
  Wire.begin(21, 22);
  qmp6988.init();

  if (! sgp.begin()){
    Serial.println("Sensor not found :(");
    while (1);
  }
  Serial.print("Found SGP30 serial #");
  Serial.println(sgp.serialnumber[0], HEX);
  
  connectToAccessPoint(ssid, password);
  configTime(9 * 3600L, 0, "ntp.nict.jp", "time.google.com");
}

void loop() {
  getLocalTime(&timeInfo);
  if ((timeInfo.tm_min % measurementIntervalInMinutes == 0) && (timeInfo.tm_sec == 0)) {
    sprintf(timeData, "%04d/%02d/%02d %02d:%02d:%02d", timeInfo.tm_year + 1900, timeInfo.tm_mon + 1, timeInfo.tm_mday, timeInfo.tm_hour, timeInfo.tm_min, timeInfo.tm_sec);
    Serial.println(timeData);
    measureEnv3();
    measureSgp30();
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
  float pressure = qmp6988.calcPressure();
  if (sht30.get() == 0) {
    temperature = sht30.cTemp;
    humidity = sht30.humidity;
  } else {
    temperature = 0, humidity = 0;
    Serial.println("Measurement failed (SGP30)");
    return;
  }
  Serial.printf("Temperature: %2.2f*C  Humiedity: %0.2f%%  Pressure: %0.2fPa\r\n", temperature, humidity, pressure);
}

uint32_t getAbsoluteHumidity(float temperature, float humidity) {
    const float absoluteHumidity = 216.7f * ((humidity / 100.0f) * 6.112f * exp((17.62f * temperature) / (243.12f + temperature)) / (273.15f + temperature)); // [g/m^3]
    const uint32_t absoluteHumidityScaled = static_cast<uint32_t>(1000.0f * absoluteHumidity); // [mg/m^3]
    return absoluteHumidityScaled;
}

void measureSgp30() {
  if(temperature != 0 || humidity != 0){
    sgp.setHumidity(getAbsoluteHumidity(temperature, humidity));
  }
  
  if (! sgp.IAQmeasure()) {
    Serial.println("Measurement failed (SGP30)");
    return;
  }
  Serial.print("TVOC: "); Serial.print(sgp.TVOC); Serial.print("ppb ");
  Serial.print("eCO2: "); Serial.print(sgp.eCO2); Serial.println("ppm");

  if (! sgp.IAQmeasureRaw()) {
    Serial.println("Raw Measurement failed");
    return;
  }
  Serial.print("Raw H2: "); Serial.print(sgp.rawH2);
  Serial.print(" Raw Ethanol: "); Serial.println(sgp.rawEthanol);
}

void split(String data, String *dataArray) {
  int index = 0;
  int arraySize = (sizeof(data) / sizeof((data)[0]));
  int datalength = data.length();
  for (int i = 0; i < datalength; i++) {
    char dataChar = data.charAt(i);
    if ( dataChar == '+' ) {
      index++;
    } else if (dataChar == '-') {
      index++;
      dataArray[index] += dataChar;
    } else {
      dataArray[index] += dataChar;
    }
  }
  return;
}

String sendCommandAndCollectResponse(String myCommand, int sendInterval, int requestNumber){
  String response = "";
  for (int j = 0; j < requestNumber; j++) {
    mySDI12.sendCommand(myCommand);
    delay(sendInterval);

    if (mySDI12.available()) {
      Serial.println("Response detected (sendInterval: " + String(sendInterval) + ")");
      while (mySDI12.available()) {
        char responseChar = mySDI12.read();
        if ((responseChar != '\n') && (responseChar != '\r')) {
          response += responseChar;
          delay(10);
        }
      }
      mySDI12.clearBuffer();
      Serial.print("Response: " + response);
      return response;
      break;
    } else {
      mySDI12.clearBuffer();
      sendInterval = sendInterval + 10;
    }
    Serial.print(".");
  }
  Serial.println("Failed to connect to sensor");
  return response = "\0";
}

boolean checkActive(byte i) {
  Serial.print("Checking address " + String(i) + "...");
  String response = "";
  String myCommand = String(i) + "!";
  int sendInterval = 50;
  int requestNumber = 5;

  response = sendCommandAndCollectResponse(myCommand, sendInterval, requestNumber);

  if(response != "\0"){
    return true;
  }else{
    return false;
  }
}
