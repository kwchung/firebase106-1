package google.com.nutc106_1project;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private TextView txt_addPost;
    private Button btn_submit;
    private ListView lv;
    private FirebaseDatabase database;
    private String TAG = "CKW-Posts";
    private List<String> PostsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(this, MyFirebaseMessagingService.class);
        this.startService(intent);

        database = FirebaseDatabase.getInstance();
        final DatabaseReference notifications = database.getReference().child("notifications");
        txt_addPost = (TextView) findViewById(R.id.txt_addPost);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        lv = (ListView)findViewById(R.id.lv);

        notifications.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Notifications n = dataSnapshot.getValue(Notifications.class);
                Log.d(TAG, "onChildAdded: " + n.getMessage());
                PostsList.add(n.getMessage());
                ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(),
                        android.R.layout.simple_expandable_list_item_1,
                        PostsList);
                lv.setAdapter(adapter);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Notifications n = dataSnapshot.getValue(Notifications.class);
                Log.d(TAG, "onChildRemoved: " + n.getMessage());
                PostsList.remove(n.getMessage());
                ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(),
                        android.R.layout.simple_expandable_list_item_1,
                        PostsList);
                lv.setAdapter(adapter);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String key = notifications.push().getKey();
                Notifications  n= new Notifications();
                n.setMessage(txt_addPost.getText().toString());
                n.setUser("");
                n.setUserAvatar("");
                notifications.child(key).setValue(n);
            }
        });
    }
}
