package nadav.altabet.nlb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class parents_children extends AppCompatActivity {
    private ListView child_listview;
    private ArrayList<String> arrChildFirstName = new ArrayList<>();
    private ArrayList<String> arrChildLastName = new ArrayList<>();
    private ArrayList<String> arrChildID = new ArrayList<>();
    private ArrayList<String> arrChildGender = new ArrayList<>();
    private ArrayList<String> arrChildBranch = new ArrayList<>();
    private ArrayList<String> arrChildClass = new ArrayList<>();
    private ArrayList<String> arrChildSchool = new ArrayList<>();
    private ArrayList<String> arrChildPhone = new ArrayList<>();
    private ArrayList<String> arrChildEmail = new ArrayList<>();
    private ArrayList<String> arrChildProfile = new ArrayList<>();
    private ArrayList<Date> arrChildBirthdate = new ArrayList<>();
    private ArrayList<Child> arrChild = new ArrayList<>();
    private FloatingActionButton add;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = database.getReference("Children");

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

        child_listview = findViewById(R.id.child_listview);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for (DataSnapshot childDatabase: children)
                {
                    Child child = childDatabase.getValue(Child.class);
                    if (child.getParent_UID().equals(Client.getUID()))
                    {
                        arrChildFirstName.add(child.getChild_first_name());
                        arrChildLastName.add(child.getChild_last_name());
                        arrChildID.add(child.getChild_ID());
                        arrChildGender.add(child.getChild_gender());
                        arrChildBranch.add(child.getChild_branch());
                        arrChildClass.add(child.getChild_class_letter() + "'" + child.getChild_class_number());
                        arrChildSchool.add(child.getChild_school());
                        arrChildPhone.add(child.getChild_phone());
                        arrChildEmail.add(child.getChild_email());
                        arrChildProfile.add(child.getChild_profile());
                        arrChildBirthdate.add(child.getChild_birthdate());
                        arrChild.add(child);
                    }

                }
                child_cardlsview_adap adap = new child_cardlsview_adap(arrChildFirstName,arrChildLastName, arrChildGender, arrChildClass, arrChildSchool,
                        arrChildID,arrChildBranch,arrChildPhone,arrChildEmail,arrChildProfile,arrChildBirthdate,parents_children.this);
                child_listview.setAdapter(adap);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}