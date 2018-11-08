package com.abacus.android;

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

public class HomeActivity extends BaseActivity implements ChatFragment.OnListFragmentInteractionListener {

    private TextView mTextMessage;
    BottomNavigationView navigation;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_equation:
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, EquationFragment.newInstance())
                            .commit();
                    return true;
                case R.id.navigation_word:
                    removeAllFragment();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, WordProblemFragment.newInstance())
                            .commit();
                    return true;
                case R.id.navigation_chatbot:
                    removeAllFragment();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, ChatFragment.newInstance())
                            .commit();
                    return true;
                case R.id.navigation_setting:
                    startActivity(SettingActivity.class, null);
                    return true;
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
        navigation.setSelectedItemId(R.id.navigation_equation);
    }

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {

    }
}
