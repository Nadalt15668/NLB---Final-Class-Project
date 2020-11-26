package nadav.altabet.nlb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class welcome_screen extends AppCompatActivity {

    private Button register, login;
    private ProgressDialog prg;
    private EditText email, password;
    //---------------------------------------------------
    private FirebaseAuth firebaseAuth = null;
    private FirebaseDatabase firebaseDatabase = null;
    private DatabaseReference databaseReference = null;
    //---------------------------------------------------

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
        setContentView(R.layout.activity_welcome_screen);
        register = findViewById(R.id.register);
        login = findViewById(R.id.login);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        prg = new ProgressDialog(welcome_screen.this);
        prg.setTitle("מעלה נתונים");
        prg.setMessage("מעלה את נתוני המשתמש");
        prg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        prg.setCancelable(false);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Parents");

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(welcome_screen.this, nadav.altabet.nlb.register.class));
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prg.show();
                String login_email = email.getText().toString();
                String login_password = password.getText().toString();
                firebaseAuth.signInWithEmailAndPassword(login_email,login_password).addOnCompleteListener(welcome_screen.this,new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            Client.setUID(firebaseAuth.getUid());
                            databaseReference.child(firebaseAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    Client.setCurrentUser(dataSnapshot.getValue(User.class));
                                    prg.dismiss();
                                    if (Client.getCurrentUser().getType().equals("parent"))
                                    {
                                        startActivity(new Intent(welcome_screen.this,parents_hub.class));
                                        Toast.makeText(welcome_screen.this, "התחברת בהצלחה!", Toast.LENGTH_SHORT).show();
                                    }
                                    else if(Client.getCurrentUser().getType().equals("admin"))
                                    {
                                        startActivity(new Intent(welcome_screen.this, admins_hub.class));
                                        Toast.makeText(welcome_screen.this, "התחברת בהצלחה!", Toast.LENGTH_SHORT).show();
                                    }
                                    else
                                    {
                                        Client.setCurrentUser(null);
                                        Client.setUID(null);
                                        Toast.makeText(welcome_screen.this, "סוג המשתמש שלך לא מזוהה במערכת, אנא פנה למזכירות התנועה לבירור הבעיה", Toast.LENGTH_LONG).show();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    Toast.makeText(welcome_screen.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                    prg.dismiss();
                                }
                            });
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(welcome_screen.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        prg.dismiss();
                    }
                });
            }
        });
    }
}