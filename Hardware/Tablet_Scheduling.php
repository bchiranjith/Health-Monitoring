<!DOCTYPE html>
<html>
<body bgcolor="green">

<h1><b>Tablet scheduling</b></h1>
<img src="https://lh3.ggpht.com/5vWTNmyOVJaz7GkOSZX3kGaYKUb2SXU7yqX5_EjY0AQhe769IeASsIEsQgC_QVIGflw=w300" align="middle">
<div>
<?php
require_once 'firebaseLib.php';
$url = 'https://sefp-1e4d0.firebaseio.com/arduino_data/Tablet/';
$token = 'DHGFKDZgRLxMJqNdY660qukxD5C09CrCOLDXP2bA';

$conn=new mysqli('localhost','root','sai','eis');
$result=$conn->query("select * from Tablet");
if($result->num_rows>0)
{
	$count = 0;
	$count1 = 0;
	$count2 = 0;
	$count3 = 0;
	$count4 = 0;
	while($row=$result->fetch_assoc())
	{
	if($count <7)
	{
		//echo $row['TIME']. '<br>';
		$r=$row['TIME'];
		if($count1==0 && $row['TAB']=="Tablet1")
		{
			//echo $row['TAB']. '<br>';
			$count1=1;
			$rr = $row['TAB'];
                //$count++;
                	$r1=$row['STATUS'];
			$firebasePath = '/';
        		$temp = $r . '>' . $rr . '>' . $r1;
        		echo $temp.'<br>';
        		$fb = new fireBase($url, $token);
        		$response = $fb->push($firebasePath,$temp);
		}
		if($count2==0 && $row['TAB']=="Tablet2")
                {
                       // echo $row['TAB']. '<br>';
			$count2=1;
			$rr = $row['TAB'];
                //$count++;
                        $r1=$row['STATUS'];
			$firebasePath = '/';
                        $temp = $r . '>' . $rr . '>' . $r1;
                        echo $temp.'<br>';
                        $fb = new fireBase($url, $token);
                        $response = $fb->push($firebasePath,$temp);

                }
		if($count3==0 && $row['TAB']=="Tablet3")
                {
                        //echo $row['TAB']. '<br>';
			$count3=1;
			$rr = $row['TAB'];
                //$count++;
                        $r1=$row['STATUS'];
			$firebasePath = '/';
                        $temp = $r . '>' . $rr . '>' . $r1;
                        echo $temp.'<br>';
                        $fb = new fireBase($url, $token);
                        $response = $fb->push($firebasePath,$temp);

                }
		if($count4==0 && $row['TAB']=="Tablet4")
                {
                        //echo $row['TAB']. '<br>';
			$count4=1;
			$rr = $row['TAB'];
                //$count++;
                        $r1=$row['STATUS'];
			$firebasePath = '/';
                        $temp = $r . '>' . $rr . '>' . $r1;
                        echo $temp.'<br>';
                        $fb = new fireBase($url, $token);
                        $response = $fb->push($firebasePath,$temp);

                }
		

		//$rr = $row['TAB'];
		$count++;
		//$r1=$row['STATUS'];
	//echo $count;
//	$firebasePath = '/';
//	$firebasePath1 = $firebasePath + '/' + $rr;
//	$firebasePath2 = $firebasePath1 + '/' + $r1;
//	$temp = $r . '-->' . $rr . '-->' . $r1;
//	echo $temp;
//	$fb = new fireBase($url, $token);
//	$response = $fb->push($firebasePath,$r);
//	$response = $fb->push($firebasePath,$temp);
//        $response = $fb->push($temp);
	//sleep(2);
	}
	}

}
?>
</div>
</body>
</html>
