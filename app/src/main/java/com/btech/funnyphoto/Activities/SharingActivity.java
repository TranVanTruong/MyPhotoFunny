package com.btech.funnyphoto.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.btech.funnyphoto.R;
import com.btech.funnyphoto.utils.Constant;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class SharingActivity extends AppCompatActivity implements OnClickListener {


    private ImageLoader imageLoader;
    private ImageView imgOrange;
    private TextView tvShareLink;
    private String path = "";
    private setImageAsyncTask mSetImageAsyncTask;
    private Typeface typeface;
    private AdView mAdView;
    private AdView adView = null;
    private InterstitialAd interstitial;
    private Bitmap src;

    private String folderPath = "/MyPhotoCollage";
    private String imageName = "MyPhotoCollage";
    private File mFolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sharing);

        //Action bar color
        if (Build.VERSION.SDK_INT >= 21) {
            // Status bar color
            getWindow().setStatusBarColor(getResources().getColor(R.color.thirdColor));
        }

        initComponent();
        setupToolbar();
        makeFolder();
        LoadBanerAd();
        LoadFullAd();

    }

    private void initComponent() {


        imgOrange = (ImageView) findViewById(R.id.imgOrange);
        tvShareLink = (TextView) findViewById(R.id.tvShareLink);
        typeface = Typeface.createFromAsset(getAssets(), "jumping_running.ttf");
        tvShareLink.setOnClickListener(this);

        initImageLoader();

        Intent intent = getIntent();

        if (intent != null) {
            path = getIntent().getStringExtra("ImagePath");
            src = BitmapFactory.decodeFile(path);
            setImage();
        }


    }


    private void setupToolbar() {
        // Initializing Toolbar and setting it as the actionbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_menubar_toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();

        TextView mTitle = (TextView) toolbar.findViewById(R.id.actionbar_tvTitle);
        ImageView ivSave = (ImageView) toolbar.findViewById(R.id.activity_base_actionbar_ivNext);
        mTitle.setTypeface(typeface);
        mTitle.setVisibility(View.VISIBLE);
        ivSave.setVisibility(View.VISIBLE);
        ivSave.setImageResource(R.drawable.ic_save);
        ivSave.setOnClickListener(this);
        mTitle.setText("Share Pics");

        if (actionBar != null) {

            actionBar.setTitle("");
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(true);
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == android.R.id.home) {
            super.onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setImage() {

        if (mSetImageAsyncTask != null
                && mSetImageAsyncTask.getStatus() == AsyncTask.Status.PENDING) {
            mSetImageAsyncTask.execute();
        } else if (mSetImageAsyncTask == null
                || mSetImageAsyncTask.getStatus() == AsyncTask.Status.FINISHED) {
            mSetImageAsyncTask = new setImageAsyncTask();
            mSetImageAsyncTask.execute();
        }
    }

    private void initImageLoader() {
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisc().imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
        ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(
                this).defaultDisplayImageOptions(defaultOptions).memoryCache(
                new WeakMemoryCache());

        ImageLoaderConfiguration config = builder.build();
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(config);
    }


    // Method to share any image.
    private void shareImage() {
        Intent share = new Intent(Intent.ACTION_SEND);

        // If you want to share a png image only, you can do:
        // setType("image/png"); OR for jpeg: setType("image/jpeg");
        share.setType("image/*");

        // Make sure you put example png image named myImage.png in your
        // directory


        Log.d("PATH", "PATH:" + path);

        File imageFileToShare = new File(path);
        Uri uri = Uri.fromFile(imageFileToShare);
        //share.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=kailash.natural.wallpaper");
        share.putExtra(Intent.EXTRA_STREAM, uri);

        startActivity(Intent.createChooser(share, "Share Image!"));
    }

    // Method to share any image.
    private void shareLink(View v) {
        Intent share = new Intent(Intent.ACTION_SEND);

        // If you want to share a png image only, you can do:
        // setType("image/png"); OR for jpeg: setType("image/jpeg");
        share.setType("image/*");

        // Make sure you put example png image named myImage.png in your
        // directory


        Log.d("", "PATH:" + path);

        File imageFileToShare = new File(path);
        Uri uri = Uri.fromFile(imageFileToShare);
        share.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=btech.natural.wallpaper");
        share.putExtra(Intent.EXTRA_STREAM, uri);

        startActivity(Intent.createChooser(share, "Share Image!"));
    }

    /**
     * Called when leaving the activity
     */
    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    /**
     * Called when returning to the activity
     */
    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    @Override
    public void onDestroy() {

        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.activity_base_actionbar_ivNext:
                saveBitmap(src);
                break;
            case R.id.tvShareLink:
                saveShareBitmap(src);
                break;

            default:
                break;
        }

    }

    private void LoadFullAd() {
        try {

            interstitial = new InterstitialAd(getApplicationContext());
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    // Create the InterstitialAd and set the adUnitId.

                    // Defined in res/values/strings.xml
                    //interstitial.setAdUnitId("ca-app-pub-4489720276000590/9232166860");
                    interstitial.setAdUnitId(getString(R.string.fulladd_id));
                    // Request for Ads
                    AdRequest adRequest = new AdRequest.Builder().addTestDevice("E4195931F1BE473915004D979ED94A8E").build();
                    //AdRequest adRequest = new AdRequest.Builder().build();
                    interstitial.loadAd(adRequest);


                    // Prepare an Interstitial Ad Listener

                    interstitial.setAdListener(new AdListener() {
                        public void onAdLoaded() {

                            displayInterstitial();
                        }

                        @Override
                        public void onAdFailedToLoad(int errorCode) {
                            Log.e("Full", "Full ADS Error" + errorCode);
                            super.onAdFailedToLoad(errorCode);

                        }

                    });


                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void displayInterstitial() {
        // If Ads are loaded, show Interstitial else show nothing.
        if (interstitial.isLoaded()) {
            Log.e("Full", "Full ADS ");
            interstitial.show();
        }
    }


    private void LoadBanerAd() {
        try {

            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    mAdView = (AdView) findViewById(R.id.ad_view);

                    AdRequest adRequest = new AdRequest.Builder()
                            .addTestDevice("E4195931F1BE473915004D979ED94A8E")
                            .build();
                    //AdRequest.Builder.addTestDevice("E4195931F1BE473915004D979ED94A8E")

                    mAdView.loadAd(adRequest);

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

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

            Toast.makeText(getApplicationContext(), "Save Successfully.", Toast.LENGTH_LONG).show();

            Intent intent = new Intent(this, GallaryCollageActivity.class);
            startActivity(intent);


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void saveShareBitmap(Bitmap bitmap)
    {
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


            //Log.d("Constant.PICURE_PATH","Constant.PICURE_PATH=="+Constant.PICURE_PATH);

            Intent share = new Intent(Intent.ACTION_SEND);
            //share.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            share.setType("image/*");
            File imageFileToShare = new File(Constant.PICURE_PATH);
            Uri uri = Uri.fromFile(imageFileToShare);
            share.putExtra(Intent.EXTRA_STREAM, uri);
            startActivity(Intent.createChooser(share, "Share Image!"));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    /****************************************************************************
     * mSetImageAsyncTask
     *
     * @CreatedDate:
     * @ModifiedBy: not yet
     * @ModifiedDate: not yet
     * @purpose:This Class Use to Create GlcCard With Perameter And Return
     * UserId
     ***************************************************************************/

    private class setImageAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... params) {
            try {


            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            imageLoader.displayImage("file://" + path, imgOrange);


        }
    }


}
