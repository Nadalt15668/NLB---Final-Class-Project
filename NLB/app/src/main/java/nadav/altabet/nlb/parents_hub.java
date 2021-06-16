package nadav.altabet.nlb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class parents_hub extends AppCompatActivity {

    private TextView parents_hub_title;
    private CardView logout,profile,children,activities_list, messages;
    private ImageView messagesImg;
    private DatabaseReference messageReference = FirebaseDatabase.getInstance().getReference("Messages").
            child(Client.getUID());

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
        messagesImg = findViewById(R.id.messagesImg);
        logout = findViewById(R.id.logout);
        profile = findViewById(R.id.myProfile);
        children = findViewById(R.id.children);
        activities_list = findViewById(R.id.activitiesList);
        parents_hub_title = findViewById(R.id.parents_hub_title);
        messages = findViewById(R.id.messages);
        if (Client.getCurrentUser().getGender().equals("male"))
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
        activities_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(parents_hub.this, list_of_activities.class));
            }
        });
        messages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(parents_hub.this, my_messages.class));
            }
        });

        messageReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for (DataSnapshot child:children) {
                    if (child.getValue(Message.class).isStatus().equals("unread"))
                    {
                        messagesImg.setImageResource(R.drawable.new_message_100px);
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
