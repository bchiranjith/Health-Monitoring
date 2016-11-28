package com.android.sai.firebasetest;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

import static java.lang.Boolean.TRUE;

public class Kirrakk extends AppCompatActivity {
    public FirebaseDatabase daa;
    public DatabaseReference ref;
    public FirebaseUser user;
    public String type;
    ArrayList<String> patients = new ArrayList<>();
    ListView patient_list;
    public String local;
    /*Contains the value of the key of the user to the database*/
    public LinearLayout main;                           //the main layout of the activity.
    public int flag1=0;
    public ScrollView s;


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
                local = howle[0];
                int j;
                for (j=0;j<i;j++) {
                    String fmdb = b[j].toString();
                    if (local.equals(fmdb)) {
                        getUserName(fmdb);
                        break;
                    }
                }
                if(j==i){
                    identifyCustomer(local,"P");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getUserName(String fmdb) {                     ///gets username or details based on the
        DatabaseReference ref1 = ref.child(fmdb);
        String parent = ref.getParent().toString();
        String child = ref.toString();
        type = child.substring(parent.length()+1);
        Log.i("type of customer",type);
        //Log.i("ref",ref1.toString());
        ref1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<Object,String> all = (HashMap<Object,String>) dataSnapshot.getValue();
                Object[] all_details = all.keySet().toArray();
                //String name = usermane.get("user");
                int i = all.size();
                ArrayList<String> det = new ArrayList<>();
                for(int j=0;j<i;j++){
                    det.add(all.get(all_details[j]));
                    //Log.i("detalis",det.get(j));
                }
                drawActivity(det,all);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void drawActivity(final ArrayList<String> det, final HashMap<Object, String> all) {
        //s = new ScrollView(getApplicationContext());
        //s.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        //setContentView(s);
        Object[] all_details = all.keySet().toArray();
        int i = all.size();
        main = new LinearLayout(this);
        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        llp.setMargins(64,64,64,128);
        llp.gravity = Gravity.CENTER;

        main.setOrientation(LinearLayout.VERTICAL);
        setContentView(main);
        //s.addView(main);
        String alll= all.get("user");
        TextView tv = new TextView(this);
        if(type.equals("D")){
            alll = "Hi!" + " Dr. " + alll;
           // Log.i("neweasdjhkja",alll);
        }
        else if (type.equals("P")){
            alll = "Hi! " + alll;
            //Log.i("neweasdjhkja",alll);
        }
        tv.setText(alll);
        tv.setTextColor(Color.BLACK);
        tv.setTextSize(32);
        tv.setLayoutParams(llp);
        tv.setGravity(Gravity.CENTER);
        main.addView(tv);
        // GridLayout gl = new GridLayout(this);
        //gl.setId(R.id.grv);
        tv = new TextView(this);
        tv.setText("PROFILE");
        tv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT,1f));
        tv.setWidth(0);
        tv.setTextColor(Color.BLACK);
        tv.setTextSize(22);
        tv.setGravity(Gravity.CENTER);
        tv.setId(R.id.profile);


        TextView tv1 = new TextView(this);
        tv1.setText("PATIENT");
        tv1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT,1f));
        tv1.setWidth(0);
        tv1.setTextColor(Color.BLACK);
        tv1.setTextSize(22);
        tv1.setGravity(Gravity.CENTER);
        tv1.setId(R.id.pat_details);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        ll.setMargins(0,0,0,64);
        layout.setWeightSum(2f);
        layout.addView(tv);
        layout.addView(tv1);
        main.addView(layout);


        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag1==0) {
                    flag1=1;
                    generateProfileData(det, all, main);
                }
            }
        });

        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(type.equals("D")){
                    getPatientList(ref);
                }
            }
        });
    }

    private void getPatientList(DatabaseReference ref) {
        DatabaseReference ref1 = ref.getParent().child("P");
        Log.i("refwer",ref1.toString());
        if(s!=null) {
            main.removeView(s);
        }
        flag1=0;
        ref1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<Object,String> all = (HashMap<Object,String>) dataSnapshot.getValue();
                Object[] all_details = all.keySet().toArray();
                //String name = usermane.get("user");
                int i = all.size();
                for(int j=0;j<i;j++){
                    Log.i("pats",all_details[j].toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void generateProfileData(ArrayList<String> det, HashMap<Object, String> all, LinearLayout main) {
        s = new ScrollView(this);
        s.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        LinearLayout ll3 = new LinearLayout(this);
        ll3.setOrientation(LinearLayout.VERTICAL);
        int i = all.size();
        Object[] all_details = all.keySet().toArray();
        for(int j=0;j<i;j++){
            det.add(all.get(all_details[j]));
            LinearLayout ll2 = new LinearLayout(this);
            ll2.setOrientation(LinearLayout.HORIZONTAL);
            ll2.setPadding(32,64,32,32);
            LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
           // ll.setMargins(32,0,32,0);

            TextView key = new TextView(this);
            key.setWidth(0);
            key.setTextColor(Color.BLUE);
            key.setAllCaps(TRUE);
            key.setTextSize(18);
            key.setGravity(Gravity.CENTER);
            key.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT,1f));
            key.setText(all_details[j].toString());

            TextView value = new TextView(this);
            value.setWidth(0);
            value.setTextColor(Color.BLUE);
            value.setTextSize(18);
            value.setGravity(Gravity.CENTER);
            value.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT,1f));
            value.setText(det.get(j));

            ll2.addView(key);
            ll2.addView(value);
            ll3.addView(ll2);
        }
        s.addView(ll3);
        main.addView(s);
    }
}