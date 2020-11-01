package kr.hs.emirim.ham.firebasestart.firestore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.firebase.ui.auth.data.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

import kr.hs.emirim.ham.firebasestart.R;

public class FirestoreActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "파이어스토d액티비티";
    FirebaseFirestore db = null;
    private ListenerRegistration eventListenerRegistration;
    private ListenerRegistration eventQueryListenerRegistration;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firestore);

        findViewById(R.id.adddatabtn).setOnClickListener(this);
        findViewById(R.id.setdatabtn).setOnClickListener(this);
        findViewById(R.id.deletedocbtn).setOnClickListener(this);
        findViewById(R.id.deletefieldbtn).setOnClickListener(this);
        findViewById(R.id.select_data_btn).setOnClickListener(this);
        findViewById(R.id.select_where_data_btn).setOnClickListener(this);
        findViewById(R.id.listener_data_btn).setOnClickListener(this);
        findViewById(R.id.listener_query_data_btn).setOnClickListener(this);

        db = FirebaseFirestore.getInstance();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(eventListenerRegistration != null){
            eventListenerRegistration.remove();
        }

        if(eventQueryListenerRegistration != null){
            eventQueryListenerRegistration.remove();
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.adddatabtn:
                addData();
                break;
            case R.id.setdatabtn:
                setData();
                break;
            case R.id.deletedocbtn:
                deleteDoc();
                break;
            case R.id.deletefieldbtn:
                deleteField();
                break;
            case R.id.select_data_btn:
                selectDoc();
                break;
            case R.id.select_where_data_btn:
                selectWhereDoc();
                break;
            case R.id.listener_data_btn:
                listenerDoc();
                break;
            case R.id.listener_query_data_btn:
                listenerQueryDoc();
                break;
        }
    }

    private void listenerQueryDoc() {
        Log.d(TAG, "리스너 쿼리 도큐먼트 시작");

        if(eventQueryListenerRegistration != null){
            return;
        }

        eventQueryListenerRegistration =
                db.collection("users")
                .whereEqualTo("id", "hong")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        Log.d(TAG, "ListenerQueryDoc in 1");

                        if(error != null){
                            Log.e(TAG, "Listen: Error", error);
                            return;
                        }

                        for (DocumentChange dc : value.getDocumentChanges()){
                            Log.d(TAG, "listenerQueryDoc dc.getType() = " + dc.getType());
                            switch (dc.getType()){
                                case ADDED:
                                    Log.d(TAG, "New city: " + dc.getDocument().getData());
                                    break;
                                case MODIFIED:
                                    Log.d(TAG, "Modified city: " + dc.getDocument().getData());
                                    break;
                                case REMOVED:
                                    Log.d(TAG, "Removed city: " + dc.getDocument().getData());
                                    break;
                            }
                        }
                    }
                });
    }

    private void listenerDoc() {
        Log.d(TAG, "리스너 도큐먼트 시작");
        final DocumentReference docRef = db.collection("users").document("userinfo");

        if(eventListenerRegistration != null){
            return;
        }

        eventListenerRegistration = docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error != null){
                    Log.e(TAG, "Listen failed", error);
                }

                if(value != null && value.exists()){
                    Log.e(TAG, "현재 데이터 : "  + value.getData() );
                }else{
                    Log.e(TAG, "현재 데이터 : null ");
                }
            }
        });
    }

    private void selectWhereDoc() {
        db.collection("users")
                .whereEqualTo("age", 25)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document : task.getResult()){
                                Log.d(TAG, "Document.getId() => " + document.getData());
                                UserInfo userInfo = document.toObject(UserInfo.class);
                                Log.d(TAG, "name = " + userInfo.getName());
                                Log.d(TAG, "address = " + userInfo.getAddress());
                                Log.d(TAG, "id = " + userInfo.getId());
                                Log.d(TAG, "pwd = " + userInfo.getPwd());
                                Log.d(TAG, "age = " + userInfo.getAge());
                            }
                        }else{
                            Log.e("TAG", "get failed with " + task.getException());
                        }
                    }
                });
    }

    private void selectDoc() {
        DocumentReference docRef = db.collection("users")
                .document("userinfo");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()){
                        Log.d(TAG, "Document Snapshot data: " + document.getData());
                        UserInfo userInfo = document.toObject(UserInfo.class);
                        Log.d(TAG, "name = " + userInfo.getName());
                        Log.d(TAG, "address = " + userInfo.getAddress());
                        Log.d(TAG, "id = " + userInfo.getId());
                        Log.d(TAG, "pwd = " + userInfo.getPwd());
                        Log.d(TAG, "age = " + userInfo.getAge());
                    }else{
                        Log.d(TAG, "해당 데이터가 없습니다");
                    }
                }else{
                    Log.e("TAG", "get failed with " + task.getException());
                }
            }
        });
    }

    private void deleteField() {
        DocumentReference docRef = db.collection("users")
                .document("userinfo");
        Map<String, Object> updates = new HashMap<>();
        updates.put("address", FieldValue.delete());
        docRef.update(updates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d(TAG, "문서 필드 성공적으로 삭제!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG,"Document Error! " + e.getMessage());
                }
         });
    }

    private void deleteDoc() {
        db.collection("users")
                .document("userinfo")
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG,"DocumentSnapshot 성공적으로 삭제됨!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG,"Document Error! " + e.getMessage());
                    }
                });
                
    }

    private void setData() {
        Map<String, Object> member = new HashMap<>();
        member.put("name", "나야나");
        member.put("address", "경기도");
        member.put("age",25);
        member.put("id","my");
        member.put("pwd","hello!");

        db.collection("users")
                .document("userinfo")
                .set(member)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG,"DocumentSnapshot 성공적으로 저장됨!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG,"Document Error! " + e.getMessage());
                    }
                });
    }

    private void addData() {
        Map<String, Object> member = new HashMap<>();
        member.put("name", "홍길동");
        member.put("address", "수원시");
        member.put("age",25);
        member.put("id","hong");
        member.put("pwd","hello!");

        db.collection("users")
                .add(member)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG,"Document ID = " + documentReference.get());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG,"Document Error! " + e.getMessage());
                    }
                });
    }
}