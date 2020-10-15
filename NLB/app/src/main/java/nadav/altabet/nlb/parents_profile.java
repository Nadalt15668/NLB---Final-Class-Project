package nadav.altabet.nlb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class parents_profile extends AppCompatActivity {

    TextView profile_name, profile_email, profile_id,profile_address, profile_home_branch, profile_phone,
            profile_gender, profile_date_of_birth;
    ImageView profile_img;
    Button btnUpdate;
    private String mypath="gs://nlb-project-2287b.appspot.com";  // Image Path

    private void showImageFromFirebase()
    {

        User myuser=Client.getCurrentUser();
        String picname=myuser.getPic();
        String suffix=picname.substring(picname.lastIndexOf(".")+1);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl(mypath).child("Profiles").child(picname);
        try
        {
            final File localFile = File.createTempFile(picname, suffix);
            storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>()
            {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot)
                {
                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    profile_img.setImageBitmap(bitmap);
                }
            }).addOnFailureListener(new OnFailureListener()
            {
                @Override
                public void onFailure(@NonNull Exception exception)
                {
                    String message=exception.getMessage();
                    Toast.makeText(parents_profile.this, message, Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e ) {} catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parents_profile);
        profile_address = findViewById(R.id.profile_address);
        profile_date_of_birth = findViewById(R.id.profile_date_of_birth);
        profile_email = findViewById(R.id.profile_email);
        profile_gender = findViewById(R.id.profile_gender);
        profile_home_branch = findViewById(R.id.profile_home_branch);
        profile_id = findViewById(R.id.profile_id);
        profile_img = findViewById(R.id.profile_img);
        profile_phone = findViewById(R.id.profile_phone);
        profile_name = findViewById(R.id.profile_name);
        btnUpdate = findViewById(R.id.btnUpdate);

        profile_address.setText(Client.getCurrentUser().getAddress() + ", " + Client.getCurrentUser().getCity());
        String dateOfBirth = Client.getCurrentUser().getDate_of_birth().getDay() + "/" +
                Client.getCurrentUser().getDate_of_birth().getMonth() + "/" + Client.getCurrentUser().getDate_of_birth().getYear();
        profile_date_of_birth.setText(dateOfBirth);
        profile_email.setText(Client.getCurrentUser().getEmail());
        profile_gender.setText(Client.getCurrentUser().getGender());
        profile_home_branch.setText(Client.getCurrentUser().getBranch_name());
        profile_id.setText(Client.getCurrentUser().getID());
        profile_phone.setText(Client.getCurrentUser().getPhone());
        String fullName = Client.getCurrentUser().getFirst_name() + " " + Client.getCurrentUser().getLast_name();
        profile_name.setText(fullName);
        showImageFromFirebase();

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(parents_profile.this, parents_update.class));
            }
        });
    }
}