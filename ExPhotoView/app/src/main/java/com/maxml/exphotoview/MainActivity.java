package com.maxml.exphotoview;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.maxml.exphotoview.entity.Photo;
import com.maxml.exphotoview.ui.fragments.PhotoListFragment;
import com.maxml.exphotoview.util.ImageViewHelper;
import com.maxml.exphotoview.util.PhotoViewConstants;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    public static Set<Photo> photos = new HashSet<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_grid);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageViewHelper.configImageLoader(this);

        if (getFragmentManager().findFragmentByTag(PhotoViewConstants.FRAGMENT_PHOTO_LIST_TAG) == null)
            getFragmentManager().beginTransaction().
                    add(R.id.fragment_container, new PhotoListFragment(), PhotoViewConstants.FRAGMENT_PHOTO_LIST_TAG).commit();
    }

    public static List<Photo> getPhotos() {
        List<Photo> result = new ArrayList<>();
        result.addAll(photos);
        return result;
    }

    @Override
    public void onBackPressed() {
        Fragment current = getFragmentManager().findFragmentByTag(PhotoViewConstants.FRAGMENT_PHOTO_DETAILS_TAG);
        if (current != null) {
            getFragmentManager().beginTransaction().
                    replace(R.id.fragment_container, new PhotoListFragment(), PhotoViewConstants.FRAGMENT_PHOTO_LIST_TAG).commit();
            return;
        }

        super.onBackPressed();
    }
}
