<?php 
require_once('../mysql_connect.php');
$user_name = $_POST['user_name'];
$user_pass = $_POST['password'];


$mysql_qry = "select id,user_name from users where user_name like '$user_name' and password like '$user_pass';";

$result = mysqli_query($dbc ,$mysql_qry);

while($row = mysqli_fetch_assoc($result))
	{
		$output[]=$row;
	}


if(mysqli_num_rows($result) > 0) {

print(json_encode($output));
}
else {
return;
}
mysqli_close($dbc);
?>