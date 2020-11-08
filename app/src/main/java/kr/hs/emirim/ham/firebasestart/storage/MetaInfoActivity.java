package kr.hs.emirim.ham.firebasestart.storage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;

import kr.hs.emirim.ham.firebasestart.R;

public class MetaInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meta_info);

        findViewById(R.id.metabtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMetaData();
            }
        });
    }

    private void getMetaData() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference cattRef = storageRef.child("storage/cat.jpg");

        cattRef.getMetadata()
                .addOnSuccessListener(new onSucessListener())
                .addOnFailureListener(new onFailureListener());
    }

    private class onSucessListener implements OnSuccessListener<StorageMetadata>{

        @Override
        public void onSuccess(StorageMetadata storageMetadata) {
            String metadata = storageMetadata.getName() + "\n" +
                    storageMetadata.getPath() + "\n" +
                    storageMetadata.getBucket();
            TextView metatxt = findViewById(R.id.metainfotxt);
            metatxt.setText(metadata);
        }
    }
    private static class onFailureListener implements OnFailureListener{

        @Override
        public void onFailure(@NonNull Exception e) {
            e.printStackTrace();
        }
    }
}