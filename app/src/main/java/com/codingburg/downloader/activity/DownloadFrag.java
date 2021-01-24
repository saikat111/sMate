package com.codingburg.downloader.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.codingburg.downloader.APIHelpers.Utils;
import com.codingburg.downloader.Models.SuppotedSites;
import com.codingburg.downloader.Models.SuppotedSitesHolder;
import com.codingburg.downloader.R;
import com.codingburg.downloader.adapters.HolderAdapter;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class DownloadFrag extends Fragment {


    RecyclerView recycler_view;
    ShimmerFrameLayout Shimmercontainer;
    RelativeLayout MainRelativeLayout;
    EditText et_text;
    ImageView imDownload,imClear;


    public DownloadFrag() {
    }


    public static DownloadFrag newInstance(String param1, String param2) {
        DownloadFrag fragment = new DownloadFrag();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view= inflater.inflate(R.layout.fragment_download, container, false);



        recycler_view=view.findViewById(R.id.recycler_view);
        Shimmercontainer = view.findViewById(R.id.shimmer_view_container);
        MainRelativeLayout=view.findViewById(R.id.MainRelativeLayout);
        imClear=view.findViewById(R.id.imClear);
        et_text=view.findViewById(R.id.et_text);
        imDownload=view.findViewById(R.id.imDownload);

        imDownload.setClickable(true);

        imClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_text.setText("");
            }
        });

        imDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String EnteredUrl=et_text.getText().toString().trim();
                if(EnteredUrl.equals("") || ( EnteredUrl.equals(null)  ) || EnteredUrl==null || ( ! URLUtil.isValidUrl(EnteredUrl) )){
                    Toasty.error(getContext(), view.getContext().getResources().getString(R.string.PleasevalidEnterLink), Toast.LENGTH_SHORT, true).show();
                }
              /*  else if (Utils.IsContainsRistriced(EnteredUrl)){
                    Toasty.error(getContext(), view.getContext().getResources().getString(R.string.Copyrightcontent), Toast.LENGTH_SHORT, true).show();
                }*/
                else{
                    Intent myIntent = new Intent( view.getContext() , download_result.class);
                    myIntent.putExtra("url", EnteredUrl);
                    view.getContext().startActivity(myIntent);
                }
            }
        });
        LoadSites();

        return  view;
    }


    public void LoadSites(){
        MainRelativeLayout.setVisibility(View.GONE);
        Shimmercontainer.startShimmer();
        new LoadSupportedSites().execute("");
    }

    public class LoadSupportedSites extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... strings) {

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url( Utils.APIURL +"/js/services.json")
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
        }

        @Override
        protected void onPostExecute(String b ){

            List<SuppotedSites> AllSuppotedSites=new ArrayList<>();
            try {
                JSONArray bArray=new JSONArray(b);
                for (int i = 0; i < bArray.length(); i++){
                    JSONObject c = bArray.getJSONObject(i);
                    SuppotedSites suppotedSite=new SuppotedSites();
                    suppotedSite.setName(c.getString("Name"));
                    suppotedSite.setServiceUrl(c.getString("ServiceUrl"));

                    try {
                        suppotedSite.setMobileSupport(c.getString("MobileSupport"));
                    }
                    catch (Exception ex){
                        suppotedSite.setMobileSupport("true");
                    }

                    JSONArray TestUrls=c.getJSONArray("TestUrls");
                    List<String> TestUrlsList=new ArrayList<>();
                    for (int u=0;u< TestUrls.length();u++){
                        JSONObject cu = TestUrls.getJSONObject(u);
                        TestUrlsList.add(cu.getString("Url"));
                    }
                    suppotedSite.setTestUrls(TestUrlsList);
                    if(!suppotedSite.getMobileSupport().equals("false")){
                        AllSuppotedSites.add(suppotedSite);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        List<SuppotedSitesHolder> SuppotedSitesHolderList=new ArrayList<>();

            SuppotedSitesHolder suppotedSitesHolder;
            List<SuppotedSites> suppotedSitesFourList=new ArrayList<>();
            int ifour=1;
            for (int i=0;i< AllSuppotedSites.size();i++){
                if(ifour==4){
                    suppotedSitesFourList.add(AllSuppotedSites.get(i));
                    suppotedSitesHolder=new SuppotedSitesHolder();
                    suppotedSitesHolder.setSuppotedSiteslist(suppotedSitesFourList);
                    SuppotedSitesHolderList.add(suppotedSitesHolder);
                    suppotedSitesFourList=new ArrayList<>();
                    ifour=1;
                }
                else
                {
                    suppotedSitesFourList.add(AllSuppotedSites.get(i));
                    ifour=ifour+1;
                }
            }

            HolderAdapter adapter=new HolderAdapter( getView().getContext(), SuppotedSitesHolderList) ;
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity() );
            recycler_view.setLayoutManager(mLayoutManager);
            recycler_view.setAdapter(adapter);
            adapter.notifyDataSetChanged();

            Shimmercontainer.stopShimmer();
            Shimmercontainer.setVisibility(View.GONE);
            MainRelativeLayout.setVisibility(View.VISIBLE);
        }
    }
}