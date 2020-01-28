<?php
	require_once 'include/DB_Functions.php';
	$db = new DB_Functions();

	$response = array("error" => FALSE);

	if(isset($_POST["lesson_code"]))
	{
		 $lesson_code = $_POST["lesson_code"];
		 $lessonCode = $db->getLessonCode($lesson_code);

		 if($lessonCode != false)
		{
			$response["error"] = FALSE;
			$response["lessonCode"]["lesson_code"] = $studentUser["lesson_code"];
			echo json_encode($reponse);
			
			$student_id = $_POST["student_id"];
			$intStudent_id = (int)$student_id;
			$studentId = $db->checkIn($intStudent_id);
			if($studentId)
			{
				$response["error"] = FALSE;
				$response["studentId"]["intStudent_id"] = $studentId["intStudent_id"];
				echo json_encode($response);
			}
			else
			{
				$response["error"] = TRUE;
   				$response["error_msg"] = "Unknown error occurred while checking in!";
   				echo json_encode($response);
			}
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
