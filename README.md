The heart rate monitor is a mobile application developed to record the heart rate of students that are currently present in the classroom. 

When the application is started, there is an option to select if you are a teacher or a student. 

When the teacher is selected you can register as a new student or login if already registered. If the teacher is logged into the application, he or she can set a lesson with a name and code. 

When the student is logged into the application via selecting the student button he or she can check in with the current lesson code set by the teacher and this allows them to be marked present for that current lesson then can the teacher view the heart rate of the students that are checked into the lesson. if the students are not checked into the lesson the teacher cannot view the heart rate of that student.

The heart rate of the student is collected using a pulse sensor connected to an Arduino uno and transmitted to the mobile application via Bluetooth. The heart rate is recorded in MYSQL database.

Hardware used: Arduino Uno, Bluethooth module HC-06, pulse sensor.

**How data is collected:**

The Arduino uno is coded to receive pulse signal from the pulse sensor. The code calculates pulse signals to BPM rate. 

**How the app receives data:**

A Bluetooth module (HC-06) is wired to the Arduino uno. The purpose of the module is to send the BPM rates from the Arduino via Bluetooth for the app to process it as information. 

