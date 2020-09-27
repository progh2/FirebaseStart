package kr.hs.emirim.ham.firebasestart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AuthActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        Button button = findViewById(R.id.firebaseuiauthbtn);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.firebaseuiauthbtn:
                Intent intent = new Intent(this, FirebaseUIActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}