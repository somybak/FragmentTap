package com.example.somy.fragmenttap;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.bumptech.glide.Glide;
import com.example.somy.fragmenttap.FiveFragment.OnListFragmentInteractionListener;
import com.example.somy.fragmenttap.dummy.DummyContent.DummyItem;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> {
    //TAG
    private final Context context;
    private final List<String> datas = new ArrayList<>();
    private final OnListFragmentInteractionListener mListener;

    public MyItemRecyclerViewAdapter(Context context, OnListFragmentInteractionListener listener) {
        this.context = context;

        ContentResolver resolver = context.getContentResolver();
        //1. define ata Uri
        Uri target = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String projection[] = {MediaStore.Images.Thumbnails.DATA}; //data : image uri column

        Cursor cursor = resolver.query(target, projection, null, null, null);
        if(cursor !=null) {
            while (cursor.moveToNext()) {
                String uriString = cursor.getString(0);
             //   Log.d(TAG, "=========="+uriString);
                datas.add(uriString);
            }
            cursor.close();
        }
        mListener = listener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        holder.imageUri = datas.get(position);
      //  holder.imageView.setImageURI(holder.imageUri); bumb~ too much big data
        Glide.with(context)
                .load(holder.imageUri)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public String imageUri;

        public ViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.imageView);
            imageUri = null;
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                     //클릭시 큰이미지 보여주기
                    Intent intent = new Intent(context,DetailActivity.class);
                    context.startActivity(intent);
                }
            });
        }
    }
}
