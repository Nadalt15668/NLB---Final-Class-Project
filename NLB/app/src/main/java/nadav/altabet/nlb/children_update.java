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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
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

public class children_update extends AppCompatActivity {

    private Child child = parents_children.arrChild.get(parents_children.Position);
    private EditText fstNameChild, lstNameChild, idChild, classLetterChild, classNumberChild, schooolChild,  emailChild, phoneChild;
    private Spinner homeBranchChild;
    private RadioButton boyRadiob, girlRadiob;
    private RadioGroup rdgbChild;
    private ImageView profileChild;
    private Button btnDateChild, btnPickChild,btnSaveChild, btnRemoveChild;
    private ArrayList<String> branchName = new ArrayList<>();
    private ProgressDialog prg;
    //-----------------------------------------
    private Calendar calendar = null;
    private int day,month,year,chosenYear = child.getChild_birthdate().getYear(),chosenMonth = child.getChild_birthdate().getMonth()
            ,chosenDay = child.getChild_birthdate().getDay();
    //-----------------------------------------
    private static Uri selectedPic = null;
    private static String picName = "";
    private static boolean chosen = false;
    //-----------------------------------------
    private final DatabaseReference branchReference = FirebaseDatabase.getInstance().getReference("Branches");
    private DatabaseReference childReference = FirebaseDatabase.getInstance().getReference("Children");
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = null;
    private FirebaseAuth firebaseAuth = null;
    private Task<Void> reference = null;
    private String mypath="gs://nlb-project-2287b.appspot.com";  // Image Path
    //-----------------------------------------

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

        String picname=child.getChild_profile();
        String suffix=picname.substring(picname.lastIndexOf(".")+1);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl(mypath).child("Child Profiles").child(picname);
        try
        {
            final File localFile = File.createTempFile(picname, suffix);
            storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>()
            {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot)
                {
                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    profileChild.setImageBitmap(bitmap);
                }
            }).addOnFailureListener(new OnFailureListener()
            {
                @Override
                public void onFailure(@NonNull Exception exception)
                {
                    String message=exception.getMessage();
                    Toast.makeText(children_update.this, message, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(children_update.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_children_update);

        fstNameChild = findViewById(R.id.fstNameChildUpd);
        lstNameChild = findViewById(R.id.lstNameChildUpd);
        idChild = findViewById(R.id.idChildUpd);
        classLetterChild = findViewById(R.id.classLetterChildUpd);
        classNumberChild = findViewById(R.id.classNumberChildUpd);
        schooolChild = findViewById(R.id.schoolChildUpd);
        emailChild = findViewById(R.id.emailChildUpd);
        phoneChild = findViewById(R.id.phoneChildUpd);
        homeBranchChild = findViewById(R.id.homeBranchChildUpd);
        boyRadiob = findViewById(R.id.boyRadiobUpd);
        girlRadiob = findViewById(R.id.girlRadiobUpd);
        rdgbChild = findViewById(R.id.rdgbChildUpd);
        profileChild = findViewById(R.id.profileChildUpd);
        btnDateChild = findViewById(R.id.btnDateChildUpd);
        btnPickChild = findViewById(R.id.btnPickChildUpd);
        btnSaveChild = findViewById(R.id.btnSaveChildUpd);
        btnRemoveChild = findViewById(R.id.btnRemoveChild);

        calendar = Calendar.getInstance();
        day  = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);
        //----------------------------------------------------
        fstNameChild.setText(child.getChild_first_name());
        lstNameChild.setText(child.getChild_last_name());
        idChild.setText(child.getChild_ID());
        classLetterChild.setText(child.getChild_class_letter());
        classNumberChild.setText(String.valueOf(child.getChild_class_number()));
        btnDateChild.setText(child.getChild_birthdate().getDay() + "/" + child.getChild_birthdate().getMonth() + "/" + child.getChild_birthdate().getYear());
        schooolChild.setText(child.getChild_school());
        emailChild.setText(child.getChild_email());
        phoneChild.setText(child.getChild_phone());
        if (child.getChild_gender().equals("ילד"))
            boyRadiob.setChecked(true);
        else
            girlRadiob.setChecked(true);
        branchReference.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for (DataSnapshot child: children) {
                    String branch = child.getValue(String.class);
                    branchName.add(branch);
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(children_update.this, R.layout.customized_spinner, branchName);
                homeBranchChild.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        homeBranchChild.setSelection(getIndex(homeBranchChild,child.getChild_branch()));
        showImageFromFirebase();

        btnRemoveChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog = new AlertDialog.Builder(children_update.this).create();
                if (Client.getCurrentUser().getGender().equals("זכר"))
                {
                    dialog.setTitle("אתה בטוח?");
                    dialog.setMessage("האם אתה בטוח שאתה רוצה לבצע פעולה זאת? אין דרך לשחזר את הנתונים לאחר המחיקה");
                    dialog.setCancelable(false);
                    dialog.setButton(dialog.BUTTON_NEGATIVE, "כן", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            reference = database.getReference("Children").child(parents_children.arrChildUID.get(parents_children.Position)).setValue(null);
                            dialog.dismiss();
                            Toast.makeText(children_update.this, "הילד הוסר בהצלחה!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(children_update.this, parents_hub.class));
                        }
                    });
                    dialog.setButton(dialog.BUTTON_POSITIVE, "לא", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                }
                else
                {
                    dialog.setTitle("את בטוחה?");
                    dialog.setMessage("האם את בטוחה שאת רוצה לבצע פעולה זאת? אין דרך לשחזר את הנתונים לאחר המחיקה");
                    dialog.setCancelable(false);
                    dialog.setButton(dialog.BUTTON_NEGATIVE, "כן", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            reference = database.getReference("Children").child(parents_children.arrChildUID.get(parents_children.Position)).setValue(null);
                            dialog.dismiss();
                            Toast.makeText(children_update.this, "הילד הוסר בהצלחה!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(children_update.this, parents_hub.class));

                        }
                    });
                    dialog.setButton(dialog.BUTTON_POSITIVE, "לא", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                }
                dialog.show();
            }
        });

        btnPickChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI),1);
            }
        });

        btnDateChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(children_update.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        btnDateChild.setText(dayOfMonth + "/" + (month+1) + "/" + year);
                        chosenDay = dayOfMonth;
                        chosenMonth = month+1;
                        chosenYear = year;
                    }
                },chosenYear,chosenMonth,chosenDay);
                datePickerDialog.show();
            }
        });

        btnSaveChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Child newChild = new Child();
                newChild.setChild_first_name(fstNameChild.getText().toString());
                newChild.setChild_last_name(lstNameChild.getText().toString());
                newChild.setChild_birthdate(new Date(chosenYear, chosenMonth, chosenDay));
                newChild.setParent_UID(Client.getUID());
                newChild.setChild_ID(idChild.getText().toString());
                newChild.setChild_class_letter(classLetterChild.getText().toString());
                newChild.setChild_class_number(Integer.parseInt(classNumberChild.getText().toString()));
                newChild.setChild_school(schooolChild.getText().toString());
                newChild.setChild_branch(homeBranchChild.getSelectedItem().toString());
                newChild.setChild_phone(phoneChild.getText().toString());
                newChild.setChild_email(emailChild.getText().toString());
                if (rdgbChild.getCheckedRadioButtonId()==boyRadiob.getId())
                    newChild.setChild_gender("ילד");
                else
                    newChild.setChild_gender("ילדה");
                newChild.setChild_profile(child.getChild_profile());
                if (chosen)
                {
                    addPic();
                    newChild.setChild_profile(picName);
                }
                reference = database.getReference("Children").child(parents_children.arrChildUID.get(parents_children.Position)).setValue(newChild);
                Toast.makeText(children_update.this, "הנתונים עודכנו בהצלחה!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(children_update.this,parents_hub .class));
            }
        });
    }
}