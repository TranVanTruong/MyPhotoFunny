package com.btech.funnyphoto.Activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.btech.funnyphoto.R;
import com.btech.funnyphoto.adapter.RecyclerViewFrameAdapter;
import com.btech.funnyphoto.collageView.MultiTouchListener;
import com.btech.funnyphoto.model.TemplateListModel;
import com.btech.funnyphoto.utils.Constant;
import com.btech.funnyphoto.utils.FileUtils;
import com.btech.funnyphoto.view.StickerView;
import com.luseen.luseenbottomnavigation.BottomNavigation.BottomNavigationItem;
import com.luseen.luseenbottomnavigation.BottomNavigation.BottomNavigationView;
import com.luseen.luseenbottomnavigation.BottomNavigation.OnBottomNavigationItemClickListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;


@SuppressLint("NewApi")
public class EditActivity extends AppCompatActivity implements OnClickListener, RecyclerViewFrameAdapter.OnItemClickListener
{


    public BottomNavigationView bottomNavigationView;
    private RelativeLayout layBack;
    private LinearLayout framePanel;
    private ImageView imgSelectedImage;


    private FrameLayout flMain;
    private String imagePath = "";
    private String[] IMAGE;

    private ArrayList<View> mViews;
    private Animation animation;
    private Typeface typeface;
    private Bitmap src;
    private ArrayList<TemplateListModel> framArrayList;
    private RecyclerView recyclerView;
    private RecyclerViewFrameAdapter adapterFrame;


    private String folderPath = "/MyPhotoCollage";
    private String imageName = "MyPhotoCollage";
    private File mFolder;
    private String stickerPath;

    private StickerView mCurrentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_new);
        init();
        setupToolbar();
        makeFolder();


        src = BitmapFactory.decodeFile(Constant.PICURE_PATH);
        imgSelectedImage.setImageBitmap(src);
        imgSelectedImage.setOnTouchListener(new MultiTouchListener());

    }

    private void setupToolbar() {
        // Initializing Toolbar and setting it as the actionbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_menubar_toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        typeface = Typeface.createFromAsset(getAssets(), "jumping_running.ttf");
        TextView mTitle = (TextView) toolbar.findViewById(R.id.actionbar_tvTitle);
        ImageView ivDone = (ImageView) toolbar.findViewById(R.id.activity_base_actionbar_ivNext);
        ivDone.setImageResource(R.drawable.ic_action_done);
        ivDone.setVisibility(View.VISIBLE);
        ivDone.setOnClickListener(this);
        //ivDone.setOnClickListener(this);
        mTitle.setTypeface(typeface);
        mTitle.setVisibility(View.VISIBLE);
        mTitle.setText("Face Changer Photo");

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

    private void init() {

        //Action bar color
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.thirdColor));
        }
        mViews = new ArrayList<>();
        flMain = (FrameLayout) findViewById(R.id.flMain);
        imagePath = Environment.getExternalStorageDirectory() + File.separator + "memoji" + File.separator + "memoji" + System.currentTimeMillis() + ".jpg".trim();
        imgSelectedImage = (ImageView) findViewById(R.id.activity_edit_img_selected_image);




        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigation);
        framePanel = (LinearLayout) findViewById(R.id.activity_edit_hsv_frame_panel);
        layBack = (RelativeLayout) findViewById(R.id.activity_edit_rl_convertview);
        animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.layout);
        setUpNavigationView();


    }

    private void setUpNavigationView() {

        int[] image = {R.drawable.frameic, R.drawable.ic_effects,
                R.drawable.ic_text, R.drawable.ic_sticker, R.drawable.ic_rotate};

        int[] color = {ContextCompat.getColor(this, R.color.thirdColor), ContextCompat.getColor(this, R.color.thirdColor),
                ContextCompat.getColor(this, R.color.thirdColor), ContextCompat.getColor(this, R.color.thirdColor), ContextCompat.getColor(this, R.color.thirdColor)};

        BottomNavigationItem bottomNavigationItem = new BottomNavigationItem
                (getString(R.string.lab_tarban), color[0], image[0]);
        BottomNavigationItem bottomNavigationItem1 = new BottomNavigationItem
                (getString(R.string.lab_hair), color[1], image[1]);
        BottomNavigationItem bottomNavigationItem2 = new BottomNavigationItem
                (getString(R.string.lab_moustache), color[2], image[2]);
        BottomNavigationItem bottomNavigationItem3 = new BottomNavigationItem
                (getString(R.string.lab_ear), color[3], image[3]);
//        BottomNavigationItem bottomNavigationItem4 = new BottomNavigationItem
//                (getString(R.string.lab_tarban), color[4], image[4]);


        bottomNavigationView.addTab(bottomNavigationItem);
        bottomNavigationView.addTab(bottomNavigationItem1);
        bottomNavigationView.addTab(bottomNavigationItem2);
        bottomNavigationView.addTab(bottomNavigationItem3);
        //bottomNavigationView.addTab(bottomNavigationItem4);


        framePanel.setVisibility(View.VISIBLE);


        bottomNavigationView.setOnBottomNavigationItemClickListener(new OnBottomNavigationItemClickListener() {
            @Override
            public void onNavigationItemClick(int index) {
                switch (index)
                {
                    case 0:
                        new setRecyclerAdapter(getString(R.string.title_tarban)).execute();
                        break;
                    case 1:
                        new setRecyclerAdapter(getString(R.string.title_hair)).execute();
                        break;
                    case 2:
                        new setRecyclerAdapter(getString(R.string.title_moutch)).execute();
                        break;
                    case 3:
                        new setRecyclerAdapter(getString(R.string.title_ear)).execute();
                        break;
//                    case 4:
//
//                        break;


                }
            }
        });

        initFrameRecyclerView();
    }

    private void initFrameRecyclerView() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager layoutManagerStiker = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        framArrayList = new ArrayList<TemplateListModel>();

        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.setLayoutManager(layoutManager);

        new setRecyclerAdapter(getString(R.string.title_tarban)).execute();


    }

    @Override
    public void onItemClick(View view, TemplateListModel viewModel) {

        String framepath = viewModel.getImage();
        stickerPath = framepath;
        addStickerView();



        // Log.d("onItemClick", "==framepath=" + framepath);

       /* if (framepath.equalsIgnoreCase("none")) {
            imgSelectedFrameImage.setImageResource(0);
        } else {
            int imageResource = getResources().getIdentifier(framepath, "drawable", getPackageName());
            imgSelectedFrameImage.setImageResource(imageResource);

        }
*/

    }


    private void addStickerView() {
        final StickerView stickerView = new StickerView(this);
        //stickerView.setImageResource(R.mipmap.ic_cat);
        // SET YOUR IMAGER SOURCE TO YOUR NEW IMAGEVIEW HERE
        //int imageResource = getApplicationContext().getResources().getIdentifier(stickerPath, null, getApplicationContext().getPackageName());
        int imageResource = getResources().getIdentifier(stickerPath, "drawable", getPackageName());

        stickerView.setImageResource(imageResource);
        stickerView.setOperationListener(new StickerView.OperationListener() {
            @Override
            public void onDeleteClick() {
                mViews.remove(stickerView);
                layBack.removeView(stickerView);
            }

            @Override
            public void onEdit(StickerView stickerView) {

                mCurrentView.setInEdit(false);
                mCurrentView = stickerView;
                mCurrentView.setInEdit(true);
            }

            @Override
            public void onTop(StickerView stickerView) {
                int position = mViews.indexOf(stickerView);
                if (position == mViews.size() - 1) {
                    return;
                }
                StickerView stickerTemp = (StickerView) mViews.remove(position);
                mViews.add(mViews.size(), stickerTemp);
            }
        });
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        layBack.addView(stickerView, lp);
        mViews.add(stickerView);
        setCurrentEdit(stickerView);
    }


    private void setCurrentEdit(StickerView stickerView) {
        if (mCurrentView != null) {
            mCurrentView.setInEdit(false);
        }

        mCurrentView = stickerView;
        stickerView.setInEdit(true);
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

    private void setFullAdapter(ArrayList<TemplateListModel> arrayList) {
        adapterFrame = new RecyclerViewFrameAdapter(getApplicationContext(), arrayList);
        adapterFrame.setOnItemClickListener(this);
        recyclerView.setAdapter(adapterFrame);


    }



    @Override
    protected void onDestroy() {
        super.onDestroy();


    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_base_actionbar_ivNext:


                if (mCurrentView != null)
                    mCurrentView.setInEdit(false);

                Bitmap bitmap = Bitmap.createBitmap(layBack.getWidth(), layBack.getHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                layBack.draw(canvas);

                String iamgePath = FileUtils.saveBitmapToLocal(bitmap, this);
                Intent intent = new Intent(this, SharingActivity.class);
                intent.putExtra("ImagePath", iamgePath);
                startActivity(intent);

                break;
        }

    }



    private class setRecyclerAdapter extends AsyncTask<Void, Void, Void> {

        private ProgressDialog dialog;
        private String[] imageArry;
        private TemplateListModel mTemplateListModel;
        private String selected_tab;

        public setRecyclerAdapter(String title)
        {
            this.selected_tab=title;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();


            if(selected_tab.equalsIgnoreCase(getString(R.string.title_ear)))
            {
                imageArry = getResources().getStringArray(R.array.templates_ear);
            }
            else if(selected_tab.equalsIgnoreCase(getString(R.string.title_hair)))
            {
                imageArry = getResources().getStringArray(R.array.templates_hair);
            }
            else if(selected_tab.equalsIgnoreCase(getString(R.string.title_moutch)))
            {
                imageArry = getResources().getStringArray(R.array.templates_moustche);
            }
            else if(selected_tab.equalsIgnoreCase(getString(R.string.title_tarban)))
            {
                imageArry = getResources().getStringArray(R.array.templates_tarban);
            }

           framArrayList = new ArrayList<TemplateListModel>();
            dialog = new ProgressDialog(EditActivity.this);
            dialog.setMessage("Saving");
            dialog.show();
           // Log.d("onPostExecute", "arrayList.length()" + imageArry.length);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

                if (imageArry.length > 0)
                {

                    for (int i = 0; i < imageArry.length; i++) {
                        mTemplateListModel = new TemplateListModel();
                        String path = imageArry[i];
                        String uri = path.split("res/drawable-xhdpi-v4/")[1];
                        uri = uri.replace(".png", "");
                       // Log.d("pathpathpath", "path===" + uri);
                        mTemplateListModel.setImage(uri);
                        framArrayList.add(mTemplateListModel);

                    }
                }


            } catch (Exception e) {
            }
            return null;

        }

        @Override
        protected void onPostExecute(Void result) {

           // Log.d("onPostExecute", "arrayList.size()" + framArrayList.size());
            dialog.dismiss();
            setFullAdapter(framArrayList);

        }


    }

}
