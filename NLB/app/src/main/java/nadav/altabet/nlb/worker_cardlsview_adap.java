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

public class worker_cardlsview_adap extends BaseAdapter {

    private ArrayList<Worker> arrWorkers;
    private ProgressDialog prg;
    private Activity ctx;
    DatabaseReference parentsReference = FirebaseDatabase.getInstance().getReference("Parents");


    private String mypath="gs://nlb-project-2287b.appspot.com";

    public worker_cardlsview_adap(ArrayList<Worker> arrWorkers,  Activity ctx, ProgressDialog prg) {
        this.arrWorkers = arrWorkers;
        this.ctx = ctx;
        this.prg = prg;
    }

    @Override
    public int getCount() {
        return this.arrWorkers.size();
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

        final View myrow= inflater.inflate(R.layout.worker_listview,null,true);

        TextView worker_email = myrow.findViewById(R.id.worker_email);
        final TextView worker_name = myrow.findViewById(R.id.worker_name);
        TextView worker_startDate = myrow.findViewById(R.id.worker_startDate);
        TextView worker_role = myrow.findViewById(R.id.worker_role);
        final ImageView worker_profile = myrow.findViewById(R.id.worker_profile);


        worker_role.setText(arrWorkers.get(position).getRole());
        worker_email.setText(this.arrWorkers.get(position).getEmail());
        String startDate = this.arrWorkers.get(position).getStartDate().getDay() + "/" +
                this.arrWorkers.get(position).getStartDate().getMonth() + "/" +
                this.arrWorkers.get(position).getStartDate().getYear();
        worker_startDate.setText(startDate);

        parentsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for (DataSnapshot childDatabase: children)
                {
                    User user = childDatabase.getValue(User.class);
                    if (user.getEmail().equals(arrWorkers.get(position).getEmail()))
                    {

                        worker_name.setText(user.getFirst_name() + " " + user.getLast_name());
                        String suffix=user.getPic().substring(user.getPic().lastIndexOf(".")+1);
                        FirebaseStorage storage = FirebaseStorage.getInstance();
                        StorageReference storageRef = storage.getReferenceFromUrl(mypath).child("Child Profiles").child(user.getPic());
                        try
                        {
                            final File localFile = File.createTempFile(user.getPic(), suffix);
                            storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>()
                            {
                                @Override
                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot)
                                {
                                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                    worker_profile.setImageBitmap(bitmap);
                                    prg.dismiss();
                                }
                            }).addOnFailureListener(new OnFailureListener()
                            {
                                @Override
                                public void onFailure(@NonNull Exception exception)
                                {
                                    String message=exception.getMessage();
                                    Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show();
                                }
                            });
                        } catch (IOException e ) {} catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        return myrow;
    }
}

