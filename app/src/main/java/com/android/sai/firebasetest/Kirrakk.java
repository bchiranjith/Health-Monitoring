package com.android.sai.firebasetest;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.google.firebase.database.Query;
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
    public ListView patient_list;
    public String local;
    /*Contains the value of the key of the user to the database*/
    public LinearLayout main;                           //the main layout of the activity.
    public int flag1=0,flag2=0;
    public ScrollView s;
    private ProgressDialog prog1,prog2,prog3;

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
        prog1 = new ProgressDialog(this);
        prog1.setIndeterminate(true);
        prog1.setMessage("Getting data!!!");
        prog1.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        prog1.show();
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
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
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

    private Context context;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
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
        main.setBackgroundColor(Color.GRAY);
        //main.setBackground(getResources().getDrawable(R.drawable.he));
        setContentView(main);
        //s.addView(main);
        String alll= all.get("user");
        TextView tv = new TextView(this);
        if(type.equals("D")){
            alll = "Hello " + " Dr. " + alll;
           // Log.i("neweasdjhkja",alll);
        }
        else if (type.equals("P")){
            alll = "Hello " + alll;
            //Log.i("neweasdjhkja",alll);
        }
        tv.setText(alll);
        tv.setTextColor(Color.BLUE);
        tv.setTypeface(null,Typeface.BOLD_ITALIC);
        tv.setTextSize(32);
        tv.setLayoutParams(llp);
        tv.setGravity(Gravity.CENTER);
        main.addView(tv);
        // GridLayout gl = new GridLayout(this);
        //gl.setId(R.id.grv);
        tv = new TextView(this);
        tv.setBackgroundColor(Color.parseColor("#63E2D8"));
        tv.setText("PROFILE");
        tv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT,1f));
        tv.setWidth(0);
        tv.setTextColor(Color.parseColor("#EB1818"));
        tv.setTextSize(22);
        tv.setGravity(Gravity.CENTER);
        tv.setId(R.id.profile);


        TextView tv1 = new TextView(this);
        tv1.setBackgroundColor(Color.parseColor("#63E2D8"));
        tv1.setText("PATIENT");
        tv1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT,1f));
        tv1.setWidth(0);
        tv1.setTextColor(Color.parseColor("#EB1818"));
        tv1.setTextSize(22);
        tv1.setGravity(Gravity.CENTER);
        tv1.setId(R.id.pat_details);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        ll.setMargins(0,0,0,64);
        layout.setWeightSum(2f);
        layout.setPadding(0,0,0,32);
        layout.addView(tv);
        layout.addView(tv1);
        main.addView(layout);
        prog1.hide();

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag1==0) {
                    flag1=1;
                    generateProfileData(det, all, main);
                }
            }
        });

        context = this.getParent();

        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(type.equals("D")){
                    if(flag2==0){
                        flag2=1;
                        getPatientList(ref);
                        //Log.i("listd",String.valueOf(integeras));
                    }
                }
                if(type.equals("P")){
                    if(flag2 == 0){
                        flag2=1;
                        patientLast();
                    }

                }
            }
        });
    }

    public ArrayList<String> arrayList;
    public ArrayList<String[]> anew,beats,tabs;
    private ProgressDialog progg;

    private void patientLast() {
        progg = new ProgressDialog(this);
        progg.setIndeterminate(true);
        progg.setMessage("Getting data!!!");
        progg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progg.show();

        DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference().child("arduino_data");
        Query query = ref1.orderByValue();
        arrayList = new ArrayList<>();
        Log.i("asdasfasf",ref.toString());
        query.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i("snap shot of dart",dataSnapshot.toString());
                for(DataSnapshot snap: dataSnapshot.getChildren()){
                    //Log.i("valuasde",snap.getChildren().toString());
                    for(DataSnapshot snap1: snap.getChildren()){
                        // Log.i("valdsfhadasdasd",snap1.getValue().toString());
                        arrayList.add(snap1.getValue().toString());
                    }
                }
                lastSeen(arrayList);
                progg.hide();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    LinearLayout no;
    String[] s1,s2;

    private void lastSeen(ArrayList<String> arrayList) {
        if(s!=null) {
            main.removeView(s);
        }
        if(no!= null){
            main.removeView(no);
        }
        flag1 = 0;
        anew = new ArrayList<>();
        beats = new ArrayList<>();
        tabs = new ArrayList<>();
        int i = arrayList.size();
        for(int j=0;j<i;j++){
            anew.add(arrayList.get(j).split(">"));
        }
        for (int j=0;j<anew.size();j++){
            String[] t = anew.get(j);
            if(t.length == 2){
                beats.add(t);
            }
            else if(t.length == 3){
                tabs.add(t);
            }
            for(int k=0;k<t.length;k++){
                // Log.i("jaffa gad chey ra",t[k]);
            }
        }

        for(int j=0;j<beats.size();j++){
            String[] t = beats.get(j);
            s1 = t;
            for(int k=0;k<t.length;k++){
                Log.i("beastas  :",t[k]);
            }
        }
        for(int j=0;j<tabs.size();j++){
            String[] t = tabs.get(j);
            s2 = t;
            for(int k=0;k<t.length;k++){
                Log.i("tabs  :",t[k]);
            }
        }
        no = new LinearLayout(this);
        no.setOrientation(LinearLayout.VERTICAL);

        TextView heart = new TextView(this);
        heart.setAllCaps(TRUE);
        heart.setGravity(Gravity.CENTER);
        heart.setTextSize(24);
        heart.setTypeface(null,Typeface.BOLD);
        heart.setTextColor(Color.WHITE);
        heart.setText("heart-rates");
        heart.setPadding(0,32,0,32);
        no.addView(heart);
        //main.addView(no);

        TextView h_details = new TextView(this);
        h_details.setGravity(Gravity.CENTER);
        h_details.setTextSize(20);
        h_details.setTypeface(null,Typeface.BOLD);
        h_details.setTextColor(Color.YELLOW);
        String temp = "The last recorded heart-rate was " + s1[1] + " at " + s1[0];
        h_details.setText(temp);
        h_details.setPadding(0,32,0,32);
        no.addView(h_details);
        //main.addView(no);

        TextView tb = new TextView(this);
        tb.setAllCaps(TRUE);
        tb.setGravity(Gravity.CENTER);
        tb.setTextSize(24);
        tb.setTypeface(null,Typeface.BOLD);
        tb.setTextColor(Color.WHITE);
        tb.setText("Tablets");
        tb.setPadding(0,32,0,32);
        no.addView(tb);

        TextView t_details = new TextView(this);
        t_details.setGravity(Gravity.CENTER);
        t_details.setTextSize(20);
        String temp1 = "Your last tablet : " + s2[1] + " at " + s1[0];
        if(s2[2].equals("Yes")){
            temp1 = temp1 + " and status: Taken";
        }
        if(s2[2].equals("No")){
            temp1 = temp1 + " and status: Not Taken";
        }
        t_details.setText(temp1);
        t_details.setTypeface(null,Typeface.BOLD);
        t_details.setTextColor(Color.YELLOW);
        t_details.setPadding(0,32,0,32);
        no.addView(t_details);
        main.addView(no);
    }

    ArrayList<String> patients = new ArrayList<>();

    private void getPatientList(DatabaseReference ref) {
        prog2 = new ProgressDialog(this);
        prog2.setIndeterminate(true);
        prog2.setMessage("Synchronizing....");
        prog2.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        prog2.show();

        patient_list = new ListView(this);
        patient_list.setBackgroundColor(Color.WHITE);

        //patient_list.setLayoutParams(paramss);
        final DatabaseReference ref1 = ref.getParent().child("P");
        if(s!=null) {
            main.removeView(s);
        }
        flag1=0;
        ref1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                patients.clear();
                HashMap<Object,String> all = (HashMap<Object,String>) dataSnapshot.getValue();
                Object[] all_details = all.keySet().toArray();
                name4Disp(all_details, ref1);
                prog2.hide();
                next(ref1);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        main.addView(patient_list);
        //return patient_list.getCount();
    }



    public Bundle bundle;

    private void next(final DatabaseReference ref1) {
        bundle = new Bundle();
        patient_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object cc = parent.getItemAtPosition(position);
                bundle.putString("p_name",cc.toString());
                bundle.putString("refer",ref1.toString());
                Log.i("value of position",cc.toString());
                Log.i("valueaf",String.valueOf(position));
                Intent pat_details = new Intent(getApplicationContext(),details.class);
                pat_details.putExtras(bundle);
                startActivity(pat_details);
            }
        });

    }


    public void name4Disp(Object[] all_details, DatabaseReference ref1) {
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, patients);
        patient_list.setAdapter(adapter);
        for (Object al : all_details) {
            String temp = al.toString();
            DatabaseReference ref2 = ref1.child(temp);
            ref2.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    HashMap<Object, String> all = (HashMap<Object, String>) dataSnapshot.getValue();
                    String name = all.get("user");
                    patients.add(name);
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    private void generateProfileData(ArrayList<String> det, HashMap<Object, String> all, LinearLayout main) {
        if(patient_list!=null){
            main.removeView(patient_list);
        }
        if(no!=null){
            main.removeView(no);
        }
        flag2=0;
        s = new ScrollView(this);
        s.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        LinearLayout ll3 = new LinearLayout(this);
        ll3.setOrientation(LinearLayout.VERTICAL);
        int i = all.size();
        Object[] all_details = all.keySet().toArray();
        for(int j=0;j<i-1;j++){
            det.add(all.get(all_details[j]));
            LinearLayout ll2 = new LinearLayout(this);
            ll2.setOrientation(LinearLayout.HORIZONTAL);
            ll2.setPadding(32,64,32,8);
            LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
           // ll.setMargins(32,0,32,0);

            TextView key = new TextView(this);
            key.setWidth(0);
            key.setTextColor(Color.BLACK);
            key.setTypeface(null, Typeface.BOLD_ITALIC);
            key.setAllCaps(TRUE);
            key.setTextSize(18);
            key.setGravity(Gravity.CENTER);
            key.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT,1f));
            key.setText(all_details[j].toString());

            TextView value = new TextView(this);
            value.setWidth(0);
            value.setTextColor(Color.BLACK);
            key.setTypeface(null, Typeface.BOLD);
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