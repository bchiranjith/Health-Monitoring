package com.android.sai.firebasetest;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import static java.lang.Boolean.TRUE;

public class details extends AppCompatActivity {
    public DatabaseReference ref;
    public ArrayList<String> arrayList;
    ListView heartrates;
    ListView tabletss;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Intent a = getIntent();
        b = new Bundle();
        b = a.getExtras();
        goTo();
    }
    public int flag=0;
    public void goTo(){
        //String patient_name = b.get("p_name").toString();
        //String db_ref_to_patient = b.get("refer").toString();
        ref= FirebaseDatabase.getInstance().getReference().child("arduino_data");
        Query query = ref.orderByValue();
        String key1 = "";
        try {
            String key = prefs.formatKey(prefs.getDefaults("mail",getApplicationContext()));
            key1 = key;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
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
                if(flag!=1) {
                    flag=1;
                    drawActivity(arrayList);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    ArrayList<String[]> anew,beats,tabs;
    LinearLayout main;
    ScrollView ss;
    Bundle b;

    private void drawActivity(ArrayList<String> arrayList) {
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
            for(int k=0;k<t.length;k++){
                Log.i("beastas  :",t[k]);
            }
        }
        for(int j=0;j<tabs.size();j++){
            String[] t = tabs.get(j);
            for(int k=0;k<t.length;k++){
                Log.i("tabs  :",t[k]);
            }
        }
       ss=new ScrollView(this);
        setContentView(ss);
        /*Setting main Layout*/
        main = new LinearLayout(this);
        main.setOrientation(LinearLayout.VERTICAL);
        //setContentView(main);
        LinearLayout.LayoutParams maina = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        maina.setMargins(32,32,32,32);
        ss.addView(main);

        TextView heart = new TextView(this);
        heart.setAllCaps(TRUE);
        heart.setGravity(Gravity.CENTER);
        heart.setTextSize(24);
        heart.setTextColor(Color.RED);
        heart.setText("heart-rates");
        heart.setPadding(0,32,0,32);
        main.addView(heart);

        ArrayList<String> rates = new ArrayList<>();
        heartrates = new ListView(this);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, rates);
        heartrates.setAdapter(adapter);
        for(int j=0;j<beats.size();j++){
            //Log.i("beats array size",String.valueOf(beats.size()));
            String[] t = beats.get(j);
            String ins = "Rate at " + t[0] + " is @ " + t[1];
            rates.add(ins);
            adapter.notifyDataSetChanged();
        }
        //heartrates.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        heartrates.setDividerHeight(14);
        heartrates.setPadding(0,32,0,32);
        main.addView(heartrates);

        TextView tablets = new TextView(this);
        tablets.setAllCaps(TRUE);
        tablets.setGravity(Gravity.CENTER);
        tablets.setTextSize(24);
        tablets.setTextColor(Color.BLUE);
        tablets.setText("Tablet history");
        tablets.setPadding(0,32,0,32);
        main.addView(tablets);

        ArrayList<String> tabls = new ArrayList<>();
        tabletss = new ListView(this);
        final ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, tabls);
        tabletss.setAdapter(adapter1);
        for(int j=0;j<tabs.size();j++){
            //Log.i("beats array size",String.valueOf(beats.size()));
            String[] t = tabs.get(j);
            String ins = t[1];
            if(t[2].equals("Yes")){
                ins = ins.concat(" taken.");
            }
            if(t[2].equals("No")){
                ins = ins.concat(" not taken.");
            }
            ins = ins + " at " + t[0];
            tabls.add(ins);
            adapter1.notifyDataSetChanged();
        }
        main.addView(tabletss);
    }
}
