package com.robin.mdpanel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MdLoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Button mdLogin;

    private TextView forgetPassword;

    private EditText mdEmail,mdPassword;
    private DatabaseReference rootRef;

    private ProgressDialog loadingbar;

    private AlertDialog.Builder dialogueBuilder;
    private AlertDialog dialogue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_md_login);

        mAuth=FirebaseAuth.getInstance();

        rootRef= FirebaseDatabase.getInstance().getReference();


        mdEmail=findViewById(R.id.mdLoginEmail);
        mdPassword=findViewById(R.id.mdLoginPassword);
        mdLogin=findViewById(R.id.md_signInButton);

        loadingbar=new ProgressDialog(this);

        forgetPassword=findViewById(R.id.forget_password_md);

        mdLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkLoginMd();
            }
        });

        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recoverPassword();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            SendUserToMainActivity();
        }

    }

    private void checkLoginMd() {

        String email=mdEmail.getText().toString();
        String password=mdPassword.getText().toString();

        if(email.isEmpty())
        {
            mdEmail.setError("Enter Your email address");
            mdEmail.requestFocus();
            return;
        }

        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            mdEmail.setError("Enter a valid email address");
            mdEmail.requestFocus();
            return;
        }

        //checking the validity of the password
        if(password.isEmpty())
        {
            mdPassword.setError("Enter a password");
            mdPassword.requestFocus();
            return;
        }

        //check password length
        if(password.length()<6){
            Toast.makeText(this, "Password Length should be 6", Toast.LENGTH_LONG).show();
            mdPassword.requestFocus();
            return;
        }

        loadingbar.setTitle("Sign In");
        loadingbar.setMessage("Please Wait Until the Login is Successfully Completed...");
        loadingbar.setCanceledOnTouchOutside(false);
        loadingbar.show();

        mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){

                            Toast.makeText(MdLoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                            loadingbar.dismiss();
                            SendUserToMainActivity();

                        }
                        else{

                            loadingbar.dismiss();
                            Toast.makeText(MdLoginActivity.this, "Error: "+task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }


    private void SendUserToMainActivity() {
        Intent Intent = new Intent(getApplicationContext(),MainActivity.class);
        Intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(Intent);
        finish();
    }

    private void recoverPassword() {

        dialogueBuilder =new AlertDialog.Builder(MdLoginActivity.this);
        View ForgetPasswordView=getLayoutInflater().inflate(R.layout.forget_password_layout_md,null);

        final EditText emailEmail=(EditText) ForgetPasswordView.findViewById(R.id.forget_pas_md_layout);
        Button sendButton =(Button) ForgetPasswordView.findViewById(R.id.forget_pas_md_button);

        dialogueBuilder.setView(ForgetPasswordView);
        dialogue=dialogueBuilder.create();
        dialogue.show();

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=emailEmail.getText().toString();

                if(email.isEmpty()){
                    emailEmail.setError("Enter Your Email");
                    emailEmail.requestFocus();
                    return;
                }

                loadingbar.setTitle("Sending Mail");
                loadingbar.setMessage("Please Wait Until The Loading Successfully Completed...");
                loadingbar.setCanceledOnTouchOutside(false);
                loadingbar.show();

                mAuth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Toast.makeText(MdLoginActivity.this, "Check Your Email and Reset Your Password.", Toast.LENGTH_LONG).show();
                        dialogue.dismiss();
                        loadingbar.dismiss();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(MdLoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        dialogue.dismiss();
                        loadingbar.dismiss();
                    }
                });
            }
        });

    }

}
