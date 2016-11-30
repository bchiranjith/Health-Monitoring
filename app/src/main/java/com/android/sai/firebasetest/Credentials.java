package com.android.sai.firebasetest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Set;



/*
add these lines to build.gradle (project ) if gives a duplicate file error.
packagingOptions {
        exclude 'META-INF/LICENSE'
        exclude 'META-INF/LICENSE-FIREBASE.txt'
        exclude 'META-INF/NOTICE'
    }
 */
public class Credentials extends AppCompatActivity {
    public DatabaseReference ref1;
    public FirebaseDatabase db1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credentials);
        //Bundle a = getIntent().getExtras();
        //redc(a);
    }

    public void redc(Bundle x){
        LinearLayout main = new LinearLayout(getApplicationContext());
        main.setOrientation(LinearLayout.VERTICAL);
        setContentView(main);
        Set a = x.keySet();
        int i = x.size();
        ArrayList<String> ll = new ArrayList<String>();
        Object[] arr = a.toArray();
        int j;
        for(j=0 ; j<i ; j++){
            ll.add(x.getString(arr[j].toString()));
        }

        for(j = 0; j < ll.size(); j++){
            LinearLayout l1=new LinearLayout(this);
            l1.setOrientation(LinearLayout.HORIZONTAL);
            main.addView(l1);
            TextView tv = new TextView(this);
            TextView tv1 = new TextView(this);
            tv1.setText(arr[j].toString());
            l1.addView(tv1);
            //Log.i("mas",arr[j].toString());
            tv.setText(ll.get(j));
            l1.addView(tv);
        }

    }

    public void Update(View v){
        db1 = FirebaseDatabase.getInstance();
        ref1= db1.getReference();
        ref1 = ref1.child("D");
        ref1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                Log.d("update","Value is: " + value);
                disp(value);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("update","Falied to read value", databaseError.toException());
            }
        });

    }
    public void disp(String s){
        RelativeLayout lll= (RelativeLayout) findViewById(R.id.activity_credentials);
        LinearLayout main = new LinearLayout(this);
        main.setOrientation(LinearLayout.VERTICAL);
        lll.addView(main);
        TextView t = new TextView(this);
        //Log.i("disp","slogan");
        main.addView(t);
        //Log.i("disp","slogan111");
        t.setText(s);
    }
}
