<?php 
require_once('../mysql_connect.php');
$place_id=$_POST["place_id"];

$user_id=$_POST["user_id"];



$checkVisit_qry= "select * from places_users where user_id like '$user_id' and place_id like '$place_id';";
$resultVisit_qry=mysqli_query($dbc ,$checkVisit_qry);



if(mysqli_num_rows($resultVisit_qry) == 0)
{

		$insertVisit_qry = "insert into places_users (user_id,place_id) values ('$user_id','$place_id')";

		if($dbc->query($insertVisit_qry)===TRUE) {


	echo "Success New visit";
	}
	else {
	echo  "error new visit" . $insertVisit_qry . "<br>" . $dbc->error ;
	}
}
mysqli_close($dbc);

?>