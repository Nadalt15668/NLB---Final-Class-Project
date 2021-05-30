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

public class guides_hub extends AppCompatActivity {

    private TextView guides_hub_title;
    private CardView my_activities, logout, messages;
    private ImageView messagesImg;
    private DatabaseReference messageReference = FirebaseDatabase.getInstance().getReference("Messages").
            child(Client.getCurrentUser().getFirst_name() + " " + Client.getCurrentUser().getLast_name());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guides_hub);
        guides_hub_title = findViewById(R.id.guides_hub_title);
        my_activities = findViewById(R.id.myActivites);
        messages = findViewById(R.id.messages);
        logout = findViewById(R.id.logout);
        messagesImg = findViewById(R.id.messagesImg);

        messages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(guides_hub.this, my_messages.class));
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Client.setCurrentUser(null);
                startActivity(new Intent(guides_hub.this, welcome_screen.class));
            }
        });

        if (Client.getCurrentUser().getGender().equals("זכר"))
            guides_hub_title.setText("ברוך הבא " + Client.getCurrentUser().getFirst_name() + "!");
        else
            guides_hub_title.setText("ברוכה הבאה " + Client.getCurrentUser().getFirst_name() + "!");

        my_activities.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(guides_hub.this, guides_activities.class));
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