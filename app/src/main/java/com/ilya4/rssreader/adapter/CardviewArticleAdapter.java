package com.ilya4.rssreader.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ilya4.rssreader.Entity;
import com.ilya4.rssreader.R;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

public class CardviewArticleAdapter extends RecyclerView.Adapter<CardviewArticleAdapter.ViewHolder> {

    private List<Entity> items;
    private Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private CardView cardView;

        public ViewHolder(CardView cardView) {
            super(cardView);
            this.cardView = cardView;
        }
    }

    public CardviewArticleAdapter(List<Entity> items, Context context) { //sortedList in MainActivity
        this.items = items;
        this.context = context;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        CardView cardView = holder.cardView;
        ImageView imageView = cardView.findViewById(R.id.image_article);

        TextView titleView = cardView.findViewById(R.id.title_article);
        titleView.setText(items.get(position).getTitle());
        new DownloadImageTask(imageView).execute(items.get(position).getImage());

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Choose Action");
                builder.setMessage(items.get(position).getTitle());
                builder.setPositiveButton("Open in Browser", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                                Uri.parse(items.get(position).getLink()));
                        context.startActivity(browserIntent);
                    }
                });
                builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_article,
                parent, false);
        return new ViewHolder(cardView);
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap>{

        ImageView imageView;

        public DownloadImageTask(ImageView imageView) {
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(String... urls) {
            Bitmap bm = null;
            InputStream in;
            try {
                in = new URL(urls[0]).openStream();
                bm = BitmapFactory.decodeStream(in);
                in.close();
            }catch (Exception e) {
                Log.e("Error", "LoadImage from: " + urls[0]);
            }


            return bm;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            imageView.setImageBitmap(bitmap);
            if (bitmap==null){
                imageView.setVisibility(View.GONE);
            }else imageView.setVisibility(View.VISIBLE);

        }
    }
}
