package com.cheqin.booking.activities.login;

import android.app.Application;
import android.os.CountDownTimer;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.cheqin.booking.activities.login.model.UserLoginResponse;
import com.cheqin.booking.network.APIController;

public class LoginSignupViewModel extends AndroidViewModel {

    public APIController apiController;
    public String phoneNumber;
    public UserLoginResponse userObject;
    private FragmentListener fragmentListener;
   public MutableLiveData<Long> longResendData = new MutableLiveData<>();
//120000
    CountDownTimer timer = new CountDownTimer(120000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            longResendData.postValue(millisUntilFinished);
        }

        @Override
        public void onFinish() {
            longResendData.postValue((long) 0.0);
//            mBinding.resendOTP.setEnabled(true);
//            mBinding.resendText.setVisibility(View.GONE);
        }
    };


    public LoginSignupViewModel(@NonNull Application application) {
        super(application);
        apiController = APIController.getInstance(application.getApplicationContext());
    }

    public void setFragmentListener(FragmentListener listener) {
        fragmentListener = listener;
    }

    public FragmentListener getFragmentListener() {
        return fragmentListener;
    }
}
