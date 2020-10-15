package nadav.altabet.nlb;

import android.app.Activity;
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
    private ArrayList<String> arrChildFirstName = new ArrayList<>();
    private ArrayList<String> arrChildLastName = new ArrayList<>();
    private ArrayList<String> arrParentEmail = new ArrayList<>();
    private ArrayList<String> arrChildID = new ArrayList<>();
    private ArrayList<String> arrChildGender = new ArrayList<>();
    private ArrayList<String> arrChildBranch = new ArrayList<>();
    private ArrayList<String> arrChildClass = new ArrayList<>();
    private ArrayList<String> arrChildPhone = new ArrayList<>();
    private ArrayList<String> arrChildEmail = new ArrayList<>();
    private ArrayList<String> arrChildProfile = new ArrayList<>();
    private ArrayList<Date> arrChildBirthdate = new ArrayList<>();
    private Activity ctx;

    private String mypath="gs://nlb-project-2287b.appspot.com";

    public child_cardlsview_adap(ArrayList<String> arrChildFirstName, ArrayList<String> arrChildLastName, ArrayList<String> arrParentEmail,
                                 ArrayList<String> arrChildID, ArrayList<String> arrChildBranch, ArrayList<String> arrChildPhone,
                                 ArrayList<String> arrChildEmail, ArrayList<String> arrChildProfile, ArrayList<Date> arrChildBirthdate, Activity ctx) {
        this.arrChildFirstName = arrChildFirstName;
        this.arrChildLastName = arrChildLastName;
        this.arrParentEmail = arrParentEmail;
        this.arrChildID = arrChildID;
        this.arrChildBranch = arrChildBranch;
        this.arrChildPhone = arrChildPhone;
        this.arrChildEmail = arrChildEmail;
        this.arrChildProfile = arrChildProfile;
        this.arrChildBirthdate = arrChildBirthdate;
        this.ctx = ctx;
    }

    private void showImageFromFirebase(final ImageView child_profile, String picname)
    {
        String suffix=picname.substring(picname.lastIndexOf(".")+1);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl(mypath).child("Child_Profiles").child(picname);
        try
        {
            final File localFile = File.createTempFile(picname, suffix);
            storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>()
            {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot)
                {
                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    child_profile.setImageBitmap(bitmap);
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
    }
    @Override
    public int getCount() {
        return this.arrChildFirstName.size();
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
        LayoutInflater inflater =ctx.getLayoutInflater();

        View myrow= inflater.inflate(R.layout.child_listview,null,true);

        TextView child_first_name = myrow.findViewById(R.id.child_first_name);
        child_first_name.setText(this.arrChildFirstName.get(position));

        TextView child_last_name = myrow.findViewById(R.id.child_last_name);
        child_last_name.setText(this.arrChildLastName.get(position));

        TextView child_ID=myrow.findViewById(R.id.child_ID);
        child_ID.setText(this.arrChildID.get(position));

        TextView child_gender=myrow.findViewById(R.id.child_gender);
        child_gender.setText(this.arrChildGender.get(position));

        TextView child_birthdate=myrow.findViewById(R.id.child_birthdate);
        child_birthdate.setText(this.arrChildBirthdate.get(position).getDay() + "/" +
                this.arrChildBirthdate.get(position).getMonth() + "/" + this.arrChildBirthdate.get(position).getYear());

        TextView child_class=myrow.findViewById(R.id.child_class);
        child_class.setText(this.arrChildClass.get(position));

        TextView child_branch=myrow.findViewById(R.id.child_branch);
        child_branch.setText(this.arrChildBranch.get(position));

        TextView child_email=myrow.findViewById(R.id.child_email);
        child_email.setText(this.arrChildEmail.get(position));

        TextView child_phone=myrow.findViewById(R.id.child_phone);
        child_phone.setText(this.arrChildPhone.get(position));

        ImageView child_profile = myrow.findViewById(R.id.child_profile);
        showImageFromFirebase(child_profile, this.arrChildProfile.get(position));

        return myrow;
    }
}
