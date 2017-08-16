<?php 
require_once('../mysql_connect.php');

$place_id = $_POST['place_id'];

$user_id = $_POST['user_id'];







$visit_id="(select visit_id from visits where user_id=$user_id and place_id like '$place_id')";
$resultVisit = mysqli_query($dbc ,$visit_id);

			
 while($row = mysqli_fetch_assoc($resultVisit)) {
      $checkVisit=$row["visit_id"];
 }



    	$CheckRevealCo_qry="(select reveal_co from visits WHERE visit_id=$checkVisit)";
		$resultRevealCo = mysqli_query($dbc ,$CheckRevealCo_qry);

		while($rowCo_qry = mysqli_fetch_assoc($resultRevealCo)) {
      	 $CheckRevealCo=$rowCo_qry["reveal_co"];
    	}
    	if ($CheckRevealCo=="1")	
    	{
				$getCo="select user_name,visits.user_id from users,visits,(SELECT user_id from group_users where member_id=$user_id AND group_id=2 UNION SELECT member_id from group_users where user_id=$user_id AND group_id=2) as Freinds  where place_id='$place_id' AND visits.user_id=Freinds.user_id AND users.id=visits.user_id AND reveal_co=1";

					$resultCo = mysqli_query($dbc ,$getCo);



					while($rowCo = mysqli_fetch_assoc($resultCo))
					{
					$outputCo[]=$rowCo;
					}

					if(mysqli_num_rows($resultCo) > 0) {
					print(json_encode($outputCo));
					}
					else
					{

						$arr = array(array('no_reveals' => 'No one from Co-workers group has revealed thier id'));

						echo json_encode($arr);
					}
				

		}else
		{
			$arr = array(array('not_revealed' => 'Plesae reveal your idenity to view who reveald from family'));

			echo json_encode($arr);
		}

	







mysqli_close($dbc);

?>