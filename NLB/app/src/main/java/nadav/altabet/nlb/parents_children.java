package nadav.altabet.nlb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class parents_children extends AppCompatActivity {
    private ListView card_listview;
    private final ArrayList<String> arrChildFirstName = new ArrayList<>();
    private final ArrayList<String> arrChildLastName = new ArrayList<>();
    private final ArrayList<String> arrParentEmail = new ArrayList<>();
    private final ArrayList<String> arrChildID = new ArrayList<>();
    private final ArrayList<String> arrChildBranch = new ArrayList<>();
    private final ArrayList<String> arrChildPhone = new ArrayList<>();
    private final ArrayList<String> arrChildEmail = new ArrayList<>();
    private final ArrayList<String> arrChildProfile = new ArrayList<>();
    private final ArrayList<String> arrChildGender = new ArrayList<>();
    private final ArrayList<String> arrChildClass = new ArrayList<>();
    private final ArrayList<Date> arrChildBirthdate = new ArrayList<>();
    private FloatingActionButton add;

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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parents_children);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        add = findViewById(R.id.add_fab);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(parents_children.this,add_child.class));
            }
        });
    }
}