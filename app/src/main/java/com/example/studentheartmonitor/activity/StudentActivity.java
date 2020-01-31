package com.example.studentheartmonitor.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.UUID;

import java.text.DateFormat;
import java.util.Calendar;

import android.os.Handler;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.studentheartmonitor.R;
import com.example.studentheartmonitor.app.AppConfig;
import com.example.studentheartmonitor.app.AppController;
import com.example.studentheartmonitor.helper.SQLiteHandler;
import com.example.studentheartmonitor.helper.SessionManager2;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;


public class StudentActivity extends AppCompatActivity {
    private static final String TAG = StudentActivity.class.getSimpleName();
    private static final String TAG2 = "bluetooth2";
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private TextView txtName;
    private ProgressDialog pDialog;
    private TextView txtCode;
    private TextView BPM;
    private TextView avgBPM;
    private int currentBPM = 0;
    public static boolean isCheckIn;
    private Button btnOn;
    private Button btnOff;
    private Handler h;
    private String sBPM;

    final int RECIEVE_MESSAGE = 1;        // Status  for Handler
    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private StringBuilder sb = new StringBuilder();

    private ConnectedThread mConnectedThread;

    // SPP UUID service
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    // MAC-address of Bluetooth module (you must edit this line)
                                        //98D331901B17

    private static String address = "98:D3:31:90:1B:17";

    private SQLiteHandler db;
    private SessionManager2 session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //giving error
       // session.checkLogin();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        btnOn = findViewById(R.id.btnOn);
        //btnOff = findViewById(R.id.btnOff);
        BPM = findViewById(R.id.BPM);

        h = new Handler() {
            public void handleMessage(android.os.Message msg) {
                switch (msg.what) {
                    case RECIEVE_MESSAGE:                                                   // if receive massage
                        byte[] readBuf = (byte[]) msg.obj;
                        String strIncom = new String(readBuf, 0, msg.arg1);                 // create string from bytes array
                        sb.append(strIncom);                                                // append string
                        int endOfLineIndex = sb.indexOf("\r\n");                            // determine the end-of-line
                        if (endOfLineIndex > 0) {                                            // if end-of-line,
                           sBPM = sb.substring(0, endOfLineIndex);               // extract string
                            sb.delete(0, sb.length());                                      // and clear
                            //String currentBPM = "";
                            //currentBPM = currentBPM + sbprint;
                            //int i = Integer.parseInt(sBPM);
                            BPM.setText("BPM is: " +  sBPM);            // update TextView
                            //if it is this, nothing get sent to the database
                            if(sBPM.equalsIgnoreCase("Remove and place finger") || sBPM.equalsIgnoreCase("oveove and place finger") || sBPM.equalsIgnoreCase("momove and place finger") || sBPM.equalsIgnoreCase("eemove and place finger") || sBPM.equalsIgnoreCase("Heartbeat Detection sample code.") || sBPM.equalsIgnoreCase("No connectionsNo connectionsNo connectionsNo connectionsNo connections 50") || sBPM.equalsIgnoreCase("No connectionsRemove and place finger") || sBPM.equalsIgnoreCase(" place finger"))
                            {

                            }
                            else {
                                    //send to sqlite
                                    db.addHeartRate(sBPM);
                                    //send to mysql
                                    sendBPM(sBPM);
                                }

//                            if(!sBPM.equalsIgnoreCase("Remove and place finger") || !sBPM.equalsIgnoreCase("oveove and place finger") || !sBPM.equalsIgnoreCase("momove and place finger") || !sBPM.equalsIgnoreCase("eemove and place finger"))
//                            {
//                                sendBPM(sBPM);
//                                db.addHeartRate(sBPM);
//                            }
                            //else{ }


                            // btnOff.setEnabled(true);
                            btnOn.setEnabled(true);

                        }
                        //Log.d(TAG2, "...String:"+ sb.toString() +  "Byte:" + msg.arg1 + "...");
                        break;
                }
            }
        };

        btAdapter = BluetoothAdapter.getDefaultAdapter();       // get Bluetooth adapter
        checkBTState();

        btnOn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                btnOn.setEnabled(false);
                mConnectedThread.write("1");    // Send "1" via Bluetooth
                //Toast.makeText(getBaseContext(), "Turn on LED", Toast.LENGTH_SHORT).show();
            }
        });

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        //toolbar
        toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        //adding the action bar
        final ActionBar actionBar = getSupportActionBar();
        if(actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }


        //menus in the navigation
        drawerLayout = findViewById(R.id.drawer_layout_student);
        navigationView = findViewById(R.id.navigationView);

        //get the navigation items selected on the menu toolbar
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId())
                {
                    case R.id.nav_join_lesson:
                        menuItem.setChecked(true);
                        openJoinLesson();
                        drawerLayout.closeDrawers();
                        return true;


                    case R.id.nav_log_out_student:
                        menuItem.setChecked(true);
                        logoutUser();
                        drawerLayout.closeDrawers();
                        return true;
                }
                return false;
            }
        });
        //store date details
        Calendar calendar = Calendar.getInstance();
        String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());

        //to display the date
        TextView textViewDate = findViewById(R.id.text_view_date2);
        textViewDate.setText(currentDate);

        //get the name and email by id
           txtName = findViewById(R.id.name);
       // txtCode =  findViewById(R.id.lessonCodeView);

        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager2(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }
        // Fetching lesson details from SQLite
        //HashMap<String,String> lesson = db.getUserLessonDetails();

        //String lessonCode = lesson.get("lesson_code");
        //txtCode.setText(lessonCode);

        // Fetching user details from SQLite
        HashMap<String, String> user = db.getStudentDetails();

        String name = user.get("student_username");

        // Displaying the user details on the screen
        txtName.setText(name);

//        showBPM();
//       showAvgBPM();
//         String StringBPM = Integer.toString(currentBPM);
//         sendBPM(StringBPM);

    }

    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
        if(Build.VERSION.SDK_INT >= 10){
            try {
                final Method  m = device.getClass().getMethod("createInsecureRfcommSocketToServiceRecord", new Class[] { UUID.class });
                return (BluetoothSocket) m.invoke(device, MY_UUID);
            } catch (Exception e) {
                Log.e(TAG2, "Could not create Insecure RFComm Connection",e);
            }
        }
        return  device.createRfcommSocketToServiceRecord(MY_UUID);
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.d(TAG2, "...onResume - try connect...");

        // Set up a pointer to the remote node using it's address.
        BluetoothDevice device = btAdapter.getRemoteDevice(address);

        // Two things are needed to make a connection:
        //   A MAC address, which we got above.
        //   A Service ID or UUID.  In this case we are using the
        //     UUID for SPP.

        try {
            btSocket = createBluetoothSocket(device);
        } catch (IOException e) {
            errorExit("Fatal Error", "In onResume() and socket create failed: " + e.getMessage() + ".");
        }

        // Discovery is resource intensive.  Make sure it isn't going on
        // when you attempt to connect and pass your message.
        btAdapter.cancelDiscovery();

        // Establish the connection.  This will block until it connects.
        Log.d(TAG2, "...Connecting...");
        try {
            btSocket.connect();
            Log.d(TAG2, "....Connection ok...");
        } catch (IOException e) {
            try {
                btSocket.close();
            } catch (IOException e2) {
                errorExit("Fatal Error", "In onResume() and unable to close socket during connection failure" + e2.getMessage() + ".");
            }
        }

        // Create a data stream so we can talk to server.
        Log.d(TAG2, "...Create Socket...");

        mConnectedThread = new ConnectedThread(btSocket);
        mConnectedThread.start();
    }


    @Override
    public void onPause() {
        super.onPause();

        Log.d(TAG2, "...In onPause()...");

        try     {
            btSocket.close();
        } catch (IOException e2) {
            errorExit("Fatal Error", "In onPause() and failed to close socket." + e2.getMessage() + ".");
        }
    }

    private void checkBTState() {
        // Check for Bluetooth support and then check to make sure it is turned on
        // Emulator doesn't support Bluetooth and will return null
        if(btAdapter==null) {
            errorExit("Fatal Error", "Bluetooth not support");
        } else {
            if (btAdapter.isEnabled()) {
                Log.d(TAG2, "...Bluetooth ON...");
            } else {
                //Prompt user to turn on Bluetooth
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);
            }
        }
    }

    private void errorExit(String title, String message){
        Toast.makeText(getBaseContext(), title + " - " + message, Toast.LENGTH_LONG).show();
        finish();
    }

    private class ConnectedThread extends Thread {
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the input and output streams, using temp objects because
            // member streams are final
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) { }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[256];  // buffer store for the stream
            int bytes; // bytes returned from read()

            // Keep listening to the InputStream until an exception occurs
            while (true) {
                try {
                    // Read from the InputStream
                    bytes = mmInStream.read(buffer);        // Get number of bytes and message in "buffer"
                    h.obtainMessage(RECIEVE_MESSAGE, bytes, -1, buffer).sendToTarget();     // Send to message queue Handler
                } catch (IOException e) {
                    break;
                }
            }
        }

        /* Call this from the main activity to send data to the remote device */
        public void write(String message) {
            Log.d(TAG2, "...Data to send: " + message + "...");
            byte[] msgBuffer = message.getBytes();
            try {
                mmOutStream.write(msgBuffer);
            } catch (IOException e) {
                Log.d(TAG2, "...Error data send: " + e.getMessage() + "...");
            }
        }
    }


    List<Integer> heartBeat = new ArrayList<>();
    public void showBPM ()
    {
//        int currentBPM = 0;
//        heartBeat.add(currentBPM);
//
//        BPM.setText(currentBPM);

    }

    public void showAvgBPM ()
    {
//        int sum = 0;
//        int average;
//        for (int beat : this.heartBeat)
//        {
//            sum = sum + beat;
//        }
//        average = sum / heartBeat.size();
//        avgBPM = findViewById(R.id.AvgBPM);
//        avgBPM.setText(average);
    }

    /**
     * Function to store BPM in MySQL database
     * */
    private void sendBPM(final String StringBPM) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        pDialog.setMessage("Sending...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_SEND_BPM, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Sending: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
//                        String sen = jObj.getString("send");
//                        JSONObject send = jObj.getJSONObject("send");
//                        String bpm = send.getString("heartrare");

                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to url
                Map<String, String> params = new HashMap<>();
                params.put("heartrate", StringBPM);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }


    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    //method to make the action bar clickable
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //for testing. to display the current item clicked on the menu bar
    private void displayMessage(String message)
    {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

    /**
     * Logging out the user. Will set isLoggedIn flag to false in shared
     * preferences Clears the user data from sqlite users table
     * */
    private void logoutUser() {
        session.setLogin(false);

        db.deleteStudents();
        db.deleteHeartRate();

        // Launching the login activity
        Intent intent = new Intent(StudentActivity.this, LoginStudentPage.class);
        startActivity(intent);
        finish();
    }

    //student join lesson
    public void openJoinLesson()
    {
        //comment addd
        Intent intent = new Intent(StudentActivity.this, JoinLesson.class);
        startActivity(intent);
    }

}
