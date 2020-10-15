package nadav.altabet.nlb;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parents_children);
    }
}