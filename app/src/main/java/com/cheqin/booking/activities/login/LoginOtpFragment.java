package com.cheqin.booking.activities.login;

import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.Task;
import com.cheqin.booking.Log.GenerateAuthtoken;
import com.cheqin.booking.R;
import com.cheqin.booking.User.UserBooking;
import com.cheqin.booking.activities.login.model.UserLoginResponse;
import com.cheqin.booking.databinding.FragmentLoginOtpBinding;
import com.cheqin.booking.network.APIResponseListener;
import com.cheqin.booking.network.models.response.AbstractResponse;
import com.cheqin.booking.utils.HttpAsync;
import com.cheqin.booking.utils.SettingSharedPrefs;
import com.onesignal.OSPermissionSubscriptionState;
import com.onesignal.OneSignal;

import java.util.HashMap;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import in.aabhasjindal.otptextview.OTPListener;
import retrofit2.Call;

import static android.app.Activity.RESULT_OK;

public class LoginOtpFragment extends Fragment {
    private LoginSignupViewModel model;
    private FragmentLoginOtpBinding mBinding;
    private String otpCode = "";

    private static final int SMS_CONSENT_REQUEST = 2;  // Set to an unused request code
    private final BroadcastReceiver smsVerificationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (SmsRetriever.SMS_RETRIEVED_ACTION.equals(intent.getAction())) {
                Bundle extras = intent.getExtras();
                Status smsRetrieverStatus = (Status) extras.get(SmsRetriever.EXTRA_STATUS);

                switch (smsRetrieverStatus.getStatusCode()) {
                    case CommonStatusCodes.SUCCESS:
                        // Get consent intent
                        Intent consentIntent = extras.getParcelable(SmsRetriever.EXTRA_CONSENT_INTENT);
                        try {
                            // Start activity to show consent dialog to user, activity must be started in
                            // 5 minutes, otherwise you'll receive another TIMEOUT intent
                            startActivityForResult(consentIntent, SMS_CONSENT_REQUEST);
                        } catch (ActivityNotFoundException e) {

                        }
                        break;
                    case CommonStatusCodes.TIMEOUT:
                        // Time out occurred, handle the error.
                        break;
                }
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = new ViewModelProvider(getActivity(),new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication())).get(LoginSignupViewModel.class);

        listenForSMS();

        IntentFilter intentFilter = new IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION);
        getActivity().registerReceiver(smsVerificationReceiver, intentFilter);
    }

    void resendTimeRemaining(long millis) {
        int minutes = (int) (millis / 1000 / 60);
        int seconds = (int) (millis / 1000 % 60);
        mBinding.resendText.setVisibility(View.VISIBLE);
        mBinding.resendText.setText(String.format(getString(R.string.otp_resend_message_text), minutes, seconds));
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentLoginOtpBinding.inflate(inflater, container, false);
        mBinding.setModel(model);
        mBinding.editNumberBack.setOnClickListener((view) -> {
            model.timer.cancel();
            NavHostFragment.findNavController(LoginOtpFragment.this).navigateUp();
        });
        init();
        model.timer.start();
        model.longResendData.observe(getViewLifecycleOwner(), new Observer<Long>() {
            @Override
            public void onChanged(Long aLong) {
                if (aLong != 0) {
                    resendTimeRemaining(aLong);
                    mBinding.resendOTP.setEnabled(false);
                    mBinding.resendText.setVisibility(View.VISIBLE);
                } else {
                    mBinding.resendOTP.setEnabled(true);
                    mBinding.resendText.setVisibility(View.GONE);
                }
            }
        });
        return mBinding.getRoot();
    }

    private void resendOtpReq() {

        Call<UserLoginResponse> callit = model.apiController.apiServices.getOTP(model.userObject.getMobile(), model.userObject.getCountryNameCode());

        model.apiController.retrofitRequest(callit, new APIResponseListener() {
            @Override
            public void onSuccess(int reqCode, Object responseObject) {
                if (responseObject instanceof UserLoginResponse) {
                    Toast.makeText(getContext(), "OTP resent to your mobile", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(int errorCode, String errorMessage) {
                Log.i("YO", "YO");
            }
        });

    }

    public void init() {
        mBinding.submitOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showError(null); // removes error if showing
                if (mBinding.otpView.getOtp().length() > 3) // check if otp isnt empty
                    verifyOtp(otpCode);
                else
                    showError("Please enter OTP recieved in SMS");
            }
        });

        mBinding.resendOTP.setOnClickListener((view) -> {
            mBinding.resendOTP.setEnabled(false);
            model.timer.cancel();
            model.timer.start();
            resendOtpReq();
        });

        mBinding.otpView.setOtpListener(new OTPListener() {
            @Override
            public void onOTPComplete(String otp) {
                otpCode = otp;
            }

            @Override
            public void onInteractionListener() {

            }
        });
    }

    private void listenForSMS() {
        Task<Void> task = SmsRetriever.getClient(getActivity()).startSmsUserConsent(null);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SMS_CONSENT_REQUEST:
                if (resultCode == RESULT_OK) {
                    String message = data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE);
                    parseOneTimeCode(message);
                }
                break;
        }
    }

    private void setOtpAndVerify(String otpCode) {
        mBinding.otpView.setOTP(otpCode);
        verifyOtp(otpCode);
    }

    private void parseOneTimeCode(String message) {
        Pattern r = Pattern.compile("\\s[a-z]*:\\d+");
        Matcher m = r.matcher(message);

        if (m.find()) {
            String str = m.group(0);
            String[] arr = str.split(":");
            if (arr.length > 1) {
                String code = arr[1];
                setOtpAndVerify(code);
            }
        }
    }

    public void verifyOtp(String otpCode) {
        showProgress();

        Call<AbstractResponse> callit = model.apiController.apiServices.veryOTP(model.userObject.getUserId(), otpCode);
        model.userObject.setOTP(otpCode);
        model.apiController.retrofitRequest(callit, new APIResponseListener() {
            @Override
            public void onSuccess(int reqCode, Object responseObject) {
                hideProgress();
                if (responseObject instanceof AbstractResponse) {
                    AbstractResponse response = (AbstractResponse) responseObject;
                    if (response.getSuccess()) {
                        SettingSharedPrefs sharedPrefs = SettingSharedPrefs.getInstance(getContext());
                        sharedPrefs.clearSharedPreference();
                        sharedPrefs.setPRE_user_login(model.userObject.getUserPresent());
                        sharedPrefs.setPRE_USER_PROFILE_id(model.userObject.getUserId());
                        sharedPrefs.setPRE_usertype(model.userObject.getUserType());
                        sharedPrefs.setPRE_OTP(model.userObject.getOTP());
                        String auth_token = GenerateAuthtoken.generate(model.userObject.getMobile(), model.userObject.getOTP(), "otp");
                        sharedPrefs.setPRE_auth_tokenOtp(auth_token);
                        if (model.userObject.getUserPresent()) {
                            Intent i = new Intent(getContext(), UserBooking.class);
                            if (getActivity().getIntent().getExtras() != null)
                                i.putExtras(getActivity().getIntent().getExtras());
                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
                            getActivity().finish();
                            pushRegister("", model.userObject.getUserId());
                            Toast.makeText(getActivity(), "Logged in Successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            model.getFragmentListener().doSignupProcess();
                            Navigation.findNavController(mBinding.submitOtp).navigate(R.id.action_otp_to_signup);
                        }
                    } else {
                        showError(response.getMessage());
                    }

                }
            }

            @Override
            public void onError(int errorCode, String errorMessage) {
                hideProgress();
            }
        });
    }

    private void showProgress() {
        mBinding.progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgress() {
        mBinding.progressBar.setVisibility(View.GONE);
    }

    public void pushRegister(String email, String appuserid) {
        OSPermissionSubscriptionState status = OneSignal.getPermissionSubscriptionState();
        boolean isEnabled = status.getPermissionStatus().getEnabled();
        boolean isSubscribed = status.getSubscriptionStatus().getSubscribed();
        boolean subscriptionSetting = status.getSubscriptionStatus().getUserSubscriptionSetting();

        String userID = status.getSubscriptionStatus().getUserId();
        String pushToken = status.getSubscriptionStatus().getPushToken();
        HashMap para = new HashMap<>();
        para.put("uid", appuserid);
        para.put("gcm_regid", userID);
        para.put("device_type", "ANDROID");

        para.put("email", email);
        HttpAsync tokenAsync = new HttpAsync(getActivity().getApplicationContext(), null, "http://api.mypillows.company/my_deals/create_updategcm/do.json", para, 2, "push_login");
        tokenAsync.execute();
    }

    private void showError(String error) {
        if (error == null) {
            mBinding.errorText.setText("");
            mBinding.errorText.setVisibility(View.GONE);
        } else {
            mBinding.otpView.showError();
            mBinding.errorText.setText(error);
            mBinding.errorText.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (smsVerificationReceiver != null) {
            getActivity().unregisterReceiver(smsVerificationReceiver);
        }
    }
}
