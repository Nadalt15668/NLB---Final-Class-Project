package nadav.altabet.nlb;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

public class parents_hub extends AppCompatActivity {

    private TextView parents_hub_title;
    private CardView logout,profile,children,online_store;

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
        setContentView(R.layout.activity_parents_hub);

        logout = findViewById(R.id.logout);
        profile = findViewById(R.id.myProfile);
        children = findViewById(R.id.children);
        online_store = findViewById(R.id.onlineStore);
        parents_hub_title = findViewById(R.id.parents_hub_title);
        if (Client.getCurrentUser().getGender().equals("זכר"))
            parents_hub_title.setText("ברוך הבא " + Client.getCurrentUser().getFirst_name() + "!");
        else
            parents_hub_title.setText("ברוכה הבאה " + Client.getCurrentUser().getFirst_name() + "!");

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Client.setCurrentUser(null);
                startActivity(new Intent(parents_hub.this, welcome_screen.class));
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(parents_hub.this, parents_profile.class));
            }
        });
        children.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(parents_hub.this,parents_children.class));
            }
        });
    }
}
