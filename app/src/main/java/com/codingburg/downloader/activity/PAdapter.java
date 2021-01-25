package com.codingburg.downloader.activity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class PagerAdapter extends FragmentPagerAdapter {

    private int numOfTabs;

    public PagerAdapter(FragmentManager fm,int numOfTabs) {
        super(fm,numOfTabs);
        this.numOfTabs=numOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                  return new DownloadFrag();
            case 1:
                return  new HistoryFrag();
            default :
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
