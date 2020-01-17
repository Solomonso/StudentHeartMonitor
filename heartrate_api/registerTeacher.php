<?php
	require_once 'include/DB_Functions.php';
	$db = new DB_Functions();

	$response = array("error" => FALSE);

	if(isset($_POST["teacher_username"]) && isset($_POST["teacher_email"]) && isset($_POST["teacher_password"]))
	{
		 // receiving the post params
		$teacher_username = $_POST["teacher_username"];
		$teacher_email = $_POST["teacher_email"];
		$teacher_password = $_POST["teacher_password"];

		 // check if user is already existed with the same email
		if($db->isTeacherUserExisted($teacher_email))
		{
			$response["error"] = TRUE;
			$response["error_msg"] = "User already existed ".$teacher_email;
			echo json_encode($response);
		}
		else
		{
			
			// create a new user
			$user = $db->storeTeacherDetails($teacher_username,$teacher_email,$teacher_password);
			if($user)
			{
				//store the teacher details
				$response["error"] = FALSE;
				$response["uid"] = $user["unique_id"];
				$response["user"]["teacher_username"] = $user["teacher_username"];
				$response["user"]["teacher_email"] = $user["teacher_email"];
				//$response["user"]["encrypted_password"] = $user["encrypted_password"];
				echo json_encode($response);	
			}
			else
			{
				$response["error"] = TRUE;
   				$response["error_msg"] = "Unknown error occurred in registration!";
   				echo json_encode($response);
			}
		}
	}
	else 
    {
    	$response["error"] = TRUE;
    	$response["error_msg"] = "Required parameters (name or password) is missing!";
    	echo json_encode($response);
  	}

?>