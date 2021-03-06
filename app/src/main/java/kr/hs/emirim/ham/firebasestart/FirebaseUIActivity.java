package kr.hs.emirim.ham.firebasestart;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class FirebaseUIActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int RC_SIGN_IN = 1000;
    private static final String TAG = "파베-FirebaseUIAct";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_ui);

        Button firebaseuiauthbtn = findViewById(R.id.firebaseuiauthbtn);
        firebaseuiauthbtn.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN){
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if(resultCode == RESULT_OK){
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                //Toast.makeText(this,"로그인 성공!!!",Toast.LENGTH_SHORT).show();
                Log.d(TAG, user.getUid() + " / " + user.getDisplayName() );

                Intent intent = new Intent(this, SignedInActivity.class);
                intent.putExtras(data);
                startActivity(intent);
            }else{
                Toast.makeText(this,"로그인 실패했습니다!",Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.firebaseuiauthbtn:
                signin();
                break;
            default:
                break;
        }
    }

    private void signin(){
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setTheme(AuthUI.getDefaultTheme())
                        .setLogo(R.drawable.alice)   // .setLogo(AuthUI.NO_LOGO)
                        .setAvailableProviders(getSelectedProviders())
                        .setTosAndPrivacyPolicyUrls("https://naver.com", "https://google.com")
                        .setIsSmartLockEnabled(true)
                        .build(), RC_SIGN_IN
        );
    }

    private List<AuthUI.IdpConfig> getSelectedProviders() {
        List<AuthUI.IdpConfig> selectedProviders = new ArrayList<>();
        CheckBox googlechk = findViewById(R.id.google_provider);
        CheckBox facebookchk = findViewById(R.id.fasebook_provider);
        CheckBox twitterchk = findViewById(R.id.twitter_provider);
        CheckBox emailchk = findViewById(R.id.email_provider);

        if(googlechk.isChecked()){
            selectedProviders.add(new AuthUI.IdpConfig.GoogleBuilder().build());
        }
        if(facebookchk.isChecked()){
            selectedProviders.add(new AuthUI.IdpConfig.FacebookBuilder().build());
        }
        if(twitterchk.isChecked()){
            selectedProviders.add(new AuthUI.IdpConfig.TwitterBuilder().build());
        }
        if(emailchk.isChecked()){
            selectedProviders.add(new AuthUI.IdpConfig.EmailBuilder().build());
        }
        return selectedProviders;
    }


}