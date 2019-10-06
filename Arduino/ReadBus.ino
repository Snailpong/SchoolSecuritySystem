#include <SoftwareSerial.h>
#include <SPI.h>
#include <MFRC522.h>

#define RST_PIN         9 
#define SS_PIN          10 

MFRC522 mfrc522(SS_PIN, RST_PIN);
SoftwareSerial mySerial(2, 3);
int started = 0;

MFRC522::MIFARE_Key key;

void setup() {
    Serial.begin(9600); 
    mySerial.begin(9600);
    while (!Serial);   
    SPI.begin();       
    mfrc522.PCD_Init(); 
    pinMode(8, INPUT);

    for (byte i = 0; i < 6; i++) {
        key.keyByte[i] = 0xFF;
    }

    Serial.println(F("Scan a MIFARE Classic PICC to demonstrate read and write."));
    Serial.print(F("Using key (for A and B):"));
    dump_byte_array(key.keyByte, MFRC522::MF_KEY_SIZE);
    Serial.println();

    Serial.println(F("BEWARE: Data will be written to the PICC, in sector #1"));
}


void loop() {
  if(digitalRead(8) && !started){
      mySerial.write(255);
      Serial.println("Bus left");
      started++;
    }

    if ( ! mfrc522.PICC_IsNewCardPresent())
        return;


    if ( ! mfrc522.PICC_ReadCardSerial())
        return;

    Serial.print(F("Card UID:"));
    dump_byte_array(mfrc522.uid.uidByte, mfrc522.uid.size);
    Serial.println();

    byte sector         = 1;
    byte blockAddr      = 4;
    byte trailerBlock   = 7;
    MFRC522::StatusCode status;
    byte buffer[18];
    byte size = sizeof(buffer);

    status = (MFRC522::StatusCode) mfrc522.PCD_Authenticate(MFRC522::PICC_CMD_MF_AUTH_KEY_A, trailerBlock, &key, &(mfrc522.uid));
    if (status != MFRC522::STATUS_OK) {
        Serial.print(F("PCD_Authenticate() failed: "));
        Serial.println(mfrc522.GetStatusCodeName(status));
        return;
    }

    status = (MFRC522::StatusCode) mfrc522.MIFARE_Read(blockAddr, buffer, &size);
    if (status != MFRC522::STATUS_OK) {
        Serial.print(F("MIFARE_Read() failed: "));
        Serial.println(mfrc522.GetStatusCodeName(status));
    }
    
    for(int i=0; i!=16; ++i) Serial.print(buffer[i] +  String(' '));

    int flag = 1;
    for(int i=1; i!=10; ++i) if(buffer[i] != i+1) flag = 0;

    if(flag){
      mySerial.write(buffer[0]);
    }

    Serial.println();
    Serial.println();

    // Halt PICC
    mfrc522.PICC_HaltA();
    // Stop encryption on PCD
    mfrc522.PCD_StopCrypto1();
}

void dump_byte_array(byte *buffer, byte bufferSize) {
    for (byte i = 0; i < bufferSize; i++) {
        Serial.print(buffer[i] < 0x10 ? " 0" : " ");
        Serial.print(buffer[i], HEX);
    }
}
