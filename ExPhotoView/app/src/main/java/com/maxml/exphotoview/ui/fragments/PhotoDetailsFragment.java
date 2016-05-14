package com.maxml.exphotoview.ui.fragments;


import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.maxml.exphotoview.MainActivity;
import com.maxml.exphotoview.R;
import com.maxml.exphotoview.entity.Photo;
import com.maxml.exphotoview.ui.elem.OnSwipeTouchListener;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PhotoDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PhotoDetailsFragment extends Fragment implements OnSwipeTouchListener.OnLeftRightListener {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String PHOTO_POSITION = "photoName-position";

    private TextView usernameView;
    private TextView photoNameView;
    private TextView cameraView;

    private ImageView photoView;

    private int position;
    private Photo photo;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment PhotoDetailsFragment.
     */
    public static PhotoDetailsFragment newInstance(int position) {
        PhotoDetailsFragment fragment = new PhotoDetailsFragment();
        Bundle args = new Bundle();
        args.putInt(PHOTO_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    public PhotoDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            position = getArguments().getInt(PHOTO_POSITION);
            photo = MainActivity.getPhotos().get(position);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_photo_details, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        usernameView = (TextView) view.findViewById(R.id.details_username);
        photoNameView = (TextView) view.findViewById(R.id.details_photo_name);
        cameraView = (TextView) view.findViewById(R.id.details_camera);
        photoView = (ImageView) view.findViewById(R.id.details_image);

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.details_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri bmpUri = getLocalBitmapUri(photoView);
                if (bmpUri != null) {
                    // Construct a ShareIntent with link to image
                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
                    shareIntent.setType("image/*");
                    // Launch sharing dialog for image
                    startActivity(Intent.createChooser(shareIntent, "Share Image:"));
                }
            }
        });

        updateViews();

        view.setOnTouchListener(new OnSwipeTouchListener(getActivity(), this));
    }

    private void updateViews() {
        usernameView.setText(photo.getUser().getUsername());
        photoNameView.setText(photo.getName());
        cameraView.setText(photo.getCamera());

        ImageLoader.getInstance().displayImage(photo.getUrl(), photoView);
    }

    @Override
    public void onLeft() {
        if (position == 0) {
            Toast.makeText(getActivity(), "Stop", Toast.LENGTH_SHORT).show();
            return;
        }
        position--;
        photo = MainActivity.getPhotos().get(position);
        updateViews();
    }

    @Override
    public void onRight() {
        if (position == MainActivity.getPhotos().size() - 1) {
            Toast.makeText(getActivity(), "Stop", Toast.LENGTH_SHORT).show();
            return;
        }
        position++;
        photo = MainActivity.getPhotos().get(position);
        updateViews();
    }

    private Uri getLocalBitmapUri(ImageView imageView) {
        // Extract Bitmap from ImageView drawable
        Drawable drawable = imageView.getDrawable();
        Bitmap bmp = null;
        if (drawable instanceof BitmapDrawable) {
            bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        } else {
            return null;
        }
        // Store image to default external storage directory
        Uri bmpUri = null;
        try {
            File file = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS), "share_image_" + System.currentTimeMillis() + ".png");
            file.getParentFile().mkdirs();
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }
}
