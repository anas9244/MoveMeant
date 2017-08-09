<html>
<head>
<title>Add Student</title>
</head>
<body>
<?php
 
if(isset($_POST['submit'])){
    
    $data_missing = array();
    
    if(empty($_POST['place_name'])){
 
        // Adds name to array
        $data_missing[] = 'Place Name';
 
    } else {
 
        // Trim white space from the name and store the name
        $p_name = $_POST['place_name'];
 
    }
 
    if(empty($_POST['type'])){
 
        // Adds name to array
        $data_missing[] = 'Type';
 
    } else{
 
        // Trim white space from the name and store the name
        $typ = $_POST['type'];
 
    }

	if(empty($_POST['location'])){
 
        // Adds name to array
        $data_missing[] = 'Location';
 
    } else{
 
        // Trim white space from the name and store the name
        $loc = $_POST['location'];
 
    }

    
    if(empty($data_missing)){
        
        require_once('../mysql_connect.php');
        
        $query = "INSERT INTO places (place_name, type, location) VALUES (?, ?, ?)";
        
        $stmt = mysqli_prepare($dbc, $query);
        
        mysqli_stmt_bind_param($stmt, "sss", $p_name, $typ, $loc);
        
        mysqli_stmt_execute($stmt);
        
        $affected_rows = mysqli_stmt_affected_rows($stmt);
        
        if($affected_rows == 1){
            
            echo 'Place Entered';
            
            mysqli_stmt_close($stmt);
            
            mysqli_close($dbc);
            
        } else {
            
            echo 'Error Occurred<br />';
            echo mysqli_error();
            
            mysqli_stmt_close($stmt);
            
            mysqli_close($dbc);
            
        }
        
    } else {
        
        echo 'You need to enter the following data<br />';
        
        foreach($data_missing as $missing){
            
            echo "$missing<br />";
            
        }
        
    }
    
}
 
?>
 
<form action="http://localhost:80/PlaceAdded.php" method="post">
 
<b>Add a New Places</b>
 
<p>Place name:
<input type="text" name="place_name" size="100" value="" />
</p>
 
<p>Type:
<input type="text" name="type" size="30" value="" />
</p>

<p>Location:
<input type="text" name="location" size="30" value="" />
</p>
 
<p>
<input type="submit" name="submit" value="Send" />
</p>
 
</form>

</body>
</html>