#include <WiFi.h>
#include <SDI12.h>
#include "UNIT_ENV.h"
#include "Adafruit_SGP30.h"
#include "SD.h"
#include "SPI.h"
#include "FS.h"

const char* ssid = "TP-Link_4454"; //myssid
const char* password = "59159919"; //mypassword
int measurementIntervalInMinutes = 1;
#define DATA_PIN 19
int mySensorAddress[] = {0, 3};
String serialNumber = "sampleNumber";
#define SD_PIN 0
bool sdFlag = false;
#define ANALOG_PIN 33

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
  mySDI12.begin();
  
  for (int i = 0; i < 3; i++) {
    if (SD.begin(SD_PIN)) {
      sdFlag = true;
      break;
    }
  }

  if(sdFlag == true){
    Serial.println("SD card Connection done");
    File file = SD.open("/log.csv");
    if (!file) {
      String first_row = "Timestamp,Serial number,# of SDI-12 sensor,Address,Volumetric water content,Soil temperature,Bulk relative permittivity,Soil bulk electric conductivity(TDT),Soil bulk electric conductivity(TDR),Soil pore water electric coductivity,Gravitational accelaration(x-axis),Gravitational accelaration(y-axis),Gravitational accelaration(z-axis),Address,Volumetric water content,Soil temperature,Bulk relative permittivity,Soil bulk electric conductivity(TDT),Soil bulk electric conductivity(TDR),Soil pore water coductivity,Gravitational accelaration(x-axis),Gravitational accelaration(y-axis),Gravitational accelaration(z-axis),Address,Volumetric water content,Soil temperature,Bulk relative permittivity,Soil bulk electric conductivity(TDT),Soil bulk electric conductivity(TDR),Soil pore water coductivity,Gravitational accelaration(x-axis),Gravitational accelaration(y-axis),Gravitational accelaration(z-axis),Address,Volumetric water content,Soil temperature,Bulk relative permittivity,Soil bulk electric conductivity(TDT),Soil bulk electric conductivity(TDR),Soil pore water coductivity,Gravitational accelaration(x-axis),Gravitational accelaration(y-axis),Gravitational accelaration(z-axis),Atmospheric pressure,Temperature,Humidity,CO2 concentration,Total volatile organic compounds,Analog value,Voltage\n";
      String second_row = "(YYYY/MM/DD hh:mm:ss),(-),(-),(-),(%),(C),(-),(dS/m),(uS/cm),(uS/cm),(G),(G),(G),(-),(%),(C),(-),(dS/m),(uS/cm),(uS/cm),(G),(G),(G),(-),(%),(C),(-),(dS/m),(uS/cm),(μS/cm),(G),(G),(G),(-),(%),(C),(-),(dS/m),(uS/cm),(uS/cm),(G),(G),(G),(hPa),(C),(%),(ppm),(ppb),(-),(V)\n";
      appendFile("/log.csv", first_row);
      appendFile("/log.csv", second_row);
    }
  }else{
    Serial.println("Card Mount Failed");
  }

  if (!sgp.begin()) {
    Serial.println("Sensor not found");
    while (1);
  }else{
    Serial.print("Found SGP30 serial #");
    Serial.println(sgp.serialnumber[0], HEX);
  }

  pinMode(ANALOG_PIN, INPUT);

  connectToAccessPoint(ssid, password);
  configTime(9 * 3600L, 0, "ntp.nict.jp", "time.google.com");
}

void loop() {
  getLocalTime(&timeInfo);
  if ((timeInfo.tm_min % measurementIntervalInMinutes == 0) && (timeInfo.tm_sec == 0)) {
    String sdSaveData = "";
    sprintf(timeData, "%04d/%02d/%02d %02d:%02d:%02d", timeInfo.tm_year + 1900, timeInfo.tm_mon + 1, timeInfo.tm_mday, timeInfo.tm_hour, timeInfo.tm_min, timeInfo.tm_sec);
    Serial.println(timeData);
    String env3Result = measureEnv3();
    String sgp30Result = measureSgp30();
    String sdiResult = measureSdi12(mySensorAddress);
    String analogResult = readAnalogValue();
    sdSaveData += String(timeData) + "," + serialNumber + "," + sdiResult + env3Result + sgp30Result + analogResult + "blankVoltageData\n";
    appendFile("/log.csv", sdSaveData);
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

String measureEnv3() {
  float pressure = qmp6988.calcPressure()/100;
  if (sht30.get() == 0) {
    temperature = sht30.cTemp;
    humidity = sht30.humidity;
  } else {
    temperature = 0, humidity = 0;
    Serial.println("Measurement failed (SGP30)");
    return ",,";
  }
  Serial.printf("Temperature: %2.2f*C  Humiedity: %0.2f%%  Pressure: %0.2fhPa\r\n", temperature, humidity, pressure);
  return String(pressure) + "," + String(temperature) + "," + String(humidity) + ",";
}

uint32_t getAbsoluteHumidity(float temperature, float humidity) {
  const float absoluteHumidity = 216.7f * ((humidity / 100.0f) * 6.112f * exp((17.62f * temperature) / (243.12f + temperature)) / (273.15f + temperature)); // [g/m^3]
  const uint32_t absoluteHumidityScaled = static_cast<uint32_t>(1000.0f * absoluteHumidity); // [mg/m^3]
  return absoluteHumidityScaled;
}

String measureSgp30() {
  if (temperature != 0 || humidity != 0) {
    sgp.setHumidity(getAbsoluteHumidity(temperature, humidity));
  }

  if (! sgp.IAQmeasure()) {
    Serial.println("Measurement failed (SGP30)");
    return ",,";
  }
  Serial.print("TVOC: "); Serial.print(sgp.TVOC); Serial.print("ppb ");
  Serial.print("eCO2: "); Serial.print(sgp.eCO2); Serial.println("ppm");

  if (! sgp.IAQmeasureRaw()) {
    Serial.println("Raw Measurement failed");
    return ",,";
  }
  Serial.print("Raw H2: "); Serial.print(sgp.rawH2);
  Serial.print(" Raw Ethanol: "); Serial.println(sgp.rawEthanol);
  return String(sgp.eCO2) + "," + String(sgp.TVOC) + ",";
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

String sendCommandAndCollectResponse(String myCommand, int sendInterval, int requestNumber) {
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
      Serial.println("Response: " + response);
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

boolean checkActiveSdi12(byte i) {
  Serial.println("Checking address " + String(i) + "...");
  String response = "";
  String myCommand = String(i) + "!";
  int sendInterval = 50;
  int requestNumber = 5;

  response = sendCommandAndCollectResponse(myCommand, sendInterval, requestNumber);

  if (response != "\0") {
    return true;
  } else {
    return false;
  }
}

String measureSdi12(int *sensorAddress) {
  String totalResponse = "";
  int sensorNumber = sizeof(sensorAddress) / sizeof(int);
  totalResponse += String(sensorNumber + 1) + ",";

  for (int i = 0; i <= sensorNumber; i++) {
    Serial.println("Start measurement (Sensor address: " + String(sensorAddress[i]) + ")");
    String response = "";
    String sdiResponse[6] = {"\0"};
    int sendInterval = 50;
    int requestNumber = 5;
    String myCommand = String(sensorAddress[i]) + "C!";
    Serial.println("Command: " + myCommand);

    response = sendCommandAndCollectResponse(myCommand, sendInterval, requestNumber);

    if (response == "\0") {
      totalResponse += ",,,,,,,,,,";
      continue;
    }

    int waitTime = response.substring(3, 4).toInt() * 1000;
    delay(waitTime);

    myCommand = String(sensorAddress[i]) + "D0!";
    Serial.println("Command: " + myCommand);
    response = sendCommandAndCollectResponse(myCommand, sendInterval, requestNumber);

    if (response == "\0") {
      totalResponse += ",,,,,,,,,,";
      continue;
    }

    split(response, sdiResponse);
    Serial.println("Address: " + sdiResponse[0] + "(-), VWC: " + sdiResponse[1] + "(%), Soil temperature: " + sdiResponse[2] + "(*C), BRP: " + sdiResponse[3] + "(-), SEC: " + sdiResponse[4] + "(dS/m)");
    totalResponse += sdiResponse[0] + "," + sdiResponse[1] + "," + sdiResponse[2] + "," + sdiResponse[3] + "," + sdiResponse[4] + ",,,,,,";
  }
  
  for (int n = 0; n < (3 - sensorNumber); n++) {
    totalResponse += ",,,,,,,,,,";
  }
  return totalResponse;
}

void listDir(const char * dirname, uint8_t levels) {
  Serial.printf("Listing directory: %s\n", dirname);

  File root = SD.open(dirname);
  if (!root) {
    Serial.println("Failed to open directory");
    return;
  }
  if (!root.isDirectory()) {
    Serial.println("Not a directory");
    return;
  }

  File file = root.openNextFile();
  while (file) {
    if (file.isDirectory()) {
      Serial.print("  DIR : ");
      Serial.println(file.name());
      if (levels) {
        listDir(file.name(), levels - 1);
      }
    } else {
      Serial.print("  FILE: ");
      Serial.print(file.name());
      Serial.print("  SIZE: ");
      Serial.println(file.size());
    }
    file = root.openNextFile();
  }
}

void createDir(const char * path) {
  Serial.printf("Creating Dir: %s\n", path);
  if (SD.mkdir(path)) {
    Serial.println("Directory created");
  } else {
    Serial.println("Make directory failed");
  }
}

void removeDir(const char * path) {
  Serial.printf("Removing Directory: %s\n", path);
  if (SD.rmdir(path)) {
    Serial.println("Directory removed");
  } else {
    Serial.println("Remove directory failed");
  }
}

void readFile(const char * path) {
  Serial.printf("Reading file: %s\n", path);

  File file = SD.open(path);
  if (!file) {
    Serial.println("Failed to open file for reading");
    return;
  }

  Serial.print("Read from file: ");
  while (file.available()) {
    Serial.write(file.read());
  }
  file.close();
}

void writeFile(const char * path, String message) {
  Serial.printf("Writing file: %s\n", path);

  File file = SD.open(path, FILE_WRITE);
  if (!file) {
    Serial.println("Failed to open file for writing");
    return;
  }
  if (file.print(message)) {
    Serial.println("File written");
  } else {
    Serial.println("Write failed");
  }
  file.close();
}

void appendFile(const char * path, String message) {
  Serial.printf("Appending to file: %s\n", path);

  File file = SD.open(path, FILE_APPEND);
  if (!file) {
    Serial.println("Failed to open file for appending");
    writeFile(path, message);
    return;
  }
  if (file.print(message)) {
    Serial.println("Message appended");
  } else {
    Serial.println("Append failed");
  }
  file.close();
}

void renameFile(const char * path1, const char * path2) {
  Serial.printf("Renaming file %s to %s\n", path1, path2);
  if (SD.rename(path1, path2)) {
    Serial.println("File renamed");
  } else {
    Serial.println("Rename failed");
  }
}

String deleteFile(const char * path) {
  String response;
  Serial.printf("Deleting file: %s\n", path);
  if (SD.remove(path)) {
    Serial.println("File deleted");
    response = "正常に削除されました。";
  } else {
    Serial.println("Delete failed");
    response = "削除に失敗しました。";
  }
  return response;
}

String readSdSize() {
  int total = SD.totalBytes() / (1024 * 1024);
  int used = SD.usedBytes() / (1024 * 1024);
  String sizelist = "SDカードの最大容量: " + String(total) + " MB<br>使用済み容量: " + String(used) + " MB<br>空き容量： " + String(total - used) + " MB";
  return sizelist;
}

String readAnalogValue(){
  String response = "";
  response = analogRead(ANALOG_PIN);
  Serial.println("Analog Value: " + response);
  return response + ",";
}
