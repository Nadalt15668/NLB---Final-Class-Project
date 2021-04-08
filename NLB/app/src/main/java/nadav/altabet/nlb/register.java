package nadav.altabet.nlb;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;

public class register extends AppCompatActivity {

    private ProgressDialog prg;
    private EditText edit_email,edit_pass,edit_fst,edit_lst,edit_id,edit_address,edit_city,edit_phone;
    private Spinner home_branch;
    private ImageView profile;
    private Button btnPick,btnSave,btnDate;
    private RadioButton female;
    private static Uri selectedPic = null;
    private static String picName = "";
    private static boolean chosen = false;
    private Calendar calendar = null;
    private int day,month,year,chosenYear,chosenMonth,chosenDay;
    private ArrayList<String> branchName = new ArrayList<>();
    //--------------------------------------------------------------------------
    private FirebaseDatabase database = null;
    private DatabaseReference databaseReference = null;
    private FirebaseAuth firebaseAuth = null;
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private final DatabaseReference branchReference = FirebaseDatabase.getInstance().getReference("Branches");
    //--------------------------------------------------------------------------
    private void addPic()
    {
        StorageReference PicturesStorage = storageReference.child("Profiles/"+picName);
        PicturesStorage.putFile(selectedPic).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(register.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
            profile.setImageURI(selectedPic);
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedPic,filePathColumn,null,null,null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picName = cursor.getString(columnIndex);
            picName = picName.substring(picName.lastIndexOf('/')+1);
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

    public boolean checkingFields()
    {
        return edit_email.getText().toString() != null && edit_address.getText().toString() != null && edit_city.getText().toString() != null &&
                edit_fst.getText().toString() != null && edit_id.getText().toString() != null && edit_lst.getText().toString() != null &&
                edit_pass.getText().toString() != null && edit_phone.getText().toString() != null && home_branch.getSelectedItem() != null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        edit_email = findViewById(R.id.emailEditTxt);
        edit_pass = findViewById(R.id.passwordEditTxt);
        edit_fst = findViewById(R.id.fstNameEditTxt);
        edit_lst = findViewById(R.id.lstNameEditTxt);
        edit_id = findViewById(R.id.idEditTxt);
        edit_address = findViewById(R.id.addressEditTxt);
        edit_city = findViewById(R.id.cityEditTxt);
        edit_phone = findViewById(R.id.phoneEditTxt);
        home_branch = findViewById(R.id.homeBranch);
        profile = findViewById(R.id.profile);
        female = findViewById(R.id.femaleRadiob);
        btnDate = findViewById(R.id.btnDate);
        btnPick = findViewById(R.id.btnPick);
        btnSave = findViewById(R.id.btnSave);



        prg = new ProgressDialog(register.this);
        prg.setTitle("מעלה נתונים");
        prg.setMessage("שומר את נתוני המשתמש");
        prg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        prg.setCancelable(false);

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

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(register.this, R.layout.customized_spinner, branchName);
                home_branch.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Permission permission = new Permission(register.this);
        permission.verifyPermissions();

        //----------------------------------------------------------------------
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Parents");
        firebaseAuth = FirebaseAuth.getInstance();
        //----------------------------------------------------------------------
        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(register.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        btnDate.setText(dayOfMonth + "/" + (month+1) + "/" + year);
                        chosenDay = dayOfMonth;
                        chosenMonth = month+1;
                        chosenYear = year;
                    }
                },year,month,day);
                datePickerDialog.show();
            }
        });
        btnPick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                startActivityForResult(new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI),1);
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prg.show();
                if (checkingFields())
                {
                    final String email = edit_email.getText().toString();
                    final String password = edit_pass.getText().toString();
                    final String fstName = edit_fst.getText().toString();
                    final String lstName = edit_lst.getText().toString();
                    final String id = edit_id.getText().toString();
                    final String address = edit_address.getText().toString();
                    final String city = edit_city.getText().toString();
                    final String phone = edit_phone.getText().toString();
                    final String type = "parent";
                    final String branchName = home_branch.getSelectedItem().toString();
                    final Date dateOfBirth = new Date(chosenYear, chosenMonth, chosenDay);
                    String pic = "nlb_logo_removedbg.png";
                    if (chosen)
                        pic = picName;
                    final String finalPic = pic;
                    String gender = "זכר";
                    if (female.isChecked())
                        gender = "נקבה";
                    final String finalGender = gender;
                    final User user = new User(email, fstName, lstName, id, city, address, phone, finalPic, dateOfBirth, finalGender, type, branchName);
                    if (dateOfBirth.checkForAbove18(new Date(year, month, day)) > 18) {
                        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(register.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                                    final String UID = currentUser.getUid();
                                    if (dateOfBirth.checkForAbove18(new Date(year, month, day)) > 18) {
                                        databaseReference.child(UID).setValue(user).addOnCompleteListener(register.this, new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    prg.dismiss();
                                                    Toast.makeText(register.this, "ההרשמה הסתיימה בהצלחה!", Toast.LENGTH_SHORT).show();
                                                    if (chosen)
                                                        addPic();
                                                    startActivity(new Intent(register.this, welcome_screen.class));
                                                }
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(register.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                            }
                                        });

                                    }
                                }
                            }
                        }).addOnFailureListener(register.this, new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    prg.dismiss();
                                    Toast.makeText(register.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                    }
                    else
                    {
                        user.setType("guideWL");
                        AlertDialog dialog = new AlertDialog.Builder(register.this).create();
                        prg.dismiss();
                        dialog.setTitle("הינך מתחת לגיל 18");
                        dialog.setMessage("לפי תאריך הלידה שלך הינך מתחת לגיל 18, האם להוסיף אותך כמדריך/ה?");
                        dialog.setCancelable(false);
                        dialog.setButton(dialog.BUTTON_NEGATIVE, "כן", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialog, int which) {
                                firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        String UID = firebaseAuth.getUid();
                                        databaseReference.child(UID).setValue(user).addOnCompleteListener(register.this, new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    dialog.dismiss();
                                                    Toast.makeText(register.this, "ההרשמה הסתיימה בהצלחה!", Toast.LENGTH_SHORT).show();
                                                    if (chosen)
                                                        addPic();
                                                    startActivity(new Intent(register.this, welcome_screen.class));
                                                }
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(register.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(register.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                        dialog.setButton(dialog.BUTTON_NEUTRAL, "לא", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(register.this, "אין באפשרותך ליצור משתמש מתחת לגיל 18", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(register.this, welcome_screen.class));
                            }
                        });
                        dialog.show();
                    }
                }
                else
                {
                    prg.dismiss();
                    Toast.makeText(register.this, "אנא מלא את כל השדות לפני סיום ההרשמה", Toast.LENGTH_LONG).show();
                }
            }


        });
    }
}
