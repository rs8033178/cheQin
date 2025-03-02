package com.cheqin.booking.Adapter;


import android.content.Context;

import com.cheqin.booking.Fragments.SplashImageFourFragment;
import com.cheqin.booking.Fragments.SplashImageOneFragment;
import com.cheqin.booking.Fragments.SplashImageThreeFragment;
import com.cheqin.booking.Fragments.SplashImageTwoFragment;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

/**
 * Created by nirmal on 12/08/15.
 */
public class ImageViewPagerAdapter extends FragmentPagerAdapter {
    private Context _context;
    public static int totalPage = 4;

    public ImageViewPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        _context = context;

    }

    @Override
    public Fragment getItem(int position) {
        Fragment f = new Fragment();
        switch (position) {
            case 0:
                f = new SplashImageOneFragment();
                break;
            case 1:
                f = new SplashImageTwoFragment();
                break;
            case 2:
                f = new SplashImageThreeFragment();
                break;
            case 3:
                f = new SplashImageFourFragment();
                break;
            /*case 4:
                f = new SplashImageFiveFragment();
                break;*/
        }
        return f;
    }

    @Override
    public int getCount() {
        return totalPage;
    }

}

