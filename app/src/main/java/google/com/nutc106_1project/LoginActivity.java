package google.com.nutc106_1project;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private Button btn_login;
    private TextView txt_email;
    private TextView txt_pwd;
    private String TAG = "CKW-Login";
    private MyFirebaseInstanceIdService MyId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Intent intent = new Intent(this, MyFirebaseInstanceIdService.class);
        this.startService(intent);

        txt_email = (TextView) findViewById(R.id.txt_email);
        txt_pwd = (TextView)findViewById(R.id.txt_pwd);
        btn_login = (Button)findViewById(R.id.btn_login);
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // Name, email address, and profile photo Url
                    String g_name = user.getDisplayName();
                    String g_email = user.getEmail();
                    Uri g_photoUrl = user.getPhotoUrl();

                    // The user's ID, unique to the Firebase project. Do NOT use this value to
                    // authenticate with your backend server, if you have one. Use
                    // FirebaseUser.getToken() instead.
                    String g_uid = user.getUid();
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + g_uid);
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = txt_email.getText().toString();
                String password = txt_pwd.getText().toString();
                Log.d(TAG, "setOnClickListener:"+email+password);

                final Task<AuthResult> authResultTask = mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {

                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    Log.w(TAG, "signInWithEmail:failed", task.getException());
                                    Toast.makeText(LoginActivity.this, "Login Fail",
                                            Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
                                    // 指定要呼叫的 Activity Class
                                    Intent newAct = new Intent();
                                    newAct.setClass( LoginActivity.this, MainActivity.class );
                                    // 呼叫新的 Activity Class
                                    startActivity( newAct );
                                    // 結束原先的 Activity Class
                                    LoginActivity.this.finish();
                                }
                            }
                        });
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
