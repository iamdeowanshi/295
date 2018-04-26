package com.abacus.poc295;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.abacus.poc295.service.Forecast.LatexPresenter;
import com.abacus.poc295.service.Forecast.LatexPresenterImpl;
import com.abacus.poc295.service.Forecast.LatexViewInteractor;
import com.abacus.poc295.util.ImageUtil;

import timber.log.Timber;

public class LaunchActivity extends AppCompatActivity implements LatexViewInteractor{

    private ImageView captureImage;
    private LatexPresenter latexPresenter;
    private ProgressBar progressBar;
    private Button solve;
    private String imageBase64;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        latexPresenter = new LatexPresenterImpl();
        latexPresenter.attachViewInteractor(this);
        progressBar = findViewById(R.id.progress);
        solve = findViewById(R.id.solve);

        findViewById(R.id.capture).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LaunchActivity.this, CameraActivity.class);

                startActivityForResult(intent, 1);
            }
        });

        solve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                latexPresenter.getLatex(imageBase64);
            }
        });
        captureImage = findViewById(R.id.captured_image);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if(requestCode==1 && resultCode==2)
        {
            Bitmap bitmap = BitmapFactory.decodeFile(data.getStringExtra("PATH"));
            captureImage.setImageBitmap(bitmap);
            imageBase64 = ImageUtil.bitmapToBase64(bitmap);
            solve.setClickable(true);
        }
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onLatexResult(LatexResponse result) {
        Timber.i(result.getLatex());
    }
}
