package com.codingburg.downloader.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.codingburg.downloader.APIHelpers.Utils;
import com.codingburg.downloader.Models.Model_Video;
import com.codingburg.downloader.R;
import com.codingburg.downloader.interfaces.DataSavedListener;
import com.codingburg.downloader.interfaces.OnFileDelete;
import java.io.File;
import java.util.ArrayList;
import es.dmoral.toasty.Toasty;

public class HistoryFrag extends Fragment implements DataSavedListener {

    VideoFolderAdapter obj_adapter;
    ArrayList<Model_Video> al_video = new ArrayList<>();
    public RecyclerView recyclerView;
    RecyclerView.LayoutManager recyclerViewLayoutManager;
    private static final int REQUEST_PERMISSIONS = 100;
    private static HistoryFrag instance = null;
    Context mContect;


    public HistoryFrag() {

    }
    public static HistoryFrag getInstance() {
        return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        if (getArguments() != null) {
                 }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_history, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view1);
        recyclerViewLayoutManager = new GridLayoutManager(view.getContext().getApplicationContext(), 2);
        recyclerView.setLayoutManager(recyclerViewLayoutManager);
        mContect=view.getContext();
        fn_checkpermission();
        return view;
    }

    private void fn_checkpermission(){
        if ((ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
            if ((ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) && (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE))) {

            } else {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_PERMISSIONS);
            }
        }else {
            fn_video();
        }
    }

    public void fn_video() {
        al_video=new ArrayList<>();
        Uri uri;
        Cursor cursor;
        int column_index_data, thum;
        String absolutePathOfImage = null;
        uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.MediaColumns.DATA, MediaStore.Video.Media.BUCKET_DISPLAY_NAME,MediaStore.Video.Media._ID,MediaStore.Video.Thumbnails.DATA,MediaStore.MediaColumns.DATE_MODIFIED};
try{
    final String orderBy = MediaStore.Images.Media.DATE_MODIFIED;
    cursor = getActivity().getApplicationContext().getContentResolver().query(uri, projection, MediaStore.Images.Media.DATA + " like ? ", new String[] {"%/AllInOneVD/%"}, orderBy + " DESC");
    column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
    thum = cursor.getColumnIndexOrThrow(MediaStore.Video.Thumbnails.DATA);
    while (cursor.moveToNext()) {
        absolutePathOfImage = cursor.getString(column_index_data);
        if((new File(absolutePathOfImage).exists()))
        {
            Model_Video obj_model = new Model_Video();
            obj_model.setBoolean_selected(false);
            obj_model.setStr_path(absolutePathOfImage);
            obj_model.setStr_thumb(cursor.getString(thum));
            al_video.add(obj_model);
        }
    }
} catch (Exception e) {
    e.printStackTrace();
}

        LoadFiles();
    }


    private void LoadFiles()
    {
        obj_adapter = new VideoFolderAdapter(getActivity().getApplicationContext(),al_video,getActivity());
        obj_adapter.setOnBluetoothDeviceClickedListener(new OnFileDelete() {
            @Override
            public void onDeleteButtonClicked(String deviceAddress) {
                DeleteConfirm(deviceAddress);
            }
        });
        recyclerView.setAdapter(obj_adapter);
    }
    private void DeleteConfirm(final String index) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.app_name);
        builder.setMessage(mContect.getResources().getString(R.string.DeleteConfirm)  );
        builder.setIcon(R.drawable.flirt_icon);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                File fdelete = new File(index).getAbsoluteFile();
                if (fdelete.exists()) {
                    if (fdelete.delete()) {
                        fn_video();
                        Toasty.success(getContext(), mContect.getResources().getString(R.string.DeletedSuccessfully), Toast.LENGTH_SHORT, true).show();

                    } else {
                        Toasty.error(getContext(),  mContect.getResources().getString(R.string.UnabletoDeletefile), Toast.LENGTH_SHORT, true).show();
                    }
                }
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_PERMISSIONS: {
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults.length > 0 && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        Utils.createFileFolder();
                        fn_video();
                    } else {
                        Toast.makeText(getActivity(), mContect.getResources().getString(R.string.StoragePermissionWarning), Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
    }

    private BroadcastReceiver updateProfileBroadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toasty.error(getContext(), mContect.getResources().getString(R.string.UnabletoDeletefile), Toast.LENGTH_SHORT, true).show();

        }
    };

    @Override
    public void onDataSaved() {
        fn_video();
    }

    @Override
    public void onResume() {
        fn_video();
        getActivity().registerReceiver(updateProfileBroadcast, new IntentFilter("KEY"));
        super.onResume();
    }
}