package com.example.app.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.app.R;
import com.example.app.widget.zoom.PhotoView;
import com.example.app.widget.zoom.PhotoViewAttacher;

/**
 * 单张图片显示Fragment
 */
public class ImageDetailFragment extends Fragment {
    private String mImageUrl;
    private PhotoView mImageView;
    private ProgressBar progressBar;
    private PhotoViewAttacher mAttacher;


    public static ImageDetailFragment newInstance(String imageUrl) {
        final ImageDetailFragment f = new ImageDetailFragment();
        final Bundle args = new Bundle();
        args.putString("url", imageUrl);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mImageUrl = getArguments() != null ? getArguments().getString("url") : null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.image_detail_fragment, container, false);
        mImageView = (PhotoView) v.findViewById(R.id.image);
        mAttacher = new PhotoViewAttacher(mImageView);
        mAttacher.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {
                getActivity().onBackPressed();
            }
        });
        progressBar = (ProgressBar) v.findViewById(R.id.loading);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Glide.with(getActivity())
                .load(mImageUrl)
                .apply(new RequestOptions()
                        .fitCenter())
                .into(mImageView);
        mAttacher.update();
    }
}