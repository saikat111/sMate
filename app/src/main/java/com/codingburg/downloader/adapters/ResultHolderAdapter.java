package com.codingburg.downloader.adapters;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.codingburg.downloader.APIHelpers.Utils;
import com.codingburg.downloader.activity.LoadingDialog;
import com.codingburg.downloader.Models.VideoResultModel;
import com.codingburg.downloader.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.concurrent.TimeUnit;

import es.dmoral.toasty.Toasty;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ResultHolderAdapter extends RecyclerView.Adapter <ResultHolderAdapter.MyViewHolder> {
    private Context mContext;
    private boolean IsExtirnal=false;
    private Activity activity;
    private List<VideoResultModel> VideoResultModelList;
    LoadingDialog dialog;
    private InterstitialAd mInterstitialAd;

    public ResultHolderAdapter(Context mContext,List<VideoResultModel> VideoResultModelList,boolean IsExtirnal,Activity mActivity){
        this.mContext=mContext;
        this.IsExtirnal=IsExtirnal;
        this.activity=mActivity;
        this.VideoResultModelList=VideoResultModelList;
        mInterstitialAd = new InterstitialAd(mContext);
        mInterstitialAd.setAdUnitId(mContext.getResources().getString(R.string.AdmobInterstitial) );
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
    }


    @NonNull
    @Override
    public ResultHolderAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.download_result_list, parent, false);
        return new ResultHolderAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final VideoResultModel result=VideoResultModelList.get(position);
        Glide.with(mContext).load(result.getThumbnil()) .placeholder(R.drawable.thumbnil_preview).into(holder.iv_poster);
        final String Title=   result.getServiceName()+ " - " + result.getTitle();
        holder.tv_film_name.setText(Title);
        String FileSize=mContext.getResources().getString(R.string.Sizeunknown);
        if((! result.getFileSize().equals("null")) && (! result.getFileSize().equals("")) ){
            FileSize=result.getFileSize();
        }
        holder.txtVidSize.setText(FileSize);
        holder.btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( (!result.getVideoUrl().equals("") ) && (result.getVideoUrl() !=null ) && (!result.getVideoUrl().equals("null") )  )
                {
                    if(IsExtirnal==false){
                        try
                        {
                            String DownloadableURL=result.getVideoUrl();
                            StringBuilder sb = new StringBuilder();
                            sb.append(Utils.SanitizeName(Title));
                            sb.append(System.currentTimeMillis());
                            sb.append(".mp4");
                            String str3 = Utils.RootDirectory;
                            Utils.downloadBegan(DownloadableURL, str3,mContext , sb.toString());
                            Toasty.success(mContext, mContext.getResources().getString(R.string.DownloadingStarted), Toast.LENGTH_SHORT, true).show();
                            if (mInterstitialAd.isLoaded()) {
                                mInterstitialAd.show();
                            }
                        }
                        catch (Exception ex){
                            Toasty.error(mContext,  mContext.getResources().getString(R.string.ErrorwhileDownloading), Toast.LENGTH_SHORT, true).show();
                        }
                    }
                    else
                    {
                        dialog=new LoadingDialog(activity);
                        dialog.startLoadingDialog();
                        new LoadExtirnalLink().execute(result.getVideoUrl(),result.getServiceName());
                    }
                }
                else if( (!result.getAudioUrl().equals("") ) && (result.getAudioUrl() !=null ) && (!result.getAudioUrl().equals("null") )   )
                {
                    if(IsExtirnal==false){
                        try
                        {
                            String DownloadableURL=result.getAudioUrl();
                            StringBuilder sb = new StringBuilder();
                            sb.append(Utils.SanitizeName(Title));
                            sb.append(System.currentTimeMillis());
                            sb.append(".mp3");
                            String str3 = Utils.RootDirectory;
                            Utils.downloadBegan(DownloadableURL, str3,mContext , sb.toString());
                            Toasty.success(mContext, mContext.getResources().getString(R.string.DownloadingStarted), Toast.LENGTH_SHORT, true).show();
                        }
                        catch (Exception ex){
                            Toasty.error(mContext,  mContext.getResources().getString(R.string.ErrorwhileDownloading), Toast.LENGTH_SHORT, true).show();
                        }
                    }
                    else
                    {
                        dialog=new LoadingDialog(activity);
                        dialog.startLoadingDialog();
                        new LoadExtirnalLink().execute(result.getVideoUrl(),result.getServiceName());
                    }
                }

            }
        });
    }

    public class LoadExtirnalLink extends AsyncTask<String, Void, String>{

        public String _Title="";
        @Override
        protected String doInBackground(String... strings){
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(100, TimeUnit.SECONDS)
                    .writeTimeout(100, TimeUnit.SECONDS)
                    .readTimeout(300, TimeUnit.SECONDS)
                    .build();;


            try {
                String URL= URLEncoder.encode(strings[0],"UTF-8");
                _Title=strings[1];
                Request request = new Request.Builder()
                        .url(Utils.APIURL + "/download.php?type=v&url="+URL+"&title="+strings[1])
                        .get()
                        .build();
                Response response = null;
                try {
                    response = client.newCall(request ).execute();
                    return response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                dialog.dismisDialoig();
                Toasty.error(mContext,  mContext.getResources().getString(R.string.ErrorwhileDownloading), Toast.LENGTH_SHORT, true).show();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String b ){

           try
           {
               Document doc = Jsoup.parse(b);
               String DownloadURL=  doc.select("a").last().attr("href");
               DownloadURL= Utils.APIURL + ""+DownloadURL;
               String DownloadableURL=DownloadURL;
               StringBuilder sb = new StringBuilder();
               sb.append(Utils.SanitizeName(_Title));
               sb.append(System.currentTimeMillis());
               sb.append(".mp4");
               String str3 = Utils.RootDirectory;
               Utils.downloadBegan(DownloadableURL, str3,mContext , sb.toString());
               Toasty.success(mContext,  mContext.getResources().getString(R.string.DownloadingStarted), Toast.LENGTH_SHORT, true).show();
               dialog.dismisDialoig();
               if (mInterstitialAd.isLoaded()) {
                   mInterstitialAd.show();
               }
           }
           catch (Exception ex){
               dialog.dismisDialoig();
               Toasty.error(mContext,  mContext.getResources().getString(R.string.ErrorwhileDownloading), Toast.LENGTH_SHORT, true).show();
           }
        }
    }

    @Override
    public int getItemCount() {
        return VideoResultModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        public ImageView iv_poster;
        public TextView tv_film_name;
        public Button btnDownload;
        public TextView txtVidSize;


        public MyViewHolder(@NonNull View view) {
            super(view);
            iv_poster=view.findViewById(R.id.iv_poster);
            tv_film_name=view.findViewById(R.id.tv_film_name);
            btnDownload=view.findViewById(R.id.btnDownload);
            txtVidSize=view.findViewById(R.id.txtVidSize);
        }
    }
}
