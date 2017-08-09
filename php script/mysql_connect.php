<?php
DEFINE ('DB_USER', 'studentweb');
DEFINE ('DB_PASSWORD', 'a1n2a3s4');
DEFINE ('DB_HOST', 'localhost');
DEFINE ('DB_NAME', 'movemeantdb');

$dbc = @mysqli_connect(DB_HOST, DB_USER, DB_PASSWORD, DB_NAME)
OR die('Could not connect to MySQL: ' .
mysqli_connect_error());


?>