package nadav.altabet.nlb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
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

public class my_messages extends AppCompatActivity {

    private ListView my_messages;
    private FloatingActionButton write_message;
    private ArrayList<Message> messageArray = new ArrayList();
    private ArrayList<User> usersArray = new ArrayList<>();
    private DatabaseReference messagesReference = FirebaseDatabase.getInstance().getReference("Messages");
    private DatabaseReference usersReference = FirebaseDatabase.getInstance().getReference("Parents");
    private Task<Void> reference = null;
    private AlertDialog replyDialog;

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
        protected void onCreate(Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_my_messages);
            my_messages = findViewById(R.id.my_messages_lsview);
            write_message = findViewById(R.id.fab_write_message);
            write_message.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(my_messages.this, message_receivers.class));
                }
            });

            usersReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                    for (DataSnapshot child : children) {
                        User user = child.getValue(User.class);
                        usersArray.add(user);
                    }
                    messagesReference.child(Client.getCurrentUser().getFirst_name() + " " +
                            Client.getCurrentUser().getLast_name()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                            for (DataSnapshot child : children) {
                                Message message = child.getValue(Message.class);
                                messageArray.add(message);
                            }
                            try {
                                final message_cardlsview_adap adap = new message_cardlsview_adap(messageArray, usersArray, nadav.altabet.nlb.my_messages.this);
                                my_messages.setAdapter(adap);
                            } catch (Exception e) {
                                Toast.makeText(my_messages.this, "אין לך הודעות חדשות", Toast.LENGTH_LONG).show();
                            }
                            my_messages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, final int position, long l) {
                                    reference = messagesReference.child(Client.getCurrentUser().getFirst_name() + " " +
                                            Client.getCurrentUser().getLast_name()).
                                            child(messageArray.get(position).getMessageUID()).child("status").setValue("true");
                                    final AlertDialog.Builder dialog = new AlertDialog.Builder(my_messages.this);
                                    dialog.setTitle(messageArray.get(position).getMessageHeadline());
                                    dialog.setMessage(messageArray.get(position).getMessageContent());
                                    dialog.setNegativeButton("אוקיי", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.cancel();
                                        }
                                    }).setPositiveButton("הגב", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, final int i) {
                                            dialogInterface.cancel();
                                            //להוסיף קוד שמאפשר מתן תגובה
                                            AlertDialog.Builder builder = new AlertDialog.Builder(my_messages.this);
                                            View writeMessageView = getLayoutInflater().inflate(R.layout.write_message, null);
                                            final EditText message_headline = writeMessageView.findViewById(R.id.message_headline);
                                            final EditText message_body = writeMessageView.findViewById(R.id.message_body);
                                            Button cancel = writeMessageView.findViewById(R.id.cancel_message);
                                            Button send = writeMessageView.findViewById(R.id.send_message);

                                            cancel.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    replyDialog.dismiss();
                                                }
                                            });
                                            send.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    if (!message_headline.getText().toString().equals("") && !message_body.getText().toString().equals("")) {
                                                        User receiver = null;
                                                        for (int j = 0; j < usersArray.size(); j++) {
                                                            if (usersArray.get(j).getEmail().equals(messageArray.get(position).getSenderEmail())) {
                                                                receiver = usersArray.get(j);
                                                                break;
                                                            }
                                                        }
                                                        String messageUID = messagesReference.child(receiver.getFirst_name() + " " + receiver.getLast_name()).push().getKey();
                                                        Message message = new Message(messageUID, receiver.getEmail(), message_body.getText().toString(), message_headline.getText().toString());
                                                        reference = messagesReference.child(receiver.getFirst_name() + " " + receiver.getLast_name()).child(messageUID).setValue(message);
                                                        Toast.makeText(my_messages.this, "ההודעה נשלחה בהצלחה!", Toast.LENGTH_SHORT).show();
                                                        replyDialog.dismiss();
                                                    } else
                                                        Toast.makeText(my_messages.this, "אנא מלאו את כל השדות", Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                            builder.setView(writeMessageView);
                                            replyDialog = builder.create();
                                            replyDialog.show();
                                        }
                                    });
                                    AlertDialog alertDialog = dialog.create();
                                    alertDialog.setTitle(messageArray.get(position).getMessageHeadline());
                                    alertDialog.show();
                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }