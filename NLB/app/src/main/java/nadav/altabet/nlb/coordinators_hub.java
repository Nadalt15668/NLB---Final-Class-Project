package nadav.altabet.nlb;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

public class coordinators_hub extends AppCompatActivity {

    private TextView coordinators_hub_title;
    private CardView guides;

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
        setContentView(R.layout.activity_coordinators_hub);
        guides = findViewById(R.id.guidesCoordinator);
        coordinators_hub_title = findViewById(R.id.coordinators_hub_title);

        if (Client.getCurrentUser().getGender().equals("זכר"))
            coordinators_hub_title.setText("ברוך הבא " + Client.getCurrentUser().getFirst_name() + "!");
        else
            coordinators_hub_title.setText("ברוכה הבאה " + Client.getCurrentUser().getFirst_name() + "!");

        guides.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(coordinators_hub.this, coordinators_guides.class));
            }
        });
    }
}