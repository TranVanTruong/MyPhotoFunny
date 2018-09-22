package com.btech.funnyphoto.Activities;

import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.btech.funnyphoto.AdmodData;
import com.btech.funnyphoto.R;
import com.btech.funnyphoto.adapter.RecyclerViewGalleryAdapter;
import com.btech.funnyphoto.model.TemplateListModel;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class GallaryCollageActivity extends AppCompatActivity implements RecyclerViewGalleryAdapter.OnItemClickListener {


    private static final int INTENT_REQUEST_GET_IMAGES = 3;
    private static final String TAG = "TedPicker";
    private ProgressDialog progress;
    private ArrayList<TemplateListModel> arrayList;
    private RecyclerView recyclerView;
    private RecyclerViewGalleryAdapter adapter;
    private Toolbar toolbar;
    private Typeface typeface;
    private String framepath = "";
    private ArrayList<Uri> image_uris = new ArrayList<Uri>();
    private ViewGroup mSelectedImagesContainer;
    private InterstitialAd interstitial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_template);

        initRecyclerView();
        setupToolbar();
        LoadFullAd();
        //new setRecyclerAdapter().execute();

    }

    @Override
    public void onEnterAnimationComplete() {
        super.onEnterAnimationComplete();
        new setRecyclerAdapter().execute();
        recyclerView.scheduleLayoutAnimation();
    }

    private void initRecyclerView() {

        arrayList = new ArrayList<TemplateListModel>();
        typeface = Typeface.createFromAsset(getAssets(), "jumping_running.ttf");

        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));


    }

    private String getMetadata(String path) {

        //Log.d("path","Path=="+path);

        String dateTime = "";

        try {
            ExifInterface exif = new ExifInterface(path);
            dateTime = exif.getAttribute(ExifInterface.TAG_DATETIME);


        } catch (IOException e) {
        }


        return dateTime;
    }

    private void setFullAdapter(ArrayList<TemplateListModel> arrayList) {
        adapter = new RecyclerViewGalleryAdapter(GallaryCollageActivity.this, arrayList);
        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);


    }

    private void setupToolbar() {
        // Initializing Toolbar and setting it as the actionbar
        toolbar = (Toolbar) findViewById(R.id.activity_menubar_toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();

        TextView mTitle = (TextView) toolbar.findViewById(R.id.actionbar_tvTitle);
        mTitle.setTypeface(typeface);
        mTitle.setVisibility(View.VISIBLE);
        mTitle.setText("My Gallery");

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

    @Override
    public void onItemClick(View view, TemplateListModel viewModel) {
        framepath = viewModel.getImage();

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
                    interstitial.setAdUnitId(AdmodData.Admod.interstial_ap_id);
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

    private class setRecyclerAdapter extends AsyncTask<Void, Void, Void> {

        private ProgressDialog dialog;

        private File file;
        private TemplateListModel mTemplateListModel;
        private File[] listFile;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            arrayList = new ArrayList<TemplateListModel>();
            dialog = new ProgressDialog(GallaryCollageActivity.this);
            dialog.setMessage("Saving...");
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

                // Locate the image folder in your SD Card
                file = new File(Environment.getExternalStorageDirectory().getPath() + "/MyPhotoCollage");

                if (file.isDirectory()) {
                    listFile = file.listFiles();

                    for (int i = 0; i < listFile.length; i++) {
                        mTemplateListModel = new TemplateListModel();
                        mTemplateListModel.setImage(listFile[i].getAbsolutePath());
                        arrayList.add(mTemplateListModel);


                        Log.d("file", "PATH==" + getMetadata("file://" + listFile[i].getAbsolutePath()));

                    }


                }

            } catch (Exception e) {
            }
            return null;

        }

        @Override
        protected void onPostExecute(Void result) {

            Log.d("onPostExecute", "arrayList.size()" + arrayList.size());
            dialog.dismiss();
            setFullAdapter(arrayList);


        }


    }


}
