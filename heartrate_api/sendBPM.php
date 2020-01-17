<?php
	
 require_once "include/DB_Functions.php";
 $db = new DB_Functions();

 $response = array("error"=>FALSE);
 if(isset($_POST["StringBPM"]))
 {
 	$StringBPM = $_POST["StringBPM"];

 	$send = $db->sendBPM($StringBPM);

 	if($send)
 	{
 		$response["error"] = FALSE;
 		$response["send"]["StringBPM"] = $send["StringBPM"];
 		echo json_encode($response);
 	}
 	else
	{
        $response["error"] = TRUE;
        $response["error_msg"] = "Unknown error occurred in sending. Please try again!";
        echo json_encode($response);
	}
 }
 else 
 {
   // required post params is missing
   $response["error"] = TRUE;
   $response["error_msg"] = "Heartrate is missing!";
   echo json_encode($response);
 }

?>