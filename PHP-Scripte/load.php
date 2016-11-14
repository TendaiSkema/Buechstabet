<?php

require init.php

$sql = "SELECT * FROM `buechstabet`";
$result = mysqli_query($con,$sql);
$response = array();

while($row = mysqli_fetch_array($result)){

array_push($response,array("wort"=>$row[0],"besch"=>$row[1],"art"=>$row[2],"pos"=>$row[3]))

}
echo json_encode(array("server_response"=>$response));

?>