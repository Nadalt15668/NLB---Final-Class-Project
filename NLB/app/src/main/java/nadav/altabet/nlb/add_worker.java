package nadav.altabet.nlb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Calendar;

public class add_worker extends AppCompatActivity {

    private EditText email_worker;
    private Button startDate, endDate, saveWorker;
    private Spinner spn_role;
    private ArrayList<String> arrRoles = new ArrayList<>();
    //-----------------------------------------
    private Calendar calendar = null;
    private int day,month,year,chosenStartYear,chosenStartMonth,chosenStartDay,
            chosenEndYear, chosenEndMonth, chosenEndDay;
    private boolean isEndDateSet = false;
    //-----------------------------------------
    private DatabaseReference workerReference = FirebaseDatabase.getInstance().getReference("Workers");
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private DatabaseReference databaseReference = null;
    private FirebaseAuth firebaseAuth = null;
    private Task<Void> reference = null;
    //-----------------------------------------
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference rolesReference = database.getReference("Roles");

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
        setContentView(R.layout.activity_add_worker);
        email_worker = findViewById(R.id.emailWorkerEditTxt);
        startDate = findViewById(R.id.btnStartDate);
        endDate = findViewById(R.id.btnEndDate);
        saveWorker = findViewById(R.id.btnSaveWorker);
        spn_role = findViewById(R.id.spnRoleWorker);

        calendar = Calendar.getInstance();
        day  = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);

        rolesReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for (DataSnapshot childDatabase: children)
                {
                    String role = childDatabase.getValue(String.class);
                    arrRoles.add(role);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //----------------------------------------------------------------------

        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(add_worker.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        startDate.setText(dayOfMonth + "/" + (month+1) + "/" + year);
                        chosenStartDay = dayOfMonth;
                        chosenStartMonth = month+1;
                        chosenStartYear = year;
                    }
                },year,month,day);
                datePickerDialog.show();
            }
        });
        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(add_worker.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        endDate.setText(dayOfMonth + "/" + (month+1) + "/" + year);
                       chosenEndDay = dayOfMonth;
                       chosenEndMonth  = month+1;
                       chosenEndYear = year;
                       isEndDateSet = true;
                    }
                },year,month,day);
                datePickerDialog.show();
            }
        });
        endDate.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                chosenEndDay = Integer.parseInt(null);
                chosenEndMonth = Integer.parseInt(null);
                chosenEndYear = Integer.parseInt(null);
                endDate.setText(R.string.בחר_תאריך_סיום);
                return false;
            }
        });
        saveWorker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = email_worker.getText().toString();
                final String role = spn_role.getSelectedItem().toString();
                final Date startDate = new Date(chosenStartYear,chosenStartMonth,chosenStartDay);
                final Date endDate = new Date(chosenEndYear,chosenEndMonth,chosenEndDay);
                Worker worker = new Worker(email,startDate,endDate,role);
                DatabaseReference newRef = workerReference.push();
                reference = newRef.setValue(worker);
                startActivity(new Intent(add_worker.this, admins_workers.class));
                Toast.makeText(add_worker.this, "העובד התווסף בהצלחה!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}