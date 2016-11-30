import serial
import MySQLdb
from  datetime import datetime
db=MySQLdb.connect("localhost","root","sai","eis")
cursor=db.cursor()
ser = serial.Serial('/dev/ttyACM0',9600)
new = []
tem = 0.0
s=[]
pr=[]
st=""
while(1):
	x = ser.readline()
	l = x.split(":")
	if l[0] == "Tablet":
		if l[0] == "Tablet" and ( l[1] == "1" or l[1] == "2" or l[1] == "3" or l[1] == "4") and ( l[2] =="Yes\r\n" or l[2] == "No\r\n"):
			#print l
			temp = datetime.now().strftime("%a,%d %b %Y %H:%M:%S")	
			
			s=l[2].split("\r\n");
			#print s[0]
			l[2]=s[0];
		#	print s										 
		#	sql = "INSERT INTO Tablet(TIME,TAB,STATUS) VALUES('now()','l[0]+l[1]','l[2]')"
                        sql = "INSERT INTO Tablet(TIME,TAB,STATUS) VALUES('%s','%s','%s')"%(temp,str(l[0]+l[1]),str(l[2]))
			print sql
			cursor.execute(sql)
			db.commit()
			#print "Succeess"
		
	else:

		if l[0]=="Heart-rate":
			print l
			pr=l[1].split("\r\n");
			temp = datetime.now().strftime(" %a,%d %b %Y %H:%M:%S")
			sql="INSERT INTO Pulse(Time,Rate) VALUES('%s','%s')"%(temp,str(pr[0]))
			cursor.execute(sql)
			db.commit()
			#print "Success"
		
db.close()
