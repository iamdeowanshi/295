package com.abacus.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.abacus.android.base.BaseActivity;
import com.abacus.android.fragment.ChatFragment;
import com.abacus.android.fragment.EquationFragment;
import com.abacus.android.fragment.WordProblemFragment;
import com.abacus.android.fragment.dummy.DummyContent;
import com.abacus.android.model.User;
import com.abacus.android.service.logEvent.LogEvent;
import com.abacus.android.service.logEvent.LogEventImp;
import com.abacus.android.util.PreferenceUtil;

import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends BaseActivity implements ChatFragment.OnListFragmentInteractionListener {

    private TextView mTextMessage;
    BottomNavigationView navigation;
    private LogEvent logPresenter;
    private Bundle bundle;


    private static User user;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_equation:
                    fragment = EquationFragment.newInstance();
                    fragment.setArguments(bundle);
                    removeAllFragment();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, fragment)
                            .commit();
                    return true;
                case R.id.navigation_word:
                    fragment = WordProblemFragment.newInstance();
                    fragment.setArguments(bundle);
                    removeAllFragment();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, fragment)
                            .commit();
                    return true;
                case R.id.navigation_chatbot:
                    //removeAllFragment();
                    /*getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, ChatFragment.newInstance())
                            .commit();*/
                    //startActivity(new Intent(Intent.ACTION_VOICE_COMMAND).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    String queryString = "Talk to abacus";
                    Intent intent = new Intent(Intent.ACTION_VOICE_COMMAND);
                    //intent.putExtra("query", queryString);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    return false;
                case R.id.navigation_setting:
                    startActivity(SettingActivity.class, null);
                    return false;
            }
            return false;
        }
    };

    private void removeAllFragment() {
        for (Fragment fragment:getSupportFragmentManager().getFragments()) {
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mTextMessage = (TextView) findViewById(R.id.message);
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.getInt("fragment") == 0 ) {
                navigation.setSelectedItemId(R.id.navigation_equation);
            } else  {
                navigation.setSelectedItemId(R.id.navigation_word);
            }
        } else {
            navigation.setSelectedItemId(R.id.navigation_equation);
        }

        setUser();

        logPresenter = new LogEventImp();
    }

    private void setUser() {
        PreferenceUtil util = new PreferenceUtil(this);
        user = (User) util.read("USER", User.class);
    }

    public void logEvent(Map<String, String> activityMap) {
        Map<String, String> logMap = new HashMap<>();
        logMap.put("other","N/A");
        logMap.putAll(activityMap);
        logMap.put("userID", getUser().getId());
        logMap.put("userEmail", getUser().getEmail());

        logPresenter.logEvent(logMap);
    }
    public User getUser() {
        return user;
    }

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {

    }
}
