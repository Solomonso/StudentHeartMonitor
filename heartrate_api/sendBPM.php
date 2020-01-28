<?php
 require_once "include/DB_Functions.php";
 $db = new DB_Functions();

 $response = array("error"=>FALSE);
 if(isset($_POST["heartrate"]))
 {
 	$heartrate = $_POST["heartrate"];

 	$send = $db->sendBPM($heartrate);

 	if($send)
 	{
 		$response["error"] = FALSE;
 		$response["send"]["heartrate"] = $send["heartrate"];
 		echo json_encode($response);
 	}
 	else
	{
        $response["error"] = TRUE;
        $response["error_msg"] = "Unable to send BPM. Please try again!";
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