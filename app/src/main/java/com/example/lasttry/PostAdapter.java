package com.example.lasttry;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private ArrayList<Postitems> mpostlist;

    public static class PostViewHolder extends RecyclerView.ViewHolder{
        protected ImageView mImageview;
        protected TextView mEmailtv;
        protected TextView mCaptiontv;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            mImageview = itemView.findViewById(R.id.postimageview);
            mEmailtv = itemView.findViewById(R.id.postemailtextview);
            mCaptiontv = itemView.findViewById(R.id.captiontext);

        }
    }



    public PostAdapter(ArrayList<Postitems> postlist){
        mpostlist = postlist;
    }
    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_items, parent, false);
        PostViewHolder pvh = new PostViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Postitems currentitem = mpostlist.get(position);
        holder.mImageview.setImageBitmap(getBitmapFromURL(currentitem.getPostUrl()));
        holder.mEmailtv.setText(currentitem.getEmail());
        holder.mCaptiontv.setText(currentitem.getCaption());
    }

    @Override
    public int getItemCount() {
        return mpostlist.size();
    }


    public Bitmap getBitmapFromURL(String url){
        ImageDownloader task=new ImageDownloader();
        Bitmap myImage = null;
        try{
            myImage=task.execute(url).get();
        }catch (Exception e){
            e.printStackTrace();
        }
        return myImage;
    }

    public class ImageDownloader extends AsyncTask<String, Void , Bitmap> {

        @Override
        protected Bitmap doInBackground(String... urls) {
            try{
                URL url=new URL(urls[0]);
                HttpsURLConnection connection=(HttpsURLConnection) url.openConnection();
                connection.connect();
                InputStream in=connection.getInputStream();
                Bitmap myBitmap= BitmapFactory.decodeStream(in);
                return myBitmap;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}
