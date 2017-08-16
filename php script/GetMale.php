<?php 
require_once('../mysql_connect.php');

$place_id = $_POST['place_id'];

$maleNum="select place_id,COUNT(Male.id)as MaleNum from visits,(select id,gender from users where gender=0)as Male  WHERE visits.user_id=Male.id and visits.place_id like'$place_id';";

$resultMale = mysqli_query($dbc ,$maleNum);

while($rowMale = mysqli_fetch_assoc($resultMale))
	{
		$outputMale[]=$rowMale;
	}
	
print(json_encode($outputMale));



mysqli_close($dbc);
?>