package com.codingburg.downloader.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

import com.codingburg.downloader.R;

public class LoadingDialog {
    private Activity activity;
    private AlertDialog alertDialog;

    public LoadingDialog(Activity mActivity){
        activity=mActivity;
    }

    public void startLoadingDialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(activity);
        LayoutInflater inflater=activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.loading_dialog,null));
        builder.setCancelable(false);
        alertDialog=builder.create();
        alertDialog.show();
    }
    public void dismisDialoig(){
        alertDialog.dismiss();
    }

}
