<?php

$db_name = "u357390212_atbr";
$user_name = "u357390212_atbr";
$user_pass = "pale2str";
$server_host = "sql19.hostinger.de";

$con = mysqli_connect($server_host,$user_name,$user_pass,db_name);
if(!$con){
	echo "Connection Error...".mysqli_connect_error();
	}
else{
	echo "<h3>Verbunden...</h3>";
	}	

?>