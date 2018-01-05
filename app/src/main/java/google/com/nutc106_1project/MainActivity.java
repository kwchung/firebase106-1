package google.com.nutc106_1project;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    private TextView txt_addPost;
    private Button btn_submit;
    private FirebaseDatabase database;
    private String TAG = "CKW-Posts";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = FirebaseDatabase.getInstance();
        final DatabaseReference posts = database.getReference().child("posts");
        txt_addPost = (TextView) findViewById(R.id.txt_addPost);
        btn_submit = (Button) findViewById(R.id.btn_submit);

        posts.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Posts p = dataSnapshot.getValue(Posts.class);
                Log.d(TAG, "onChildAdded: " + p.getMessage());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Posts p = dataSnapshot.getValue(Posts.class);
                Log.d(TAG, "onChildChanged: " + p.getMessage());
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
