package nadav.altabet.nlb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class list_of_activities extends AppCompatActivity {
    private ListView activities_list;
    private ArrayList<Activity> activitiesArray = new ArrayList<>();
    private Context context = this;
    ArrayList<String> childrenUID = new ArrayList<>();
    ArrayList<String> activitiesUID = new ArrayList<>();
    ArrayList<Child> childrenArray = new ArrayList<>();
    private Task<Void> reference=null;

    private DatabaseReference childrenReference = FirebaseDatabase.getInstance().getReference("Children");
    private  DatabaseReference activitiesReference;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        switch (Client.getCurrentUser().getType())
        {
            case "parent":
            {
                MenuInflater inflater = getMenuInflater();
                inflater.inflate(R.menu.parents_menu, menu);
                return true;
            }
            case "guide":
            {
                MenuInflater inflater = getMenuInflater();
                inflater.inflate(R.menu.guides_menu, menu);
                return true;
            }
            case "worker":
            {
                MenuInflater inflater = getMenuInflater();
                inflater.inflate(R.menu.coordinators_menu, menu);
                return true;
            }
            case "admin":
            {
                MenuInflater inflater = getMenuInflater();
                inflater.inflate(R.menu.admins_menu, menu);
                return true;
            }
        }
        return false;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (Client.getCurrentUser().getType()) {
            case "parent": {
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
            case "guide": {
                switch (item.getItemId()) {
                    case R.id.menu__guides_homepage:
                        startActivity(new Intent(this, guides_hub.class));
                        return true;
                    case R.id.menu_guides_activities:
                        startActivity(new Intent(this, guides_activities.class));
                        return true;
                    default:
                        return super.onOptionsItemSelected(item);
                }
            }
            case "worker":
            {
                switch (item.getItemId()) {
                    case R.id.menu__coordinators_homepage:
                        startActivity(new Intent(this, coordinators_hub.class));
                        return true;
                    case R.id.menu_coordinators_guides:
                        startActivity(new Intent(this, coordinators_guides.class));
                        return true;
                    default:
                        return super.onOptionsItemSelected(item);
                }
            }
            case "admin":
            {
                switch (item.getItemId()) {
                    case R.id.menu_admins_profile:
                        return true;
                    case R.id.menu__admins_homepage:
                        startActivity(new Intent(this, admins_hub.class));
                        return true;
                    case R.id.menu_admins_workers:
                        startActivity(new Intent(this, admins_workers.class));
                        return true;
                    default:
                        return super.onOptionsItemSelected(item);
                }
            }
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_activities);

        activities_list = findViewById(R.id.list_of_activities);

        childrenReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for (final DataSnapshot childReferenceDatabase : children) {

                    Child child = childReferenceDatabase.getValue(Child.class);

                    if (child.getParent_UID().equals(Client.getUID()))
                    {
                        childrenUID.add(childReferenceDatabase.getKey());
                        childrenArray.add(child);

                        activitiesReference = FirebaseDatabase.getInstance().getReference("Activities").
                                child(child.getChild_branch());

                        activitiesReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                                for (DataSnapshot activityReferenceDatabase:children) {

                                    Activity activity = activityReferenceDatabase.getValue(Activity.class);
                                    if (activity.getChildUID().contains(childReferenceDatabase.getKey()))
                                    {
                                        activitiesArray.add(activity);
                                        activitiesUID.add(activityReferenceDatabase.getKey());
                                    }
                                }

                                activity_cardlsview_adap adap = new activity_cardlsview_adap(activitiesArray, list_of_activities.this);
                                activities_list.setAdapter(adap);

                                activities_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                                        AlertDialog.Builder dialog = new AlertDialog.Builder(list_of_activities.this);
                                        dialog.setTitle("האם את/ה בטוח/ה?");
                                        dialog.setMessage("האם את/ה בטוח/ה שברצונך להסיר את החניך/ים הרשום/ים מהפעולה? ");
                                        dialog.setNegativeButton("כן", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                final ArrayList<String> childrenUIDArray = activitiesArray.get(position).getChildUID();
                                                final String[] branchName = new String[1];
                                                childrenReference.addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                                                        for (DataSnapshot childDatabase:children) {
                                                            if (childDatabase.getKey().equals(childrenUIDArray.get(1)))
                                                            {
                                                                Child childForBranch = childDatabase.getValue(Child.class);
                                                                branchName[0] = childForBranch.getChild_branch();
                                                            }
                                                        }

                                                        for (int i = 0; i < childrenUID.size(); i++) {
                                                            if (childrenUIDArray.contains(childrenUID.get(i)))
                                                                childrenUIDArray.remove(childrenUID.get(i));
                                                        }
                                                        reference = FirebaseDatabase.getInstance().getReference("Activities").
                                                                child(branchName[0]).child(activitiesUID.get(position)).child("childUID").setValue(childrenUIDArray);
                                                        Toast.makeText(list_of_activities.this, "החניך/ים הוסר/ו מהפעולה בהצלחה!", Toast.LENGTH_SHORT).show();
                                                        startActivity(new Intent(list_of_activities.this, parents_hub.class));
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                    }
                                                });
                                            }
                                        }).setPositiveButton("לא", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        });
                                        dialog.show();
                                    }
                                });

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}