<?php 
require_once('../mysql_connect.php');
$user_name = $_POST["user_name"];
$user_pass = $_POST["password"];
$mysql_qry = "select id from users where user_name like '$user_name' and password like '$user_pass';";
$result = mysqli_query($dbc ,$mysql_qry);

if(mysqli_num_rows($result) > 0) {

$row = mysqli_fetch_array($result);
echo $row['id'];
}
else {
echo  "0";
}
mysqli_close($dbc);
?>