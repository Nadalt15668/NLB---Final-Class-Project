package nadav.altabet.nlb;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class coordinators_hub extends AppCompatActivity {

    private CardView guides;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coordinators_hub);
        guides = findViewById(R.id.guidesCoordinator);


        guides.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(coordinators_hub.this, coordinators_guides.class));
            }
        });
    }
}