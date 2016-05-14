package com.maxml.exphotoview.ui.adapter;

import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.maxml.exphotoview.R;
import com.maxml.exphotoview.entity.Photo;
import com.maxml.exphotoview.util.ImageViewHelper;
import com.maxml.exphotoview.util.PhotoViewConstants;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by Maxml on 14.05.2016.
 */
public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.MyViewHolder> {

    private List<Photo> photos;
    private Handler handler;

    public PhotoAdapter(List<Photo> photos, Handler handler) {
        this.photos = photos;
        this.handler = handler;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_photo_details, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Photo photo = photos.get(position);
        holder.author.setText(photo.getUser().getUsername());
        holder.camera.setText(photo.getCamera());
        holder.photoName.setText(photo.getName());
        getImageView(holder.photo, photo.getUrl());

        if (position >= getItemCount() - 1) {
            handler.sendEmptyMessage(PhotoViewConstants.LOAD_MORE_EVENT);
        }
    }

    private void getImageView(ImageView view, String url) {
        ImageLoader.getInstance().displayImage(url, view);
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView author, photoName, camera;
        private ImageView photo;

        public MyViewHolder(View view) {
            super(view);

            author = (TextView) view.findViewById(R.id.item_author);
            camera = (TextView) view.findViewById(R.id.item_camera);
            photoName = (TextView) view.findViewById(R.id.item_photo_name);
            photo = (ImageView) view.findViewById(R.id.item_photo_image);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Message message = handler.obtainMessage();
            message.what = PhotoViewConstants.POSITION_RESULT;
            message.arg1 = getAdapterPosition();

            handler.sendMessage(message);
        }
    }


}