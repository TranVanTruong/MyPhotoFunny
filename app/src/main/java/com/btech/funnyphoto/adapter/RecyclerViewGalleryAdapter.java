package com.btech.funnyphoto.adapter;

import android.app.WallpaperManager;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.btech.funnyphoto.Activities.GallaryCollageActivity;
import com.btech.funnyphoto.R;
import com.btech.funnyphoto.model.TemplateListModel;
import com.bumptech.glide.Glide;

import java.io.File;
import java.util.List;


public class RecyclerViewGalleryAdapter extends RecyclerView.Adapter<RecyclerViewGalleryAdapter.ViewHolder> implements View.OnClickListener {

    private List<TemplateListModel> items;
    private OnItemClickListener onItemClickListener;
    //private Context mContext;
    private GallaryCollageActivity mGallaryCollageActivity;

    public RecyclerViewGalleryAdapter(GallaryCollageActivity context, List<TemplateListModel> items) {
        this.items = items;
        this.mGallaryCollageActivity = context;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_new, parent, false);
        v.setOnClickListener(this);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final TemplateListModel item = items.get(position);
        holder.image.setImageBitmap(null);
        int imageResource = mGallaryCollageActivity.getResources().getIdentifier(item.getImage(), null, mGallaryCollageActivity.getPackageName());
        //Picasso.with(mContext).load(item.getImage()).into(holder.image);
        holder.itemView.setTag(item);
        Glide.with(mGallaryCollageActivity).load("file://" + item.getImage()).fitCenter().into(holder.image);


        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                File file = new File(item.getImage());
                boolean deleted = file.delete();

                if (deleted) {
                    Toast.makeText(mGallaryCollageActivity.getApplicationContext(), "Deleted Successfully.", Toast.LENGTH_LONG).show();

                    items.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, items.size());

                }
            }
        });


        holder.ivShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                shareImage(item.getImage());

            }
        });


        holder.tvdateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setWallPaper(item.getImage());
            }
        });


    }

    private void setWallPaper(String image) {

        try {

            WallpaperManager wallpaperManager = WallpaperManager.getInstance(mGallaryCollageActivity.getApplicationContext());
            wallpaperManager.setBitmap(BitmapFactory.decodeFile(image));

            Toast.makeText(mGallaryCollageActivity.getApplicationContext(), "Wallpaper set Successfully", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void onClick(final View v) {
        // Give some time to the ripple to finish the effect
        if (onItemClickListener != null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    onItemClickListener.onItemClick(v, (TemplateListModel) v.getTag());
                }
            }, 200);
        }
    }

    private void shareImage(final String imagePath) {
        Intent share = new Intent(Intent.ACTION_SEND);
        //share.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        share.setType("image/*");
        File imageFileToShare = new File(imagePath);
        Uri uri = Uri.fromFile(imageFileToShare);
        share.putExtra(Intent.EXTRA_STREAM, uri);
        mGallaryCollageActivity.startActivity(Intent.createChooser(share, "Share Image!"));
    }

    public interface OnItemClickListener {

        void onItemClick(View view, TemplateListModel viewModel);

    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public ImageView ivShare;
        public ImageView ivDelete;
        private TextView tvdateTime;


        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
            ivShare = (ImageView) itemView.findViewById(R.id.ivShare);
            ivDelete = (ImageView) itemView.findViewById(R.id.ivdelete);
            ivDelete = (ImageView) itemView.findViewById(R.id.ivdelete);
            tvdateTime = (TextView) itemView.findViewById(R.id.tvDateTime);

        }
    }
}
