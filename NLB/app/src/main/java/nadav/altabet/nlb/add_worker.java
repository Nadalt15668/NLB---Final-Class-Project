package nadav.altabet.nlb;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.Calendar;

public class add_worker extends AppCompatActivity {

    private EditText email_worker;
    private Button startDate, endDate, saveWorker;
    private Spinner role;
    //-----------------------------------------
    private Calendar calendar = null;
    private int day,month,year,chosenStartYear,chosenStartMonth,chosenStartDay,
            chosenEndYear, chosenEndMonth, chosenEndDay;
    private boolean isEndDateSet = false;
    //-----------------------------------------

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
        role = findViewById(R.id.spnRoleWorker);

        calendar = Calendar.getInstance();
        day  = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);
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
                return false;
            }
        });
    }
}