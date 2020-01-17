<?php
	require_once "include/DB_Functions.php";
	$db = new DB_Functions();

	$response = array("error" => FALSE);

	if(isset($_POST["student_username"]) && isset($_POST["student_email"]) && isset($_POST["student_password"]))
	{
		$student_username = $_POST["student_username"];
		$student_email = $_POST["student_email"];
		$student_password = $_POST["student_password"];

		 // check if user is already existed with the same email
		if($db->isStudentUserExisted($student_email))
		{
			$response["error"] = TRUE;
			$response["error_msg"] = "User already existed ".$student_email;
			echo json_encode($response);
		}
		else
		{
			//create a new user
			$user = $db->storeStudentDetails($student_username,$student_email,$student_password);
			if($user)
			{
				$response["error"] = FALSE;
				$response["uid"] = $user["unique_id"];
				$response["user"]["student_username"] = $user["student_username"];
				$response["user"]["student_email"] = $user["student_email"];
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