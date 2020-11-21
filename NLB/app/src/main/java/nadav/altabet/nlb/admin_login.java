package nadav.altabet.nlb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

public class admin_login extends AppCompatActivity {

    private EditText email_admin, password_admin;
    private Button login_admin;
    private ProgressDialog prg;
    //---------------------------------------------------
    private FirebaseAuth firebaseAuth = null;
    private FirebaseDatabase firebaseDatabase = null;
    private DatabaseReference databaseReference = null;
    //---------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);
        email_admin = findViewById(R.id.email_admin);
        password_admin = findViewById(R.id.password_admin);
        login_admin = findViewById(R.id.login_admin);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Admins");

        prg = new ProgressDialog(admin_login.this);
        prg.setTitle("מתחבר כמנהל");
        prg.setMessage("מעלה את נתוני המשתמש");
        prg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        prg.setCancelable(false);

        login_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = email_admin.getText().toString();
                final String password = password_admin.getText().toString();
                firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(admin_login.this,new OnCompleteListener<AuthResult>() {
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
                                    Toast.makeText(admin_login.this, "התחברת בהצלחה!", Toast.LENGTH_SHORT).show();
                                    if (Client.getCurrentUser().getType().equals("admin"))
                                        startActivity(new Intent(admin_login.this,admins_hub.class));
                                    else
                                    {
                                        Client.setCurrentUser(null);
                                        Client.setUID(null);
                                        prg.dismiss();
                                        AlertDialog.Builder dialog = new AlertDialog.Builder(admin_login.this)
                                                .setTitle("הפרטים שהזנת אינם מתאימים")
                                                .setMessage("אנא בדוק שוב האם הפרטים שהזנת הינם הפרטים הנכונים. במידה " +
                                                        "והם נכונים ונפלה טעות בבדיקתם, אנא צור קשר עם הנהלת התנועה לבירור הפרטים השמורים במערכת, תודה.")
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();
                                                    }
                                                });
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    Toast.makeText(admin_login.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                    prg.dismiss();
                                }
                            });
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(admin_login.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        prg.dismiss();
                    }
                });
            }
        });
    }
}