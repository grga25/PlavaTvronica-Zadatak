package com.plavatvornica.mislav.plavatvornica_zadatak;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.plavatvornica.mislav.plavatvornica_zadatak.adapters.PagerAdapter;


public class SingleNewsActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    int position;
    public static final String KEY_POSITION = "position";
    public static final String KEY_BROADCAST_INTENT = "update";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_news);

        position = getIntent().getIntExtra(KEY_POSITION, 0);

        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager(), this);
        pager.setAdapter(pagerAdapter);
        pager.setCurrentItem(position);
        pager.addOnPageChangeListener(this);

    }

    public int getPosition() {
        return position;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        Intent data = new Intent(KEY_BROADCAST_INTENT);
        data.putExtra(KEY_POSITION, position);
        SingleNewsActivity.this.sendBroadcast(data);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
