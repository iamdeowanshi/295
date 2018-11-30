package com.abacus.android.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.abacus.android.model.LatexResponse;
import com.abacus.android.R;
import com.abacus.android.model.WordProblem;
import com.abacus.android.base.BaseFragment;
import com.abacus.android.camera.CameraPreview;
import com.abacus.android.camera.CameraUtil;
import com.abacus.android.cropcontrol.CropController;
import com.abacus.android.service.latex.LatexPresenter;
import com.abacus.android.service.latex.LatexPresenterImpl;
import com.abacus.android.service.latex.LatexViewInteractor;
import com.abacus.android.util.ImageUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class CameraFragment extends BaseFragment implements LatexViewInteractor {

    @BindView(R.id.camera_preview)
    FrameLayout mCameraPreview;
    @BindView(R.id.image_view)
    ImageView imageView;
    @BindView(R.id.crop_control)
    RelativeLayout cropControl;
    @BindView(R.id.capture)
    ImageView picture;
    @BindView(R.id.gallery)
    ImageView gallery;
    @BindView(R.id.keyboard)
    ImageView keyboard;
    @BindView(R.id.view_scan_line)
    View mScanLine;

    private CropController mCropController;
    Camera mCamera;
    private CameraPreview mPreview;

    private LatexPresenter presenter;
    private String filePath;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_camera, container, false);
        ButterKnife.bind(this, rootView);

        setupCropControl(rootView);
        presenter = new LatexPresenterImpl();
        presenter.attachViewInteractor(this);

        return rootView;
    }

    //region Camera Preview
    @Override
    public void onResume() {
        super.onResume();
        startPreview();
    }

    @Override
    public void onPause() {
        super.onPause();
        stopPreview();
    }

    private void startPreview() {
        //mCropImageView.setImageBitmap(null);
        //mCameraSnapshot.setImageBitmap(null);
        resetDragControl();

        if (mCamera != null) {
            try {
                mCamera.startPreview();
                return;
            } catch (Exception e) {
//                e.printStackTrace();
                //jump to below
            }
        }

        try {
            mCamera = CameraUtil.getCameraInstance();

            if (mCamera == null) {
//                showAlert("Can not connect to camera.");
            } else {
                mPreview = new CameraPreview(getContext(), mCamera);
                mCameraPreview.removeAllViews();
                mCameraPreview.addView(mPreview);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void stopPreview() {
        if (mCamera != null) {
            try {
                mCamera.setPreviewCallback(null);
                mPreview.getHolder().removeCallback(mPreview);
                mCamera.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    //endregion

    private void imageCropped(final Bitmap bitmap) {
        String imageBase64 = ImageUtil.bitmapToBase64(bitmap);
        presenter.getLatex(imageBase64);

    }


    private void showErrorAndReset(String errMessage) {
        //Toast.makeText(getContext(), errMessage, Toast.LENGTH_LONG).show();
        startPreview();
        resetDragControl();
    }

    //region CropControl
    public void setupCropControl(View view) {
        //mCropStatusTextView.setText(R.string.start_dragging_crop);
        mCropController = new CropController(cropControl, new CropController.TouchStateListener() {
            @Override
            public void onDragBegan() {
                //mCropStatusTextView.setText(R.string.release_to_take_photo);
            }

            @Override
            public void onDragEnded() {
            }
        });
    }

    public void resetDragControl() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //mCropImageView.setImageBitmap(null);
                //mCropStatusTextView.setText(R.string.start_dragging_crop);
                ViewGroup.LayoutParams layoutParams = cropControl.getLayoutParams();
                layoutParams.width = (int) getResources().getDimension(R.dimen.crop_control_width);
                layoutParams.height = (int) getResources().getDimension(R.dimen.crop_control_height);
                cropControl.setLayoutParams(layoutParams);
            }
        }, 500);

        picture.setEnabled(true);

        stopScanAnimation();
    }

    private void startScanAnimation() {
        int inset = (int) getResources().getDimension(R.dimen.crop_corner_width_halved);
        Rect cropFrame = new Rect(cropControl.getLeft(), cropControl.getTop(), cropControl.getRight(), cropControl.getBottom());
        final TranslateAnimation animation = new TranslateAnimation(0,0, inset,cropFrame.height() - inset);
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(1000);
        animation.setRepeatMode(Animation.REVERSE);
        animation.setDuration(1000);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mScanLine.setVisibility(View.VISIBLE);
                mScanLine.startAnimation(animation);
            }
        }, 100);

    }

    private void stopScanAnimation() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mScanLine.setAnimation(null);
                mScanLine.setVisibility(View.GONE);
            }
        }, 500);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick(R.id.capture)
    void onTakePhotoButtonClicked() {
        if (mCamera == null) return;

        mCamera.takePicture(null, null, new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(final byte[] data, Camera camera) {
                stopPreview();

                Bitmap bm = ImageUtil.toBitmap(data);
                if (bm.getWidth() > bm.getHeight()) {
                    bm = ImageUtil.rotate(bm, 90);
                }

                Rect cropFrame = new Rect(cropControl.getLeft(), cropControl.getTop(), cropControl.getRight(), cropControl.getBottom());

                int inset = (int) getResources().getDimension(R.dimen.crop_corner_width_halved);
                int viewWidth = getView().getWidth();
                int viewHeight = getView().getHeight();
                int cropWidth = cropFrame.width() - inset * 2;
                int cropHeight = cropFrame.height() - inset * 2;

                int centerX = bm.getWidth() / 2;
                int centerY = bm.getHeight() / 2;
                int targetWidth = (cropWidth * bm.getWidth()) / viewWidth;
                int targetHeight = (cropHeight * bm.getHeight()) / viewHeight;

                Bitmap result = Bitmap.createBitmap(bm, centerX - targetWidth / 2, centerY - targetHeight / 2, targetWidth, targetHeight);
                result = Bitmap.createScaledBitmap(result, targetWidth / 2, targetHeight / 2, false);

                imageCropped(result);

                //region avoid black camera issue - create snapshot and load to imageview overlay
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.US);

                        File files = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                        File outFile = new File(files, sdf.format(new Date()).concat(".").concat("jpg"));
                        FileOutputStream outStream = null;
                        try {
                            outStream = new FileOutputStream(outFile);
                            outStream.write(data);
                            outStream.flush();
                            outStream.close();
                            filePath = outFile.getAbsolutePath();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                //endregion
            }
        });

        picture.setEnabled(false);
    }

    @OnClick(R.id.keyboard)
    void onManualClick() {
        getActivity().finish();
    }

    @OnClick(R.id.crop_control)
    void onTapCropView() {
        if (mPreview != null) {
            try {
                mPreview.autoFocus();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void showProgress() {
        startScanAnimation();
    }

    @Override
    public void hideProgress() {
        stopScanAnimation();
    }

    @Override
    public void onLatexResult(LatexResponse result) {
        Timber.i(result.toString());
        Intent intent = new Intent();
        intent.putExtra("PATH",filePath);
        intent.putExtra("LATEX", result.getLatex());
        getActivity().setResult(2, intent);
        getActivity().finish();
    }

    @Override
    public void onError(String error) {
        showErrorAndReset(error);
        picture.setEnabled(true);

    }
}