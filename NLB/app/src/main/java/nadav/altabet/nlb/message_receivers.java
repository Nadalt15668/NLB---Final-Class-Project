package nadav.altabet.nlb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class message_receivers extends AppCompatActivity {

    private ListView receivers_lsview;
    private Button search, delete, cancel, send;
    private EditText searchLineEdit, message_headline, message_body;
    private ArrayList<User> usersArray = new ArrayList<>();
    private ArrayList<User> searchAdapter = new ArrayList<>();
    private AlertDialog dialog;
    private Task<Void> reference = null;

    private DatabaseReference usersReference = FirebaseDatabase.getInstance().getReference("Parents");
    private DatabaseReference messagesReference = FirebaseDatabase.getInstance().getReference("Messages");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_receivers);
        receivers_lsview = findViewById(R.id.receivers_lsview);
        search = findViewById(R.id.search);
        searchLineEdit = findViewById(R.id.searchLineEditTxt);
        delete = findViewById(R.id.delete);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchAdapter.clear();
                String searchLine = searchLineEdit.getText().toString();
                if (!searchLine.matches(""))
                {
                    String fullName = "";
                    for (int i = 0; i < usersArray.size()-1; i++) {
                         fullName = usersArray.get(i).getFirst_name() + usersArray.get(i).getLast_name();
                        if ((fullName.contains(searchLine)))
                        {
                            searchAdapter.add(usersArray.get(i));
                        }
                    }
                    ArrayAdapter adapter = new ArrayAdapter(message_receivers.this, android.R.layout.simple_list_item_1, searchAdapter);
                    receivers_lsview.setAdapter(adapter);
                }
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchAdapter.clear();
                searchLineEdit.setText("");
                ArrayAdapter adapter = new ArrayAdapter(message_receivers.this, android.R.layout.simple_list_item_1, usersArray);
                receivers_lsview.setAdapter(adapter);
            }
        });

        usersReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for (DataSnapshot userDatabase:children) {
                    User user = userDatabase.getValue(User.class);
                    usersArray.add(user);
                }
                ArrayAdapter adapter = new ArrayAdapter(message_receivers.this, android.R.layout.simple_list_item_1, usersArray);
                receivers_lsview.setAdapter(adapter);

                receivers_lsview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(message_receivers.this);
                        View writeMessageView = new View(message_receivers.this, null, R.layout.write_message);
                        message_headline = writeMessageView.findViewById(R.id.message_headline);
                        message_body = writeMessageView.findViewById(R.id.message_body);
                        cancel = writeMessageView.findViewById(R.id.cancel_message);
                        send = writeMessageView.findViewById(R.id.send_message);

                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        send.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                User receiver = ((User) receivers_lsview.getAdapter().getItem(position));
                                String messageUID = messagesReference.child(receiver.getFirst_name() + " " + receiver.getLast_name()).push().getKey();
                                Message message = new Message(messageUID, receiver.getEmail(), message_body.getText().toString(), message_headline.getText().toString());
                                reference = messagesReference.child(receiver.getFirst_name() + " " + receiver.getLast_name()).child(messageUID).setValue(message);
                                Toast.makeText(message_receivers.this, "ההודעה נשלחה בהצלחה!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(message_receivers.this, my_messages.class));
                                dialog.dismiss();
                            }
                        });
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(message_receivers.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }
}