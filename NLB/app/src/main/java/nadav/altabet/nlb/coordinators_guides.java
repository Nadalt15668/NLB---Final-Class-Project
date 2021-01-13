package nadav.altabet.nlb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class coordinators_guides extends AppCompatActivity {

    private FloatingActionButton add_guide;
    private ListView guide_listview;
    public static ArrayList<User> arrGuide = new ArrayList<>();
    public static ArrayList<String> arrGuideUid = new ArrayList<>();
    private ProgressDialog prg;

    private Task<Void> reference = null;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference parentsReference = database.getReference("Parents");

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
        setContentView(R.layout.activity_coordinators_guides);

        add_guide = findViewById(R.id.add_fab_guide);

        prg = new ProgressDialog(coordinators_guides.this);
        prg.setTitle("מציג נתונים");
        prg.setMessage("מעלה את נתוני המדריכים");
        prg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        prg.setCancelable(false);
        arrGuide.clear();
        arrGuideUid.clear();
        guide_listview = findViewById(R.id.guide_listview);


        add_guide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(coordinators_guides.this, nadav.altabet.nlb.add_guide.class));
            }
        });

        parentsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for (DataSnapshot childDatabase: children)
                {
                    User guideWL = childDatabase.getValue(User.class);
                    if (guideWL.getType().equals("guide") && guideWL.getBranch_name().equals(Client.getCurrentUser().getBranch_name()))
                    {
                        arrGuide.add(guideWL);
                        arrGuideUid.add(childDatabase.getKey());
                    }
                }
                guide_cardlsview_adap guide_cardlsview_adap = new guide_cardlsview_adap(arrGuide, coordinators_guides.this, prg);
                guide_listview.setAdapter(guide_cardlsview_adap);

                guide_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                        AlertDialog dialog = new AlertDialog.Builder(coordinators_guides.this).create();
                        String dialogMessage, dialogTitle;
                        if (Client.getCurrentUser().getGender().equals("זכר"))
                        {
                            dialogTitle = "אתה בטוח?";
                            dialogMessage = "האם אתה בטוח שאתה רוצה להסיר?";
                        }
                        else
                        {
                            dialogTitle = "את בטוחה?";
                            dialogMessage = "האם את בטוחה שאת רוצה להסיר?";
                        }
                        dialog.setTitle(dialogTitle);
                        dialog.setMessage(dialogMessage);
                        dialog.setCancelable(false);
                        dialog.setButton(dialog.BUTTON_NEGATIVE, "כן", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                reference = database.getReference("Parents").child(arrGuideUid.get(position)).child("type").setValue("guideWL");
                                startActivity(new Intent(coordinators_guides.this, coordinators_hub.class));
                                Toast.makeText(coordinators_guides.this, "המדריך הוסר בהצלחה!", Toast.LENGTH_SHORT).show();
                            }
                        });
                        dialog.setButton(dialog.BUTTON_POSITIVE, "לא", new DialogInterface.OnClickListener() {
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