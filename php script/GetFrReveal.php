<?php 
require_once('../mysql_connect.php');

$place_id = $_POST['place_id'];

$user_id = $_POST['user_id'];







$visit_id="(select visit_id from visits where user_id=$user_id and place_id like '$place_id')";
$resultVisit = mysqli_query($dbc ,$visit_id);

			
 while($row = mysqli_fetch_assoc($resultVisit)) {
      $checkVisit=$row["visit_id"];
 }

$CheckRevealFr_qry="(select reveal_fr from visits WHERE visit_id=$checkVisit)";
		$resultRevealFr = mysqli_query($dbc ,$CheckRevealFr_qry);

		while($rowFr_qry = mysqli_fetch_assoc($resultRevealFr)) {
      	 $CheckRevealFr=$rowFr_qry["reveal_fr"];
    	}



		if ($CheckRevealFr=="1")	{

				$getFr="select user_name,visits.user_id from users,visits,(SELECT user_id from group_users where member_id=$user_id AND group_id=4 UNION SELECT member_id from group_users where user_id=$user_id AND group_id=4) as Freinds  where place_id='$place_id' AND visits.user_id=Freinds.user_id AND users.id=visits.user_id AND reveal_fr=1";

					$resultFr = mysqli_query($dbc ,$getFr);

					while($rowFr = mysqli_fetch_assoc($resultFr))
					{
					$outputFr[]=$rowFr;
					}


					if(mysqli_num_rows($resultFr) > 0) {
					print(json_encode($outputFr));
					}
					else
					{

						$arr = array(array('no_reveals' => 'No one from friends group has revealed thier id'));

						echo json_encode($arr);
					}
				

		}else
		{
			$arr = array(array('not_revealed' => 'Plesae reveal your idenity to view who reveald from family'));

			echo json_encode($arr);
		}

	







mysqli_close($dbc);

?>