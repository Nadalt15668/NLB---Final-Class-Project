package nadav.altabet.nlb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class coordinators_guides extends AppCompatActivity {

    private FloatingActionButton add_guide;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coordinators_guides);

        add_guide = findViewById(R.id.add_fab_guide);

        add_guide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(coordinators_guides.this, nadav.altabet.nlb.add_guide.class));
            }
        });
    }
}