#include <Wire.h>
#include <Adafruit_PN532.h>

#define SDA_PIN 2
#define SCL_PIN 1

Adafruit_PN532 nfc(SDA_PIN, SCL_PIN);

void setup(void) {
  Serial.begin(115200);

  nfc.begin();
  uint32_t versiondata = nfc.getFirmwareVersion();
  if (!versiondata) {
    Serial.print("Didn't find PN53x board");
    while (1);
  }

  nfc.SAMConfig();

  // Send a marker string to identify the device
  Serial.println("Adafruit PN532 NFC Marker");
}

void loop(void) {
  uint8_t success;
  uint8_t uid[] = { 0, 0, 0, 0, 0, 0, 0 };
  uint8_t uidLength;

  success = nfc.readPassiveTargetID(PN532_MIFARE_ISO14443A, uid, &uidLength);

  if (success) {
    for (uint8_t i = 0; i < uidLength; i++) {
      String hexString = (uid[i] < 0x10 ? "0" : "") + String(uid[i], HEX);
      hexString.toUpperCase();
      Serial.print(hexString);
    }
    Serial.println("");
    delay(1000);
  }
}
