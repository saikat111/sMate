package com.codingburg.downloader.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.codingburg.downloader.BuildConfig;
import com.codingburg.downloader.Models.ModelVideo;
import com.codingburg.downloader.R;
import com.codingburg.downloader.interfaces.OnFileDelete;
import com.bumptech.glide.Glide;
import java.io.File;
import java.net.URLConnection;
import java.util.ArrayList;


public class VideoFolderAdapter extends RecyclerView.Adapter<VideoFolderAdapter.ViewHolder> {

    ArrayList<ModelVideo> al_video;
    Context context;
    Activity activity;
    private OnFileDelete onFileDelete;

    public void setOnBluetoothDeviceClickedListener(OnFileDelete l) {
        onFileDelete = l;
    }

    public VideoFolderAdapter(Context context, ArrayList<ModelVideo> al_video, Activity activity) {

        this.al_video = al_video;
        this.context = context;
        this.activity = activity;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView iv_image;
        RelativeLayout rl_select;

        public ViewHolder(View v) {
            super(v);
            iv_image = (ImageView) v.findViewById(R.id.iv_image);
            rl_select = (RelativeLayout) v.findViewById(R.id.rl_select);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_videos, parent, false);
        ViewHolder viewHolder1 = new ViewHolder(view);
        return viewHolder1;
    }

    @Override
    public void onBindViewHolder(final ViewHolder Vholder, final int position) {
        Glide.with(context).load("file://" + al_video.get(position).getStr_thumb())
                .skipMemoryCache(false)
                .into(Vholder.iv_image);
        Vholder.rl_select.setBackgroundColor(Color.parseColor("#FFFFFF"));
        Vholder.rl_select.setAlpha(0);
        Vholder.rl_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(context, view);
                popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        String SSd= (String) item.getTitle();
                        if (item.getTitle().toString().equals( context.getResources().getString(R.string.Play) )) {
                              Intent intent = new Intent(Intent.ACTION_VIEW);
                              intent.setDataAndType(Uri.parse(al_video.get(position).getStr_path()), "video/mp4");
                              activity.startActivity(intent);
                        }
                        else if (item.getTitle().toString().equals(context.getResources().getString(R.string.Share))) {

                            File file = new File(al_video.get(position).getStr_path()).getAbsoluteFile();
                            Uri uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file);
                            Intent intentShareFile = new Intent(Intent.ACTION_SEND);
                            intentShareFile.setType(URLConnection.guessContentTypeFromName(file.getName()));
                            intentShareFile.putExtra(Intent.EXTRA_STREAM,uri);
                            activity.startActivity(Intent.createChooser(intentShareFile, context.getResources().getString(R.string.ShareUsing)));
                        }
                        else if (item.getTitle().toString().equals(context.getResources().getString(R.string.Delete))) {
                            if (onFileDelete != null) {
                                onFileDelete.onDeleteButtonClicked(al_video.get(position).getStr_path());
                            }
                        }
                        return true;
                    }
                });
                popup.show();

            }
        });

        Vholder.rl_select.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {

        return al_video.size();
    }

}

