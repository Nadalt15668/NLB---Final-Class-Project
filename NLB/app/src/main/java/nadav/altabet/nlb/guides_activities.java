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
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
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
    private Button activity_date, btn_add_activity, choose_file, download_file, activity_time,delete_activity, update_activity;
    private TextView file_name;
    private Spinner activity_class;
    private ListView activity_listview;
    private FloatingActionButton add_activity;
    private FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    private StorageReference storageReference;
    private StorageReference ref;
    private ProgressDialog prg;
    private Uri docxUri;
    private ArrayList<String> classArray = new ArrayList<>();
    private ArrayList<Activity> activityArray = new ArrayList<>();
    private ArrayList<String> fileArray = new ArrayList<>();
    private ProgressDialog progressDialog;
    private boolean isDateUpdated, isTimeUpdated;
    int chosenDay = -1, chosenMonth = -1, chosenYear = -1, chosenHour = -1, chosenMinute = -1;
    int day, month, year, hour, minute;

    private AlertDialog.Builder builder;
    private View view;
    private AlertDialog dialog;

    private DatabaseReference classesReference = FirebaseDatabase.getInstance().getReference("Classes");
    private DatabaseReference activitiesReference = FirebaseDatabase.getInstance().getReference("Activities").child(Client.getCurrentUser().getBranch_name());
    

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
        Toast.makeText(context, "הפעולה הורדה בהצלחה!", Toast.LENGTH_SHORT).show();
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
            file_name.setText("הקובץ שנבחר: " + data.getData().getLastPathSegment());
        }
        else
        {
            Toast.makeText(this, "יש לבחור פעולה להעלאה", Toast.LENGTH_SHORT).show();
        }
    }

    private void chooseFile() {
        //Intent intent = new Intent("com.sec.android.app.myfiles.PICK_DATA");
        //intent.putExtra("docx", "/");
        //intent.addCategory(Intent.CATEGORY_DEFAULT);
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        startActivityForResult(intent, 86);
    }
    private void uploadFile(Uri docxUri, final Activity activity)
    {
           progressDialog = new ProgressDialog(this);
           progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
           progressDialog.setTitle("מעלה פעולה...");
           progressDialog.setProgress(0);
           progressDialog.show();
           final String fileName = System.currentTimeMillis()+".docx";
            final String fileName1 = System.currentTimeMillis()+"";
           StorageReference storageReference = firebaseStorage.getReference();

           storageReference.child("Activities").child(Client.getCurrentUser().getBranch_name()).
                   child(fileName).putFile(docxUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
               @Override
               public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                   String url = taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();
                   activity.setFile_url(url);

                   DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                   databaseReference.child("Activities").child(Client.getCurrentUser().getBranch_name()).
                           child(fileName1).setValue(activity).addOnCompleteListener(new OnCompleteListener<Void>() {
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
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guides_activities);

        download_file = findViewById(R.id.btnDownloadFile);
        add_activity = findViewById(R.id.add_fab_activity);
        activity_listview = findViewById(R.id.activity_listview);

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
                activity_time = view.findViewById(R.id.btnActivityTime);
                choose_file = view.findViewById(R.id.btnChooseFile);
                btn_add_activity = view.findViewById(R.id.btnAddActivity);
                file_name = view.findViewById(R.id.fileNameTxtView);
                activity_class = view.findViewById(R.id.activityClass);


                //Choosing Activity Date & Time:
                Calendar calendar = Calendar.getInstance();
                day  = calendar.get(Calendar.DAY_OF_MONTH);
                month = calendar.get(Calendar.MONTH);
                year = calendar.get(Calendar.YEAR);
                hour = calendar.get(Calendar.HOUR_OF_DAY);
                minute = calendar.get(Calendar.MINUTE);

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
                activity_time.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TimePickerDialog timePickerDialog = new TimePickerDialog(guides_activities.this, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                activity_time.setText(hourOfDay + ":" + minute);
                                chosenHour = hourOfDay;
                                chosenMinute = minute;
                            }
                        },hour,minute,true);
                        timePickerDialog.show();
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
                        if (docxUri != null && (!activity_name.getText().toString().isEmpty()) && chosenDay != -1 && chosenMonth != -1 && chosenYear != -1 &&
                        chosenHour != -1 && chosenMinute != -1)
                        {
                            Activity activity = new Activity(activity_name.getText().toString(), new Date(chosenYear,chosenMonth,chosenDay, chosenHour, chosenMinute),
                                    new Date(year,month,day,hour,minute),activity_class.getSelectedItem().toString(), Client.getCurrentUser().getFirst_name() + "" +
                                    Client.getCurrentUser().getLast_name(), Client.getCurrentUser().getPhone());
                            uploadFile(docxUri, activity);
                        }
                        else
                            Toast.makeText(guides_activities.this, "יש למלא את כל השדות", Toast.LENGTH_SHORT).show();
                    }
                });


                classesReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                        for (DataSnapshot child: children) {
                            String activityClass = child.getValue(String.class);
                            classArray.add(activityClass);
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(guides_activities.this, R.layout.customized_spinner, classArray);
                        activity_class.setAdapter(adapter);
                        
                        builder.setView(view);
                        dialog = builder.create();
                        dialog.show();

                    }
                    //
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
                    }
                });

            }

        });

        activitiesReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for (DataSnapshot child: children) {
                    Activity activity = child.getValue(Activity.class);
                    activityArray.add(activity);
                    fileArray.add(child.getKey());
                }
                activity_cardlsview_adap adap = new activity_cardlsview_adap(activityArray, guides_activities.this);
                activity_listview.setAdapter(adap);
                
                activity_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(guides_activities.this);
                        View viewDialog = getLayoutInflater().inflate(R.layout.activity_update, null);
                        activity_date = viewDialog.findViewById(R.id.btnActivityDate);
                        activity_time = viewDialog.findViewById(R.id.btnActivityTime);
                        delete_activity = viewDialog.findViewById(R.id.btnDeleteActivity);
                        update_activity = viewDialog.findViewById(R.id.btnUpdateActivity);

                        //Choosing Activity Date & Time:
                        Calendar calendar = Calendar.getInstance();
                        day  = activityArray.get(position).getActivity_date().getDay();
                        month = activityArray.get(position).getActivity_date().getMonth();
                        year = activityArray.get(position).getActivity_date().getYear();
                        hour = activityArray.get(position).getActivity_date().getHour();
                        minute = activityArray.get(position).getActivity_date().getMinute();
                        activity_date.setText(day + "/" + month + "/" + year);
                        activity_time.setText(minute + ":" + hour);

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
                                        isDateUpdated = true;
                                    }
                                },year,month,day);
                                datePickerDialog.show();
                            }
                        });
                        activity_time.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                TimePickerDialog timePickerDialog = new TimePickerDialog(guides_activities.this, new TimePickerDialog.OnTimeSetListener() {
                                    @Override
                                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                        activity_time.setText(hourOfDay + ":" + minute);
                                        chosenHour = hourOfDay;
                                        chosenMinute = minute;
                                        isTimeUpdated = true;
                                    }
                                },hour,minute,true);
                                timePickerDialog.show();
                            }
                        });
                        delete_activity.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Task<Void> reference = activitiesReference.child(fileArray.get(position)).setValue(null);
                                startActivity(new Intent(guides_activities.this, guides_hub.class));
                                Toast.makeText(guides_activities.this, "הפעולה הוסרה בהצלחה!", Toast.LENGTH_SHORT).show();
                            }
                        });
                        update_activity.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (isDateUpdated && isTimeUpdated)
                                {
                                    Activity activity = new Activity(activityArray.get(position));
                                    activity.setActivity_date(new Date(chosenYear, chosenMonth, chosenDay,chosenHour,chosenMinute));
                                    Task<Void> reference = activitiesReference.child(fileArray.get(position)).setValue(activity);
                                    Toast.makeText(guides_activities.this, "הפעולה עודכנה בהצלחה!", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(guides_activities.this, guides_hub.class));
                                }
                            }
                        });
                        builder.setView(viewDialog);
                        dialog = builder.create();
                        dialog.show();
                    }
                });

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(guides_activities.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}