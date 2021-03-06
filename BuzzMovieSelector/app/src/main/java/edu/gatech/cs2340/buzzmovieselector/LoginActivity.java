package edu.gatech.cs2340.buzzmovieselector;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Context;
import android.widget.Button;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private UserManager userManager;
    private EditText usernameBox;
    private EditText passBox;
    private Toast lockedToast;
    private Toast bannedToast;
    private String lockedMessage;
    private String bannedMessage;
    private Firebase userTable = new Firebase("https://buzz-movie-selector5.firebaseio.com/Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Firebase.setAndroidContext(LoginActivity.this);
        userManager = UserManager.getInstance();

        usernameBox = (EditText) findViewById(R.id.username);
        passBox = (EditText) findViewById(R.id.password);

        Button signIn = (Button) findViewById(R.id.signIn);
        Button cancel = (Button) findViewById(R.id.cancel);


        signIn.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                onLoginButtonPressed(view);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCancelButtonPressed(view);
            }
        });

    }

    /**
     * Upon clicking the lgoin button, this method facilitates the validation of user input
     * for username and password
     *
     * @param v login button view
     */

    public void onLoginButtonPressed(View v) {

        final String username = usernameBox.getText().toString().trim();
        final String password = passBox.getText().toString().trim();
        if (isAdmin(username, password)) {
            Intent intent = new Intent(LoginActivity.this, AdminActivity.class);
            startActivity(intent);
        }
        final Firebase ref = userManager.getUserTable();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                               @Override
                                               public void onDataChange(DataSnapshot dataSnapshot) {
                                                   if (!username.equals("") && !password.equals("")) {
                                                       if (dataSnapshot.child(username).exists() &&
                                                               dataSnapshot.child(username)
                                                                       .child("password").getValue()
                                                                       .toString().equals(password)) {
                                                           HashMap<String, String> firebaseUser = (HashMap<String, String>) dataSnapshot.child(username).getValue();

                                                           // Check if user is Banned
                                                           int duration = Toast.LENGTH_LONG;
                                                           if (firebaseUser.get("status").equals("Ban")) {
                                                               bannedMessage = "Your account has been banned!";
                                                               bannedToast = Toast.makeText(LoginActivity.this, bannedMessage, duration);
                                                               bannedToast.show();
                                                           } else {
                                                               User newUser = new User(firebaseUser.get("name"),
                                                                       firebaseUser.get("username"),
                                                                       firebaseUser.get("email"),
                                                                       firebaseUser.get("password"),
                                                                       firebaseUser.get("major"),
                                                                       firebaseUser.get("gender"),
                                                                       firebaseUser.get("interests"));

                                                               UserManager.getInstance().setCurrentUser(newUser);

                                                               Intent intent = new Intent(LoginActivity.this, HomePageActivity.class);
                                                               startActivity(intent);
                                                           }
                                                       } else if (dataSnapshot.child(username).exists()) {
                                                           //until we clean this up
                                                           HashMap<String, String> firebaseUser2 = (HashMap<String, String>) dataSnapshot.child(username).getValue();
                                                           Object tempHolder = firebaseUser2.get("loginAttempts");
                                                           Long loginAttempts = (Long) tempHolder;
                                                           int duration = Toast.LENGTH_LONG;
                                                           if (loginAttempts >= 3) {
                                                               lockedMessage = "Your account has been locked!";
                                                               lockedToast = Toast.makeText(LoginActivity.this, lockedMessage, duration);
                                                               lockedToast.show();
                                                               userTable.child(username).child("status").setValue("Locked");
                                                           }
                                                           loginAttempts++;
                                                           userTable.child(username).child("loginAttempts").setValue(loginAttempts);
                                                       }
                                                   }

                                               }

                                               @Override
                                               public void onCancelled(FirebaseError firebaseError) {

                                               }
                                           }

        );


    }

    /**
     * Upon cancellation of registering for an account, this takes you to the wlecome page.
     *
     * @param v cancel button view
     */

    public void onCancelButtonPressed(View v) {
        Intent intent = new Intent(this, WelcomePageActivity.class);
        startActivity(intent);
    }
    public boolean isAdmin(String userName, String passWord) {
        Admin admin = new Admin();
        return userName.equals(admin.getAdminUserName())
                && passWord.equals(admin.getAdminPassWord());
    }

}
