<?php
	require_once 'include/DB_Functions.php';
	$db = new DB_Functions();

	$response = array("error" => FALSE);

		 $checkedIn = $db->getStudentThatAreCheckedIn();

		 if($checkedIn)
		{
			$response["error"] = FALSE;
			$response["lessonCode"]["inputLessonCode"] = $studentUser["inputLessonCode"];
			echo json_encode($reponse);
			
			$student_id = $_POST["student_id"];
			$studentId = $db->checkIn($student_Id);
			if($studentId)
			{
				$response["error"] = FALSE;
				$response["studentId"]["student_id"] = $studentId["student_id"];
				echo json_encode($response);
			}
			else
			{
				$response["error"] = TRUE;
   				$response["error_msg"] = "Unknown error occurred while checking in!";
   				echo json_encode($response);
			}
		
		else
		{
			// lesson code not found
			$response["error"] = TRUE;
			$response["error_msg"] = "No lesson with provided code found";
			echo json_encode($response);
		}
	}
	else 
	{
		   // required post params is missing
		   $response["error"] = TRUE;
		   $response["error_msg"] = "Please input the lesson code!";
		   echo json_encode($response);
	}
	
?>
