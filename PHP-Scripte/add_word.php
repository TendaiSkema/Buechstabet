<?php

require "init.php";
$word = $_POST["word"];
$pos = $_POST["pos"];
$besch = $_POST["besch"];
$art = $_POST["art"];

$sql_query = "insert into buechstabet values('$pos','$word','$besch','$art');";

if(mysqli_query($con,$sql_query)){
	echo "<h3>Hat gefunzt XD</h3>";
	}
else{
	echo "Einspeichern misslungen".mysqli_error($con);
	}
	
?>