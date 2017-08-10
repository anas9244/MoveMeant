<?php 
require_once('../mysql_connect.php');

$mysql_qry = "select place_id,COUNT(user_id)'NumOfUsers' from places_users group by place_id;";

$result = mysqli_query($dbc ,$mysql_qry);

while($row = mysqli_fetch_assoc($result))
	{
		$output[]=$row;
	}
	
print(json_encode($output));

mysqli_close($dbc);
?>