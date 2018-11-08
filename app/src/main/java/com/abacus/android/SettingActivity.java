package com.abacus.android;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.abacus.android.base.BaseActivity;
import com.abacus.android.model.User;
import com.abacus.android.util.PicassoCircleTransformation;
import com.abacus.android.util.PreferenceUtil;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingActivity extends BaseActivity {

    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.txtName)
    TextView txtName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        loadUserData();
    }

    private void loadUserData() {
        PreferenceUtil util = new PreferenceUtil(this);
        User user = (User) util.read("USER", User.class);
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
    }

    @OnClick(R.id.history)
    public void onHistoryClicked() {
    }
}
