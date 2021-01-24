package com.codingburg.downloader.APIHelpers;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils
{
    public static String AppDirName="AllInOneVD";
    public static File RootDirectoryShow = null;
    public static String APIURL="http://download.shulovshop.com/";
    public static String API_LOGO_DIR_URL=APIURL+"/logos/";
    public static String API_LOGO_Imagetype="webp";

    public static String RootDirectory = "/"+AppDirName+"/";

    static {
        StringBuilder sb = new StringBuilder();
        sb.append(Environment.getExternalStorageDirectory());
        sb.append("/Download/"+ AppDirName);
        RootDirectoryShow = new File(sb.toString());
    }

    public static void createFileFolder()
    {
        if (! RootDirectoryShow.exists()) {
            RootDirectoryShow.mkdirs();
        }
    }
    public static String SanitizeName( String Title ){
        Title.replaceAll("[-+.^:,]","");
        if(Title.length()>30){
            Title = Title.substring(0, Math.min(Title.length(), 30)) ;
        }
        return Title+"_";
    }

    public static boolean IsContainsRistriced(String Query)
    {
        boolean _IsContainsRistriced=false;
        ArrayList<String> list=GetRistricteds();
        for (int i=0;i< list.size();i++){
            if( Query.contains(list.get(i)) ){
                _IsContainsRistriced=true;
            }
        }
        return _IsContainsRistriced;
    }

    static ArrayList<String> Ristricteds;
    private static  ArrayList<String>  GetRistricteds(){
        Ristricteds=new ArrayList<>();
        Ristricteds.add("youtube");
        Ristricteds.add("youtu.be");
        return Ristricteds;
    }

    public static List<String> extractUrls(String input) {
        List<String> result = new ArrayList<String>();

        Pattern pattern = Pattern.compile(
                "\\b(((ht|f)tp(s?)\\:\\/\\/|~\\/|\\/)|www.)" +
                        "(\\w+:\\w+@)?(([-\\w]+\\.)+(com|org|net|gov" +
                        "|mil|biz|info|mobi|name|aero|jobs|museum" +
                        "|travel|[a-z]{2}))(:[\\d]{1,5})?" +
                        "(((\\/([-\\w~!$+|.,=]|%[a-f\\d]{2})+)+|\\/)+|\\?|#)?" +
                        "((\\?([-\\w~!$+|.,*:]|%[a-f\\d{2}])+=?" +
                        "([-\\w~!$+|.,*:=]|%[a-f\\d]{2})*)" +
                        "(&(?:[-\\w~!$+|.,*:]|%[a-f\\d{2}])+=?" +
                        "([-\\w~!$+|.,*:=]|%[a-f\\d]{2})*)*)*" +
                        "(#([-\\w~!$+|.,*:=]|%[a-f\\d]{2})*)?\\b");

        Matcher matcher = pattern.matcher(input);
        while (matcher.find()) {
            result.add(matcher.group());
        }

        return result;
    }

    public static long downloadBegan(String str, String str2, Context context2, String str3)
    {
        Request request = new Request(Uri.parse(str));
        request.setAllowedNetworkTypes(3);
        request.setNotificationVisibility(1);
        StringBuilder sb = new StringBuilder();
        sb.append(str3);
        sb.append("");
        request.setTitle(sb.toString());
        String str4 = Environment.DIRECTORY_DOWNLOADS;
        StringBuilder sb2 = new StringBuilder();
        sb2.append(str2);
        sb2.append(str3);
        request.setDestinationInExternalPublicDir(str4, sb2.toString());
        return ((DownloadManager) context2.getSystemService(Context.DOWNLOAD_SERVICE)).enqueue(request);
    }
}
