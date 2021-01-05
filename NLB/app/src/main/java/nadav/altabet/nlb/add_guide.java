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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class add_guide extends AppCompatActivity {

    private ListView guideWL_listview;
    public static ArrayList<User> arrGuideWL = new ArrayList<>();
    public static ArrayList<String> arrGuideWLuid = new ArrayList<>();
    private ProgressDialog prg;

    private Task<Void> reference = null;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference guidesWLReference = database.getReference("GuidesWL");
    DatabaseReference guidesReference = database.getReference("Guides");

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
        setContentView(R.layout.activity_add_guide);

        prg = new ProgressDialog(add_guide.this);
        prg.setTitle("מציג נתונים");
        prg.setMessage("מעלה את נתוני המדריכים");
        prg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        prg.setCancelable(false);

        arrGuideWL.clear();
        arrGuideWLuid.clear();

        guideWL_listview = findViewById(R.id.guideWL_listview);

        guidesWLReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for (DataSnapshot childDatabase: children)
                {
                    User guideWL = childDatabase.getValue(User.class);
                    if (guideWL.getBranch_name().equals(Client.getCurrentUser().getBranch_name()))
                    {
                        arrGuideWL.add(guideWL);
                        arrGuideWLuid.add(childDatabase.getKey());
                    }
                }
                guide_cardlsview_adap guide_cardlsview_adap = new guide_cardlsview_adap(arrGuideWL, add_guide.this, prg);
                guideWL_listview.setAdapter(guide_cardlsview_adap);

                guideWL_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                        AlertDialog dialog = new AlertDialog.Builder(add_guide.this).create();
                        String dialogMessage, dialogTitle;
                        if (Client.getCurrentUser().getGender().equals("זכר"))
                        {
                            dialogTitle = "אתה בטוח?";
                            dialogMessage = "האם אתה בטוח שאתה רוצה להוסיף?";
                        }
                        else
                        {
                            dialogTitle = "את בטוחה?";
                            dialogMessage = "האם את בטוחה שאת רוצה להוסיף?";
                        }
                        dialog.setTitle(dialogTitle);
                        dialog.setMessage(dialogMessage);
                        dialog.setCancelable(false);
                        dialog.setButton(dialog.BUTTON_NEGATIVE, "כן", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                reference = database.getReference("GuidesWL").child(arrGuideWLuid.get(position)).setValue(null);
                                DatabaseReference newRef = guidesReference.push();
                                User guideWL = arrGuideWL.get(position);
                                guideWL.setType("guideWL");
                                reference = newRef.setValue(guideWL);
                                startActivity(new Intent(add_guide.this, coordinators_guides.class));
                                Toast.makeText(add_guide.this, "המדריך נוסף בהצלחה!", Toast.LENGTH_SHORT).show();
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