<?php
require "init.php";
$sql = "SELECT `word` FROM `buechstabet`";
$result = mysqli_query($con,$sql);
$response = array();

while($row = mysqli_fetch_array($result)){

array_push($response,array("wort"=>$row[0]));

}
echo json_encode(array("server_response"=>$response));

?>