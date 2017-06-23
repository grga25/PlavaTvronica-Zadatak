package com.plavatvornica.mislav.plavatvornica_zadatak.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.plavatvornica.mislav.plavatvornica_zadatak.SingleNewsFragment;
import com.plavatvornica.mislav.plavatvornica_zadatak.database.DatabaseHandler;


public class PagerAdapter extends FragmentPagerAdapter {

    Context context;

    public PagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        return new SingleNewsFragment();
    }

    @Override
    public int getCount() {
        DatabaseHandler db = new DatabaseHandler(context);
        return db.getArticlesCount();
    }

}
