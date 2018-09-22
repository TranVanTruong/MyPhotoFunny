package com.btech.funnyphoto;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;

import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * *************************************************************************
 * TemplateAplication
 *
 * @CreatedDate:
 * @ModifiedBy: not yet
 * @ModifiedDate: not yet
 * @purpose:This application class to set application level variable and method which
 * used through-out application
 * <p/>
 * *************************************************************************
 */

public class TemplateAplication extends Application {


    private static TemplateAplication mInstance;
    public ImageLoader imageLoader;
    private SharedPreferences sharedPreferences;
    private DisplayImageOptions imageOption;

    public static TemplateAplication getmInstance() {
        return mInstance;
    }

    public static void setmInstance(TemplateAplication mInstance) {
        TemplateAplication.mInstance = mInstance;
    }

    /**
     * Called when application start
     */
    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        initImageLoader(getApplicationContext());

        sharedPreferences = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);


    }

    public ImageLoader getImageLoader() {
        return imageLoader;
    }

    public void setImageLoader(ImageLoader imageLoader) {
        this.imageLoader = imageLoader;
    }

    /**
     * public method to initialize image loader
     *
     * @param context
     */
    public void initImageLoader(Context context) {

        final ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext()).threadPoolSize(5).threadPriority(Thread.MAX_PRIORITY - 2)
                .memoryCacheSize(15 * 1024 * 1024).denyCacheImageMultipleSizesInMemory().discCacheFileCount(50).denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new HashCodeFileNameGenerator()).build();

        imageLoader = ImageLoader.getInstance();
        imageLoader.init(config);
    }

    public DisplayImageOptions getImageOption() {
        if (imageOption != null) {

            imageOption = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisc(true).considerExifParams(true).bitmapConfig(Bitmap.Config.RGB_565)
                    .showStubImage(R.drawable.ic_launcher).showImageForEmptyUri(R.drawable.ic_launcher).showImageOnFail(R.drawable.ic_launcher).build();
        }

        return imageOption;
    }


    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    public void setSharedPreferences(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public void savePreferenceDataString(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public void savePreferenceDataBoolean(String key, Boolean value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public void savePreferenceDataInt(String key, int value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }


    /**
     * Call when application is close
     */
    @Override
    public void onTerminate() {
        super.onTerminate();
        if (mInstance != null) {
            mInstance = null;
        }
    }


}
