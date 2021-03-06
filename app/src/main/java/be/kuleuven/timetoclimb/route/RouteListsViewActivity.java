package be.kuleuven.timetoclimb.route;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import be.kuleuven.timetoclimb.R;
import be.kuleuven.timetoclimb.User;
import be.kuleuven.timetoclimb.databinding.ActivityRoutelistViewBinding;
import be.kuleuven.timetoclimb.dbConnection.DBConnector;
import be.kuleuven.timetoclimb.dbConnection.ServerCallback;

public class RouteListsViewActivity extends AppCompatActivity {

    private ActivityRoutelistViewBinding binding;
    private RecyclerView routeListRecyclerView;
    private RouteListRVAdapter routeListRVAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private DBConnector dbConnector;
    private List<Route> routeList = new ArrayList<>();
    private User user = null;
    private static final String RouteListsViewActivity_TAG = RouteListsViewActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRoutelistViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        this.user = (User) getIntent().getSerializableExtra("User");

        dbConnector = new DBConnector(RouteListsViewActivity.this);
        retrieveDataFromDB("getRouteAndHallName");

        //inflating main layout
        routeListRecyclerView = findViewById(R.id.routeListRecyclerView);
    }

    public void retrieveDataFromDB(String extendedUrl){
        //retrieve String objects
        dbConnector.JSONRequest(extendedUrl, new ServerCallback() {
            @Override
            public void onSuccess(JSONArray jsonArrayResponse) {

                //for each obj in the list, create a route, then added to routeList
                if(jsonArrayResponse.length() == 0){ //if there is no object in the array
                    Toast.makeText(RouteListsViewActivity.this,"there is no routes",Toast.LENGTH_LONG).show();
                    return;
                }

                for(int i = 0; i < jsonArrayResponse.length(); i++){

                    JSONObject jsonObject = null;

                    try {
                        jsonObject = jsonArrayResponse.getJSONObject(i);

                        String hallName = jsonObject.getString("hall_name");
                        int routeNo = Integer.parseInt(jsonObject.getString("route_nr"));
                        float grade = Float.parseFloat(jsonObject.getString("grade"));
                        String author = jsonObject.getString("author");

                        //for optional items
                        String description = null;
                        if(!jsonObject.isNull("description")){ description = jsonObject.getString("description");}
                        String b64String = null;
                        if(!jsonObject.isNull("route_picture")){ b64String = jsonObject.getString("route_picture");}

                        routeList.add(new Route(hallName,routeNo,grade, author,description,b64String));

                    } catch (JSONException e) {
                        System.out.println("object is empty");
                        e.printStackTrace();
                    }
                }

                //configure the corresponding adapter
                routeListRVAdapter = new RouteListRVAdapter(RouteListsViewActivity.this,routeList,routeListRecyclerView,user);
                routeListRecyclerView.setAdapter(routeListRVAdapter);
                routeListRecyclerView.setHasFixedSize(true);
                //set RV layout manager as linear since we have linear multiple
                routeListRecyclerView.setLayoutManager(new LinearLayoutManager(RouteListsViewActivity.this));
            }
        });
    }
}