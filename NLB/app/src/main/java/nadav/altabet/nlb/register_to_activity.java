package nadav.altabet.nlb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
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

public class register_to_activity extends AppCompatActivity {

    private ListView activities_lsview;
    private ArrayList<ArrayList<String>> arrChildUidArray = new ArrayList<>();
    private ArrayList<Activity> arrActivities = new ArrayList<>();
    private ArrayList<String> arrUID = new ArrayList<>();
    private Task<Void> reference = null;

    private DatabaseReference activitiesReference = FirebaseDatabase.getInstance().getReference("Activities").
            child(parents_children.arrChild.get(parents_children.Position).getChild_branch());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_to_activity);

        activities_lsview = findViewById(R.id.activities_list_lsview);

        activitiesReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for (DataSnapshot childDatabase: children)
                {
                    Activity activity = childDatabase.getValue(Activity.class);
                    if (activity.getActivity_class().equals(parents_children.arrChild.get(parents_children.Position).getChild_class_letter()))
                    {
                        arrActivities.add(activity);
                        arrChildUidArray.add(activity.getChildUID());
                        arrUID.add(childDatabase.getKey());
                    }

                    activity_cardlsview_adap adap = new activity_cardlsview_adap(arrActivities, register_to_activity.this);
                    activities_lsview.setAdapter(adap);

                    activities_lsview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                            if (!arrActivities.get(position).getChildUID().contains(parents_children.arrChildUID.get(parents_children.Position)))
                            {
                                android.app.AlertDialog dialog = new AlertDialog.Builder(register_to_activity.this).create();
                                dialog.setTitle("הרשמה לפעולה");
                                dialog.setMessage("בחרת לרשום את ילדך לפעולה: " + arrActivities.get(position).getActivity_name());
                                dialog.setCancelable(false);
                                dialog.setButton(dialog.BUTTON_NEGATIVE, "כן", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        ArrayList<String> childrenArray = arrChildUidArray.get(position);
                                        childrenArray.add(parents_children.arrChildUID.get(parents_children.Position));
                                        reference = activitiesReference.child(arrUID.get(position)).child("childUID").setValue(childrenArray);
                                        startActivity(new Intent(register_to_activity.this, parents_hub.class));
                                        Toast.makeText(register_to_activity.this, "החניך נרשם בהצלחה לפעולה!", Toast.LENGTH_SHORT).show();
                                        String phone = arrActivities.get(position).getGuidePhone();
                                        String Message = parents_children.arrChild.get(parents_children.Position).getChild_first_name() + " " +
                                                parents_children.arrChild.get(parents_children.Position).getChild_last_name() + " נרשם לפעולה " + '"' +
                                                arrActivities.get(position).getActivity_name() + '"';
                                        SmsManager sm = SmsManager.getDefault();
                                        sm.sendTextMessage(phone, null, Message, null, null);
                                        Toast.makeText(register_to_activity.this, "הודעה על ההרשמה נשלחה למדריך!", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
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
                            else {
                                android.app.AlertDialog dialog = new AlertDialog.Builder(register_to_activity.this).create();
                                dialog.setTitle("הילד כבר רשום לפעולה");
                                dialog.setMessage("ילדך כבר רשום לפעולה זו, אנא בחר פעולה אחרת." + arrActivities.get(position).getActivity_name());
                                dialog.setCancelable(false);
                                dialog.setButton(dialog.BUTTON_POSITIVE, "כן", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                                dialog.show();
                            }
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(register_to_activity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}