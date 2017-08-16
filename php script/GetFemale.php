<?php 
require_once('../mysql_connect.php');

$place_id = $_POST['place_id'];

$femaleNum="select place_id,COUNT(Female.id)as FemaleNum from visits,(select id,gender from users where gender=1)as Female  WHERE visits.user_id=Female.id and visits.place_id like'$place_id';";

$resultFemale = mysqli_query($dbc ,$femaleNum);

while($rowFemale = mysqli_fetch_assoc($resultFemale))
	{
		$outputFemale[]=$rowFemale;
	}
	
print(json_encode($outputFemale));



mysqli_close($dbc);
?>