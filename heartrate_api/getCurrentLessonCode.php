<?php
	
 require_once "include/DB_Functions.php";
 $db = new DB_Functions();

 $response = array("error"=>FALSE);
 if(isset($_POST["lesson_code"]))
 {
 	$lesson_code = $_POST["lesson_code"];

 	$user = $db->getLessonCodeForStudent($lesson_code);

 	if($user != false)
 	{
 		$response["error"] = FALSE;
 		$response["user"]["lesson_code"] = $user["lesson_code"];
 		echo json_encode($response);
 	}
 	else
	{
		// user is not found with the credentials
        $response["error"] = TRUE;
        $response["error_msg"] = "Lesson Code entered is incorrect. Enter the currrent lesson code";
        echo json_encode($response);
	}
 }
 else 
 {
	   // required post params is missing
	   $response["error"] = TRUE;
	   $response["error_msg"] = "Required parameters lesson code missing";
	   echo json_encode($response);
 }

?>