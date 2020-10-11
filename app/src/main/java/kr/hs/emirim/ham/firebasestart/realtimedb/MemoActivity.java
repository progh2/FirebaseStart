package kr.hs.emirim.ham.firebasestart.realtimedb;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

import kr.hs.emirim.ham.firebasestart.R;

public class MemoActivity extends AppCompatActivity implements View.OnClickListener,
        MemoViewListener{

    private static final String TAG = "파베";
    private ArrayList<MemoItem> memoItems = null;
    private MemoAdapter memoAdapter = null;
    private String username = null;
    private String uid = null;

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo);

        init();
        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        addChildEvent();
        addValueEventListener();
    }

    private void addValueEventListener() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d(TAG, "값 = " + snapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void addChildEvent() {
        databaseReference.child("memo").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                //Toast.makeText(getApplicationContext(), "새 글이 등록되었어요!",Toast.LENGTH_SHORT).show();
                Log.d(TAG, "차일드 [추가] 이벤트 발생");
                MemoItem item = snapshot.getValue(MemoItem.class);
                memoItems.add(item);
                memoAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void init() {
        memoItems = new ArrayList<>();
        username = "user_" + new Random().nextInt(1000);
    }

    private void initView() {
        Button regbtn = findViewById(R.id.memobtn);
        regbtn.setOnClickListener(this);
        Button userbtn = findViewById(R.id.reguserbtn);
        userbtn.setOnClickListener(this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView recyclerView = findViewById(R.id.memolist);
        memoAdapter = new MemoAdapter(this,memoItems,this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(memoAdapter);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.memobtn:
                regMemo();
                break;
            case R.id.reguserbtn:
                writeNewUser();
                break;
        }
    }

    private void writeNewUser() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            String name = user.getDisplayName();
            String email = user.getEmail();
            String uid = user.getUid();
            Log.d(TAG, "name = " + name);
            Log.d(TAG, "email = " + email);
            Log.d(TAG, "uid = " + uid);

            UserInfo userInfo = new UserInfo();
            userInfo.setUsername(name);
            userInfo.setUserpwd("1234");
            userInfo.setEmailaddr(email);

            databaseReference.child("users").child(uid).setValue(userInfo);
        }else{
            Log.d(TAG, "그런 사용자 없는디?");
        }
    }

    private void regMemo() {
        EditText titleedit = findViewById(R.id.memotitle);
        EditText contentsedit = findViewById(R.id.memocontents);
        if(titleedit.getText().toString().length() ==  0 ||
            contentsedit.getText().toString().length() == 0){
            Toast.makeText(this,
                    "메모 제목 또는 메모 내용이 입력되지 않았습니다. 입력 후 다시 시작해주세요.",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        MemoItem item = new MemoItem();
        item.setUser(this.username);
        item.setTilte(titleedit.getText().toString());
        item.setMemocontents(contentsedit.getText().toString());

        databaseReference.child("memo").push().setValue(item);
        //memoItems.add(item);
        //memoAdapter.notifyDataSetChanged();// ***
    }

    @Override
    public void onItemClick(int position, View view) {

    }
}