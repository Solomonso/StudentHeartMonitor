<?php
	
 require_once "include/DB_Functions.php";
 $db = new DB_Functions();

 $response = array("error"=>FALSE);
 if(isset($_POST["student_email"]) && isset($_POST["student_password"]))
 {
 	$student_email = $_POST["student_email"];
 	$student_password = $_POST["student_password"];

 	$user = $db->getStudentByEmailAndPassword($student_email,$student_password);

 	if($user != false)
 	{
 		$response["error"] = FALSE;
 		$response["uid"] = $user["unique_id"];
 		$response["user"]["student_username"] = $user["student_username"];
 		$response["user"]["student_email"] = $user["student_email"];
 		$response["user"]["encrypted_password"] = $user["encrypted_password"];
 		echo json_encode($response);
 	}
 	else
	{
		// user is not found with the credentials
        $response["error"] = TRUE;
        $response["error_msg"] = "Login credentials are wrong. Please try again!";
        echo json_encode($response);
	}
 }
 else 
 {
	   // required post params is missing
	   $response["error"] = TRUE;
	   $response["error_msg"] = "Required parameters name or password is missing!";
	   echo json_encode($response);
 }

?>