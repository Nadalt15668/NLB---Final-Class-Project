package nadav.altabet.nlb;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Calendar;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class guides_activities extends AppCompatActivity {

    private EditText activity_name;
    private Button activity_date;
    private Button choose_file;
    private Button btn_add_activity;
    private TextView file_name;
    private Spinner activity_class;
    private FloatingActionButton add_activity;
    private Button download_file;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private StorageReference ref;
    private ProgressDialog prg;
    private Uri docxUri;
    private String[] classArray = new String[12];
    private ProgressDialog progressDialog;
    int chosenDay = -1, chosenMonth = -1, chosenYear = -1;
    int day, month, year;

    private AlertDialog.Builder builder;
    private View view;
    private AlertDialog dialog;

    private DatabaseReference classesReference = FirebaseDatabase.getInstance().getReference("Classes");

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.guides_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu__guides_homepage:
                startActivity(new Intent(this, guides_hub.class));
                return true;
            case R.id.menu_guides_activities:
                startActivity(new Intent(this, guides_activities.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void download(){
        storageReference = firebaseStorage.getInstance().getReference();
        ref = storageReference.child("טופס כתיבת פעולה.docx");
        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String url = uri.toString();
                downloadFiles(guides_activities.this,"טופס כתיבת פעולה", ".docx",  DIRECTORY_DOWNLOADS, url);
                prg.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }
    public void downloadFiles(Context context, String fileName, String fileExtension, String destinationDirectory, String url)
    {
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context, destinationDirectory, fileName + fileExtension);
        downloadManager.enqueue(request);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
        {
            chooseFile();
        }
        else
            Toast.makeText(this, "יש לאשר גישה לקבצים בשביל לבחור את הפעולה", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 86 && resultCode == RESULT_OK && data != null) {
            docxUri = data.getData();
            file_name.setText(data.getData().getLastPathSegment() + "הקובץ שנבחר: ");
        }
        else
        {
            Toast.makeText(this, "יש לבחור פעולה להעלאה", Toast.LENGTH_SHORT).show();
        }
    }

    private void chooseFile() {
        Intent intent = new Intent();
        intent.setType("docx/*");
        intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        startActivityForResult(intent, 86);
    }
    private void uploadFile(Uri docxUri, final Activity activity)
    {
           progressDialog = new ProgressDialog(this);
           progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
           progressDialog.setTitle("מעלה פעולה...");
           progressDialog.setProgress(0);
           progressDialog.show();
           final String fileName = System.currentTimeMillis()+"";
           StorageReference storageReference = firebaseStorage.getReference();

           storageReference.child("Activities").child(Client.getCurrentUser().getBranch_name()).
                   child(Client.getCurrentUser().getFirst_name() + Client.getCurrentUser().getLast_name()).
                   child(fileName).putFile(docxUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
               @Override
               public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                   String url = taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();
                   activity.setFile_url(url);

                   DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                   databaseReference.child("Activities").child(Client.getCurrentUser().getBranch_name()).
                           child(Client.getCurrentUser().getFirst_name() + Client.getCurrentUser().getLast_name()).
                           child(fileName).setValue(activity).addOnCompleteListener(new OnCompleteListener<Void>() {
                       @Override
                       public void onComplete(@NonNull Task<Void> task) {
                           if (task.isSuccessful())
                           {
                               Toast.makeText(guides_activities.this, "הפעולה הועלתה בהצלחה!", Toast.LENGTH_SHORT).show();
                               dialog.dismiss();
                               startActivity(new Intent(guides_activities.this, guides_hub.class));
                           }
                           else
                               Toast.makeText(guides_activities.this, "העלאת הפעולה נכשלה", Toast.LENGTH_SHORT).show();
                       }
                   });
               }
           }).addOnFailureListener(new OnFailureListener() {
               @Override
               public void onFailure(@NonNull Exception e) {
                   Toast.makeText(guides_activities.this, "העלאת הפעולה נכשלה", Toast.LENGTH_SHORT).show();
               }
           }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
               @Override
               public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                   int currentProgress = (int) (100*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                   progressDialog.setProgress(currentProgress);
               }
           });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guides_activities);

        download_file = findViewById(R.id.btnDownloadFile);
        add_activity = findViewById(R.id.add_fab_activity);

        prg = new ProgressDialog(guides_activities.this);
        prg.setCancelable(false);
        prg.setTitle("מוריד קבצים");
        prg.setMessage("אנא המתן להורדת הקבצים");

        download_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prg.show();
                download();
            }
        });

        add_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                builder = new AlertDialog.Builder(guides_activities.this);
                view = getLayoutInflater().inflate(R.layout.add_activity, null);
                activity_name = view.findViewById(R.id.activityNameEditTxt);
                activity_date = view.findViewById(R.id.btnActivityDate);
                choose_file = view.findViewById(R.id.btnChooseFile);
                btn_add_activity = view.findViewById(R.id.btnAddActivity);
                file_name = view.findViewById(R.id.fileNameTxtView);
                activity_class = view.findViewById(R.id.activityClass);

                //classesReference.addValueEventListener(new ValueEventListener() {
                //    @Override
                //    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //        Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                //        for (DataSnapshot child: children) {
                //            String activityClass = child.getValue(String.class);
                //            classArray[Integer.parseInt(child.getKey())] = activityClass;
                //        }
                //        ArrayAdapter<String> adapter = new ArrayAdapter<String>(guides_activities.this, R.layout.customized_spinner, classArray);
                //        activity_class.setAdapter(adapter);
                //
                //    }
//
                //    @Override
                //    public void onCancelled(@NonNull DatabaseError databaseError) {
//
                //    }
                //});

                //Choosing Activity Date:
                Calendar calendar = Calendar.getInstance();
                day  = calendar.get(Calendar.DAY_OF_MONTH);
                month = calendar.get(Calendar.MONTH);
                year = calendar.get(Calendar.YEAR);

                activity_date.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatePickerDialog datePickerDialog = new DatePickerDialog(guides_activities.this, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                activity_date.setText(dayOfMonth + "/" + (month+1) + "/" + year);
                                chosenDay= dayOfMonth;
                                chosenMonth= month+1;
                                chosenYear= year;
                            }
                        },year,month,day);
                        datePickerDialog.show();
                    }
                });

                //Choosing An Activity File For Uploading:
                choose_file.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (ContextCompat.checkSelfPermission(guides_activities.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                        {
                            chooseFile();
                        }
                        else
                            ActivityCompat.requestPermissions(guides_activities.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
                    }
                });

                btn_add_activity.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (docxUri != null && (!activity_name.getText().toString().isEmpty() )&& chosenDay != -1 && chosenMonth != -1 && chosenYear != -1)
                        {
                            Activity activity = new Activity(activity_name.getText().toString(), new Date(chosenYear,chosenMonth,chosenDay),
                                    new Date(year,month,day),activity_class.getSelectedItem().toString());
                            uploadFile(docxUri, activity);
                        }
                        else
                            Toast.makeText(guides_activities.this, "יש למלא את כל השדות", Toast.LENGTH_SHORT).show();
                    }
                });

                builder.setView(view);
                dialog = builder.create();
                dialog.show();
            }

        });


    }

}