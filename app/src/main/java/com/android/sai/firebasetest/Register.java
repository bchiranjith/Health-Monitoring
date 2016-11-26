package com.android.sai.firebasetest;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Set;

import static com.android.sai.firebasetest.R.id.user;
import static com.android.sai.firebasetest.prefs.formatKey;

public class Register extends AppCompatActivity {
    public FirebaseAuth auth1;
    public FirebaseAuth.AuthStateListener listener;
    public FirebaseUser u;
    public int count = 0;
    public int flag = 0;
    Bundle bb = new Bundle();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        auth1=FirebaseAuth.getInstance();
        listener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.i("no", "onAuthStateChanged:signed_in:" + user.getUid());
                }
                else {
                    // User is signed out
                    Log.i("yes","onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        auth1.addAuthStateListener(listener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (listener != null) {
            auth1.removeAuthStateListener(listener);
        }
    }

    public void getCred(View v) throws JSONException {
        //Bundle b = new Bundle();
        EditText e1=(EditText)findViewById(user);
        EditText e2=(EditText)findViewById(R.id.passs);
        EditText e3=(EditText)findViewById(R.id.mai);
        EditText e=(EditText)findViewById(R.id.phno);
        EditText e4=(EditText)findViewById(R.id.original);
        String full=e4.getText().toString();
        String name = e1.getText().toString();
        String word = e2.getText().toString();
        String id = e3.getText().toString();
        String no = e.getText().toString();
        if(!full.isEmpty()){
            bb.putString("name",full);
        }
        if(!name.isEmpty()) {
            bb.putString("user",name);
        }
        if(!word.isEmpty()) {
            bb.putString("pass",word);
        }
        if (!id.isEmpty()) {
            bb.putString("email",id);
        }
        if(!no.isEmpty()) {
            bb.putString("ph",no);
        }
        if(full.isEmpty()){
            e4.setError("Invalid Input");
        }
        if(name.isEmpty()){
            e1.setError("Invalid Input");
        }
        if(word.isEmpty()){
            e2.setError("Invalid Input");
        }
        if(id.isEmpty()){
            e3.setError("Invalid Input");
        }
        if(no.isEmpty()){
            e.setError("Invalid Input");
        }
        if (count == 0){
            bb.putString("job","doctor");
        }
        if (flag == 0){
            bb.putString("sex","female");
        }
        if(!name.isEmpty() && !word.isEmpty() && !no.isEmpty() && !id.isEmpty()){
            send2Intent(bb);
        }
    }

    public void radClick1(View v){
        count=1;
        boolean profession;
        boolean checked = ((RadioButton) v).isChecked();
        switch (v.getId()){
            case R.id.doc:
                if(checked){
                    profession=true;
                    bb.putString("job","doctor");
                    break;
                }
            case R.id.pat:
                if(checked){
                    profession=false;
                    bb.putString("job","patient");
                    break;
                }
        }

    }

    public void radClick2(View v){
        flag=1;
        boolean checked = ((RadioButton) v).isChecked();
        switch (v.getId()){
            case R.id.male:
                if(checked){
                    bb.putString("sex","male");
                    break;
                }
            case R.id.female:
                if(checked) {
                    bb.putString("sex","female");
                    break;
                }
        }
    }

    public void send2Intent(Bundle bund) throws JSONException {
        Set a = bund.keySet();
        int i = bund.size();
        //String ii = String.valueOf(i);
        ArrayList<String> aaa = new ArrayList<>();
        Object[] arr = a.toArray();
        for(int j=0;j<i;j++){
            //Log.i("set",arr[j].toString());
            aaa.add(bund.getString(arr[j].toString()));
           // Log.i("value",aaa.get(j));
        }
        createId(bund.getString("email"),bund.getString("pass"),bund);
        /*Intent reg = new Intent(getApplicationContext(),Credentials.class);
        reg.putExtras(bund);
        startActivity(reg);*/
    }
    public DatabaseReference ref;
    public FirebaseDatabase d1;

    private void store2Db(Bundle bund) throws UnsupportedEncodingException {
        Set a = bund.keySet();
        int lena=bund.size();
        ArrayList<String> aaa = new ArrayList<>();
        Object[] arr = a.toArray();
        for(int j=0;j<lena;j++){
            aaa.add(bund.getString(arr[j].toString()));
        }
        d1 = FirebaseDatabase.getInstance();
        if(bund.getString("job") == "doctor"){
            ref = d1.getReference("D");
        }
        else if (bund.getString("job") == "patient"){
            ref = d1.getReference("P");
        }
        String imp = bund.getString("email");
        String encode = formatKey(imp);
        a.remove("name");
        aaa.remove(bund.getString("name"));
        lena = bund.size();
        for (int j=0;j<=lena; j++){
            Log.i("db entries",aaa.get(j));
            ref.child(encode).child(arr[j].toString()).setValue(bund.getString(arr[j].toString()));
        }

        //Intent i = new Intent(getApplicationContext(),Credentials.class);
        //startActivity(i);
    }

    public void createId(String m, String p, final Bundle  bund) {
        /*
        * ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        * NetworkInfo ni = cm.getActiveNetworkInfo();
        * */
        auth1.createUserWithEmailAndPassword(m, p)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            try {
                                store2Db(bund);
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                            Bundle b = new Bundle();
                            b.putString("s", "Success");
                            //Intent reg = new Intent(Register.this,MainActivity.class);
                            startActivity(new Intent(Register.this, MainActivity.class).putExtras(b));
                        }
                        if (!task.isSuccessful()) {
                            FirebaseException a = ((FirebaseException) task.getException());
                            String bbbb = a.getMessage();
                            Toast.makeText(Register.this, bbbb, Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
