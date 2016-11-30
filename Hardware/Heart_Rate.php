<!DOCTYPE html>
<html>
<body bgcolor="Green">

<h1><b>Heart rate monitor</b></h1>
<img src="https://lh3.ggpht.com/5vWTNmyOVJaz7GkOSZX3kGaYKUb2SXU7yqX5_EjY0AQhe769IeASsIEsQgC_QVIGflw=w300" align="middle">
<div>
<?php
require_once 'firebaseLib.php';
$url = 'https://sefp-1e4d0.firebaseio.com/arduino_data/';
$token = 'DHGFKDZgRLxMJqNdY660qukxD5C09CrCOLDXP2bA';

$conn=new mysqli('localhost','root','sai','eis');
$result=$conn->query("select * from Pulse");
//$query1 = $conn->query("select count(*) from Pulse");
//echo $query1;
//$i = 0
if($result->num_rows>0)
{
	//$count =0;
	//$N = 0;
	while($row=$result->fetch_assoc())
	{
//		echo $row['aTIME']. '<br>';
		$r=$row['Time'];

		$r1=$row['Rate'];
		//$count = $count + $r1;
	}
		$firebasePath = '/Heart-Rate';
		$temp = $r. '>'.$r1. 'BPM';
		$fb = new fireBase($url, $token);
		//$response = $fb->push($firebasePath,$r);
        	$response = $fb->push($firebasePath, $temp);
//		sleep(2);
//		}
//	}
		echo " <h2>The heart-rate at time: $r is  <b><i>$r1</i></b> beats per minute</h2>";
		echo '<br>';

}
?>
</div>
</body>
</html>
