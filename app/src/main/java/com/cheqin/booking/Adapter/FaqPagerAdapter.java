package com.cheqin.booking.Adapter;



import com.cheqin.booking.Fragments.FaqFragment;
import com.cheqin.booking.Fragments.TermsFragment;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class FaqPagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    public FaqPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                TermsFragment tab1 = new TermsFragment();

                return tab1;
            case 1:
                FaqFragment tab2 = new FaqFragment();
                return tab2;

        }
            return null;
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }

}
