package be.kuleuven.timetoclimb;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import be.kuleuven.timetoclimb.adapter.RecyclerAdapterViewDate;

public class AttendingEvents extends AppCompatActivity {
    private ArrayList<Event> eventList;
    private ArrayList<Climbinghall> climbinghalls;
    private String currentDateTime;
    private User user;
    private RecyclerView rvAttendingEvents;
    private RecyclerAdapterViewDate adapterViewDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attending_events);

        // Get intent extras
        Bundle extras = getIntent().getExtras();
        user = (User) extras.getSerializable("User");
        currentDateTime = extras.getString("currentDateTime");

        // Instantiate lists
        eventList = new ArrayList<>();
        climbinghalls = new ArrayList<>();

        // Populate lists from DB
        populateEventList();
    }

    public void populateEventList() {
        String requestURL = "https://studev.groept.be/api/a21pt411/getAttendingEvents";
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest submitRequest = new StringRequest(Request.Method.POST, requestURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject objResponse = new JSONObject();
                            for(int i = 0; i < jsonArray.length(); i++) {
                                objResponse = jsonArray.getJSONObject(i);
                                Event event = new Event(
                                        objResponse.getInt("id"),
                                        objResponse.getString("organiser"),
                                        objResponse.getInt("event_climbing_hall_id"),
                                        objResponse.getString("description_event"),
                                        objResponse.getString("title"),
                                        objResponse.getString("begin_datetime"),
                                        objResponse.getString("end_datetime")
                                );
                                eventList.add(event);
                                //addClimbinghall()
                            }
                            addClimbingHalls(eventList);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Database", "onErrorResponse: " + error.getLocalizedMessage());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", user.getUsername());
                params.put("currentdatetime", currentDateTime);
                return params;
            }
        };
        requestQueue.add(submitRequest);
    }

    public void addClimbingHalls(ArrayList<Event> events) {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        String requestURL = "https://studev.groept.be/api/a21pt411/getHallNameByID";
        for(int i = 0; i < events.size(); i++) {
            int finalI = i;
            int finalI1 = i;
            StringRequest stringRequestRequest = new StringRequest(Request.Method.POST, requestURL,
                    new Response.Listener<String>()
                    {
                        @Override
                        public void onResponse(String response)
                        {
                            VolleyLog.v("Response:%n %s", response);
                            System.out.println("Response climbinghalls: \n" + response);
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                // Set empty list if no attendees, otherwise add by iteration
                                JSONObject objResponse = jsonArray.getJSONObject(0);
                                Climbinghall climbinghall = new Climbinghall(objResponse);
                                climbinghalls.add(climbinghall);
                            } catch (JSONException e) {
                                System.out.println("error addclimb: " + e.getLocalizedMessage());
                            }
                            if(finalI1 == events.size() - 1) {
                                setAdapter();
                            }
                        }
                    },
                    new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error)
                        {
                            Log.d("Database" ,error.getLocalizedMessage(), error);
                            System.out.println("error: " + error.getLocalizedMessage());
                        }
                    }
            ) { //NOTE THIS PART: here we are passing the parameter to the webservice, NOT in the URL!
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("id", Integer.toString(events.get(finalI).getClimbingHallID()));
                    return params;
                }
            };
            requestQueue.add(stringRequestRequest);
        }
    }


    public void passEventAndLocation(Event e, Climbinghall climbinghall) {
        Intent intentViewEvent = new Intent(this, ViewEvent.class);
        intentViewEvent.putExtra("User", user);
        intentViewEvent.putExtra("Event", e);
        intentViewEvent.putExtra("hall_name", climbinghall.getHallName());
        intentViewEvent.putExtra("address", climbinghall.getAddress());
        startActivity(intentViewEvent);
    }

    public void setAdapter() {
        rvAttendingEvents = findViewById(R.id.rvAttendingEvents);
        adapterViewDate = new RecyclerAdapterViewDate(eventList, climbinghalls, new RecyclerAdapterViewDate.OnItemClickListener() {
            @Override
            public void onItemClick(Event event, Climbinghall climbinghall) {
                passEventAndLocation(event, climbinghall);
            }
        });

        // set layoutmanager, itemanimator, adapter
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        rvAttendingEvents.setLayoutManager(layoutManager);
        rvAttendingEvents.setItemAnimator(new DefaultItemAnimator());
        rvAttendingEvents.setAdapter(adapterViewDate);
    }

}