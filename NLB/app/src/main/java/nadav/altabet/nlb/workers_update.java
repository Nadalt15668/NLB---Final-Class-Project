package nadav.altabet.nlb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class workers_update extends AppCompatActivity {

    private Spinner updRoleWorker;
    private Button updStartDate, updRemoveWorker,updSaveWorker;
    private ArrayList<String> arrRoles = new ArrayList<>();
    //-----------------------------------------
    private Calendar calendar = null;
    private int day,month,year,chosenStartYear,chosenStartMonth,chosenStartDay,
            chosenEndYear, chosenEndMonth, chosenEndDay;
    private boolean isEndDateSet = false;
    //-----------------------------------------
    private Task<Void> reference = null;
    private DatabaseReference rolesReference = FirebaseDatabase.getInstance().getReference("Roles");
    private DatabaseReference workersReference = FirebaseDatabase.getInstance().getReference("Workers");
    private DatabaseReference parentsReference = FirebaseDatabase.getInstance().getReference("Parents");

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.admins_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_admins_profile:
                return true;
            case R.id.menu__admins_homepage:
                startActivity(new Intent(this, admins_hub.class));
                return true;
            case R.id.menu_admins_workers:
                startActivity(new Intent(this, admins_workers.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private Integer[] getSpinnerSelectionID(final String role)
    {
        final Integer[] result = {0};
        rolesReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for (DataSnapshot childDatabase: children)
                {
                    String role = childDatabase.getValue(String.class);
                    arrRoles.add(role);
                }
                for (int i = 0; i < arrRoles.size(); i++) {
                    if (arrRoles.get(i).equals(role))
                        result[0] =1;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return result;
    }

    private void changeWorkerRole()
    {
        parentsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for (DataSnapshot childDatabase: children)
                {
                    User user = childDatabase.getValue(User.class);
                    if (user.getEmail().equals(admins_workers.arrWorkers.get(admins_workers.Position).getEmail()))
                    {
                        user.setType("parent");
                        reference = FirebaseDatabase.getInstance().getReference("Parents").child(childDatabase.getKey()).setValue(user);
                        break;
                    }
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workers_update);
        updRoleWorker = findViewById(R.id.updRoleWorker);
        updStartDate = findViewById(R.id.updStartDate);
        updRemoveWorker = findViewById(R.id.updRemoveWorker);
        updSaveWorker = findViewById(R.id.updSaveWorker);

        calendar = Calendar.getInstance();
        day  = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);

        chosenStartDay = admins_workers.arrWorkers.get(admins_workers.Position).getStartDate().getDay();
        chosenStartMonth = admins_workers.arrWorkers.get(admins_workers.Position).getStartDate().getMonth();
        chosenStartYear = admins_workers.arrWorkers.get(admins_workers.Position).getStartDate().getYear();
        chosenEndDay = admins_workers.arrWorkers.get(admins_workers.Position).getEndDate().getDay();
        chosenEndMonth = admins_workers.arrWorkers.get(admins_workers.Position).getEndDate().getMonth();
        chosenEndYear = admins_workers.arrWorkers.get(admins_workers.Position).getEndDate().getYear();

        rolesReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for (DataSnapshot childDatabase: children)
                {
                    String role = childDatabase.getValue(String.class);
                    arrRoles.add(role);
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(workers_update.this, R.layout.customized_spinner, arrRoles);
                updRoleWorker.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        updRoleWorker.setSelection(getSpinnerSelectionID(admins_workers.arrWorkers.get(admins_workers.Position).getRole())[0]);
        String startDate = admins_workers.arrWorkers.get(admins_workers.Position).getStartDate().getDay() + "/" +
                admins_workers.arrWorkers.get(admins_workers.Position).getStartDate().getMonth() + "/" +
                admins_workers.arrWorkers.get(admins_workers.Position).getStartDate().getYear();
        updStartDate.setText(startDate);

        updStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(workers_update.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        updStartDate.setText(dayOfMonth + "/" + (month+1) + "/" + year);
                        chosenStartDay = dayOfMonth;
                        chosenStartMonth = month+1;
                        chosenStartYear = year;
                    }
                },year,month,day);
                datePickerDialog.show();
            }
        });

        updSaveWorker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Worker worker = new Worker();
                worker.setEmail(admins_workers.arrWorkers.get(admins_workers.Position).getEmail());
                worker.setRole(updRoleWorker.getSelectedItem().toString());
                worker.setStartDate(new Date(chosenStartYear,chosenStartMonth,chosenStartDay));
                worker.setEndDate(new Date(chosenEndYear,chosenEndMonth,chosenEndDay));

                String workerUID = admins_workers.arrWorkersUID.get(admins_workers.Position);
                reference = FirebaseDatabase.getInstance().getReference("Workers").child(workerUID).setValue(worker);
                Toast.makeText(workers_update.this, "הנתונים עודכנו בהצלחה!", Toast.LENGTH_SHORT).show();
            }
        });

        updRemoveWorker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog = new AlertDialog.Builder(workers_update.this).create();
                if (Client.getCurrentUser().getGender().equals("זכר"))
                {
                    dialog.setTitle("אתה בטוח?");
                    dialog.setMessage("האם אתה בטוח שאתה רוצה לבצע פעולה זאת? אין דרך לשחזר את הנתונים לאחר ההסרה");
                    dialog.setCancelable(false);
                    dialog.setButton(dialog.BUTTON_NEGATIVE, "כן", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String workerUID = admins_workers.arrWorkersUID.get(admins_workers.Position);
                            reference = FirebaseDatabase.getInstance().getReference("Workers").child(workerUID).setValue(new Worker(admins_workers.arrWorkers.get(admins_workers.Position)));
                            changeWorkerRole();
                            dialog.dismiss();
                            Toast.makeText(workers_update.this, "העובד הוסר בהצלחה!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(workers_update.this, admins_hub.class));
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
                    dialog.setMessage("האם את בטוחה שאת רוצה לבצע פעולה זאת? אין דרך לשחזר את הנתונים לאחר ההסרה");
                    dialog.setCancelable(false);
                    dialog.setButton(dialog.BUTTON_NEGATIVE, "כן", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String workerUID = admins_workers.arrWorkersUID.get(admins_workers.Position);
                            reference = FirebaseDatabase.getInstance().getReference("Workers").child(workerUID).setValue(new Worker(admins_workers.arrWorkers.get(admins_workers.Position)));
                            changeWorkerRole();
                            dialog.dismiss();
                            Toast.makeText(workers_update.this, "העובד הוסר בהצלחה!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(workers_update.this, admins_hub.class));
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
    }
}