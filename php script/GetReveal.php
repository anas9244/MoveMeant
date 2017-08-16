<?php 
require_once('../mysql_connect.php');

$place_id = "ChIJi2BzhcIapEcRhovt0zYFrT0";

$user_id = "5";







$visit_id="(select visit_id from visits where user_id=$user_id and place_id like '$place_id')";
$resultVisit = mysqli_query($dbc ,$visit_id);


 while($row = mysqli_fetch_assoc($resultVisit)) {
      $checkVisit=$row["visit_id"];
 }




		$CheckRevealFam_qry="(select reveal_fam from visits WHERE visit_id=$checkVisit)";
		$resultRevealFam = mysqli_query($dbc ,$CheckRevealFam_qry);

		while($rowFam_qry = mysqli_fetch_assoc($resultRevealFam)) {
      	 $CheckRevealFam=$rowFam_qry["reveal_fam"];
    	}


		if ($CheckRevealFam=="1")	{

				$getFam="select user_name,visits.user_id from users,visits,(SELECT user_id from group_users where member_id=$user_id AND group_id=1 UNION SELECT member_id from group_users where user_id=$user_id AND group_id=1) as Freinds  where place_id='$place_id' AND visits.user_id=Freinds.user_id AND users.id=visits.user_id AND reveal_fam=1";

					$resultFam = mysqli_query($dbc ,$getFam);

					while($rowFam = mysqli_fetch_assoc($resultFam))
					{
					$outputFam[]=$rowFam;
					}
			
				if(mysqli_num_rows($resultCo) > 0) {
					print(json_encode($outputFam));
				}
				else
					{

						$arr = array(array('no reveals' => 1));

						echo json_encode($arr);
					}
				

		}else
		{
			$arr = array(array('not revealed' => 1));

			echo json_encode($arr);
		}






mysqli_close($dbc);

?>