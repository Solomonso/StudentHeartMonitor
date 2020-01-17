<?php
	
	require_once 'include/DB_Functions.php';
	$db = new DB_Functions();

	$response = array("error" => FALSE);

	if(isset($_POST["lesson_code"]) && isset($_POST["lesson_name"]))
	{
		 // receiving the post params
		$lesson_code = $_POST["lesson_code"];
		$lesson_name = $_POST["lesson_name"];
	
			// create a new lesson
			$user = $db->storeLessonDetails($lesson_code,$lesson_name);
			if($user)
			{
				//store the lesson details
				$response["error"] = FALSE;
				$response["user"]["lesson_code"] = $user["lesson_code"];
				$response["user"]["lesson_name"] = $user["lesson_name"];
				echo json_encode($response);	
			}
			else
			{
				$response["error"] = TRUE;
   				$response["error_msg"] = "Unknown error occurred in creating lesson!";
   				echo json_encode($response);
			}
	}
	else 
    {
    	$response["error"] = TRUE;
    	$response["error_msg"] = "Required parameters (lesson code or lesson name) is missing!";
    	echo json_encode($response);
  	}

?>