<?php 
require_once('../mysql_connect.php');
$place_id = $_POST['place_id'];
$user_id = $_POST['user_id'];


$visit_id="(select visit_id from visits where user_id=$user_id and place_id like '$place_id')";

$result = mysqli_query($dbc ,$visit_id);

while($row = mysqli_fetch_assoc($result)) 
 	{
        $output[]=$row;
 	}



if(mysqli_num_rows($result) > 0) {

print(json_encode($output));
}
else {
return ;
}
mysqli_close($dbc);
?>