<?php
require "init.php";
$position = $_POST["pos"];

$sql = "SELECT `bsch`,`art` FROM `buechstabet` WHERE `pos` LIKE '$pos'";
$result = mysqli_query($con,$sql);
$response = array();

while($row = mysqli_fetch_array($result)){

array_push($response,array("besch"=>$row[0],"art"=>$row[1]));

}
echo json_encode(array("server_response"=>$response));
?>