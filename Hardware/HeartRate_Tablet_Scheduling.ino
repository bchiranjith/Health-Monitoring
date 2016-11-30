// Pin for capactitive touch sensor
//#include <Time.h>
#include <SoftwareSerial.h>
#include<LiquidCrystal.h>
#include <SoftwareSerial.h>

//SoftwareSerial mySerial(9, 10);

LiquidCrystal lcd(12, 11, 5, 4, 3, 2);
int in = 8;
int count = 0, rate = 0;
int flag5 = 0;
unsigned long time2, time1;
byte heart[8] =
{
  0b00000,
  0b01010,
  0b11111,
  0b11111,
  0b11111,
  0b01110,
  0b00100,
  0b00000
};

int i;
int ledPin = 13; // pin for the LED
int fl[4] = {0};
int flag = 0;
int f = 0;
long int t1, t2, t3, t4;
int t = 1;
int tab[4] = {0};
int k = 0;
SoftwareSerial mySerial(9, 10);
int tim[5] = {0};
void setup() {
  //t1 = millis();
  tim[0] = millis();
  mySerial.begin(9600);
  Serial.begin(9600);
  pinMode(in, INPUT);
  // Setting the baud rate of GSM Module
  delay(100);
  //Monitor (Arduino)
  for (i = 4; i <= 7; i++)
  {
    pinMode(i, INPUT);
  }

  //Serial.print("Program started at: ");
  //Serial.println(t1);
}
int flag2 = 0;

int flag3 = 0;
int flag4 = 0;
void loop() {
  //Serial.print("main");

  if (digitalRead(4) == LOW && digitalRead(5) == LOW && digitalRead(6) == LOW && digitalRead(7) == HIGH && flag == 0)
  {
    //I'm using pins 4 - 7 so subtract 3 to match
    //numbering on module
    //Serial.println(i - 3);
    if (digitalRead(4) == LOW && digitalRead(5) == LOW && digitalRead(6) == LOW && flag == 0)
    {
      Serial.println("Tablet:1:Yes");
      Serial.println("Tablet:2:Yes");
      Serial.println("Tablet:3:Yes");
      //Serial.print("1st,2nd and 3rd");
      //Serial.println("  Schedules");
      flag = 1;
      
    }
    tab[3] = 1;
    loop2();
    flag = 1;
    loop3();
  }
  else if (digitalRead(4) == LOW && digitalRead(5) == LOW && digitalRead(6) == LOW && digitalRead(7) == LOW && flag == 0)
  {
    Serial.println("Tablet:1:Yes");
    Serial.println("Tablet:2:Yes");
    Serial.println("Tablet:3:Yes");
    Serial.println("Tablet:4:Yes");
    flag = 1;
    loop3();
  }
  else if (flag != 1)
  {
    for (i = 4; i <= 7; i++)
    {
      if (digitalRead(i) == HIGH)
      {
        //I'm using pins 4 - 7 so subtract 3 to match
        //numbering on module
        //Serial.println(i - 3);
        tab[(i - 3) - 1] = 1;
      }
    }

    for (k = 0; k < 4 && flag2 != 1; k++)

    {
      //if(tab[k]==0 && f==0)
      if (tab[1] == 0 && tab[2] == 0 && f == 0)
      {
        Serial.println("Tablet:2:Yes");
        Serial.println("Tablet:3:Yes");
        //Serial.print(k + 1);
        //Serial.println("  Schedule");
        fl[k] = 1;
        f = 1;
      }
      if (tab[k] == 1)
      {

        //Serial.print("For Tablet :");
        //Serial.println(k + 1);
        tim[k + 1] = millis();
        //Serial.println(tim[k+1]);
        //f = 0;
        while ((tim[k + 1] - tim[0] <= 5000) && (flag2 != 1) && (flag != 1))
        {
          tim[k + 1] = millis();
          if (digitalRead(k + 4) == LOW )
          {
            Serial.println("Tablet:1:Yes");
            flag = 1;
            flag2 = 1;
            loop2();
            loop3();
            //f = 1;
            break;
          }
        }

        if ((tim[k + 1] - tim[0] >= 5000) && (flag2 != 1))
        {
          Serial.println("Tablet:1:No");
          loop1();
          //Serial.println("Sending SMS.....");
          flag2 = 1;
          loop2();
          
          loop3();
          
        }
      }

    }
  }

}
//delay(1500);
void loop2()
{
  if (flag3 != 1 && flag4 == 0)
  {
    if (tab[3] == 1 )
    {
      //Serial.print("For Tablet :");
      //Serial.println(" 4");
      tim[4] = millis();
      //Serial.println(tim[k+1]);
      //f = 0;
      while ((tim[4] - tim[1] <= 5000) && (flag3 != 1))
      {
        tim[4] = millis();
        if (digitalRead(7) == LOW )
        {
          Serial.println("Tablet:4:Yes");
          flag3 = 1;
          //f = 1;
          break;
        }
      }

      if ((tim[4] - tim[1] >= 5000) && flag4 == 0)
      {
        Serial.println("Tablet:4:No");
        //Serial.println("Sending SMS.....");
        flag4 = 1;
        loop1();

      }
    }
    else {
      Serial.println("Tablet:4:Yes");
    }
  }
}
void loop1() {
  if (t > 0)
    //Serial.println("sendnig");
    SendMessage();
  delay(500);
  t = 0;
}
void SendMessage() {
  //Serial.println("sendnig");
  //Serial.println("AT+CMGF=1");
  delay(100);
  mySerial.println("AT+CMGS=\"+917032347161\"\r");
  delay(1000);
  mySerial.println("Patient Not Taken Tablet as per the scheduled time !!!........Emergency");
  mySerial.println((char)26);
  delay(1000);
}

void loop3()
{
  while(1){
 //if (flag5 != 1)
  //{
    
    k = 0;
    while (k < 1)
    {
      if (digitalRead(in))
      {
        if (k == 0)
          time1 = millis();
        k++;
        while (digitalRead(in));
      }
    }
    int j=0;
   j=random(65,80);
    delay(700);
    time2 = millis();
    rate = time2 - time1;
    rate = rate / 1;
    rate = 60000 / rate;
    //Serial.print("pulse rate is:");
    //Serial.println(rate);
    Serial.print("Heart-rate:");
    Serial.println(rate);
   // if (rate > 100)
    //{
     // Serial.println("Sending message");
      //SendMessage();
      //flag = 1;
    //}

    //if (mySerial.available() > 0)
    //Serial.write(mySerial.read());

    k = 0;
    rate = 0;
    delay(1000);
  //}

}
}

