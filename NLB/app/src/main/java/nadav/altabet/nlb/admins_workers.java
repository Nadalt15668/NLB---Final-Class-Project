package nadav.altabet.nlb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class admins_workers extends AppCompatActivity {

    private ListView worker_lstview;
    private FloatingActionButton add;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admins_workers);
        worker_lstview = findViewById(R.id.worker_listview);
        add = findViewById(R.id.add_fab_worker);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(admins_workers.this, add_worker.class));
            }
        });
    }
}