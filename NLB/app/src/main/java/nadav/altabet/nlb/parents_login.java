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

public class parents_login extends AppCompatActivity {
    private EditText parents_email,parents_password;
    private Button parents_register,parents_login;
    private ProgressDialog prg;
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
        setContentView(R.layout.activity_parents_login);
        parents_email = findViewById(R.id.parents_email);
        parents_password = findViewById(R.id.parents_password);
        parents_register = findViewById(R.id.parents_register);
        parents_login = findViewById(R.id.parents_login);

        prg = new ProgressDialog(parents_login.this);
        prg.setTitle("Loading Data");
        prg.setMessage("Logging In");
        prg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        prg.setCancelable(false);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Parents");

        parents_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(parents_login.this, nadav.altabet.nlb.parents_register.class));
            }
        });
        parents_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prg.show();
                final String email = parents_email.getText().toString();
                final String password = parents_password.getText().toString();
                firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(parents_login.this,new OnCompleteListener<AuthResult>() {
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
                                    Toast.makeText(parents_login.this, "התחברת בהצלחה!", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(parents_login.this,parents_hub.class));
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    Toast.makeText(parents_login.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                    prg.dismiss();
                                }
                            });
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(parents_login.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        prg.dismiss();
                    }
                });
            }
        });
    }
}
