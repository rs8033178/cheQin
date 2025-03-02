package com.cheqin.booking.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.databinding.DataBindingUtil;

import com.cheqin.booking.R;
import com.cheqin.booking.animation.Utils;
import com.cheqin.booking.databinding.CountrySelectionViewBinding;
import com.cheqin.booking.utils.Constants;
import com.rilixtech.widget.countrycodepicker.Country;

public class CountrySelectionView extends LinearLayout {
    public EditText getMobile() {
        return mBinding.mobile;
    }

    private Context mContext;
    private CountrySelectionViewBinding mBinding;
//    private ValidFieldListener mValidFieldListener;

    public CountrySelectionView(Context context) {
        super(context);
        mContext = context;
        initView(context, null);
    }

    public CountrySelectionView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView(context, attrs);
    }

    public CountrySelectionView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        initView(context, attrs);
    }

    @SuppressLint("ResourceType")
    private void initView(Context context, AttributeSet attrs) {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.country_selection_view, this, true);
        mBinding.ccp.registerPhoneNumberTextView(mBinding.mobile);
    }

  /*  public void setError(String error) {
        mBinding.tvErrorText.setVisibility(VISIBLE);
        mBinding.tvErrorText.setText(error);
        mBinding.inputParent.setActivated(true);

//        if (mValidFieldListener != null)
//            mValidFieldListener.fieldStatus(ValidFieldListener.FieldStatus.ERROR);
    }*/


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    public interface OnPhoneChangedListener {
        void onPhoneChanged(String phone);
    }

    public void setCountryDetail(Country countryDetail) {
        if (countryDetail == null)
            return;

//        mBinding.image.setImageResource(countryDetail.getResId());
//        mBinding.countryCode.setText("+" + countryDetail.getCountryCode());
    }

    public String getMobileNumber() {
        if (mBinding.ccp != null && mBinding.ccp.getPhoneNumber() != null)
            return "" + mBinding.ccp.getPhoneNumber().getNationalNumber();
        else
            return "";
    }

    public String getCountryNameCode() {
        return String.valueOf(mBinding.ccp.getSelectedCountryNameCode());
    }

    public String getCountryCode() {
        return String.valueOf(mBinding.ccp.getSelectedCountryCode());
    }
}
