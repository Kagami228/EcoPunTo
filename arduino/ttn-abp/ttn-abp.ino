/*******************************************************************************
 * Copyright (c) 2015 Thomas Telkamp and Matthijs Kooijman
 *
 * Permission is hereby granted, free of charge, to anyone
 * obtaining a copy of this document and accompanying files,
 * to do whatever they want with them without any restriction,
 * including, but not limited to, copying, modification and redistribution.
 * NO WARRANTY OF ANY KIND IS PROVIDED.
 *
 * This example sends a valid LoRaWAN packet with payload "Hello,
 * world!", using frequency and encryption settings matching those of
 * the The Things Network.
 *
 * This uses ABP (Activation-by-personalisation), where a DevAddr and
 * Session keys are preconfigured (unlike OTAA, where a DevEUI and
 * application key is configured, while the DevAddr and session keys are
 * assigned/generated in the over-the-air-activation procedure).
 *
 * Note: LoRaWAN per sub-band duty-cycle limitation is enforced (1% in
 * g1, 0.1% in g2), but not the TTN fair usage policy (which is probably
 * violated by this sketch when left running for longer)!
 *
 * To use this sketch, first register your application and device with
 * the things network, to set or generate a DevAddr, NwkSKey and
 * AppSKey. Each device should have their own unique values for these
 * fields.
 *
 * Do not forget to define the radio type correctly in config.h.
 *
 *******************************************************************************/
#include <lmic.h>
#include <hal/hal.h>
#include <SPI.h>
#include <Adafruit_GPS.h>
#include <avr/sleep.h>

#define CFG_eu868 1

// LoRaWAN NwkSKey, network session key
// This is the default Semtech key, which is used by the early prototype TTN
// network.

//  static const PROGMEM u1_t NWKSKEY[16] = { 0xE7, 0xBB, 0x6F, 0x81, 0xAA, 0xE5, 0x91, 0x70, 0xA8, 0x95, 0x38, 0x4B, 0xB3, 0x8E, 0x74, 0x26 };
//  static const u1_t PROGMEM APPSKEY[16] = { 0x65, 0xC6, 0xC2, 0xDF, 0x4F, 0xCF, 0x49, 0x40, 0xDD, 0xFA, 0xDF, 0x07, 0x3F, 0x5D, 0xD9, 0xD0 };
//  static const u4_t DEVADDR = 0x2601184F ; // <-- Change this address for every node!

  static const PROGMEM u1_t NWKSKEY[16] = {  0x19, 0x1E, 0x8C, 0x6C, 0xD9, 0x87, 0x90, 0x9C, 0x06, 0x6F, 0xE8, 0xD8, 0xD0, 0x3B, 0x81, 0x26 };
  static const u1_t PROGMEM APPSKEY[16] = {  0x9E, 0x37, 0x3D, 0x7A, 0x6B, 0xEB, 0x39, 0x44, 0x48, 0x54, 0x77, 0xE3, 0x42, 0x21, 0x4A, 0xD1};
  static const u4_t DEVADDR = 0x26011F9C;

  const byte interruptPin=2;
 bool trigger=false;

static osjob_t sendjob;

// Schedule TX every this many seconds (might become longer due to duty
// cycle limitations).
const unsigned TX_INTERVAL = 10;//msecs;

#define GPSSerial Serial1
Adafruit_GPS GPS(&GPSSerial);

// Pin mapping
const lmic_pinmap lmic_pins = {
    .nss = 8,
    .rxtx = LMIC_UNUSED_PIN,
    .rst = 4,
    .dio =  {7, 5, LMIC_UNUSED_PIN},
};

void onEvent (ev_t ev) {
//    Serial.print(os_getTime());
//    Serial.print(": ");
    switch(ev) {
        case EV_SCAN_TIMEOUT:
            Serial.println(F("EV_SCAN_TIMEOUT"));
            break;
        case EV_BEACON_FOUND:
            Serial.println(F("EV_BEACON_FOUND"));
            break;
        case EV_BEACON_MISSED:
            Serial.println(F("EV_BEACON_MISSED"));
            break;
        case EV_BEACON_TRACKED:
            Serial.println(F("EV_BEACON_TRACKED"));
            break;
        case EV_JOINING:
            Serial.println(F("EV_JOINING"));
            break;
        case EV_JOINED:
            Serial.println(F("EV_JOINED"));
            break;
        case EV_RFU1:
            Serial.println(F("EV_RFU1"));
            break;
        case EV_JOIN_FAILED:
            Serial.println(F("EV_JOIN_FAILED"));
            break;
        case EV_REJOIN_FAILED:
            Serial.println(F("EV_REJOIN_FAILED"));
            break;
        case EV_TXCOMPLETE:
            Serial.println(F("EV_TXCOMPLETE (includes waiting for RX windows)"));
//            if (LMIC.txrxFlags & TXRX_ACK)
//              Serial.println(F("Received ack"));
//            if (LMIC.dataLen) {
//              Serial.println(F("Received "));
//              Serial.println(LMIC.dataLen);
//              Serial.println(F(" bytes of payload"));
//            }
            // Schedule next transmission
           digitalWrite(LED_BUILTIN, LOW);
           trigger=false;
          break;
        case EV_LOST_TSYNC:
            Serial.println(F("EV_LOST_TSYNC"));
            break;
        case EV_RESET:
            Serial.println(F("EV_RESET"));
            break;
        case EV_RXCOMPLETE:
            //data received in ping slot
            Serial.println(F("EV_RXCOMPLETE"));
            break;
        case EV_LINK_DEAD:
            Serial.println(F("EV_LINK_DEAD"));
            break;
        case EV_LINK_ALIVE:
            Serial.println(F("EV_LINK_ALIVE"));
            break;
         default:
            Serial.println(F("Unknown event"));
            break;
    }
}

void do_send(osjob_t* j){
    if (LMIC.opmode & OP_TXRXPEND) {
        Serial.println(F("OP_TXRXPEND, not sending"));
    } else {
       digitalWrite(LED_BUILTIN, HIGH);
       if(j->param==1){
         int val = digitalRead(interruptPin);
         j->param=(void *)(val+1);
         Serial.println(String((int)j->param));
      }
        // Prepare upstream data transmission at the next possible time.
        #define VBATPIN A9
        float measuredvbat = analogRead(VBATPIN);
        measuredvbat *= 2;    // we divided by 2, so multiply back
        measuredvbat *= 3.3;  // Multiply by 3.3V, our reference voltage
measuredvbat /= 1024; // convert to voltage
        //  String batStr(measuredvbat,2);
        
        uint8_t buff[14];
           buff[0]=(int)j->param;
           buff[1]=1;
        *((int32_t*)&buff[2])=GPS.latitude_fixed;
        *((int32_t*)&buff[6])=GPS.longitude_fixed;
        *((int32_t*)&buff[10])=(int)(measuredvbat*100);
  
        LMIC_setTxData2(1, buff, 14, 0);
        Serial.println(F("Packet queued"));
    }

    // Next TX is scheduled after TX_COMPLETE event.

}
void disableLoraChannels(char exludeCh)
{
  for(int i=0; i<16; i++) { // For EU; for US use i<71
    if(i != exludeCh) 
      LMIC_disableChannel(i);
  }
}
void Lora_Setup()
{
   os_init();
    // Reset the MAC state. Session and pending data transfers will be discarded.
    LMIC_reset();

    // On AVR, these values are stored in flash and only copied to RAM
    // once. Copy them to a temporary buffer here, LMIC_setSession will
    // copy them into a buffer of its own again.
    uint8_t appskey[sizeof(APPSKEY)];
    uint8_t nwkskey[sizeof(NWKSKEY)];
    memcpy_P(appskey, APPSKEY, sizeof(APPSKEY));
    memcpy_P(nwkskey, NWKSKEY, sizeof(NWKSKEY));
    LMIC_setSession (0x1, DEVADDR, nwkskey, appskey);
    //disableLoraChannels(0);
    LMIC_setupChannel(0, 868100000, DR_RANGE_MAP(DR_SF12, DR_SF7),  BAND_CENTI);      // g-band
    // TTN defines an additional channel at 869.525Mhz using SF9 for class B
    // devices' ping slots. LMIC does not have an easy way to define set this
    // frequency and support for class B is spotty and untested, so this
    // frequency is not configured here.
    
    // Disable link check validation
    LMIC_setLinkCheckMode(0);
    // TTN uses SF9 for its RX2 window.
    LMIC.dn2Dr = DR_SF9;
    // Set data rate and transmit power for uplink (note: txpow seems to be ignored by the library)
    LMIC_setDrTxpow(DR_SF7 ,14);
    // Start job
    //do_send(&sendjob);
}
#define PMTK_PERIODIC_MODE "$PMTK225,2,10000,50000,0,0*2D"

void GPS_Setup()
{
   GPS.begin(9600);
  // uncomment this line to turn on RMC (recommended minimum) and GGA (fix data) including altitude
//  GPS.sendCommand(PMTK_SET_NMEA_OUTPUT_RMCGGA);
//  // uncomment this line to turn on only the "minimum recommended" data
//  GPS.sendCommand(PMTK_SET_NMEA_OUTPUT_RMCONLY);
//  // For parsing data, we don't suggest using anything but either RMC only or RMC+GGA since
//  // the parser doesn't care about other sentences at this time
//  // Set the update rate
//  GPS.sendCommand(PMTK_SET_NMEA_UPDATE_1HZ); 
//  GPS.sendCommand(PMTK_API_SET_FIX_CTL_1HZ);
//  // For the parsing code to work nicely and have time to sort thru the data, and
//  // print it out we don't suggest using anything higher than 1 Hz
//     
//  // Request updates on antenna status, comment out to keep quiet
//  GPS.sendCommand(PGCMD_ANTENNA);
 
 GPS.sendCommand(PMTK_SET_NMEA_OUTPUT_RMCGGA);
  // uncomment this line to turn on only the "minimum recommended" data
  //GPS.sendCommand(PMTK_SET_NMEA_OUTPUT_RMCONLY);
  // For parsing data, we don't suggest using anything but either RMC only or RMC+GGA since
  // the parser doesn't care about other sentences at this time

  // Set the update rate
  GPS.sendCommand(PMTK_SET_NMEA_UPDATE_1HZ);   // 1 Hz update rate
  // For the parsing code to work nicely and have time to sort thru the data, and
  // print it out we don't suggest using anything higher than 1 Hz

  // Request updates on antenna status, comment out to keep quiet
  GPS.sendCommand(PGCMD_NOANTENNA);

  //Set GPS into Periodic Mode
  //GPS.sendCommand(PMTK_PERIODIC_MODE);
}

uint32_t dataTimeStamp=0;

//bit_t recieved=false;
//SIGNAL(TIMER0_COMPA_vect) {
//  static uint32_t counter=0;
//  if(recieved && counter>=100)
//  {
//    digitalWrite(LED_BUILTIN,LOW);
//    recieved=false;
//    counter = 0;
//  }
//  else if(recieved)
//    counter++;
//}

void useInterrupt(boolean v) {
  if (v) {
    // Timer0 is already used for millis() - we'll just interrupt somewhere
    // in the middle and call the "Compare A" function above
    OCR0A = 0xAF;
    TIMSK0 |= _BV(OCIE0A);
    //usingIn
terrupt = true;
  } else {
    // do not call the interrupt function COMPA anymore
    TIMSK0 &= ~_BV(OCIE0A);
    //usingInterrupt = false;
  }
}

void setup() {
    Serial.begin(115200);
//      while (!Serial) {
//      delay(1);
//    }
   pinMode(LED_BUILTIN, OUTPUT);
   pinMode(interruptPin, INPUT_PULLUP);
   digitalWrite(LED_BUILTIN, LOW);
   delay(2500);
   Serial.println("Startup!");
   //useInterrupt(true);
   GPS_Setup();
   Lora_Setup();
   attachInterrupt(digitalPinToInterrupt(interruptPin),blink ,CHANGE);
}

void blink(){
   if(!trigger){
    trigger=true;
    sendjob.param=1;
    os_setTimedCallback(&sendjob, os_getTime()+ms2osticks(TX_INTERVAL), do_send);
   }  
}
void GetGPS_Data()
{
   static bool firstTime=false;
    char c = GPS.read();
    if (c)   dataTimeStamp = millis();
    if (GPS.newNMEAreceived()) 
    {
      //recieved=true;
     // Serial.println(GPS.lastNMEA());
      GPS.parse(GPS.lastNMEA());
      if(GPS.fix && !firstTime){
        sendjob.param=0;
        os_setTimedCallback(&sendjob, os_getTime()+ms2osticks(TX_INTERVAL), do_send);
        firstTime=true;
      }
    }
}
void sleepNow();

void loop() {
   os_runloop_once();
   GetGPS_Data();
}