package com.cheqin.booking.bottomsheet;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cheqin.booking.R;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;
import biz.laenger.android.vpbs.BottomSheetUtils;
import biz.laenger.android.vpbs.ViewPagerBottomSheetDialogFragment;

/**
 * Created by Obaro on 01/08/2016.
 */
public class TopOffersBottomSheetDialogFragment extends ViewPagerBottomSheetDialogFragment {

    ViewPager bottomSheetViewPager;

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);

        final View contentView = View.inflate(getContext(), R.layout.bottom_sheet_top_offers, null);
        dialog.setContentView(contentView);
        bottomSheetViewPager = contentView.findViewById(R.id.bottom_sheet_viewpager);
        setupViewPager();
    }

    private void setupViewPager() {
        bottomSheetViewPager.setOffscreenPageLimit(1);
       // bottomSheetViewPager.setAdapter(new SimplePagerAdapter(getContext()));
        BottomSheetUtils.setupViewPager(bottomSheetViewPager);
    }
/*
    static class SimplePagerAdapter extends PagerAdapter {

        private final Context context;

        SimplePagerAdapter(Context context) {
            this.context = context;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "";
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return object == view;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            final View view = LayoutInflater.from(container.getContext()).inflate(R.layout.bottom_sheet_top_offers, container, false);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }

    }*/


}
