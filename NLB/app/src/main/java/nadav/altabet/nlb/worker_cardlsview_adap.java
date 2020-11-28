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

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
    public View getView(int position, View view, ViewGroup viewGroup) {

        this.prg.show();
        LayoutInflater inflater =ctx.getLayoutInflater();

        final View myrow= inflater.inflate(R.layout.child_listview,null,true);

        return myrow;
    }
}

