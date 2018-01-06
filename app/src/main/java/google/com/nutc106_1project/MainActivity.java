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
        final DatabaseReference posts = database.getReference().child("posts");
        txt_addPost = (TextView) findViewById(R.id.txt_addPost);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        lv = (ListView)findViewById(R.id.lv);

        posts.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Posts p = dataSnapshot.getValue(Posts.class);
                Log.d(TAG, "onChildAdded: " + p.getMessage());
                PostsList.add(p.getMessage());
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
                String key = posts.push().getKey();
                Posts p = new Posts();
                p.setUid(key);
                p.setMessage(txt_addPost.getText().toString());
                posts.child(key).setValue(p);
            }
        });
    }
}
