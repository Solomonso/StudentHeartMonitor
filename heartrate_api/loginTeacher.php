<?php
	
 require_once "include/DB_Functions.php";
 $db = new DB_Functions();

//json response array
 $response = array("error" => FALSE);

 if(isset($_POST["teacher_email"]) && isset($_POST["teacher_password"]))
 {
 	 //receiving the post params
 	$teacher_username = $_POST["teacher_email"];
 	$teacher_password = $_POST["teacher_password"];

 	 // get the user by email and password
 	$user = $db->getTeacherByEmailAndPassword($teacher_username,$teacher_password);

 	if($user != false)
 	{
 		// user is found
 		$response["error"] = FALSE;
 		$response["uid"] = $user["unique_id"];
 		$response["user"]["teacher_username"] = $user["teacher_username"];
 		$response["user"]["teacher_email"] = $user["teacher_email"];
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