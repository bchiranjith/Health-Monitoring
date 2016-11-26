package com.android.sai.firebasetest;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Set;

public class MainActivity extends AppCompatActivity {
    public FirebaseAuth auth1;
    public FirebaseAuth.AuthStateListener list;
    FirebaseUser user1;
    public FirebaseDatabase db;
    public DatabaseReference reference;
    private Set<String> set;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        Bundle a = getIntent().getExtras();
        if(a!=null) {
            set = a.keySet();
        }
        auth1 = FirebaseAuth.getInstance();
        user1 = auth1.getCurrentUser();
        if(user1 == null){
            setContentView(R.layout.activity_main);
        }
        else{

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    /*@Override
    public void onStart() {
        super.onStart();
        auth1.addAuthStateListener(list);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (list != null) {
            auth1.removeAuthStateListener(list);
        }
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.out:
                Log.i("lam","asd");
                return true;
            default:
                return false;
        }
    }

    public void Authenticate(String u,String p){
        final Bundle login = new Bundle();
        login.putString(u, p);
        //String re= "https://sefp-1e4d0.firebaseio.com";
        prefs.setDefaults("mail",u,this);
        auth1.signInWithEmailAndPassword(u,p)
                .addOnCompleteListener(this,new OnCompleteListener<AuthResult>(){
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            Intent i = new Intent(MainActivity.this, Kirrakk.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            i.putExtras(login);
                            startActivity(i);
                            Log.d("sign in", "signInWithEmail:onComplete:" + task.isSuccessful());
                        }
                        if (!task.isSuccessful()) {
                            FirebaseException a = ((FirebaseException)task.getException());
                            String bbbb = a.getMessage();
                            Log.i("errrrrrrrrrrr",bbbb);
                            Toast.makeText(MainActivity.this,bbbb, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    public void getInfo(View view){
        String a;
        String b;
        Bundle bundle = new Bundle();
        EditText u = (EditText)findViewById(R.id.usr);
        EditText p = (EditText)findViewById(R.id.pas);
        a = u.getText().toString();
        b = p.getText().toString();
        Log.i("getting into","getInfo method");
        if (b.isEmpty()){
            p.setError("Invalid input");
        }
        if (a.isEmpty()){
            u.setError("Invalid input");
        }
        if (!a.isEmpty()) {
            bundle.putString("mailer",a);
        }
        if(!b.isEmpty()) {
            bundle.putString("password",b);
        }
        if(!a.isEmpty() && !b.isEmpty()){
            Log.i("exiting","getInfo method");
            Authenticate(a,b);
        }
    }

    public void seePass(View v){
        //CheckBox c = (CheckBox)findViewById(R.id.isc);
        CheckBox c = (CheckBox) v;
        EditText p = (EditText) findViewById(R.id.pas);
        if(c.isChecked()) {
            p.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
            p.setSelection(p.getText().length());
        }
        else if(!c.isChecked()){
            p.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
            p.setSelection(p.getText().length());
        }
    }
    public void Register(View view){
        Intent i = new Intent(getApplicationContext(),Register.class);
        startActivity(i);
    }
}
