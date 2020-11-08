package kr.hs.emirim.ham.firebasestart.storage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

import kr.hs.emirim.ham.firebasestart.R;

public class UploadActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "업로드액티비티";
    private final int REQ_CODE_SELECT_IMAGE = 1000;
    private String mImgPath = null;
    private String mImgTitle = null;
    private String mImgOrient = null;

    private Button mButton = null;
    private ProgressBar mProgressBar = null;
    private ProgressBar mProgressBar2 = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        mButton = findViewById(R.id.uploadbtn);
        mButton.setOnClickListener(this);
        mButton.setEnabled(true);
        mProgressBar = findViewById(R.id.progressBar);
        mProgressBar.setVisibility(View.GONE);
        mProgressBar2 = findViewById(R.id.progressBar2);
        mProgressBar2.setVisibility(View.GONE);

        getGallery();
    }

    private void getGallery() {
        Intent intent = null;
        if(Build.VERSION.SDK_INT >= 19){
            intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }else{
            intent = new Intent(Intent.ACTION_GET_CONTENT);
        }
        intent.setType("image/*");
        startActivityForResult(intent,REQ_CODE_SELECT_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQ_CODE_SELECT_IMAGE){
            if(resultCode == Activity.RESULT_OK){
                Uri uri = data.getData();
                getImageNameToUri(uri);

                try{
                    Bitmap bm = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                    ImageView img = findViewById(R.id.showimg);
                    img.setImageBitmap(bm);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    private void getImageNameToUri(Uri uri) {
        String[] proj = {
          MediaStore.Images.Media.DATA,
          MediaStore.Images.Media.TITLE,
          MediaStore.Images.Media.ORIENTATION,
        };
        Cursor cursor = this.getContentResolver().query(uri, proj, null, null, null);
        cursor.moveToFirst();

        int column_data = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        int column_title = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.TITLE);
        int column_orientation = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.ORIENTATION);

        mImgPath = cursor.getString(column_data);
        mImgTitle = cursor.getString(column_title);
        mImgOrient = cursor.getString(column_orientation);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.uploadbtn:
                mProgressBar.setVisibility(View.VISIBLE);
                mProgressBar2.setVisibility(View.VISIBLE);
                mButton.setEnabled(false);
                uploadFile(mImgPath);
                break;
            default:
                break;
        }
    }

    private void uploadFile(String aFilePath) {
        Uri file = Uri.fromFile(new File(aFilePath));
        StorageMetadata metadata = new StorageMetadata.Builder()
                .setContentType("image/jpeg").build();

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        UploadTask uploadTask = storageRef.child("storage/"
                + file.getLastPathSegment()).putFile(file,metadata);
        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double progress = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                mProgressBar2.setProgress((int)progress);
                Toast.makeText(UploadActivity.this,
                        "Upload is :" + progress + "% done", Toast.LENGTH_SHORT).show();
            }
        }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onPaused(@NonNull UploadTask.TaskSnapshot snapshot) {
                Log.d(TAG, "Upload is Paused");
                mProgressBar.setVisibility(View.GONE);
                mProgressBar2.setVisibility(View.GONE);
                mButton.setEnabled(true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Upload Exception!!");
                mProgressBar.setVisibility(View.GONE);
                mProgressBar2.setVisibility(View.GONE);
                mButton.setEnabled(true);
            }
        }).addOnCanceledListener(new OnCanceledListener() {
            @Override
            public void onCanceled() {
                Log.d(TAG, "Upload is Canceled!!");
                mProgressBar.setVisibility(View.GONE);
                mProgressBar2.setVisibility(View.GONE);
                mButton.setEnabled(true);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d(TAG, "업로드 성공!");
                mProgressBar.setVisibility(View.GONE);
                mProgressBar2.setVisibility(View.GONE);
                mButton.setEnabled(true);
            }
        });
    }
}