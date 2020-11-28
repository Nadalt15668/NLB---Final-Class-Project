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
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class child_cardlsview_adap extends BaseAdapter {
    private ArrayList<Child> arrChild;
    private ArrayList<String> arrChildUID;
    private ProgressDialog prg;
    private Activity ctx;

    private String mypath="gs://nlb-project-2287b.appspot.com";

    public child_cardlsview_adap(ArrayList<Child> arrChild, ArrayList<String> arrChildUID,  Activity ctx, ProgressDialog prg) {
        this.arrChild = arrChild;
        this.arrChildUID = arrChildUID;
        this.ctx = ctx;
        this.prg = prg;
    }

    @Override
    public int getCount() {
        return this.arrChild.size();
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
    public View getView(int position, View view, ViewGroup viewGroup) {

        this.prg.show();
        LayoutInflater inflater =ctx.getLayoutInflater();

        final View myrow= inflater.inflate(R.layout.child_listview,null,true);

        TextView child_name = myrow.findViewById(R.id.child_name);
        child_name.setText(this.arrChild.get(position).getChild_first_name() + " " + this.arrChild.get(position).getChild_last_name());

        TextView child_ID=myrow.findViewById(R.id.child_ID);
        child_ID.setText(this.arrChild.get(position).getChild_ID());

        TextView child_gender=myrow.findViewById(R.id.child_gender);
        child_gender.setText(this.arrChild.get(position).getChild_gender());

        TextView child_birthdate=myrow.findViewById(R.id.child_birthdate);
        child_birthdate.setText(this.arrChild.get(position).getChild_birthdate().getDay() + "/" +
                this.arrChild.get(position).getChild_birthdate().getMonth() + "/" + this.arrChild.get(position).getChild_birthdate().getYear());

        TextView child_class=myrow.findViewById(R.id.child_class);
        child_class.setText(this.arrChild.get(position).getChild_class_letter() + "' " + this.arrChild.get(position).getChild_class_number());

        TextView child_school=myrow.findViewById(R.id.child_school);
        child_school.setText(this.arrChild.get(position).getChild_school());

        TextView child_branch=myrow.findViewById(R.id.child_branch);
        child_branch.setText(this.arrChild.get(position).getChild_branch());

        TextView child_email=myrow.findViewById(R.id.child_email);
        child_email.setText(this.arrChild.get(position).getChild_email());

        TextView child_phone=myrow.findViewById(R.id.child_phone);
        child_phone.setText(this.arrChild.get(position).getChild_phone());

        final ImageView child_profile = myrow.findViewById(R.id.child_profile);

        String suffix=arrChild.get(position).getChild_profile().substring(arrChild.get(position).getChild_profile().lastIndexOf(".")+1);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl(mypath).child("Child Profiles").child(arrChild.get(position).getChild_profile());
        try
        {
            final File localFile = File.createTempFile(arrChild.get(position).getChild_profile(), suffix);
            storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>()
            {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot)
                {
                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    child_profile.setImageBitmap(bitmap);
                    prg.dismiss();
                }
            }).addOnFailureListener(new OnFailureListener()
            {
                @Override
                public void onFailure(@NonNull Exception exception)
                {
                    String message=exception.getMessage();
                }
            });
        } catch (IOException e ) {} catch (Exception e) {
            e.printStackTrace();
        }

        return myrow;
    }
}
