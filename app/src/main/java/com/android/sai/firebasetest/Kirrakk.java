package com.android.sai.firebasetest;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

public class Kirrakk extends AppCompatActivity {
    public FirebaseDatabase daa;
    public DatabaseReference ref;
    public FirebaseUser user;
    public String mane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseAuth auth1= FirebaseAuth.getInstance();
        FirebaseAuth.AuthStateListener listener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (user != null) {
                    // User is signed in
                    setContentView(R.layout.activity_kirrakk);
                    Log.i("no", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.i("yes", "onAuthStateChanged:signed_out");
                }
            }
        };
        String st = prefs.getDefaults("mail",getApplicationContext());
        identifyCustomer(st,"D");
    }


    public int flag=0;
    public void identifyCustomer(String mane,String a){
        daa = FirebaseDatabase.getInstance();
        ref = daa.getReference();
        ref = ref.child(a);
        final String[] howle = {mane};
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<Object,Object> map = (HashMap<Object, Object>) dataSnapshot.getValue();
                Object[] b = map.keySet().toArray();
                int i = map.size();
                try {
                    howle[0] = prefs.formatKey(howle[0]);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                //Log.v("refer",ref.toString());
                for (int j=0;j<i;j++){
                    String local = howle[0];
                    String fmdb = b[j].toString();
                    if(local.equals(fmdb)){
                        Log.i("jshgd","Foound");
                        getUserName(fmdb);
                    }
                    else if(flag!=1){
                        Log.i("sds","not found");
                        identifyCustomer(local,"P");
                        flag=1;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getUserName(String fmdb) {
        DatabaseReference ref1 = ref.child(fmdb);
        Log.i("ref",ref1.toString());
        String enc = fmdb;
        ref1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<Object,String> usermane = (HashMap<Object,String>) dataSnapshot.getValue();
                String name = usermane.get("user");
                Log.i("name of customer",name);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
