<?php 
require_once('../mysql_connect.php');



$user_id = $_POST['user_id'];
$group_id= $_POST['group_id'];







				$members_qry="select user_name from users,(select user_id from group_users where member_id=$user_id AND group_id=$group_id UNION SELECT member_id from group_users where user_id=$user_id AND group_id=$group_id) as Members where users.id=Members.user_id";

					$result = mysqli_query($dbc ,$members_qry);



					while($row = mysqli_fetch_assoc($result))
					{
					$output[]=$row;
					}

					if(mysqli_num_rows($result) > 0) {
					print(json_encode($output));
					}
					else
					{

						$arr = array(array('no_members' => 'You do not share any user this group'));

						echo json_encode($arr);
					}
				

		

	







mysqli_close($dbc);

?>