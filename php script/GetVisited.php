<?php 
require_once('../mysql_connect.php');

$mysql_qry = "select place_id,COUNT(user_id)'NumOfUsers' from visits group by place_id order by NumOfUsers desc ;";

$result = mysqli_query($dbc ,$mysql_qry);

while($row = mysqli_fetch_assoc($result))
	{
		$output[]=$row;
	}
	
	
	print(json_encode($output));

	



mysqli_close($dbc);
?>