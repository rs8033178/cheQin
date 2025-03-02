package com.cheqin.booking.activities.login;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;


import com.crashlytics.android.Crashlytics;
import com.cheqin.booking.R;
import com.cheqin.booking.activities.login.model.UserLoginResponse;
import com.cheqin.booking.databinding.FragmentLoginNumberBinding;
import com.cheqin.booking.network.APIResponseListener;
import com.cheqin.booking.network.models.response.AbstractResponse;

import java.util.Objects;


import retrofit2.Call;

public class LoginNumberFragment extends Fragment {

    private LoginSignupViewModel model;
    private FragmentLoginNumberBinding binding;
    private String otpCode = "";
    private boolean serverCalling = false;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = new ViewModelProvider(getActivity(), new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication())).get(LoginSignupViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login_number, container, false);
        binding.btGetOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showError(null);
                if (binding.countrySelectionView.getMobileNumber().length() > 8) {
                    if (!serverCalling)
                        signupRequest(binding.countrySelectionView.getMobileNumber(), binding.countrySelectionView.getCountryNameCode());
                    model.phoneNumber = binding.countrySelectionView.getCountryCode() + binding.countrySelectionView.getMobileNumber();

                } else {
                    showError("Please enter valid mobile number");
                }

            }
        });
        return binding.getRoot();
    }

    private void showError(String error) {
        if (error == null) {
            binding.errorText.setText("");
            binding.errorText.setVisibility(View.GONE);
        } else {
            binding.errorText.setText(error);
            binding.errorText.setVisibility(View.VISIBLE);
        }
    }

    private void signupRequest(String mobile, String countryCode) {
        serverCalling = true;
        Call<UserLoginResponse> callit = model.apiController.apiServices.getOTP(mobile, countryCode);
        model.apiController.retrofitRequest(callit, new APIResponseListener() {
            @Override
            public void onSuccess(int reqCode, Object responseObject) {
                serverCalling = false;
                binding.btGetOtp.setVisibility(View.GONE);
                if (responseObject instanceof UserLoginResponse) {
                    model.userObject = (UserLoginResponse) responseObject;
                    model.userObject.setMobile(binding.countrySelectionView.getMobileNumber());
                    model.userObject.setCountryNameCode(binding.countrySelectionView.getCountryNameCode());
                    Navigation.findNavController(binding.btGetOtp).navigate(R.id.gotoVerifyOtp);

                }
            }

            @Override
            public void onError(int errorCode, String errorMessage) {
                serverCalling = false;
                Crashlytics.log("error servercall error on LoginNumberFragment" + errorCode + errorMessage);
                Log.i("YO", "YO");

            }
        });

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}
