<?php 
require_once('../mysql_connect.php');

$user_id = $_POST['user_id'];


$mysql_qry = "select user_name from users,group_users where users.id=group_users.member_id and member_id NOT IN(select member_id from group_users where user_id='$user_id') and member_id not like '$user_id';";

$result = mysqli_query($dbc ,$mysql_qry);

while($row = mysqli_fetch_assoc($result))
	{
		$output[]=$row;
	}
	
print(json_encode($output));

mysqli_close($dbc);
?>