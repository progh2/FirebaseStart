package kr.hs.emirim.ham.firebasestart;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import kr.hs.emirim.ham.firebasestart.firestore.FirestoreActivity;
import kr.hs.emirim.ham.firebasestart.realtimedb.MemoActivity;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    final static String TAG = "파베-Main";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "MainActivity onCreate 실행됨");
        Button firebaseauthbtn = findViewById(R.id.firebaseauthbtn);
        Button firebaserealtimedbbtn = findViewById(R.id.firebaserealtimdbbtn);
        firebaseauthbtn.setOnClickListener(this);
        firebaserealtimedbbtn.setOnClickListener(this);

        findViewById(R.id.firebasefirestorebtn).setOnClickListener(this);
        Log.d(TAG, "MainActivity 버튼 객체 참조 연결하고 리스너 등록");
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        // Toast.makeText(this, "버튼 눌렸어요", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "파이어베이스 버튼 눌림!");
        Intent intent = null;
        switch (view.getId()){
            case R.id.firebaseauthbtn:
                Log.d(TAG, "파이어베이스 인증 버튼 눌림! ");
                intent = new Intent(this, AuthActivity.class);
                startActivity(intent);
                break;
            case R.id.firebaserealtimdbbtn:
                Log.d(TAG, "파이어베이스 리얼타임디비 버튼 눌림! ");
                intent = new Intent(this, MemoActivity.class);
                startActivity(intent);
                break;
            case R.id.firebasefirestorebtn:
                Log.d(TAG, "파이어베이스 파이어스토어 버튼 눌림!");
                intent = new Intent(this, FirestoreActivity.class);
                startActivity(intent);
                break;
            default:
                Log.d(TAG, "모르는 클릭?");
                break;
        }
    }
}
