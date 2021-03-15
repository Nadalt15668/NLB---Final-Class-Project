package nadav.altabet.nlb;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class my_messages extends AppCompatActivity {

    private FloatingActionButton write_message;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_messages);
        write_message = findViewById(R.id.fab_write_message);
        
    }
}