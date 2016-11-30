package com.android.sai.firebasetest;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
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
    private ProgressDialog prog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Intent a = getIntent();
        b = new Bundle();
        b = a.getExtras();
        prog = new ProgressDialog(this);
        prog.setIndeterminate(true);
        prog.setMessage("Populating.....");
        prog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        prog.show();
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
                drawActivity(arrayList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    ArrayList<String[]> anew,beats,tabs;
    LinearLayout main,main1;
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
       // ss=new ScrollView(this);
        //setContentView(ss);
        /*Setting main Layout*/
        main1 = new LinearLayout(this);
        main = new LinearLayout(this);
        main1.setOrientation(LinearLayout.VERTICAL);
        main.setOrientation(LinearLayout.VERTICAL);
        setContentView(main1);

        LinearLayout.LayoutParams maina = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        maina.setMargins(32,32,32,32);
        main1.addView(main);
        main.setBackgroundColor(Color.parseColor("#1865EB"));
        //ss.addView(main1);
        /*
        * Creating separate lists for showing the heartrates and tablet history....
        * */

        ListView.LayoutParams params = new ListView.LayoutParams(ListView.LayoutParams.WRAP_CONTENT,ListView.LayoutParams.WRAP_CONTENT);
        params.height = 580;
        heartrates = new ListView(this);
        heartrates.setBackgroundColor(Color.WHITE);
        tabletss = new ListView(this);
        tabletss.setBackgroundColor(Color.WHITE);
        heartrates.setLayoutParams(params);
        tabletss.setLayoutParams(params);
        //setListViewHeightBasedaOnChildren(tabletss);

        /*
        * Heart- rates starts here.
        * */

        TextView heart = new TextView(this);
        heart.setAllCaps(TRUE);
        heart.setGravity(Gravity.CENTER);
        heart.setTextSize(24);
        heart.setTypeface(null, Typeface.BOLD_ITALIC);
        heart.setTextColor(Color.WHITE);
        heart.setText("heart-rates");
        heart.setPadding(0,32,0,32);
        main.addView(heart);

        ArrayList<String> rates = new ArrayList<>();
        rates.clear();
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, rates);
        heartrates.setAdapter(adapter);

        String s1="",s2="";

        for(int j=0;j<beats.size();j++){
            //Log.i("beats array size",String.valueOf(beats.size()));
            String[] t = beats.get(j);
            String ins = "Rate at " + t[0] + " is @ " + t[1];
            s1 = ins;
            rates.add(ins);
            adapter.notifyDataSetChanged();
        }
        //heartrates.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        heartrates.setDividerHeight(4);
        heartrates.setPadding(64,32,64,32);
        //setListViewHeightBasedOnChildren(heartrates);
        main.addView(heartrates);

        TextView tablets = new TextView(this);
        tablets.setAllCaps(TRUE);
        tablets.setGravity(Gravity.CENTER);
        tablets.setTextSize(24);
        tablets.setTextColor(Color.WHITE);
        tablets.setText("Tablet history");
        tablets.setTypeface(null,Typeface.BOLD_ITALIC);
        tablets.setPadding(0,32,0,32);
        main.addView(tablets);

        ArrayList<String> tabls = new ArrayList<>();
        tabls.clear();
        final ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, tabls);
        tabletss.setAdapter(adapter1);
        int flag = 0;
        for(int j=0;j<tabs.size();j++){
            //Log.i("beats array size",String.valueOf(beats.size()));
            String[] t = tabs.get(j);
            String ins = t[1];
            if(t[2].equals("Yes")){
                s2 = "";
                ins = ins.concat(" taken ");
                s2 = ins;
                s2 = s2 + " at " + t[0];
            }
            if(t[2].equals("No")){
                ins = ins.concat(" not taken ");
            }

            ins = ins + " at " + t[0];
            tabls.add(ins);
            adapter1.notifyDataSetChanged();
        }
        tabletss.setDividerHeight(4);
        tabletss.setPadding(64,32,64,32);
        //setListViewHeightBasedOnChildren(tabletss);
        main.addView(tabletss);
        prog.hide();
//*newly added
//
//
//
// */
        TextView heart1 = new TextView(this);
        heart1.setAllCaps(TRUE);
        heart1.setGravity(Gravity.CENTER);
        heart1.setTextSize(20);
        heart1.setTextColor(Color.WHITE);
        heart1.setTypeface(null,Typeface.BOLD);
        heart1.setText("Last heart rate");
        heart1.setPadding(0,32,0,32);
        main.addView(heart1);
        //main.addView(no);

        TextView h_details = new TextView(this);
        h_details.setGravity(Gravity.CENTER);
        h_details.setTextSize(18);
        h_details.setTextColor(Color.YELLOW);
        //String temp = "The last recorded heart-rate was " + s1[1] + " at " + s1[0];
        h_details.setTypeface(null,Typeface.BOLD);
        s1 = "Last: " + s1;
        h_details.setText(s1);
        h_details.setPadding(0,32,0,32);
        main.addView(h_details);
        //main.addView(no);

        TextView tb = new TextView(this);
        tb.setAllCaps(TRUE);
        tb.setGravity(Gravity.CENTER);
        tb.setTextSize(20);
        tb.setTextColor(Color.parseColor("#ffffff"));
        tb.setText("Last - taken");
        tb.setTypeface(null,Typeface.BOLD);
        tb.setPadding(0,32,0,32);
        main.addView(tb);

        TextView t_details = new TextView(this);
        t_details.setGravity(Gravity.CENTER);
        t_details.setTextSize(20);
        t_details.setTypeface(null,Typeface.BOLD);
        t_details.setTextColor(Color.YELLOW);
        s2 ="Last: " + s2;
        t_details.setText(s2);
        t_details.setPadding(0,32,0,32);
        main.addView(t_details);
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
}

