package google.com.nutc106_1project;

import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by User on 2018/1/6.
 */

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {
    private String TAG = "CKW-InstanceIdService";
    private FirebaseDatabase database = FirebaseDatabase.getInstance();;
    final DatabaseReference tokens = database.getReference().child("tokens");
    String key = tokens.push().getKey();

    @Override
    public void onCreate()
    {
        Log.d(TAG, "onCreate()");
        super.onCreate();
        this.onTokenRefresh();
    }

    @Override
    public void onDestroy()
    {
        Log.d(TAG, "onDestroy()");
        super.onDestroy();
    }

    @Override
    public void onTokenRefresh() {

        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(refreshedToken);

        tokens.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void sendRegistrationToServer(String refreshedToken) {
        Tokens t = new Tokens();
        t.setTokenId(refreshedToken);
        tokens.child(key).setValue(t);
    }
}
