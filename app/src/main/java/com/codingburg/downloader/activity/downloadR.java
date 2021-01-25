package com.codingburg.downloader.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.codingburg.downloader.APIHelpers.Utils;
import com.codingburg.downloader.Models.ApiModel;
import com.codingburg.downloader.Models.ResultModel;
import com.codingburg.downloader.R;
import com.codingburg.downloader.adapters.ResultHolderAdapter;
import com.codingburg.downloader.interfaces.AsyncResponse;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class download_result extends AppCompatActivity {

    Context c;
    RecyclerView result_recycler_view;
    ShimmerFrameLayout shimmer_view_result_container;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.download_result);
        Bundle bundle = getIntent().getExtras();
        String URL ="";

        try {
            URL=onSharedIntent();
        }
        catch (Exception ex)
        {}
        if(URL.equals("")){
            URL = bundle.getString("url");
        }

      /*  if (Utils.IsContainsRistriced(URL)){
            Toasty.error(this, this.getResources().getString(R.string.Copyrightcontent), Toast.LENGTH_SHORT, true).show();
            onBackPressed();
        }*/

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        c=this;
        result_recycler_view=findViewById(R.id.result_recycler_view);
        shimmer_view_result_container=findViewById(R.id.shimmer_view_result_container);
        shimmer_view_result_container.startShimmer();
        DownloadNow(URL);
    }

    private String onSharedIntent()
    {
        Intent receiverdIntent = getIntent();
        String receivedAction = receiverdIntent.getAction();
        String receivedType = receiverdIntent.getType();
        if (receivedAction.equals(Intent.ACTION_SEND))
        {
            if (receivedType.startsWith("text/"))
            {
                String receivedText = receiverdIntent
                        .getStringExtra(Intent.EXTRA_TEXT);
                if (receivedText != null) {
                   return CheckUrls(receivedText);
                }
            }
        }
        return "";
    }


    private String CheckUrls(String text){
        List<String> result= Utils.extractUrls(text);
        if(result.size()==0)
        {
            Toasty.error(download_result.this, "No Url Found in you input", Toast.LENGTH_SHORT, true).show();
            return "";
        }
        else
        {
           return result.get(0);
        }
    }

     void DownloadNow(String URL){
        new PostDataAsync().execute(URL);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    public class PostDataAsync extends AsyncTask<String, Void, String>{
        public AsyncResponse delegate = null;

        @Override
        protected String doInBackground(String... params) {
            String URL=params[0];
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            RequestBody body = RequestBody.create("{\"url\":\""+URL +"\"}",JSON );

            RequestBody formBody = new FormBody.Builder()
                    .add("url", URL)
                    .build();

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url( Utils.APIURL +"/services/downloader_api.php")
                    .post( formBody)
                    .build();
            try {
                Response response = client.newCall(request ).execute();
                return response.body().string();
            } catch (IOException e) {
                    e.printStackTrace();
                StopShimmer();
                Toasty.error(c, c.getResources().getString(R.string.DownloadFailed), Toast.LENGTH_SHORT, true).show();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String b ){
                try {
                    ApiModel apiModel =new ApiModel();
                    List<ResultModel> videoResultList=new ArrayList<>();
                    ResultModel VideoResult;

                    JSONObject bObject=new JSONObject(b);
                    JSONArray vArray=bObject.getJSONArray("VideoResult");
                    Boolean IsExtirnal=false;
                    try {
                        IsExtirnal=bObject.getBoolean("InternalDownload");
                    }catch (Exception ex){}
                    for (int i = 0; i < vArray.length(); i++){
                        JSONObject cArrayObject=vArray.getJSONObject(i);
                        VideoResult=new ResultModel();
                        VideoResult.setAudioUrl( cArrayObject.getString("AudioUrl") );
                        VideoResult.setFileSize( cArrayObject.getString("FileSize") );
                        VideoResult.setQuality( cArrayObject.getString("Quality") );
                        VideoResult.setServiceName( cArrayObject.getString("ServiceName") );
                        VideoResult.setTitle( cArrayObject.getString("Title") );
                        VideoResult.setVideoUrl( cArrayObject.getString("VideoUrl") );
                        VideoResult.setThumbnil( cArrayObject.getString("thumbnil") );
                        videoResultList.add(VideoResult);
                    }
                    apiModel.setVideoResult(videoResultList);
                    apiModel.setStatus(bObject.getString("Status"));


                    ResultHolderAdapter adapter=new ResultHolderAdapter( c,videoResultList,IsExtirnal,download_result.this) ;
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(c );
                    result_recycler_view.setLayoutManager(mLayoutManager);
                    result_recycler_view.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                    StopShimmer();
                }
                catch (Exception ex){
                    Toasty.error(c, c.getResources().getString(R.string.NoVideoFound), Toast.LENGTH_SHORT, true).show();
                    StopShimmer();
                    onBackPressed();
                }
        }

    }

    private void StopShimmer(){
        shimmer_view_result_container.stopShimmer();
        shimmer_view_result_container.setVisibility(View.GONE);
    }
}