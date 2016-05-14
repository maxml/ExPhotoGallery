package com.maxml.exphotoview.ui.fragments;


import android.app.Fragment;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.maxml.exphotoview.MainActivity;
import com.maxml.exphotoview.R;
import com.maxml.exphotoview.db.ServerApi;
import com.maxml.exphotoview.ui.adapter.PhotoAdapter;
import com.maxml.exphotoview.util.PhotoViewConstants;

/**
 * A simple {@link Fragment} subclass.
 */
public class PhotoListFragment extends Fragment {

    private RecyclerView photosView;
    private PhotoAdapter mAdapter;

    private ServerApi api;
    private int currentPage = 0;

    public PhotoListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        api = new ServerApi(listHandler);
        return inflater.inflate(R.layout.fragment_photo_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        api.getInfo(++currentPage);

        photosView = (RecyclerView) view.findViewById(R.id.recycler_view);

        photosView.setLayoutManager(new StaggeredGridLayoutManager(getDisplayColumns(), StaggeredGridLayoutManager.VERTICAL));
        photosView.setItemAnimator(new DefaultItemAnimator());
    }

    private boolean isTablet(Context context) {
        boolean xlarge = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == 4);
        boolean large = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK)
                == Configuration.SCREENLAYOUT_SIZE_LARGE);
        return (xlarge || large);
    }

    private boolean isLandscapeMode() {
        return getResources().getConfiguration().orientation == getResources().getConfiguration().ORIENTATION_LANDSCAPE;
    }

    private int getDisplayColumns() {
        int columnCount = 2;
        if (isTablet(getActivity()) || isLandscapeMode()) {
            columnCount = 3;
        }
        return columnCount;
    }

    private void updateAdapter() {
        mAdapter = new PhotoAdapter(MainActivity.getPhotos(), listHandler);
        photosView.setAdapter(mAdapter);
    }

    private Handler listHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case PhotoViewConstants.POSITION_RESULT:
                    getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            PhotoDetailsFragment.newInstance(msg.arg1), PhotoViewConstants.FRAGMENT_PHOTO_DETAILS_TAG).commit();
                    break;
                case PhotoViewConstants.SUCCESS_RESULT:
                    updateAdapter();
                    break;
                case PhotoViewConstants.ERROR_RESULT:
                    Toast.makeText(getActivity(), (String) msg.obj, Toast.LENGTH_SHORT).show();
                    break;
                case PhotoViewConstants.LOAD_MORE_EVENT:
                    api.getInfo(++currentPage);
                    break;
            }
        }
    };
}
