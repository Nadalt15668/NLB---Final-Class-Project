package nadav.altabet.nlb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

public class parents_children extends AppCompatActivity {
    private ListView child_listview;
    public static ArrayList<Child> arrChild = new ArrayList<>();
    public static ArrayList<String> arrChildUID = new ArrayList<>();
    private FloatingActionButton add;
    private ProgressDialog prg;
    public static int Position = 0;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = database.getReference("Children");

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.parents_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu__parents_profile:
                startActivity(new Intent(this, parents_profile.class));
                return true;
            case R.id.menu_parents_children:
                startActivity(new Intent(this, parents_children.class));
                return true;
            case R.id.menu_parents_logout:
                Client.setCurrentUser(null);
                startActivity(new Intent(this, welcome_screen.class));
                return true;
            case R.id.menu__parents_homepage:
                startActivity(new Intent(this, parents_hub.class));
                return true;
            case R.id.menu_parents_online_store:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parents_children);

        prg = new ProgressDialog(parents_children.this);
        prg.setTitle("מציג נתונים");
        prg.setMessage("מעלה את נתוני הילדים");
        prg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        prg.setCancelable(false);

        arrChild.clear();
        arrChildUID.clear();
        add = findViewById(R.id.add_fab_child);
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
                        arrChild.add(child);
                        arrChildUID.add(childDatabase.getKey());
                    }

                }
                child_cardlsview_adap adap = new child_cardlsview_adap(arrChild, arrChildUID, parents_children.this, prg);
                child_listview.setAdapter(adap);

                child_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Position = position;
                        startActivity(new Intent(parents_children.this,children_update.class));
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}