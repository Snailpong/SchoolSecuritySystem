#include "FirebaseESP8266.h"
#include <ESP8266WiFi.h>
#include <SoftwareSerial.h>
SoftwareSerial mySerial(D11, D12);

#define FIREBASE_HOST "schoolsecuritysystem-15###.firebaseio.com"
#define FIREBASE_AUTH "ok###"
#define WIFI_SSID "6203-1_2G"
#define WIFI_PASSWORD "cse###"

//Define FirebaseESP8266 data object
FirebaseData firebaseData;
String path = "/ESP8266_Test";
int started = 0;

void printJsonObjectContent(FirebaseData &data);

void setup()
{
  Serial.begin(9600);
  mySerial.begin(9600);

  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  Serial.print("Connecting to Wi-Fi");
  while (WiFi.status() != WL_CONNECTED)
  {
    Serial.print(".");
    delay(300);
  }
  Serial.println();
  Serial.print("Connected with IP: ");
  Serial.println(WiFi.localIP());
  Serial.println();

  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);
  Firebase.reconnectWiFi(true);

  //Set database read timeout to 1 minute (max 15 minutes)
  Firebase.setReadTimeout(firebaseData, 1000 * 60);
  //tiny, small, medium, large and unlimited.
  //Size and its write timeout e.g. tiny (1s), small (10s), medium (30s) and large (60s).
  Firebase.setwriteSizeLimit(firebaseData, "tiny");
  
  FirebaseJson json;
 
}

void loop()
{
  if(mySerial.available()){
    int id = mySerial.read();
    Serial.println(id);
    if(id < 16){
      if(Firebase.setString(firebaseData, "/StudentManagement/20191004/Grade1/1100"+String(id)+"/busdriver", "Mason"));
      if(Firebase.setString(firebaseData, "/StudentManagement/20191004/Grade1/1100"+String(id)+"/busnumber", "23A5698"));
      if(!started){
        Serial.println("get on");
        if(Firebase.setString(firebaseData, "/StudentManagement/20191004/Grade1/1100"+String(id)+"/states", "Got on Bus"));
      } else {
        Serial.println("get off");
        if(Firebase.setString(firebaseData, "/StudentManagement/20191004/Grade1/1100"+String(id)+"/states", "Got off Bus"));
      }
      if(Firebase.setString(firebaseData, "/StudentManagement/20191004/Grade1/1100"+String(id)+"/time", String(millis()))){
        Serial.println("PASSED");
        Serial.println("PATH: " + firebaseData.dataPath());
        Serial.println("TYPE: " + firebaseData.dataType());
        Serial.println("ETag: " + firebaseData.ETag());
        Serial.print("VALUE: ");
      }
    }
    if(id == 255){
      started = 1;
      Serial.println("Bus left");
      if(Firebase.setInt(firebaseData, "/Notification/20191004/", 1));
      if(Firebase.setInt(firebaseData, "/Notification/20191004/", 0));
    }
  }
 
}

void printJsonObjectContent(FirebaseData &data){
  size_t tokenCount = data.jsonObject().parse(false).getJsonObjectIteratorCount();
  String key;
  String value;
  FirebaseJsonObject jsonParseResult;
  Serial.println();
  for (size_t i = 0; i < tokenCount; i++)
  {
    data.jsonObject().jsonObjectiterator(i,key,value);
    jsonParseResult = data.jsonObject().parseResult();
    Serial.print("KEY: ");
    Serial.print(key);
    Serial.print(", ");
    Serial.print("VALUE: ");
    Serial.print(value); 
    Serial.print(", ");
    Serial.print("TYPE: ");
    Serial.println(jsonParseResult.type);        

  }
}
