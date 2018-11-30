package com.abacus.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abacus.android.base.BaseActivity;
import com.abacus.android.model.User;
import com.abacus.android.service.logEvent.LogEventImp;
import com.abacus.android.util.PicassoCircleTransformation;
import com.abacus.android.util.PreferenceUtil;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingActivity extends BaseActivity {

    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.txtName)
    TextView txtName;
    @BindView(R.id.analytics)
    RelativeLayout analyticView;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        boolean admin = true;
        showAnalytics(admin);
        loadUserData();
    }

    private void showAnalytics(boolean admin) {
        int value = (admin) ? View.VISIBLE : View.GONE;
        analyticView.setVisibility(value);
    }

    private void loadUserData() {
        PreferenceUtil util = new PreferenceUtil(this);
        user = (User) util.read("USER", User.class);
        if (user == null)
            return;

        txtName.setText(user.getName());
        Picasso.get().load(user.getPhotoUrl()).placeholder(R.drawable.ic_person).transform(new PicassoCircleTransformation()).into(imageView);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //finish();
                onBackPressed();
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        //Execute your code here
        finish();
    }

    @OnClick(R.id.btnBack)
    public void onHomeClicked() {
        onBackPressed();
    }

    @OnClick(R.id.bookMark)
    public void onBookMarkClicked() {
        startActivity(BookmarkActivity.class, null);
    }

    @OnClick(R.id.history)
    public void onHistoryClicked() {
        startActivity(HistoryActivity.class, null);
    }

    @OnClick(R.id.analytics)
    public void onAnalyticsClick() {
        startActivity(AnalyticsActivity.class, null);
    }

    @OnClick(R.id.logout)
    public void onViewClicked() {
        logEvent();
        PreferenceUtil util = new PreferenceUtil(this);
        util.clear();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void logEvent() {
        Map<String, String> logMap = new HashMap<>();
        logMap.put("other","N/A");
        logMap.put("userID", user.getId());
        logMap.put("userEmail", user.getEmail());
        logMap.put("userActivity", "Logout");

        new LogEventImp().logEvent(logMap);
    }
}
