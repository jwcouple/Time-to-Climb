package be.kuleuven.timetoclimb.profile;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.io.IOException;

import be.kuleuven.timetoclimb.Home;
import be.kuleuven.timetoclimb.User;
import be.kuleuven.timetoclimb.databinding.ActivityProfileBinding;
import be.kuleuven.timetoclimb.dbConnection.DBConnector;
import be.kuleuven.timetoclimb.toolsInterface.imageResolver;

public class ProfileActivity extends AppCompatActivity implements imageResolver {

    private ActivityProfileBinding binding;
    private RequestQueue requestQueue;
    private TextView username, bio;
    private ImageView profileImage;
    private Uri imageUri;
    private Bitmap selectedImageBM;
    private String encodedImage;
    private Button btnUpdate;
    private String databaseUrl = "setProfileImage";
    private String retrieveImgUrl = "getProfileImage";
    private int PICK_IMAGE_REQUEST = 111;
    private User user = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        this.username = binding.txtProfileName;
        this.bio = binding.txtBioField;
        this.profileImage = binding.imgProfile;
        this.btnUpdate = binding.btnUpdate;

        requestQueue = Volley.newRequestQueue(this);

        this.user = (User) getIntent().getSerializableExtra("User");
        this.username.setText(user.getUsername());



        username.setOnClickListener(new View.OnClickListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, EditNameActivity.class);
                intent.putExtra("userName", username.getText().toString());
                //startActivity(intent);
                startActivityForResult(intent, 0);
            }

        });
        bio.setOnClickListener(new View.OnClickListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, BioEditActivity.class);
                intent.putExtra("bio", bio.getText().toString());
                //startActivity(intent);
                startActivityForResult(intent, 1);
            }
        });
        profileImage.setOnClickListener(new View.OnClickListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                //not finished yet
                startActivityForResult(intent, 45);
            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                DBConnector dbConnector = new DBConnector(getApplicationContext());
                try {
                    dbConnector.ProfileImageUploadRequest(databaseUrl,username.getText().toString().trim(),selectedImageBM);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Intent intentToMain = new Intent(ProfileActivity.this, Home.class);
                intentToMain.putExtra("User",user);
                startActivity(intentToMain);
            }
        });

    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        DBConnector dbConnector = new DBConnector(getApplicationContext());
        dbConnector.ProfileImageRetrieveRequest(retrieveImgUrl, username.getText().toString().trim(), profileImage);
        //System.out.println("username from user: "+ user.getUsername() + " password from user: " + user.getPassword()+ " profileImage from user: " + user.getProfileImage());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0:
                if (resultCode == RESULT_OK) {
                    this.username.setText(data.getStringExtra("profileName"));
                }
                break;
            case 1:
                if (resultCode == RESULT_OK) {
                    this.bio.setText(data.getStringExtra("bio"));
                }
                break;
            case 45:
                if (resultCode == RESULT_OK && data != null) {
                    try {

                        this.imageUri = data.getData();
                        this.selectedImageBM = uriToBitmap(imageUri);
                        //user
                        user.setProfileImage(bitmapToString(selectedImageBM));
                    } catch (IOException e) {
                        Log.d("profileImage",e.toString());
                    }
                    //Setting image to ImageView
                    profileImage.setImageBitmap(this.selectedImageBM);

                    return;
                }
                break;
        }
    }
}

