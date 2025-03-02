package com.cheqin.booking.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cheqin.booking.Adapter.ImageViewPagerAdapter;
import com.cheqin.booking.R;
import com.cheqin.booking.User.UserBooking;
import com.cheqin.booking.gcm.SharedPreference;
import com.cheqin.booking.utils.Common;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

/**
 * A placeholder fragment containing a simple view.
 */
public class SplashActivityFragment extends Fragment {
    private ViewPager _mViewPager;
    private ImageViewPagerAdapter _adapter;
    private ImageView _btn1, _btn2, _btn3, _btn4;
    private TextView btnPrev, btnNext;
    private int currentItem = 0;
    SharedPreference sp;
    public SplashActivityFragment() {
    }


    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sp=new SharedPreference();
        if(sp.getSplashKey(getContext())==false) {
            setUpView();
            setTab();
            onCircleButtonClick();
        }else{
            if (Common.isNetworkAvailable(getActivity())) {
                Intent user = new Intent(getActivity(), UserBooking.class);
                user.putExtra("type", "buyer");
                user.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(user);
                sp.setSplashKey(getContext());
                getActivity().finish();
                return;
            }else{
                Toast.makeText(getActivity(),"Please Check Internet Connection, Try again",Toast.LENGTH_LONG).show();
                return;
            }
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.splash_fragment_main, container, false);
    }

    private void onCircleButtonClick() {

        _btn1.setOnClickListener(v -> {
            _btn1.setImageResource(R.drawable.fill_circle);
            currentItem = 0;
            _mViewPager.setCurrentItem(currentItem);
        });

        _btn2.setOnClickListener(v -> {
            _btn2.setImageResource(R.drawable.fill_circle);
            currentItem = 1;
            _mViewPager.setCurrentItem(currentItem);
        });
        _btn3.setOnClickListener(v -> {
            _btn3.setImageResource(R.drawable.fill_circle);
            currentItem = 2;
            _mViewPager.setCurrentItem(currentItem);
        });
        _btn4.setOnClickListener(v -> {
            _btn4.setImageResource(R.drawable.fill_circle);
            currentItem = 3;
            _mViewPager.setCurrentItem(currentItem);
        });

        btnNext.setOnClickListener(v -> {
            currentItem = currentItem + 1;
            if (currentItem > 4) {
                return;
            }

            if (currentItem == 4) {
                if (Common.isNetworkAvailable(getActivity())) {
                    sp.setSplashKey(getContext());
                    Intent user = new Intent(getActivity(), UserBooking.class);
                    user.putExtra("type", "buyer");
                    user.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(user);
                    getActivity().finish();
                    return;
                }else{
                    Toast.makeText(getActivity(),"Please Check Internet Connection, Try again",Toast.LENGTH_LONG).show();
                    return;
                }
            }

            _mViewPager.setCurrentItem(currentItem);
        });

        btnPrev.setOnClickListener(v -> {

            if (currentItem == 0) {
                return;
            }
            currentItem = currentItem - 1;
            _mViewPager.setCurrentItem(currentItem);
        });
    }

    private void setUpView() {
        _mViewPager = getView().findViewById(R.id.imageviewPager);
        _adapter = new ImageViewPagerAdapter(getActivity(), getFragmentManager());
        btnNext = getView().findViewById(R.id.btn_next);
        btnPrev = getView().findViewById(R.id.btn_previous);
        _mViewPager.setAdapter(_adapter);
        _mViewPager.setCurrentItem(0);
        initButton();
    }

    private void setTab() {
        _mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrollStateChanged(int position) {
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageSelected(int position) {
                // TODO Auto-generated method stub
                _btn1.setImageResource(R.drawable.holo_circle);
                _btn2.setImageResource(R.drawable.holo_circle);
                _btn3.setImageResource(R.drawable.holo_circle);
                _btn4.setImageResource(R.drawable.holo_circle);
              //  _btn5.setImageResource(R.drawable.holo_circle);

                btnAction(position);
            }

        });

    }

    private void btnAction(int action) {
        currentItem = action;
        switch (action) {
            case 0:
                _btn1.setImageResource(R.drawable.fill_circle);
                btnNext.setText("NEXT");
                btnPrev.setText("");
                break;

            case 1:
                _btn2.setImageResource(R.drawable.fill_circle);
                btnNext.setText("NEXT");
                btnPrev.setText("PREV");
                break;
            case 2:
                _btn3.setImageResource(R.drawable.fill_circle);
                btnNext.setText("NEXT");
                btnPrev.setText("PREV");
                break;
            case 3:
                _btn4.setImageResource(R.drawable.fill_circle);
                btnNext.setText("DONE");
                btnPrev.setText("PREV");
                break;
        }
    }

    private void initButton() {
        _btn1 = (ImageView) getView().findViewById(R.id.btn1);
        _btn1.setImageResource(R.drawable.fill_circle);
        _btn2 = (ImageView) getView().findViewById(R.id.btn2);
        _btn3 = (ImageView) getView().findViewById(R.id.btn3);
        _btn4 = (ImageView) getView().findViewById(R.id.btn4);
        //_btn5 = (ImageView) getView().findViewById(R.id.btn5);

    }

    private void setButton(Button btn, String text, int h, int w) {
        btn.setWidth(w);
        btn.setHeight(h);
        btn.setText(text);
    }
}
