#include <WiFi.h>
#include <SDI12.h>
#include "UNIT_ENV.h"
#include "Adafruit_SGP30.h"
#include <M5_BH1750FVI.h>
#include "SD.h"
#include "SPI.h"
#include "FS.h"
#include "driver/adc.h"
#include "esp_adc_cal.h"

#define SD_PIN 0
#define DATA_PIN 19
#define ADC_PIN 32
#define ANALOG_PIN 33
#define ADC_CHANNEL ADC_CHANNEL_4

uint8_t sda = 21;
uint8_t scl = 22;

String serialNumber = "sampleNumber";
const char* ssid = ""; //myssid
const char* password = ""; //mypassword
const char* host = ""; //バックエンドのIPを指定
int port = 8082; //バックエンドのポートを指定
String postMeasuredDataUri = "/ems/measured-data"; //POST先のエンドポイントを指定

int measurementIntervalInMinutes = 1;

int mySensorAddress[] = {0,3};

int calculateNumber = 100;

bool sdFlag = false;

float temperature, humidity;

struct tm timeInfo;
char timeData[20];

int maxSdi12SensorNum = 4;

SDI12 mySDI12(DATA_PIN); //SDI-12センサ
SHT3X sht30; //温湿度センサ
QMP6988 qmp6988; //大気圧センサ
Adafruit_SGP30 sgp; //二酸化炭素濃度センサ
esp_adc_cal_characteristics_t adcChar; //ADCモジュール
M5_BH1750FVI bh1750; //デジタル光センサ

void setup() {
  Serial.begin(115200);
  while(!Serial){}
  
  Wire.begin(sda, scl);
  
  //光センサ
  bh1750.begin(&Wire,sda,scl);
  bh1750.setMode(CONTINUOUSLY_H_RESOLUTION_MODE);

  qmp6988.init(); //大気圧センサ
  
  mySDI12.begin(); //SDI-12センサ

  //SDカードの接続確認
  for (int i = 0; i < 3; i++) {
    if (SD.begin(SD_PIN)) {
      sdFlag = true;
      break;
    }
  }

  //SDカードに初期値を挿入
  if(sdFlag == true){
    Serial.println("SD card Connection done");
    File file = SD.open("/log.csv"); //ログファイルの有無確認
    if (!file) { //ログファイルがない場合に先頭と2行目を追加
      String first_row = "Timestamp,Serial number,# of SDI-12 sensor,Address,Volumetric water content,Soil temperature,Bulk relative permittivity,Soil bulk electric conductivity(TDT),Soil bulk electric conductivity(TDR),Soil pore water electric coductivity,Gravitational acceleration(x-axis),Gravitational accelaration(y-axis),Gravitational accelaration(z-axis),Address,Volumetric water content,Soil temperature,Bulk relative permittivity,Soil bulk electric conductivity(TDT),Soil bulk electric conductivity(TDR),Soil pore water coductivity,Gravitational acceleration(x-axis),Gravitational acceleration(y-axis),Gravitational acceleration(z-axis),Address,Volumetric water content,Soil temperature,Bulk relative permittivity,Soil bulk electric conductivity(TDT),Soil bulk electric conductivity(TDR),Soil pore water coductivity,Gravitational acceleration(x-axis),Gravitational acceleration(y-axis),Gravitational acceleration(z-axis),Address,Volumetric water content,Soil temperature,Bulk relative permittivity,Soil bulk electric conductivity(TDT),Soil bulk electric conductivity(TDR),Soil pore water coductivity,Gravitational acceleration(x-axis),Gravitational acceleration(y-axis),Gravitational acceleration(z-axis),Atmospheric pressure,Temperature,Humidity,CO2 concentration,Total volatile organic compounds,Illumination,Analog value,Voltage\n";
      String second_row = "(YYYY/MM/DD hh:mm:ss),(-),(-),(-),(%),(C),(-),(dS/m),(uS/cm),(uS/cm),(G),(G),(G),(-),(%),(C),(-),(dS/m),(uS/cm),(uS/cm),(G),(G),(G),(-),(%),(C),(-),(dS/m),(uS/cm),(μS/cm),(G),(G),(G),(-),(%),(C),(-),(dS/m),(uS/cm),(uS/cm),(G),(G),(G),(hPa),(C),(%),(ppm),(ppb),(lux),(-),(V)\n";
      appendFile("/log.csv", first_row);
      appendFile("/log.csv", second_row);
    }
  }else{
    Serial.println("Card Mount Failed");
  }

  //二酸化炭素濃度センサの接続確認
  if (!sgp.begin()) {
    Serial.println("Sensor not found");
  }else{
    Serial.print("Found SGP30 serial #");
    Serial.println(sgp.serialnumber[0], HEX);
  }

  //アナログセンサのピン設定
  pinMode(ANALOG_PIN, INPUT);

  //ADCのピン設定
  pinMode(ADC_PIN, ANALOG);
  adcInit(); //ADCの設定

  //アクセスポイントに接続
  connectToAccessPoint(ssid, password);
  
  //NTPサーバの設定
  configTime(9 * 3600L, 0, "ntp.nict.jp", "time.google.com");
}

void loop() {
  getLocalTime(&timeInfo);
  if ((timeInfo.tm_min % measurementIntervalInMinutes == 0) && (timeInfo.tm_sec == 0)) {
    sprintf(timeData, "%04d/%02d/%02d %02d:%02d:%02d", timeInfo.tm_year + 1900, timeInfo.tm_mon + 1, timeInfo.tm_mday, timeInfo.tm_hour, timeInfo.tm_min, timeInfo.tm_sec);
    Serial.println(timeData);
    String macAddress = WiFi.macAddress();

    String postString = "{\"macAddress\":\"" + WiFi.macAddress() + "\",";
    String sdSaveData = "";

    String adcResult = readAdcValue(calculateNumber);
    postString += "\"voltage\":\"" + adcResult + "\",";
  
    //SDI-12
    int sensorNum = sizeof(mySensorAddress) / sizeof(int);
    String sdiResultForSd = "";
    String sdiResultForPost = "";
    postString += "\"sdi12Param\": [";
    //測定, SDデータへの追加, POSTデータへの追加
    for(int i = 0;i < sensorNum; i++){
      String sdi12Response = measureSdi12(mySensorAddress[i]);
      sdiResultForSd += "," + convertSdi12ResultForSd(sdi12Response);
      sdiResultForPost += convertSdi12ResultForPost(sdi12Response);
      //最後の1本以外の場合はカンマを追加
      if(i != sensorNum - 1 && sdiResultForPost != ""){
        sdiResultForPost += ",";
      }
    }
    //最大本数未満の場合，SDデータに空データ示すカンマを追加する
    if(maxSdi12SensorNum > sensorNum){
      for(int i = 0;i < (maxSdi12SensorNum - sensorNum); i++){
        sdiResultForSd += ",,,,,,,,,,";
      }
    }
    
    postString += sdiResultForPost + "],";

    //環境センサ系
    postString += "\"environmentalDataParam\":[{";

    String env3Result = measureEnv3();
    postString += convertEnv3ResultForPost(env3Result);

    String sgp30Result = measureSgp30();
    postString += convertSgp30ResultForPost(sgp30Result);
    
    String illuminationResult = measureIllumination();
    //測定結果が空でない場合, POST用文字列に加える
    if(illuminationResult != ""){
      postString += "\"light\":\"" + illuminationResult + "\",";
    }

    String analogResult = readAnalogValue();
    //測定結果が空でない場合, POST用文字列に加える
    if(illuminationResult != ""){
      postString += "\"analogValue\":\"" + analogResult + "\"";
    }

    postString += "}]}";
    Serial.println(postString);

    Serial.println(sdiResultForSd);

    sdSaveData += String(timeData) + "," + macAddress + "," + sensorNum + "," + sdiResultForSd + convertEnv3ResultForSd(env3Result) + convertSgp30ResultForSd(sgp30Result) + illuminationResult + "," + analogResult + "," + adcResult + "\n";
    Serial.println(sdSaveData);
    appendFile("/log.csv", sdSaveData);
    postRequest(host, port, postMeasuredDataUri, postString);
    delay(1000);
  }
}

/**
 * @brief アクセスポイントに接続
 * 
 * @param ssid 接続先SSID
 * @param password 接続先PASS
 */
void connectToAccessPoint(const char* ssid, const char* password) {
  WiFi.begin(ssid, password);
  int waitCountInSec = 0;
  while (WiFi.status() != WL_CONNECTED) {
    waitCountInSec++;
    delay(1000);
    Serial.print(".");
    //10秒待って接続できなかった場合，再起動
    if (waitCountInSec == 10) {
      Serial.println("\nWi-Fi Connection failed. Please check the ssid and password.");
      delay(3000);
      ESP.restart();
    }
  }
  Serial.println("\nWiFi connected.");
}

/**
 * @brief ENV3センサの測定
 * 
 * @return String 測定結果
 */
String measureEnv3() {
  float pressure = qmp6988.calcPressure()/100;
  if (sht30.get() == 0) {
    temperature = sht30.cTemp;
    humidity = sht30.humidity;
  } else {
    temperature = 0, humidity = 0;
    Serial.println("Measurement failed (ENV3)");
    return "++";
  }
  Serial.printf("Temperature: %2.2f*C  Humiedity: %0.2f%%  Pressure: %0.2fhPa\r\n", temperature, humidity, pressure);
  return String(pressure) + "+" + String(temperature) + "+" + String(humidity);
}

/**
 * @brief ENV3の出力結果をSDカード保存用文字列に変換する
 * 
 * @param result ENV3の出力結果
 * @return String カンマ区切り文字列
 */
String convertEnv3ResultForSd(String result){
  String response[3] = {"\0"};
  String resultForSd;
  split(result, response);
  // センサID(response[0])を除外し，カンマ区切りに変換
  // return resultForSd = response[0] + "," + response[1] + "," + response[2] + "," ;
  return resultForSd = response[0] + "," + response[1] + "," + response[2] + "," ;
}

/**
 * @brief ENVの出力結果をPOST用JsonStringに変換する
 * 
 * @param result ENV3の出力結果
 * @return String JsonString
 */
String convertEnv3ResultForPost(String result){
  String response[3] = {"\0"};
  String resultForPost;
  split(result, response);
  //測定値がからの場合, 空文字を返す
  if(response[0] == "" && response[1] == "" && response[2] == ""){
    return "";
  }
  
  //戻り値の生成(Json文字列を生成する)
  return resultForPost = "\"airPress\":\"" + response[0] + "\"," + "\"temp\":\"" + response[1] + "\",\"humi\":\"" + response[2] + "\",";
}

/**
 * @brief 気温と相対湿度から絶対湿度を算出
 * 
 * @param temperature 気温
 * @param humidity 相対湿度
 * @return uint32_t 絶対湿度
 */
uint32_t getAbsoluteHumidity(float temperature, float humidity) {
  const float absoluteHumidity = 216.7f * ((humidity / 100.0f) * 6.112f * exp((17.62f * temperature) / (243.12f + temperature)) / (273.15f + temperature)); // [g/m^3]
  const uint32_t absoluteHumidityScaled = static_cast<uint32_t>(1000.0f * absoluteHumidity); // [mg/m^3]
  return absoluteHumidityScaled;
}

/**
 * @brief SGP30二酸化炭素濃度センサの測定
 * 
 * @return String 二酸化炭素濃度, 総揮発性有機化合物
 */
String measureSgp30() {
  if (temperature != 0 || humidity != 0) {
    sgp.setHumidity(getAbsoluteHumidity(temperature, humidity));
  }

  if (! sgp.IAQmeasure()) {
    Serial.println("Measurement failed (SGP30)");
    return "++";
  }
  Serial.print("TVOC: "); Serial.print(sgp.TVOC); Serial.print("ppb ");
  Serial.print("eCO2: "); Serial.print(sgp.eCO2); Serial.println("ppm");

  if (! sgp.IAQmeasureRaw()) {
    Serial.println("Raw Measurement failed");
    return "++";
  }
  Serial.print("Raw H2: "); Serial.print(sgp.rawH2);
  Serial.print(" Raw Ethanol: "); Serial.println(sgp.rawEthanol);
  return String(sgp.eCO2) + "+" + String(sgp.TVOC);
}

/**
 * @brief SGP30の出力結果をSDカード保存用文字列に変換する
 * 
 * @param result SGP30の出力結果
 * @return String カンマ区切り文字列
 */
String convertSgp30ResultForSd(String result){
  String response[2] = {"\0"};
  String resultForSd;
  split(result, response);
  // センサID(response[0])を除外し，カンマ区切りに変換
  return resultForSd = response[0] + "," + response[1] + ",";
}

/**
 * @brief SGP30の出力結果をPOST用JsonStringに変換する
 * 
 * @param result SGP30の出力結果
 * @return String JsonString
 */
String convertSgp30ResultForPost(String result){
  String response[2] = {"\0"};
  String resultForPost;
  split(result, response);
  //測定値がからの場合, 空文字を返す
  if(response[0] == "" && response[1] == ""){
    return "";
  }
  
  //戻り値の生成(Json文字列を生成する)
  return resultForPost = "\"co2Concent\":\"" + response[0] + "\"," + "\"tvoc\":\"" + response[1] + "\",";
}

/**
 * @brief +区切りの文字列を配列に分割
 * 
 * @param data +区切りの文字列
 * @param dataArray 結果配列
 */
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

/**
 * @brief SDI-12にコマンドを送信し, レスポンスを取得
 * 
 * @param myCommand コマンド文字列
 * @param sendInterval 送信感覚
 * @param requestNumber 送信回数
 * @return String SDI-12レスポンス
 */
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

/**
 * @brief 対象アドレスのSDI-12センサがアクティブかを判定
 * 
 * @param i SDI-12センサアドレス
 * @return boolean アクティブor非アクティブ
 */
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

/**
 * @brief SDI-12センサのレスポンスを取得する
 * 
 * @param sensorAddress センサアドレス
 * @return String 整形されたSDI-12レスポンス
 */
String measureSdi12(int sensorAddress) {
  String totalResponseString = "";
  int requestNumber = 5;
  int sendInterval = 50;

  //SDI-12のレスポンスを受け取るための配列
  String sdi12Response[10] = {"\0"};

  Serial.println("Start measurement (Sensor address: " + String(sensorAddress) + ")");

  String myCommand = String(sensorAddress) + "I!";
  String identResponse = sendCommandAndCollectResponse(myCommand, sendInterval, requestNumber); //識別番号を取得
  String sensorIdent = identResponse.substring(11, 16); //センサの型番を取り出し
  
  //ARPセンサ用(sendIntervalが短いと測定エラーを起こす為)
  if (sensorIdent == "5WTA " || sensorIdent == "5WET" || sensorIdent == "5WT  ") {
    sendInterval = 150;
  }

  String rawResponse = "";
  myCommand = String(sensorAddress) + "C!";
  Serial.println("Command: " + myCommand);

  rawResponse = sendCommandAndCollectResponse(myCommand, sendInterval, requestNumber);

  //レスポンスがなかった場合，測定失敗としてreturn
  if (rawResponse == "\0") {
    totalResponseString += "++++++++++";
    return totalResponseString;
  }

  int waitTime = rawResponse.substring(3, 4).toInt() * 1000;
  delay(waitTime);

  myCommand = String(sensorAddress) + "D0!";
  Serial.println("Command: " + myCommand);
  rawResponse = sendCommandAndCollectResponse(myCommand, sendInterval, requestNumber);

  //レスポンスがなかった場合，測定失敗としてreturn
  if (rawResponse == "\0") {
    totalResponseString += "++++++++++";
    return totalResponseString;
  }

  split(rawResponse, sdi12Response);

  //センサID, センサアドレス, 体積含水率, 地温, バルク誘電率, バルク電気伝導度, 土壌間隙水電気伝導度, 加速度(X), 加速度(Y), 加速度(Z)
  //センサID----------
  //1:ARP WD-5 WET
  //2:ARP WD-5 WTA
  //3:ARP WD-5 WT
  //4:Acclima TDT
  //5:Acclima TDR-315
  //6:METER TEROS-12
  //7:METER TEROS-11
  //8:METER TEROS-21
  //-----------------
  if (sensorIdent == "5WET ") {
    totalResponseString = "1+" + sdi12Response[0] + "+" + sdi12Response[1] + "+" + sdi12Response[3] + "+" + sdi12Response[2] + "+++++";
  } else if (sensorIdent == "5WTA ") {
    totalResponseString = "2+" + sdi12Response[0] + "+" + sdi12Response[1] + "+" + sdi12Response[2] + "++++" + sdi12Response[3] + "+" + sdi12Response[4] + "+" + sdi12Response[5];
  } else if (sensorIdent == "5WT  ") {
    totalResponseString = "3+" + sdi12Response[0] + "+" + sdi12Response[1] + "+" + sdi12Response[2] + "++++++";
  } else if (sensorIdent == "TDT  " || sensorIdent == "00303" || sensorIdent == " 0030") {
    totalResponseString = "4+" + sdi12Response[0] + "+" + sdi12Response[1] + "+" + sdi12Response[2] + "+" + sdi12Response[3] + "+" + sdi12Response[4] + "++++";
  } else if (sensorIdent == "TR315") {
    totalResponseString = "5+" + sdi12Response[0] + "+" + sdi12Response[1] + "+" + sdi12Response[2] + "+" + sdi12Response[3] + "+" + sdi12Response[4] + "+" + sdi12Response[5] + "+++";
  } else if (sensorIdent == "TER12") {
    totalResponseString = "6+" + sdi12Response[0] + "+" + sdi12Response[1] + "+" + sdi12Response[2] + "++" + sdi12Response[3] + "++++";
  } else if (sensorIdent == "TER11") {
    totalResponseString = "7+" + sdi12Response[0] + "+" + sdi12Response[1] + "+" + sdi12Response[2] + "++++++";
  } else if (sensorIdent == "TER21" || sensorIdent == "MPS-2" || sensorIdent == "MPS-6") {
    totalResponseString = "8+" + sdi12Response[0] + "+" + sdi12Response[1] + "+" + sdi12Response[2] + "++++++";
  } else {
    totalResponseString = "999+" + sdi12Response[0] + "+" + sdi12Response[1] + "+" + sdi12Response[2] + "+" + sdi12Response[3] + "+" + sdi12Response[4] + "+" + sdi12Response[5] + "+" + sdi12Response[6] + "+" + sdi12Response[7] + "+" + sdi12Response[8];
  }
  
  return totalResponseString;
}

/**
 * @brief SDI-12の出力結果をSDカード保存用文字列に変換する
 * 
 * @param result SDI-12の出力結果
 * @return String カンマ区切り文字列
 */
String convertSdi12ResultForSd(String result){
  String response[10] = {"\0"};
  String resultForSd;
  split(result, response);
  // センサID(response[0])を除外し，カンマ区切りに変換
  return resultForSd = response[1] + "," + response[2] + "," + response[3] + "," + response[4] + "," + response[5] + "," + response[6] + "," + response[7] + "," + response[8] + ","; 
}

/**
 * @brief SDI-12の出力結果をPOST用JsonStringに変換する
 * 
 * @param result SDI-12の出力結果
 * @return String JsonString
 */
String convertSdi12ResultForPost(String result){
  String response[10] = {"\0"};
  String resultForPost;
  split(result, response);
  boolean isBlank = true;

  //配列の空判定
  for(int i = 0; i < 10; i++){
    //既に何かしらのデータが入っていた場合, continue
    if(!isBlank){
      continue;
    }
    //何かしらのデータが入ってい場合, 空判定をfalseに変更
    if(response[i] != ""){
      isBlank = false;
    }
  }

  //空の場合，空文字列を返す
  if(isBlank){
    return "";
  }
  
  //戻り値の生成(Json文字列を生成する)
  return resultForPost = "{\"sensorId\":\"" + response[0] + "\"," + "\"sdiAddress\":\"" + response[1] + "\",\"vwc\":\"" + response[2] + "\",\"soilTemp\":\"" + response[3] + "\",\"brp\":\"" + response[4] + "\",\"sbec\":\"" + response[5] + "\",\"spwec\":\"" + response[6] + "\",\"gax\":\"" + response[7] + "\",\"gay\":\"" + response[8] + "\",\"gaz\":\"" + response[9] + "\"}";
}

/**
 * @brief デジタル光センサの測定
 * 
 * @return String 照度
 */
String measureIllumination() {
  uint16_t lux = bh1750.getLUX();
  Serial.println("デジタル光センサ: " + String(lux) + " (lux)");
  return String(lux);
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

/**
 * @brief アナログセンサの測定
 * 
 * @return String アナログセンサ出力値
 */
String readAnalogValue(){
  String response = "";
  response = analogRead(ANALOG_PIN);
  Serial.println("Analog Value: " + response);
  return response;
}

/**
 * @brief ADCの設定
 * 
 */
void adcInit(void) {
  // adc_power_on(); //不要のアラートが出るため，コメントアウト
  adc_gpio_init(ADC_UNIT_1, ADC_CHANNEL);
  adc1_config_width(ADC_WIDTH_BIT_12);
  adc1_config_channel_atten(ADC1_CHANNEL_4, ADC_ATTEN_DB_11);
  esp_adc_cal_characterize(ADC_UNIT_1, ADC_ATTEN_DB_11, ADC_WIDTH_BIT_12, 1100, &adcChar);
}

/**
 * @brief ADCの出力値を取得
 * 
 * @param calculateNumber 測定回数
 * @return String 出力値の平均値
 */
String readAdcValue(int calculateNumber) {
  uint32_t readValue = 0;
  int readValueSum = 0;
  float readValueAvg = 0;
  for(int i = 0; i < calculateNumber; i++){
    esp_adc_cal_get_voltage(ADC_CHANNEL, &adcChar, &readValue);
    readValueSum += readValue;
  }
  readValueAvg = readValueSum / float(calculateNumber);
  Serial.print("Average value(ADC): "); Serial.println(String(readValueAvg));
  return String(readValueAvg);
}

/**
 * @brief POSTリクエストを送信
 * 
 * @param host ホスト
 * @param port ポート番号
 * @param uri エンドポイントのURI
 * @param measureData 測定データ(json文字列)
 */
void postRequest(char* host, int port, String uri, String measureData) {
  Serial.println("\r\n-----Connecting to API-----\r\n");
  WiFiClient micorControllerClient;

  const String hostString = String(host);
  const int requestLimit = 5;

  Serial.print("Urlencoded data:"); Serial.println(measureData);

  for (int j = 0; j < requestLimit; j++) {
    if (micorControllerClient.connect(host, port)) {
      Serial.println("\r\n-----Posting measured data-----\r\n");
      micorControllerClient.println("POST "+ uri + " HTTP/1.1");
      micorControllerClient.println("Host: " + hostString + ":" + String(port));
      micorControllerClient.println("Connection: close");
      micorControllerClient.println("Content-Type: application/json");
      micorControllerClient.print("Content-Length: ");
      micorControllerClient.println(measureData.length());
      micorControllerClient.println();
      micorControllerClient.println(measureData);
      delay(100);
      String apiResponse = micorControllerClient.readString();
      int bodypos = apiResponse.indexOf("\r\n");
      delay(100);
      Serial.print("API response:"); Serial.println(apiResponse);
      micorControllerClient.stop();
      return;
    } else {
      Serial.println(".");
      delay(200);
    }
  }
  Serial.println("\r\n-----POST request failed.-----\r\n");
}
