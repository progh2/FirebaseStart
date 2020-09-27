package kr.hs.emirim.ham.firebasestart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignedInActivity extends AppCompatActivity implements View.OnClickListener {
    private IdpResponse mIdpResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signedin);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser == null){
            finish();
        }
        mIdpResponse = IdpResponse.fromResultIntent(getIntent());

        //TODO : populateProfile();
        //TODO : populateIdpToken();
        Button signoutbtn = findViewById(R.id.sign_out);
        signoutbtn.setOnClickListener(this);

        Button deleteuser = findViewById(R.id.delete_account);
        deleteuser.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.sign_out:
                signOut();
                break;
            case R.id.delete_account:
                deleteAccountClicked();
                break;
            default:
                break;
        }
    }

    private void deleteAccountClicked() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setMessage("증말로 계정 삭제할꺼에요?")
                .setPositiveButton("응, 폭파시켜!",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                deleteAccount();
                            }
                        })
                .setNegativeButton("아니, 냅둘래",null)
                .create();
        dialog.show();
    }

    private void deleteAccount() {
        AuthUI.getInstance().delete(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getApplicationContext(),"계정 삭제 성공!",Toast.LENGTH_SHORT).show();
                            finish();
                        }else{
                            Toast.makeText(getApplicationContext(),"계정 삭제 실패!",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void signOut() {
        AuthUI.getInstance().signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            finish();
                        }
                    }
                });
    }
}