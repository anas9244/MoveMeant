<?php 
require_once('../mysql_connect.php');
$user_name = "chris";
$user_pass = "12345";

$checkName_qry = "select user_name from users where user_name like '$user_name';";

$resultName_qry=mysqli_query($dbc ,$checkName_qry);

if(mysqli_num_rows($resultName_qry) == 0){

	$newUser_qry = "insert into users (user_name, password) VALUES ('$user_name', '$user_pass');";
	$resultnewUser_qry=mysqli_query($dbc ,$newUser_qry);

	if($dbc->query($resultnewUser_qry)===TRUE) {

			$newUserSel_qry= "select * from users where user_name like '$user_name';";
			$resultNewUserSel_qry=mysqli_query($dbc ,$newUserSel_qry);

			
			while($row = mysqli_fetch_assoc($resultNewUserSel_qry))
			{
				$output[]=$row;
			}
	

			print(json_encode($output));
	}else {
	echo  "error new visit" . $resultnewUser_qry . "<br>" . $dbc->error ;
	}
}
else{
return;
}


mysqli_close($dbc);
?>