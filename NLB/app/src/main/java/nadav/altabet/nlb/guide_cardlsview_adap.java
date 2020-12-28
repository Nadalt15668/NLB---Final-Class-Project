package nadav.altabet.nlb;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class guide_cardlsview_adap  extends BaseAdapter {

    private ArrayList<User> arrGuideWL;
    private ProgressDialog prg;
    private Activity ctx;
    DatabaseReference guidesReference = FirebaseDatabase.getInstance().getReference("Guides");
    private String mypath="gs://nlb-project-2287b.appspot.com";

    public guide_cardlsview_adap(ArrayList<User> arrGuideWL,  Activity ctx, ProgressDialog prg) {
        this.arrGuideWL = arrGuideWL;
        this.ctx = ctx;
        this.prg = prg;
    }

    @Override
    public int getCount() {
        return this.arrGuideWL.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {

        this.prg.show();
        LayoutInflater inflater =ctx.getLayoutInflater();
        final View myrow= inflater.inflate(R.layout.guide_listview,null,true);

        TextView guide_name = myrow.findViewById(R.id.guide_name);
        guide_name.setText(arrGuideWL.get(position).getFirst_name() + arrGuideWL.get(position).getLast_name());
        TextView guide_email = myrow.findViewById(R.id.guide_email);
        guide_email.setText(arrGuideWL.get(position).getEmail());
        TextView guide_gender = myrow.findViewById(R.id.guide_gender);
        guide_gender.setText(arrGuideWL.get(position).getGender());
        TextView guide_phone = myrow.findViewById(R.id.guide_phone);
        guide_phone.setText(arrGuideWL.get(position).getPhone());
        return myrow;
    }
}