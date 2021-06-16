package nadav.altabet.nlb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.media.Image;
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

public class coordinators_hub extends AppCompatActivity {

    private TextView coordinators_hub_title;
    private CardView guides,logout,messages;
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
        setContentView(R.layout.activity_coordinators_hub);
        guides = findViewById(R.id.guidesCoordinator);
        logout = findViewById(R.id.logout);
        messages = findViewById(R.id.messages);
        coordinators_hub_title = findViewById(R.id.coordinators_hub_title);
        messagesImg = findViewById(R.id.messagesImg);

        if (Client.getCurrentUser().getGender().equals("זכר"))
            coordinators_hub_title.setText("ברוך הבא " + Client.getCurrentUser().getFirst_name() + "!");
        else
            coordinators_hub_title.setText("ברוכה הבאה " + Client.getCurrentUser().getFirst_name() + "!");

        guides.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(coordinators_hub.this, coordinators_guides.class));
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Client.setCurrentUser(null);
                Client.setUID(null);
                startActivity(new Intent(coordinators_hub.this, welcome_screen.class));
            }
        });
        messages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(coordinators_hub.this, my_messages.class));
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