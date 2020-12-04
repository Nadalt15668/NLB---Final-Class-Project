package nadav.altabet.nlb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class admins_workers extends AppCompatActivity {

    private ListView worker_lstview;
    private FloatingActionButton add;
    public static ArrayList<Worker> arrWorkers = new ArrayList<>();
    public static ArrayList<String> arrWorkersUID = new ArrayList<>();
    private ProgressDialog prg;
    public static int Position = 0;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference workersReference = database.getReference("Workers");

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
        setContentView(R.layout.activity_admins_workers);

        prg = new ProgressDialog(admins_workers.this);
        prg.setTitle("מציג נתונים");
        prg.setMessage("מעלה את נתוני העובדים");
        prg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        prg.setCancelable(false);

        arrWorkers.clear();
        arrWorkersUID.clear();

        worker_lstview = findViewById(R.id.worker_listview);
        add = findViewById(R.id.add_fab_worker);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(admins_workers.this, add_worker.class));
            }
        });

        workersReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for (DataSnapshot childDatabase: children)
                {
                    Worker worker = childDatabase.getValue(Worker.class);
                    if (worker.getEndDate().getDay() == -1)
                    {
                        arrWorkers.add(worker);
                        arrWorkersUID.add(childDatabase.getKey());
                    }
                }
                worker_cardlsview_adap worker_cardlsview_adap = new worker_cardlsview_adap(arrWorkers, admins_workers.this, prg);
                worker_lstview.setAdapter(worker_cardlsview_adap);

                worker_lstview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Position = position;
                        startActivity(new Intent(admins_workers.this,workers_update.class));
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}