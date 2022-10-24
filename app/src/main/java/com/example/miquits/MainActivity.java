package com.example.miquits;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private EditText bookingNameEdt, bookingContactNumberEdt, bookingDateEdt, bookingTimeEdt;
    private Button submitBookingBtn;
    private String bookingName, bookingContactNumber, bookingDate, bookingTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bookingNameEdt = findViewById(R.id.booking_name);
        bookingContactNumberEdt = findViewById(R.id.booking_contactnumber);
        bookingDateEdt = findViewById(R.id.booking_date);
        bookingTimeEdt = findViewById(R.id.booking_time);
        submitBookingBtn = findViewById(R.id.booking_add_btn);

        submitBookingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bookingName = bookingNameEdt.getText().toString();
                bookingContactNumber = bookingContactNumberEdt.getText().toString();
                bookingDate = bookingDateEdt.getText().toString();
                bookingTime = bookingTimeEdt.getText().toString();

                if (TextUtils.isEmpty(bookingName)) {
                    bookingNameEdt.setError("Please provide your name.");
                } else if (TextUtils.isEmpty(bookingContactNumber)) {
                    bookingContactNumberEdt.setError("Please provide your contact number.");
                } else if (TextUtils.isEmpty(bookingDate)) {
                    bookingDateEdt.setError("Please provide your scheduled day.");
                } else if (TextUtils.isEmpty(bookingTime)) {
                    bookingTimeEdt.setError("Please provide your scheduled time.");
                } else {
                    addDataToDatabase(bookingName, bookingContactNumber, bookingDate, bookingTime);
                }
            }
        });
    }

    private void addDataToDatabase(String name, String contactNumber, String date, String time) {
        String url = "http://192.168.1.8/miquits/admin/bookings/add_bookings.php";

        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);

        // on below line we are calling a string
        // request method to post the data to our API
        // in this we are calling a post method.
        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("TAG", "RESPONSE IS " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    // on below line we are displaying a success toast message.
                    Toast.makeText(MainActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // and setting data to edit text as empty
                bookingNameEdt.setText("");
                bookingContactNumberEdt.setText("");
                bookingDateEdt.setText("");
                bookingTimeEdt.setText("");
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                Toast.makeText(MainActivity.this, "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public String getBodyContentType() {
                // as we are passing data in the form of url encoded
                // so we are passing the content type below
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() {

                // below line we are creating a map for storing
                // our values in key and value pair.
                Map<String, String> params = new HashMap<String, String>();

                // on below line we are passing our
                // key and value pair to our parameters.
                params.put("name", name);
                params.put("contact_number", contactNumber);
                params.put("date", date);
                params.put("time", time);

                // at last we are returning our params.
                return params;
            }
        };
        // below line is to make
        // a json object request.
        queue.add(request);
    }

//    public Connection connectionClass() {
//        Connection con = null;
//        String ip = "172.0.0.1", port ="3306", username="root", password="", database_name="miquits";
//        StrictMode.ThreadPolicy tp = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//        StrictMode.setThreadPolicy(tp);
//        try {
//            Class.forName("net.sourceforge.jtds.jdbc.Driver");
//            String connectionUrl = "jdbc:jtds:sqlserver://" + ip + ":" + port + ";databasename=" + database_name + ";User=" + username + ";password=" + password + ";";
//            con = DriverManager.getConnection(connectionUrl);
//        } catch (Exception exception) {
//            Log.e("Error", exception.getMessage());
//        }
//        return con;
//    }
}