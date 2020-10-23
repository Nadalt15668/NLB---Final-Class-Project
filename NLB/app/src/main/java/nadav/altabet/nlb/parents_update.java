package nadav.altabet.nlb;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

public class parents_update extends AppCompatActivity {

    private TextView update_fst, update_lst, update_city, update_address, update_phone;
    private Spinner update_home_branch;
    private ImageView update_img;
    private RadioButton male_update, female_update;
    private Button btnPickUpdate, btnDateUpdate, btnUpdate;
    private RadioGroup rdgp;
    private Calendar calendar = null;
    private int day,month,year,chosenYear = Client.getCurrentUser().getDate_of_birth().getYear(),chosenMonth = Client.getCurrentUser().getDate_of_birth().getMonth(),
            chosenDay = Client.getCurrentUser().getDate_of_birth().getDay();
    private ArrayList<String> branchName = new ArrayList<>();
    //----------------------------------------------------------
    private static Uri selectedPic = null;
    private static String picName = "";
    private static boolean chosen = false;
    //----------------------------------------------------------
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private Task<Void> reference=null;
    private final DatabaseReference branchReference = FirebaseDatabase.getInstance().getReference("Branches");
    private String mypath="gs://nlb-project-2287b.appspot.com";  // Image Path
    //----------------------------------------------------------
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
    private int getIndex(Spinner spinner, String myString){
        //הפעולה מחזירה את מיקום הסניף הבית הנבחר כדי לבחור בו ברגע שנכנסים לדף
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                return i;
            }
        }

        return 0;
    }

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
                    update_img.setImageBitmap(bitmap);
                }
            }).addOnFailureListener(new OnFailureListener()
            {
                @Override
                public void onFailure(@NonNull Exception exception)
                {
                    String message=exception.getMessage();
                    Toast.makeText(parents_update.this, message, Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e ) {} catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void addPic()
    {
        StorageReference PicturesStorage = storageReference.child("Profiles/"+picName);
        PicturesStorage.putFile(selectedPic).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(parents_update.this, "Profile Picture Has Been Added", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(parents_update.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && data!= null)
        {
            chosen = true;
            selectedPic = data.getData();
            update_img.setImageURI(selectedPic);
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedPic,filePathColumn,null,null,null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picName = cursor.getString(columnIndex);
            picName = picName.substring(picName.lastIndexOf('/')+1);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parents_update);

        update_fst = findViewById(R.id.fstUpdate);
        update_lst = findViewById(R.id.lstUpdate);
        update_city = findViewById(R.id.cityUpdate);
        update_address = findViewById(R.id.addressUpdate);
        update_phone = findViewById(R.id.phoneUpdate);
        update_home_branch = findViewById(R.id.homeBranchUpdate);
        update_img = findViewById(R.id.profileUpdate);
        male_update = findViewById(R.id.maleUpdate);
        female_update = findViewById(R.id.femaleUpdate);
        btnPickUpdate = findViewById(R.id.btnPickUpdate);
        btnDateUpdate = findViewById(R.id.btnDateUpdate);
        btnUpdate = findViewById(R.id.btnUpdate);
        rdgp = findViewById(R.id.update_rdgp);

        calendar = Calendar.getInstance();
        day  = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);

        branchReference.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for (DataSnapshot child: children) {
                    String branch = child.getValue(String.class);
                    branchName.add(branch);
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(parents_update.this, R.layout.customized_spinner, branchName);
                update_home_branch.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        update_address.setText(Client.getCurrentUser().getAddress());

        final String dateOfBirth = Client.getCurrentUser().getDate_of_birth().getDay() + "/" +
                Client.getCurrentUser().getDate_of_birth().getMonth() + "/" + Client.getCurrentUser().getDate_of_birth().getYear();
        btnDateUpdate.setText(dateOfBirth);
        update_home_branch.setSelection(getIndex(update_home_branch,Client.getCurrentUser().getBranch_name()));
        update_phone.setText(Client.getCurrentUser().getPhone());
        update_fst.setText(Client.getCurrentUser().getFirst_name());
        update_lst.setText(Client.getCurrentUser().getLast_name());
        if (Client.getCurrentUser().getGender().equals("זכר"))
            male_update.setChecked(true);
        else
            female_update.setChecked(true);
        update_city.setText(Client.getCurrentUser().getCity());
        showImageFromFirebase();

        btnDateUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(parents_update.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        btnDateUpdate.setText(dayOfMonth + "/" + (month+1) + "/" + year);
                        chosenDay = dayOfMonth;
                        chosenMonth = month+1;
                        chosenYear = year;
                    }
                },year,month,day);
                datePickerDialog.show();
            }
        });

        btnPickUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI),1);
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User newUser = new User();
                newUser.setFirst_name(update_fst.getText().toString());
                newUser.setLast_name(update_lst.getText().toString());
                newUser.setEmail(Client.getCurrentUser().getEmail());
                newUser.setAddress(update_address.getText().toString());
                newUser.setCity(update_city.getText().toString());
                newUser.setPhone(update_phone.getText().toString());
                newUser.setBranch_name(update_home_branch.getSelectedItem().toString());
                if (rdgp.getCheckedRadioButtonId()==male_update.getId())
                    newUser.setGender("male");
                else
                    newUser.setGender("female");
                Date date = new Date(chosenDay,chosenMonth,chosenYear);
                newUser.setDate_of_birth(date);
                newUser.setPic(Client.getCurrentUser().getPic());
                if (chosen)
                {
                    addPic();
                    newUser.setPic(picName);
                }
                reference = database.getReference("Parents").child(Client.getUID()).setValue(newUser);
                Client.setCurrentUser(newUser);
                Toast.makeText(parents_update.this, "User Information Has Updated Successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(parents_update.this,parents_profile.class));

            }
        });
    }
}