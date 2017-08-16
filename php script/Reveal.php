<?php 
require_once('../mysql_connect.php');

$place_id = $_POST['place_id'];
$user_id = $_POST['user_id'];

$revealGroup= $_POST['revealGroup'];



$visit_id="(select visit_id from visits where user_id like '$user_id' and place_id like '$place_id')";
$resultVisit = mysqli_query($dbc ,$visit_id);


 while($row = mysqli_fetch_assoc($resultVisit)) {
       $checkVisit=$row["visit_id"];
    }





		if ($revealGroup=="1")	{

				$reveal_qry= "update visits SET reveal_fam=1 WHERE visit_id=$checkVisit;";



				if($dbc->query($reveal_qry)===TRUE) {

						$getFam="select user_name,visits.user_id from users,visits,(SELECT user_id from group_users where member_id=$user_id AND group_id=1 UNION SELECT member_id from group_users where user_id=$user_id AND group_id=1) as Freinds  where place_id='$place_id' AND visits.user_id=Freinds.user_id AND users.id=visits.user_id AND reveal_fam=1";

						$resultFam = mysqli_query($dbc ,$getFam);

						while($rowFam = mysqli_fetch_assoc($resultFam))
						{
						$outputFam[]=$rowFam;
						}
				
					if(mysqli_num_rows($resultFam) > 0) {
						print(json_encode($outputFam));
					}
					else
					{

						$arr = array(array('no_reveals' => 'No one from family group has revealed thier id'));

						echo json_encode($arr);
					}
			 	}
				else {
				echo  "error reveal  " . $reveal_qry . "<br>" . $dbc->error ;
				}

		}


		if ($revealGroup=="2")	{

				$reveal_qry= "update visits SET reveal_co=1 WHERE visit_id=$checkVisit;";



				if($dbc->query($reveal_qry)===TRUE) {

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
			 	}
				else {
				echo  "error reveal  " . $reveal_qry . "<br>" . $dbc->error ;
				}
				
		}

		if ($revealGroup=="3")	{

				$reveal_qry= "update visits SET reveal_ne=1 WHERE visit_id=$checkVisit;";



				if($dbc->query($reveal_qry)===TRUE) {

					
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
			 	}
				else {
				echo  "error reveal  " . $reveal_qry . "<br>" . $dbc->error ;
				}
				
		}

		if ($revealGroup=="4")	{

				$reveal_qry= "update visits SET reveal_fr=1 WHERE visit_id=$checkVisit;";



				if($dbc->query($reveal_qry)===TRUE) {

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
			 	}
				else {
				echo  "error reveal  " . $reveal_qry . "<br>" . $dbc->error ;
				}
				
		}




	

mysqli_close($dbc);

?>