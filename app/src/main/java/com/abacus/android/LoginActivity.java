package com.abacus.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.abacus.android.base.BaseActivity;
import com.abacus.android.model.User;
import com.abacus.android.util.PreferenceUtil;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity implements OnCompleteListener {

    private static final int RC_SIGN_IN = 1;
    @BindView(R.id.sign_in_button)
    SignInButton signInButton;
    @BindView(R.id.progress)
    ProgressBar progress;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    private PreferenceUtil util;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        util = new PreferenceUtil(this);

        User user = (User) util.read("USER", User.class);

        if (user != null) {
            startActivity(HomeActivity.class, null);
            finish();
        }

        mAuth = FirebaseAuth.getInstance();
        init();
    }

    private void updateUI(FirebaseUser currentUser) {
        saveToPreference(currentUser);
        Toast.makeText(this, currentUser.getDisplayName(), Toast.LENGTH_LONG).show();
        startActivity(HomeActivity.class, null);
        finish();
    }

    private void saveToPreference(FirebaseUser currentUser) {

        User user = new User();
        user.setName(currentUser.getDisplayName());
        user.setEmail(currentUser.getEmail());
        user.setPhotoUrl(currentUser.getPhotoUrl().toString());

        util.save("USER", user);

    }

    private void init() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken("804846675443-5oaltodabo5g08q0deag9bablf1vh1hd.apps.googleusercontent.com")
                .requestProfile()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    @OnClick(R.id.sign_in_button)
    public void onViewClicked() {
        progress.setVisibility(View.VISIBLE);
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }

        progress.setVisibility(View.GONE);
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            Toast.makeText(this, account.getDisplayName(), Toast.LENGTH_LONG).show();
            firebaseAuthWithGoogle(account);
            // Signed in successfully, show authenticated UI.
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            //updateUI(null);
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acc) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acc.getIdToken(), null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(this);

    }

    @Override
    public void onComplete(@NonNull Task task) {
        progress.setVisibility(View.GONE);
        if (task.isSuccessful()) {
            FirebaseUser user = mAuth.getCurrentUser();
            updateUI(user);
            // Sign in success, update UI with the signed-in user's information
            //Log.d(TAG, "signInWithCredential:success");

        } else {
            // If sign in fails, display a message to the user.
            //Log.w(TAG, "signInWithCredential:failure", task.getException());
            Snackbar.make(findViewById(R.id.root), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
        }
    }
}
