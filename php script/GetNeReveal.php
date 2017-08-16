<?php 
require_once('../mysql_connect.php');

$place_id = $_POST['place_id'];

$user_id = $_POST['user_id'];






$visit_id="(select visit_id from visits where user_id=$user_id and place_id like '$place_id')";
$resultVisit = mysqli_query($dbc ,$visit_id);

			
 while($row = mysqli_fetch_assoc($resultVisit)) {
      $checkVisit=$row["visit_id"];
 }


$CheckRevealNe_qry="(select reveal_ne from visits WHERE visit_id=$checkVisit)";
		$resultRevealNe = mysqli_query($dbc ,$CheckRevealNe_qry);

		while($rowNe_qry = mysqli_fetch_assoc($resultRevealNe)) {
      	 $CheckRevealNe=$rowNe_qry["reveal_ne"];
    	}

    	if ($CheckRevealNe=="1")	{

				$getNe="select user_name,visits.user_id from users,visits,(SELECT user_id from group_users where member_id=$user_id AND group_id=3 UNION SELECT member_id from group_users where user_id=$user_id AND group_id=3) as Freinds  where place_id='$place_id' AND visits.user_id=Freinds.user_id AND users.id=visits.user_id AND reveal_ne=1";

					$resultNe = mysqli_query($dbc ,$getNe);

					while($rowNe = mysqli_fetch_assoc($resultNe))
					{
					$outputNe[]=$rowNe;
					}

					if(mysqli_num_rows($resultNe) > 0) {
					print(json_encode($outputNe));
					}
					else
					{

						$arr = array(array('no_reveals' => 'No one from neighbors group has revealed thier id'));

						echo json_encode($arr);
					}
				

		}else
		{
			$arr = array(array('not_revealed' => 'Plesae reveal your idenity to view who reveald from family'));

			echo json_encode($arr);
		}

	







mysqli_close($dbc);

?>