package com.cheqin.booking.network;


import com.cheqin.booking.activities.login.model.UserLoginResponse;
import com.cheqin.booking.network.models.request.SignUpRequest;
import com.cheqin.booking.network.models.response.AbstractResponse;
import com.cheqin.booking.utils.Constants;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface APIServices {

    /**************************************** API Calls - GET ************************************/

    @GET(Constants.APIEndPoints.API_LOGIN)
    Call<Response<AbstractResponse>> login(@Query("username") String username,
                                           @Query("password") String password);


    @GET(Constants.APIEndPoints.API_GET_OTP)
    Call<UserLoginResponse> getOTP(@Query("mobile_no") String mobile,
                                   @Query("country_code") String countryCode);

    @GET(Constants.APIEndPoints.API_VERIFY_OTP)
    Call<AbstractResponse> veryOTP(@Query("user_id") String userId,
                                   @Query("otp_code") String otpCode);

    @GET(Constants.APIEndPoints.API_SIGN_UP)
    Call<AbstractResponse> signupUser(@Query("username") String userName,
                                      @Query("user_email") String userEmail,
                                      @Query("otp_code") String otpCode,
                                      @Query("verify_mode") String verifyMode,
                                      @Query("user_id") String userIds,
                                      @Query("user[fname]") String fName,
                                      @Query("user[lname]") String lName,
                                      @Query("user[password]") String inputPassword,
                                      @Query("address[city]") String city);

    /**************************************** API Calls - POST ************************************/

    @POST(Constants.APIEndPoints.API_SIGN_UP)
    Call<Response<AbstractResponse>> signUp(@Body SignUpRequest request);

    @POST(Constants.APIEndPoints.API_SEND_PURCHASE_OTP)
    Call<Response<AbstractResponse>> purchaseOTP(@Field("mofr_id") String offerId, @Field("auth_token") String token, @Field("id") String id );


}