package com.cheqin.booking.activities.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.cheqin.booking.User.UserBooking;
import com.cheqin.booking.databinding.FragmentSignupBinding;
import com.cheqin.booking.network.APIResponseListener;
import com.cheqin.booking.network.models.response.AbstractResponse;
import com.cheqin.booking.utils.HttpAsync;
import com.onesignal.OSPermissionSubscriptionState;
import com.onesignal.OneSignal;

import java.util.HashMap;

import retrofit2.Call;

public class SignupFragment extends Fragment {
    private LoginSignupViewModel model;
    private FragmentSignupBinding mBinding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = new ViewModelProvider(getActivity()).get(LoginSignupViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentSignupBinding.inflate(inflater, container, false);
        mBinding.btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();
                init();
            }
        });
        return mBinding.getRoot();
    }

    public void removeErrors() {
        mBinding.nameInputLayout.setError(null);
        mBinding.emailInputLayout.setError(null);
    }


    public boolean validate() {
        removeErrors();
        boolean isValid = false;
        if (mBinding.fName.getText().toString().length() > 0) {
            isValid = true;
        } else {
            mBinding.nameInputLayout.setError("Name can not be empty");
            isValid = false;
            return isValid;
        }

        String email = mBinding.inputEmail.getText().toString();
        if (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            isValid = true;
        } else {
            mBinding.emailInputLayout.setError("Enter valid email address");
            return false;
        }

        return isValid;
    }


    private void init() {
        String userName = mBinding.fName.getText().toString() + " " + mBinding.lName.getText().toString();
        String userEmail = mBinding.inputEmail.getText().toString();


  /*      para = new HashMap<>();
        para.put("user[fname]", first_name.getText().toString());
        para.put("user[lname]", last_name.getText().toString());
        para.put("user[email]", emailText.getText().toString());
        para.put("user[password]", passwd);
        para.put("user[country_code]", countryShortname);
        para.put("address[mobile]", phone.getText().toString());
        para.put("user_type", "buyer");
        para.put("address[city]", autoselectcity.getText().toString());
        para.put("cityLatLong", selectedLat+"||"+selectedLng);
        para.put("partnerId", "hh");
*/


        Call<AbstractResponse> call = model.apiController.apiServices.signupUser(userName,
                userEmail, model.userObject.getOTP(),
                "signup",
                model.userObject.getUserId(),
                mBinding.fName.getText().toString(),
                mBinding.lName.getText().toString(),
                mBinding.inputPassword.getText().toString(),
                null);

        model.apiController.retrofitRequest(call, new APIResponseListener() {
            @Override
            public void onSuccess(int reqCode, Object responseObject) {
                AbstractResponse response = (AbstractResponse) responseObject;
                if (response.getSuccess()) {

                    pushRegister("", model.userObject.getUserId());

                    Intent i = new Intent(getContext(), UserBooking.class);
                    if (getActivity().getIntent().getExtras() != null)
                        i.putExtras(getActivity().getIntent().getExtras());
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    getActivity().finish();

                } else {
                    Toast.makeText(getContext(), response.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onError(int errorCode, String errorMessage) {
                Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
            }
        });


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


}


