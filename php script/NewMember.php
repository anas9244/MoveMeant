<?php 
require_once('../mysql_connect.php');

$user_id = $_POST['user_id'];
$member_id= $_POST['member_id']; 
$group_id= $_POST['group_id']; 

$insertMember_qry = "insert into group_users (user_id,group_id,member_id) values ('$user_id','$group_id','$member_id');";

if($dbc->query($insertMember_qry)===TRUE) {

			$checkNewMember_qry= "select * from group_users where user_id like '$user_id' and member_id like '$member_id';";
			$resultNewMember_qry=mysqli_query($dbc ,$checkNewMember_qry);

			
			while($row = mysqli_fetch_assoc($resultNewMember_qry))
			{
				$output[]=$row;
			}
	

			print(json_encode($output));
	}
	else {
	echo  "error new member" . $insertMember_qry . "<br>" . $dbc->error ;
	}
mysqli_close($dbc);
?>