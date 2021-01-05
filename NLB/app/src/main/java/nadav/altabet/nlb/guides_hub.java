package nadav.altabet.nlb;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.widget.TextView;

public class guides_hub extends AppCompatActivity {

    private TextView guides_hub_title;
    private CardView my_activities;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guides_hub);
        guides_hub_title = findViewById(R.id.guides_hub_title);
        my_activities = findViewById(R.id.myActivites);

        if (Client.getCurrentUser().getGender().equals("זכר"))
            guides_hub_title.setText("ברוך הבא " + Client.getCurrentUser().getFirst_name() + "!");
        else
            guides_hub_title.setText("ברוכה הבאה " + Client.getCurrentUser().getFirst_name() + "!");
    }
}