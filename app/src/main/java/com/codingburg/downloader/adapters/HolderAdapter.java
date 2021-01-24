package com.codingburg.downloader.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.codingburg.downloader.APIHelpers.Utils;
import com.codingburg.downloader.Models.SuppotedSites;
import com.codingburg.downloader.Models.SuppotedSitesHolder;
import com.codingburg.downloader.R;
import com.bumptech.glide.Glide;

import java.util.List;

public class HolderAdapter extends RecyclerView.Adapter <HolderAdapter.MyViewHolder> {

    private Context mContext;
    private List<SuppotedSitesHolder> suppotedSitesList;

    public HolderAdapter(Context mContext,List<SuppotedSitesHolder> suppotedSitesList){
        this.mContext=mContext;
        this.suppotedSitesList=suppotedSitesList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardholder, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        SuppotedSitesHolder sites=suppotedSitesList.get(position);
        List<SuppotedSites> suppotedSites=sites.getSuppotedSiteslist();

        if(suppotedSites.get(0)!=null){
            SuppotedSites suppotedSite=suppotedSites.get(0);
            holder.LLSS_TXT_1.setText(suppotedSite.getName());
            Glide.with(mContext).load(
                    Utils.API_LOGO_DIR_URL+suppotedSite.getName()+"."+ Utils.API_LOGO_Imagetype
                ).into(holder.LLSS_IM_1);
        }else { holder.LL_MAIN_1.setVisibility(View.GONE); }

        if(suppotedSites.get(1)!=null){
            SuppotedSites suppotedSite=suppotedSites.get(1);
            holder.LLSS_TXT_2.setText(suppotedSite.getName());
            Glide.with(mContext).load(
                    Utils.API_LOGO_DIR_URL+suppotedSite.getName()+"."+Utils.API_LOGO_Imagetype
            ).into(holder.LLSS_IM_2);
        }else { holder.LL_MAIN_2.setVisibility(View.GONE); }


        if(suppotedSites.get(2)!=null){
            SuppotedSites suppotedSite=suppotedSites.get(2);
            holder.LLSS_TXT_3.setText(suppotedSite.getName());
            Glide.with(mContext).load(
                    Utils.API_LOGO_DIR_URL+suppotedSite.getName()+"." + Utils.API_LOGO_Imagetype
            ).into(holder.LLSS_IM_3);
        }else { holder.LL_MAIN_3.setVisibility(View.GONE); }

        if(suppotedSites.get(3)!=null){
            SuppotedSites suppotedSite=suppotedSites.get(3);
            holder.LLSS_TXT_4.setText(suppotedSite.getName());
            Glide.with(mContext).load(
                    Utils.API_LOGO_DIR_URL+suppotedSite.getName()+"."+ Utils.API_LOGO_Imagetype
            ).into(holder.LLSS_IM_4);
        }else { holder.LL_MAIN_4.setVisibility(View.GONE); }

    }

    @Override
    public int getItemCount() {
        return suppotedSitesList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView LLSS_TXT_1,LLSS_TXT_2,LLSS_TXT_3,LLSS_TXT_4;
        public ImageView LLSS_IM_1,LLSS_IM_2,LLSS_IM_3,LLSS_IM_4;
        public LinearLayout LL_MAIN_1,LL_MAIN_2,LL_MAIN_3,LL_MAIN_4;

        public MyViewHolder(@NonNull View view) {
            super(view);
            LLSS_TXT_1 = (TextView) view.findViewById(R.id.LLSS_TXT_1);
            LLSS_TXT_2 = (TextView) view.findViewById(R.id.LLSS_TXT_2);
            LLSS_TXT_3 = (TextView) view.findViewById(R.id.LLSS_TXT_3);
            LLSS_TXT_4 = (TextView) view.findViewById(R.id.LLSS_TXT_4);

            LLSS_IM_1 =(ImageView) view.findViewById(R.id.LLSS_IM_1);
            LLSS_IM_2 =(ImageView) view.findViewById(R.id.LLSS_IM_2);
            LLSS_IM_3 =(ImageView) view.findViewById(R.id.LLSS_IM_3);
            LLSS_IM_4 =(ImageView) view.findViewById(R.id.LLSS_IM_4);

            LL_MAIN_1=(LinearLayout) view.findViewById(R.id.LL_MAIN_1);
            LL_MAIN_2=(LinearLayout) view.findViewById(R.id.LL_MAIN_2);
            LL_MAIN_3=(LinearLayout) view.findViewById(R.id.LL_MAIN_3);
            LL_MAIN_4=(LinearLayout) view.findViewById(R.id.LL_MAIN_4);


        }
    }
}
