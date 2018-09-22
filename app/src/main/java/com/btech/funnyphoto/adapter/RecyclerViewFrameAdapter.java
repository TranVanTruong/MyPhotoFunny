package com.btech.funnyphoto.adapter;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.btech.funnyphoto.R;
import com.btech.funnyphoto.model.TemplateListModel;
import com.squareup.picasso.Picasso;

import java.util.List;


public class RecyclerViewFrameAdapter extends RecyclerView.Adapter<RecyclerViewFrameAdapter.ViewHolder> implements View.OnClickListener {

    private List<TemplateListModel> items;
    private OnItemClickListener onItemClickListener;
    private Context mContext;

    public RecyclerViewFrameAdapter(Context context, List<TemplateListModel> items) {
        this.items = items;
        this.mContext = context;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_frame_recycler, parent, false);
        v.setOnClickListener(this);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TemplateListModel item = items.get(position);
        holder.image.setImageBitmap(null);

        int imageResource = mContext.getResources().getIdentifier(item.getImage(), "drawable", mContext.getPackageName());
        Picasso.with(mContext).load(imageResource).into(holder.image);
        holder.itemView.setTag(item);


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

    public interface OnItemClickListener {

        void onItemClick(View view, TemplateListModel viewModel);

    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;


        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);

        }
    }
}
