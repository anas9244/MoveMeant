<?php 
require_once('../mysql_connect.php');

$user_id = $_POST['user_id'];


$mysql_qry = "Select user_name,users.id from users where users.id not in (SELECT user_id from group_users where member_id=$user_id  UNION SELECT member_id from group_users where user_id=$user_id) and users.id<>$user_id";

$result = mysqli_query($dbc ,$mysql_qry);

while($row = mysqli_fetch_assoc($result))
	{
		$output[]=$row;
	}
	
if(mysqli_num_rows($result) > 0) {
					print(json_encode($output));
				}
				else
					{

						$arr = array(array('no_members' => 'No Members available'));

						echo json_encode($arr);
					}

mysqli_close($dbc);
?>