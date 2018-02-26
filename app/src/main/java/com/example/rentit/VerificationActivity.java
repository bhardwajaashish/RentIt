package com.example.rentit;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class VerificationActivity extends AppCompatActivity {

    Button verify;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        mAuth = FirebaseAuth.getInstance();
        Intent intent=getIntent();
        String key=intent.getStringExtra("user");

        verify=(Button)findViewById(R.id.verify);
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmailVerification();
            }
        });

    }
    private void sendEmailVerification() {
        // Disable button
        findViewById(R.id.verify).setEnabled(false);

        // Send verification email
        // [START send_email_verification]
        final FirebaseUser user = mAuth.getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // [START_EXCLUDE]
                        // Re-enable button
                        findViewById(R.id.verify).setEnabled(true);

                        if (task.isSuccessful()) {
                            Toast.makeText(VerificationActivity.this,
                                    "Verification email sent to " + user.getEmail(),
                                    Toast.LENGTH_SHORT).show();
                            Intent i=new Intent(VerificationActivity.this,SignInActivity.class);
                            startActivity(i);
                        } else {
                            Log.e("EmailPassword", "sendEmailVerification", task.getException());
                            Toast.makeText(VerificationActivity.this,
                                    "Failed to send verification email.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        // [END_EXCLUDE]
                    }
                });
        /*if(!user.isEmailVerified()){
            while(!user.isEmailVerified()) {

            }
            Intent intent = new Intent(VerificationActivity.this, SignInActivity.class);
            startActivity(intent);
        }*/
        // [END send_email_verification]
    }
}
