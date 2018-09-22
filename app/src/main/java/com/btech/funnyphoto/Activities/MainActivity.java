package com.btech.funnyphoto.Activities;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.btech.funnyphoto.AdmodData;
import com.btech.funnyphoto.R;
import com.btech.funnyphoto.imagecrop.BitmapUtil;
import com.btech.funnyphoto.imagecrop.CropHandler;
import com.btech.funnyphoto.imagecrop.CropHelper;
import com.btech.funnyphoto.imagecrop.CropParams;
import com.btech.funnyphoto.utils.Constant;
import com.btech.funnyphoto.utils.Utility;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends Activity implements CropHandler {

    private Animation animation;
    private RelativeLayout top_holder;
    private RelativeLayout bottom_holder;
    private RelativeLayout step_number;
    private Uri imageUri;
    private boolean click_status = true;
    private TextView tvMyGallery;


    private String userChoosenTask;
    private String folderPath = "/memoji";
    private String imageName = "memoji";
    private File mFolder;
    private CropParams mCropParams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainnew);
        new AdmodData(this);
        top_holder = (RelativeLayout) findViewById(R.id.top_holder);
        bottom_holder = (RelativeLayout) findViewById(R.id.bottom_holder);
        step_number = (RelativeLayout) findViewById(R.id.step_number);
        tvMyGallery=(TextView) findViewById(R.id.tvMyGallery);
        mCropParams = new CropParams(MainActivity.this);

        makeFolder();//To make particular folder where this application images store

        tvMyGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent editB = new Intent(MainActivity.this, GallaryCollageActivity.class);
                startActivity(editB);

            }
        });
    }

    @Override
    protected void onStart() {
        overridePendingTransition(0, 0);
        flyIn();
        super.onStart();
    }

    @Override
    protected void onStop() {
        overridePendingTransition(0, 0);
        super.onStop();
    }

    public void startGallery(View view)
    {
        flyOut("openGallery");
    }

    public void startCamera(View view)
    {
        flyOut("openCamera");
    }




    //Take permission for marshmallow to read external storage
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (userChoosenTask.equals(getString(R.string.take_photo)))
                        openGallery();//This method is used to capture image from camera
                    else if (userChoosenTask.equals(getString(R.string.choose_from_gallery)))
                        openCamera();
                } else {
                    //code for deny
                }
                break;
        }
    }


    public void openGallery() {
        mCropParams.refreshUri();
        mCropParams.enable = true;
        mCropParams.compress = false;
        Intent intent = CropHelper.buildGalleryIntent(mCropParams);
        startActivityForResult(intent, CropHelper.REQUEST_CROP);

    }

    private void openCamera() {
        mCropParams.refreshUri();
        mCropParams.enable = true;
        mCropParams.compress = false;
        Intent intent = CropHelper.buildCameraIntent(mCropParams);
        startActivityForResult(intent, CropHelper.REQUEST_CAMERA);

    }

    @Override
    public CropParams getCropParams() {
        return mCropParams;
    }

    @Override
    public void onPhotoCropped(Uri uri) {
        // Original or Cropped uri

        if (!mCropParams.compress) {

            Bitmap thePic = BitmapUtil.decodeUriAsBitmap(this, uri);
            saveBitmap(thePic);
            Intent editB = new Intent(MainActivity.this, EditActivity.class);
            startActivity(editB);

        }

    }


    @Override
    public void onCompressed(Uri uri) {


        Bitmap thePic = BitmapUtil.decodeUriAsBitmap(this, uri);
        saveBitmap(thePic);
        Intent editB = new Intent(MainActivity.this, EditActivity.class);
        startActivity(editB);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == CropHelper.REQUEST_CROP) {
            CropHelper.handleResult(this, requestCode, resultCode, data);
        } else if (requestCode == CropHelper.REQUEST_CAMERA) {
            CropHelper.handleResult(this, requestCode, resultCode, data);
        }

    }

    @Override
    public void onCancel() {
        //Toast.makeText(this, "Crop canceled!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onFailed(String message) {
        //Toast.makeText(this, "Crop failed: " + message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void handleIntent(Intent intent, int requestCode) {
        startActivityForResult(intent, requestCode);
    }




    public void makeFolder() {
        String extr = Environment.getExternalStorageDirectory().toString();
        mFolder = new File(extr + folderPath);
        if (!mFolder.exists()) {
            mFolder.mkdir();
        }
    }

    public void saveBitmap(Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        String fileName = imageName + System.currentTimeMillis() + "tmp.jpg";
        File destination = new File(mFolder.getAbsolutePath(), fileName);
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
            Constant.PICURE_PATH = destination.getPath();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }







    private void flyOut(final String method_name) {
        if (click_status) {
            click_status = false;

            animation = AnimationUtils.loadAnimation(this, R.anim.step_number_back);
            step_number.startAnimation(animation);

            animation = AnimationUtils.loadAnimation(this, R.anim.holder_top_back);
            top_holder.startAnimation(animation);

            animation = AnimationUtils.loadAnimation(this, R.anim.holder_bottom_back);
            bottom_holder.startAnimation(animation);

            animation.setAnimationListener(new AnimationListener() {
                @Override
                public void onAnimationStart(Animation arg0) {
                }

                @Override
                public void onAnimationRepeat(Animation arg0) {
                }

                @Override
                public void onAnimationEnd(Animation arg0) {
                    //callMethod(method_name);



                    if(method_name.equalsIgnoreCase("openGallery"))
                    {
                        openGallery();
                    }
                    else if (method_name.equalsIgnoreCase("openCamera"))
                    {
                         openCamera();
                    }
                    else
                    {

                    }
                }
            });
        }
    }

    private void callMethod(String method_name)
    {
        if (method_name.equals("finish")) {
            overridePendingTransition(0, 0);
            finish();
        }
        else {
            try {
//                Method method = getClass().getDeclaredMethod(method_name);
//                method.invoke(this, new Object[]{});

                openGallery();
            } catch (Exception e) {
            }
        }
    }

    @Override
    public void onBackPressed() {
        flyOut("finish");
        super.onBackPressed();
    }

    private void flyIn() {
        click_status = true;

        animation = AnimationUtils.loadAnimation(this, R.anim.holder_top);
        top_holder.startAnimation(animation);

        animation = AnimationUtils.loadAnimation(this, R.anim.holder_bottom);
        bottom_holder.startAnimation(animation);

        animation = AnimationUtils.loadAnimation(this, R.anim.step_number);
        step_number.startAnimation(animation);
    }

}
