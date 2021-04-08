package nadav.altabet.nlb;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.timepicker.ClockFaceView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;

public class add_child extends AppCompatActivity {

    private EditText fstNameChild, lstNameChild, idChild, classLetterChild, classNumberChild, schooolChild,  emailChild, phoneChild;
    private Spinner homeBranchChild;
    private RadioButton boyRadiob, girlRadiob;
    private RadioGroup rdgbChild;
    private ImageView profileChild;
    private Button btnDateChild, btnPickChild,btnSaveChild;
    private ArrayList<String> branchName = new ArrayList<>();
    private ProgressDialog prg;
    //-----------------------------------------
    private Calendar calendar = null;
    private int day,month,year,chosenYear,chosenMonth,chosenDay;
    //-----------------------------------------
    private static Uri selectedPic = null;
    private static String picName = "";
    private static boolean chosen = false;
    //-----------------------------------------
    private final DatabaseReference branchReference = FirebaseDatabase.getInstance().getReference("Branches");
    private DatabaseReference childReference = FirebaseDatabase.getInstance().getReference("Children");
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private FirebaseDatabase database = null;
    private DatabaseReference databaseReference = null;
    private FirebaseAuth firebaseAuth = null;
    private Task<Void> reference = null;
    //-----------------------------------------

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.parents_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu__parents_profile:
                startActivity(new Intent(this, parents_profile.class));
                return true;
            case R.id.menu_parents_children:
                startActivity(new Intent(this, parents_children.class));
                return true;
            case R.id.menu_parents_logout:
                Client.setCurrentUser(null);
                startActivity(new Intent(this, welcome_screen.class));
                return true;
            case R.id.menu__parents_homepage:
                startActivity(new Intent(this, parents_hub.class));
                return true;
            case R.id.menu_parents_online_store:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && data!= null)
        {
            chosen = true;
            selectedPic = data.getData();
            profileChild.setImageURI(selectedPic);
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedPic,filePathColumn,null,null,null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picName = cursor.getString(columnIndex);
            picName = picName.substring(picName.lastIndexOf('/')+1);
        }
        else if(requestCode == 100 && data!=null)
        {
            chosen=true;
            selectedPic = getImageUri(add_child.this, (Bitmap) data.getExtras().get("data"));
            profileChild.setImageURI(selectedPic);

            String[] filepathcolumn={MediaStore.Images.Media.DATA};
            Cursor cursor=getContentResolver().query(selectedPic,filepathcolumn,null,null,null);
            cursor.moveToFirst();
            int columnindex=cursor.getColumnIndex(filepathcolumn[0]);

            picName=cursor.getString(columnindex);

            picName=picName.substring(picName.lastIndexOf("/")+1);
        }
    }
    private void addPic()
    {
        StorageReference PicturesStorage = storageReference.child("Child Profiles/"+picName);
        PicturesStorage.putFile(selectedPic).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(add_child.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    public boolean checkingFields()
    {
        return fstNameChild.getText().toString() != null && lstNameChild.getText().toString() != null && idChild.getText().toString() != null &&
                classLetterChild.getText().toString() != null && classNumberChild.getText().toString() != null && schooolChild.getText().toString() != null;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_child);
        fstNameChild = findViewById(R.id.fstNameChild);
        lstNameChild = findViewById(R.id.lstNameChild);
        idChild = findViewById(R.id.idChild);
        classLetterChild = findViewById(R.id.classLetterChild);
        classNumberChild = findViewById(R.id.classNumberChild);
        schooolChild = findViewById(R.id.schoolChild);
        emailChild = findViewById(R.id.emailChild);
        phoneChild = findViewById(R.id.phoneChild);
        homeBranchChild = findViewById(R.id.homeBranchChild);
        boyRadiob = findViewById(R.id.boyRadiob);
        girlRadiob = findViewById(R.id.girlRadiob);
        rdgbChild = findViewById(R.id.rdgbChild);
        profileChild = findViewById(R.id.profileChild);
        btnDateChild = findViewById(R.id.btnDateChild);
        btnPickChild = findViewById(R.id.btnPickChild);
        btnSaveChild = findViewById(R.id.btnSaveChild);

        calendar = Calendar.getInstance();
        day  = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);

        //----------------------------------------------------------------------
        branchReference.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for (DataSnapshot child: children) {
                    String branch = child.getValue(String.class);
                    branchName.add(branch);
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(add_child.this, R.layout.customized_spinner, branchName);
                homeBranchChild.setAdapter(adapter);

                for (int i = 0; i < branchName.size(); i++) {
                    if (branchName.get(i).equals(Client.getCurrentUser().getBranch_name()))
                    homeBranchChild.setSelection(i);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        lstNameChild.setText(Client.getCurrentUser().getLast_name());
        //----------------------------------------------------------------------
        Permission permission = new Permission(add_child.this);
        permission.verifyPermissions();
        if(ContextCompat.checkSelfPermission(add_child.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(add_child.this, new String[] {
                            Manifest.permission.CAMERA
                    },100);
        }
        //----------------------------------------------------------------------
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Parents");
        firebaseAuth = FirebaseAuth.getInstance();
        //----------------------------------------------------------------------
        prg = new ProgressDialog(add_child.this);
        prg.setTitle("מעלה נתונים");
        prg.setMessage("שומר את נתוני המשתמש");
        prg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        prg.setCancelable(false);
        //----------------------------------------------------------------------
        btnPickChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(add_child.this);
                builder.setTitle("העלאת תמונה לאפליקציה").
                    setMessage("צילום תמונה או בחירה מהגלריה?").
                    setPositiveButton("תמונה מהגלריה", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI),1);
                            dialogInterface.cancel();
                        }
                    }).setNegativeButton("צילום תמונה", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE), 100);
                            dialogInterface.cancel();
                        }
                    }).setCancelable(false);
                AlertDialog dialog = builder.create();
                dialog.setTitle("העלאת תמונה לאפליקציה");
                dialog.show();
            }
        });
        btnDateChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(add_child.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        btnDateChild.setText(dayOfMonth + "/" + (month+1) + "/" + year);
                        chosenDay = dayOfMonth;
                        chosenMonth = month+1;
                        chosenYear = year;
                    }
                },year,month,day);
                datePickerDialog.show();
            }
        });
        btnSaveChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkingFields())
                {
                    final String fstName = fstNameChild.getText().toString();
                    final String lstName = lstNameChild.getText().toString();
                    final Date childBirthdate = new Date(chosenYear,chosenMonth,chosenDay);
                    final String parentUID = Client.getUID();
                    final String childID = idChild.getText().toString();
                    final String childGender;
                    if (boyRadiob.getId() == rdgbChild.getCheckedRadioButtonId())
                        childGender = "boy";
                    else
                        childGender = "girl";
                    final String childClassLetter = classLetterChild.getText().toString();
                    final Integer childClassNumber = Integer.parseInt(classNumberChild.getText().toString());
                    final String childSchool = schooolChild.getText().toString();
                    final String childBranch = homeBranchChild.getSelectedItem().toString();
                    final String childPhone = phoneChild.getText().toString();
                    final String childEmail = emailChild.getText().toString();
                    if (chosen)
                        addPic();
                    else
                        picName = "nlb_logo_removedbg.png";
                    Child child = new Child(fstName,lstName,parentUID,childBirthdate,childID,childGender
                            ,childClassLetter,childClassNumber,childSchool, childBranch, childPhone, childEmail
                            ,picName);
                    DatabaseReference newRef = childReference.push();
                    reference = newRef.setValue(child);
                    startActivity(new Intent(add_child.this,parents_hub.class));
                    Toast.makeText(add_child.this, "הילד נוסף בהצלחה!", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
}