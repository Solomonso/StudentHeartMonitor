<?php
	
	class DB_Functions
	{
		private $conn;

		function __construct()
		{
			require_once 'DB_Connect.php';

			//connecting to db
			$db = new Db_Connect();
			$this->conn = $db->connect();
		}
			//destructor
			function __destruct()
			{

			}
			
		/*storing the teacher information
		 *returns teacher details
		*/

		//storing teacher information in the database
		public function storeTeacherDetails($teacherName, $teacherEmail, $teacherPass)
		{
			$uuid = uniqid('', true);
			//Encrypting password
        	$hash = $this->hashSSHA($teacherPass);
        	$encrypted_password = $hash["encrypted"]; // encrypted password
        	$salt = $hash["salt"]; // salt

			$stmt = $this->conn->prepare("INSERT INTO teacher(teacher_id, unique_id,teacher_username,teacher_email,encrypted_password,salt) VALUES(NULL,?,?,?,?,?)");
			$stmt->bind_param("sssss",$uuid,$teacherName,$teacherEmail,$encrypted_password,$salt);
			$result = $stmt->execute();
			$stmt->close();

			//check for successfull store
			if($result)
			{
				$stmt = $this->conn->prepare("SELECT * FROM teacher WHERE teacher_email = ?");
				$stmt->bind_param("s",$teacherEmail);
				$stmt->execute();
				$teacherInfo = $stmt->get_result()->fetch_assoc();
				$stmt->close();

				return $teacherInfo;
			}
			else 
			{
				return false;
			}	
		}

		//get teacher details by username and password
		public function getTeacherByEmailAndPassword($teacherEmail, $teacherPass)
		{
			$stmt = $this->conn->prepare("SELECT * FROM teacher WHERE teacher_email = ?");
			
			$stmt->bind_param("s",$teacherEmail);

			if($stmt->execute())
			{
				$teacherInfo = $stmt->get_result()->fetch_assoc();
				$stmt->close();

				 // verifying user password
            	$salt = $teacherInfo['salt'];
            	$encrypted_password = $teacherInfo['encrypted_password'];
            	//use the salt to help in decrypting the password
            	$hash = $this->checkhashSSHA($salt, $teacherPass);


            	 // check for password equality
            	if ($encrypted_password == $hash)
            	{
					return $teacherInfo;
				}

			}else{ return NULL;}
		}

		//check if teacher details already existed
		public function isTeacherUserExisted($teacherEmail)
		{
			$stmt = $this->conn->prepare("SELECT teacher_email FROM teacher WHERE teacher_email = ?");
			
			$stmt->bind_param("s",$teacherEmail);

			$stmt->execute();

			//for the rows
			$stmt->store_result();

			if($stmt->num_rows > 0)
			{
				$stmt->close();
				return true;

			}
			else
			{
				$stmt->close(); 
				return false;
			}
		}
			/**
	     * Encrypting password
	     * @param password
	     * returns salt and encrypted password
	     */
	    //
	    public function hashSSHA($password) 
	    {

	        $salt = sha1(rand());
	        $salt = substr($salt, 0, 10);
	        $encrypted = base64_encode(sha1($password . $salt, true) . $salt);
	        $hash = array("salt" => $salt, "encrypted" => $encrypted);
	        return $hash;
	    }

	    /**
	     * Decrypting password
	     * @param salt, password
	     * returns hash string
	     */
	    public function checkhashSSHA($salt, $password) 
	    {

	        $hash = base64_encode(sha1($password . $salt, true) . $salt);

	        return $hash;
	    }


		/*storing the student information
		 *returns student details
		*/

		//storing student information in the database
		public function storeStudentDetails($studentName, $studentEmail, $studentPass)
		{

			$uuid = uniqid('', true);
			//Encrypting password
        	$hash = $this->hashSSHA($studentPass);
        	$encrypted_password = $hash["encrypted"]; // encrypted password
        	$salt = $hash["salt"]; // salt

			$stmt = $this->conn->prepare("INSERT INTO student(student_id, unique_id,student_username,student_email,encrypted_password,salt) VALUES(NULL,?,?,?,?,?)");
			$stmt->bind_param("sssss",$uuid,$studentName,$studentEmail,$encrypted_password,$salt);
			$result = $stmt->execute();
			$stmt->close();

			//check for successfull store
			if($result)
			{
				$stmt = $this->conn->prepare("SELECT * FROM student WHERE student_email = ?");
				$stmt->bind_param("s",$studentEmail);
				$stmt->execute();
				$studentInfo = $stmt->get_result()->fetch_assoc();
				$stmt->close();

				return $studentInfo;
			}
			else 
			{
				return false;
			}	
		}

		//get student details by username and password
		public function getStudentByEmailAndPassword($studentEmail, $studentPass)
		{
			$stmt = $this->conn->prepare("SELECT * FROM student WHERE student_email = ?");
			
			$stmt->bind_param("s",$studentEmail);

			if($stmt->execute())
			{
				$studentInfo = $stmt->get_result()->fetch_assoc();
				$stmt->close();

				 // verifying user password
            	$salt = $studentInfo['salt'];
            	$encrypted_password = $studentInfo['encrypted_password'];
            	//use the salt to help in decrypting the password
            	$hash = $this->checkhashSSHA($salt, $studentPass);


            	 // check for password equality
            	if ($encrypted_password == $hash)
            	{
					return $studentInfo;
				}

			}else{ return NULL;}
		}

		//check if student details already exist
		public function isStudentUserExisted($studentEmail)
		{
			$stmt = $this->conn->prepare("SELECT student_email FROM student WHERE student_email = ?");
			
			$stmt->bind_param("s",$studentEmail);

			$stmt->execute();

			//for the rows
			$stmt->store_result();

			if($stmt->num_rows > 0)
			{
				$stmt->close();
				return true;

			}
			else
			{
				$stmt->close(); 
				return false;
			}
		}

		//storing the lesson code in the database
		function storeLessonDetails($lessonCode,$lessonName)
		{
			$stmt = $this->conn->prepare("INSERT INTO lesson (lesson_id,lesson_code,lesson_name,lesson_date) VALUES(NULL,?,?,NULL)");
			$stmt->bind_param("ss",$lessonCode,$lessonName);
			$result = $stmt->execute();
			$stmt->close();

			//check for successfull store
			if($result)
			{
				$stmt = $this->conn->prepare("SELECT lesson_code,lesson_name FROM lesson ORDER BY lesson_id DESC LIMIT 1");
				$stmt->bind_result($lessonCode,$lessonName);
				$stmt->execute();
				$lessonInfo = $stmt->get_result()->fetch_assoc();
				$stmt->close();

				return $lessonInfo;
			}
			else 
			{
				return false;
			}	
		}
		
		//get LessonCode for student
		public function getLessonCode($inputLessonCode)
		{
			$stmt = $this->conn->prepare("SELECT lesson_code FROM lesson WHERE lesson_code = ?");
			$stmt->bind_param("s",$inputLessonCode);

			if($stmt->execute())
			{
				$currentLessonCode = $stmt->get_result()->fetch_assoc();
				$stmt->close();

					return $currentLessonCode;

			}else{ return NULL;}
		}
		
		//checkin to lesson
		public function checkIn($intStudent_id)
		{
			$stmt = $this->conn->prepare("UPDATE heartrate SET isCheckIn = "true" WHERE student_id = ?");
			$stmt->bind_param("s",$intStudent_id);
			$result = $stmt->execute();
			$stmt->close();
			
			if($result)
			{
				$stmt = $this->conn->prepare("SELECT isCheckIn FROM heartrate WHERE student_id = ?");
				$stmt->bind_result($isCheckin);
				$stmt->execute();
				$lessonInfo = $stmt->get_result()->fetch_assoc();
				$stmt->close();

				return $isCheckin;
			}
			else 
			{
				return false;
			}
		}

		//sending BPM to db
		public function sendBPM($StringBPM)
		{
			$stmt = $this->conn->prepare("UPDATE heartrate SET isCheckIn = true WHERE student_id = ?");
			$result = $stmt->execute();
			$stmt->close();
			
			if($result)
			{
				$stmt = $this->conn->prepare("SELECT isCheckIn FROM heartrate WHERE student_id = ?");
				$stmt->execute();
				$lessonInfo = $stmt->get_result()->fetch_assoc();
				$stmt->close();

				return $isCheckin;
			}
			else 
			{
				return false;
			}
		}
		
		//getting student that are checked in
		public function getStudentThatAreCheckedIn()
		{
			$stmt = $this->conn->prepare("SELECT student.student_id, student.student_username, heartrate.heartrate 
										 FROM student 
										 INNER JOIN heartrate
										 ON student.student_id = heartrate.student_id
										 WHERE heartrate.isCheckIn = "true"");
			$stmt->bind_param("sss", $studentId, $studentName, $BPM);
			if($stmt->execute())
			{
				$stmt->store_result();
				$studentsCheckedIn = $stmt->get_result()->fetch_assoc();
				$stmt->close();
			}else{ return NULL;}
		}
		
	}
	
?>