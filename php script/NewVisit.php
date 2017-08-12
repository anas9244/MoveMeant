<?php 
require_once('../mysql_connect.php');
$place_id = $_POST['place_id'];

$user_id=$_POST['user_id'];



$checkVisit_qry= "select * from visits where user_id like '$user_id' and place_id like '$place_id';";
$resultVisit_qry=mysqli_query($dbc ,$checkVisit_qry);





if(mysqli_num_rows($resultVisit_qry) == 0)
{

		$insertVisit_qry = "insert into visits (user_id,place_id,last_visit) values ('$user_id','$place_id',now())";

		if($dbc->query($insertVisit_qry)===TRUE) {

			$checkNewVisit_qry= "select * from visits where user_id like '$user_id' and place_id like '$place_id';";
			$resultNewVisit_qry=mysqli_query($dbc ,$checkNewVisit_qry);

			
			while($row = mysqli_fetch_assoc($resultNewVisit_qry))
			{
				$output[]=$row;
			}
	

			print(json_encode($output));
	}
	else {
	echo  "error new visit" . $insertVisit_qry . "<br>" . $dbc->error ;
	}
}
else
{
$newVisit_qry="CALL NewVisit('$place_id','$user_id');";

if($dbc->query($newVisit_qry)===TRUE) {

			$checkNewVisit_qry= "select * from visits where user_id like '$user_id' and place_id like '$place_id';";
			$resultNewVisit_qry=mysqli_query($dbc ,$checkNewVisit_qry);

			
			while($row = mysqli_fetch_assoc($resultNewVisit_qry))
			{
				$output[]=$row;
			}
	

			print(json_encode($output));
	}
	else {
	echo  "error new visit" . $insertVisit_qry . "<br>" . $dbc->error ;
	}


}
mysqli_close($dbc);

?>