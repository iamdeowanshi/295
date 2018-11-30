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
import com.abacus.android.service.logEvent.LogEvent;
import com.abacus.android.service.logEvent.LogEventImp;
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

import java.util.HashMap;
import java.util.Map;

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
    private User user;
    private LogEvent logPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        util = new PreferenceUtil(this);

        user = (User) util.read("USER", User.class);

        if (user != null) {
            startActivity(HomeActivity.class, null);
            finish();
        }

        mAuth = FirebaseAuth.getInstance();
        init();
        logPresenter = new LogEventImp();
    }

    private void updateUI(FirebaseUser currentUser) {
        User user = saveToPreference(currentUser);
        Map<String, String> logMap = new HashMap<>();
        logMap.put("userID", user.getId());
        logMap.put("userEmail", user.getEmail());
        logMap.put("userActivity", "Login");
        logMap.put("other","N/A");
        logPresenter.logEvent(logMap);
        Toast.makeText(this, currentUser.getDisplayName(), Toast.LENGTH_LONG).show();
        startActivity(HomeActivity.class, null);
        mGoogleSignInClient.signOut();
        finish();
    }

    private User saveToPreference(FirebaseUser currentUser) {

        User user = new User();
        user.setId(currentUser.getUid());
        user.setName(currentUser.getDisplayName());
        user.setEmail(currentUser.getEmail());
        user.setPhotoUrl(currentUser.getPhotoUrl().toString());

        util.save("USER", user);

        return user;

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
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                e.printStackTrace();
            }
        }

        progress.setVisibility(View.GONE);
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acc) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acc.getIdToken(), null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(this);
        progress.setVisibility(View.VISIBLE);
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

    @Override
    protected void onResume() {
        super.onResume();
/*        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (user == null && currentUser != null )
            updateUI(currentUser);*/
    }
}
