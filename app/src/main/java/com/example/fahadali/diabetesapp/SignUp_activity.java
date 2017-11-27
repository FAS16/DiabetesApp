package com.example.fahadali.diabetesapp;


import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fahadali.diabetesapp.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class SignUp_activity extends AppCompatActivity implements View.OnClickListener {

    private EditText firstName_ET, lastName_ET, email_ET, password_ET;
    private Button signUp_BTN;
    private TextView backToLogin_TV;
    private FirebaseAuth firebaseAuth;
    private SharedPreferences prefs;
    private FirebaseUser firebaseUser;
    private User user = User.getUserInstance();
    public DatabaseReference db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance().getReference();

        firstName_ET = findViewById(R.id.createFirstName_ET);
        lastName_ET = findViewById(R.id.createLastName_ET);
        email_ET = findViewById(R.id.createEmail_ET);
        password_ET = findViewById(R.id.createPassword_ET);

        signUp_BTN = findViewById(R.id.signUp_BTN);
        signUp_BTN.setOnClickListener(this);

        backToLogin_TV = findViewById(R.id.backToLogin_TV);
        backToLogin_TV.setOnClickListener(this);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);



    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in and update UI accordingly.
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        updateUI(currentUser);
    }



    @Override
    public void onClick(View view) {

        if(view == backToLogin_TV){
            backToLogin_TV.setTypeface(Typeface.DEFAULT_BOLD);
            finish();
        }

        if(view == signUp_BTN){
            createUserAccount(email_ET.getText().toString(), password_ET.getText().toString());

        }
    }

    private void createLocalUser(){
        user.setID(firebaseUser.getUid().toString());
        user.setName(firstName_ET.getText().toString());
        user.setMail(firebaseUser.getEmail());
        createDBUser();
    }

    private void createDBUser(){
        db.child("users").child(firebaseUser.getUid()).setValue(user);
    }

    protected void createUserAccount(String email, String password){

        if(!passwordValidation()) return;
        if(!userInputValidation()) return;


        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // User is logged in, and the UI updates with the specific user values
                            Log.d("SUCCESSFULL LOGIN", "createUserWithEmail: success");
                            firebaseUser = firebaseAuth.getCurrentUser();
                            createLocalUser();
                            Toast.makeText(SignUp_activity.this, "Bruger oprettet", Toast.LENGTH_SHORT).show();
                            sendEmailVerification();
                           // updateUI(user);
                            showAlertDialog();
                            saveTempLogin();


                        } else {
                            // If sign in fails, display a message to the user.
                           Log.w("FAILED LOGIN", "createUserWithEmail: failure", task.getException());
                            Toast.makeText(SignUp_activity.this, "FEJL, bruger ikke oprettet", Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        //hideProgressDialog();
                    }
                });
        }

    private void sendEmailVerification() {


        // Send verification email
         FirebaseUser user = firebaseAuth.getCurrentUser();

        if(user != null){
        user.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {
                            Toast.makeText(SignUp_activity.this, "Verificérings e-mail er sendt til dig e-mail ", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e("FAILED TO SEND EMAIL", "sendEmailVerification method", task.getException());
                            Toast.makeText(SignUp_activity.this, "Failed to send verification email.", Toast.LENGTH_SHORT).show();
                        }

                    }
                });}
    }


    private boolean userInputValidation() {
        boolean valid = true;

        String email = email_ET.getText().toString();
        if (TextUtils.isEmpty(email)) {
            email_ET.setError("Udfyld venligst.");
            valid = false;

        } else {
            email_ET.setError(null);
        }

        String password = password_ET.getText().toString();
        if (TextUtils.isEmpty(password)) {
            password_ET.setError("Udfyld venligst.");
            valid = false;
        } else {
            password_ET.setError(null);
        }

        return valid;
    }

    public boolean passwordValidation(){
        boolean valid = true;

        String password = password_ET.getText().toString();

        if(password.length() < 6) {
            password_ET.setError("Adgangskoden skal være på minimum 6 tegn.");
            valid = false;

        }

        else if(!password.matches(".*[0-9].*")) {
            password_ET.setError("Adgangskoden skal indeholde minimum ét tal.");
            valid = false;

        }

        else  if(!password.matches(".*[A-Z].*")){
            password_ET.setError("Adgangskoden skal indeholde minimum ét stort bogstav.");
            valid = false;
        }

        return valid;


    }


    public void showAlertDialog(){

        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Brugeren er oprettet.");
        alertDialog.setMessage("Verificér venligst din konto via. e-mail.");


        alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                finish();
            }
        });

        alertDialog.show();
    }

    public void saveTempLogin(){

        prefs.edit()
                .putString("e-mail", email_ET.getText().toString())
                .putString("password", password_ET.getText().toString())
                .commit();
    }






    private void updateUI(FirebaseUser currentUser) {
    }
}