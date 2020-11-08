package kr.hs.emirim.ham.firebasestart.storage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOError;
import java.io.IOException;

import kr.hs.emirim.ham.firebasestart.R;

public class DownloadActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "다운로드액티비티";
    private File localfile = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);

        findViewById(R.id.localimgdownloadbtn).setOnClickListener(this);
        findViewById(R.id.imgfirebaseuidnbtn).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.localimgdownloadbtn:
                showDownloadLocalFileImageView();
                break;
            case R.id.imgfirebaseuidnbtn:
                showFirebaseUiDownloadImageView();
                break;
            default:
                break;
        }
    }

    private void showFirebaseUiDownloadImageView() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference pathReference = storageRef.child("storage/cat.jpg");
        ImageView imageView = findViewById(R.id.storageimg);
        imageView.setAdjustViewBounds(true);
        Glide.with(this)
                .using(new FirebaseImageLoader())
                .load(pathReference)
                .centerCrop()
                .override(400,400)
                .placeholder(R.drawable.alice)
                .into(imageView);
    }

    private void showDownloadLocalFileImageView() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference pathReference = storageRef.child("storage/cat.jpg");

        try{
            localfile = File.createTempFile("images","jpg");
        }catch (IOException e){
            e.printStackTrace();
        }

        pathReference.getFile(localfile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
               long filesize = taskSnapshot.getTotalByteCount();
               Log.d(TAG,"File Size = " + filesize);
               Log.d(TAG, "File Name = " + localfile.getAbsolutePath());

                ImageView imageView = (ImageView) findViewById(R.id.storageimg);
                imageView.setAdjustViewBounds(true);
                Glide.with(DownloadActivity.this)
                        .load(new File(localfile.getAbsolutePath()))
                        .fitCenter()
                        //.override(800,400)
                        .placeholder(R.drawable.alice)
                        .into(imageView);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "onFailure in");
                e.printStackTrace();
            }
        });

    }
}