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

public class activity_cardlsview_adap extends BaseAdapter {

    private ArrayList<nadav.altabet.nlb.Activity> arrActivities;
    private ProgressDialog prg;
    private android.app.Activity ctx;
    DatabaseReference parentsReference = FirebaseDatabase.getInstance().getReference("Parents");



    public activity_cardlsview_adap(ArrayList<nadav.altabet.nlb.Activity> arrActivities, Activity ctx) {
        this.arrActivities = arrActivities;
        this.ctx = ctx;
    }

    @Override
    public int getCount() {
        return this.arrActivities.size();
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

        LayoutInflater inflater =ctx.getLayoutInflater();
        final View myrow= inflater.inflate(R.layout.activity_listview,null,true);

        TextView activity_name = myrow.findViewById(R.id.activity_name);
        TextView activity_class = myrow.findViewById(R.id.activity_class);
        TextView activity_date = myrow.findViewById(R.id.activity_date);
        TextView creation_date = myrow.findViewById(R.id.creation_date);
        String activityDate = this.arrActivities.get(position).getActivity_date().getDay() + "/" +
                this.arrActivities.get(position).getActivity_date().getMonth() + "/" +
                this.arrActivities.get(position).getActivity_date().getYear();
        String creationDate = this.arrActivities.get(position).getCreation_date().getDay() + "/" +
                this.arrActivities.get(position).getCreation_date().getMonth() + "/" +
                this.arrActivities.get(position).getCreation_date().getYear();

        activity_name.setText(arrActivities.get(position).getActivity_name());
        activity_class.setText(arrActivities.get(position).getActivity_class());
        activity_date.setText(activityDate);
        creation_date.setText(creationDate);
        return myrow;
    }
}