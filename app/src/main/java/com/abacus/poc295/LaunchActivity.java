package com.abacus.poc295;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class LaunchActivity extends AppCompatActivity {

    private ImageView captureImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        findViewById(R.id.capture).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LaunchActivity.this, CameraActivity.class);

                startActivityForResult(intent, 1);
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
        }
    }
}
