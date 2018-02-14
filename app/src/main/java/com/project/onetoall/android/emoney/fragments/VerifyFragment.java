package com.project.onetoall.android.emoney.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.project.onetoall.android.emoney.R;
import com.project.onetoall.android.emoney.api.APIOneToAll;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

import static com.loopj.android.http.AsyncHttpClient.log;

/**
 * Created by c.anupol on 19/12/17.
 */

public class VerifyFragment extends Fragment implements View.OnClickListener {

    private EditText tbOtpCode;
    private Button btnVerifyOtp, btnResendOtp;
    private Context context;
    private String apiService = "API_function.php";
    private ProgressDialog pDialog;
    private String rsStatus;
    private String getPasspostID, getFirstName, getLastName, getUserName, getPassword, getEmail;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_otp_active, container, false);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = view.getContext();
        getBundleData();
        initUI(view);
        btnResendOtp.setOnClickListener(this);
        btnVerifyOtp.setOnClickListener(this);
    }

    private void initUI(View view) {
        tbOtpCode = (EditText) view.findViewById(R.id.tbOtpCode);
        btnVerifyOtp = (Button) view.findViewById(R.id.btnVerifyOtp);
        btnResendOtp = (Button) view.findViewById(R.id.btnResendOtp);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnVerifyOtp :
                registerPost();
                //verifyOtpPost();
                break;
            case R.id.btnResendOtp :
                sendOtpPost();
                break;
        }
    }

    public void getBundleData() {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            //int myInt = bundle.getInt(provider, defaultValue);
            getPasspostID = bundle.getString("passpostId");
            getFirstName = bundle.getString("firstName");
            getLastName = bundle.getString("lastName");
            getUserName = bundle.getString("userName");
            getPassword = bundle.getString("password");
            getEmail = bundle.getString("email");
        }
    }

    private void verifyOtpPost() {

        APIOneToAll verifyOtpPost = new APIOneToAll();

        final RequestParams params = new RequestParams();
        params.put("auth_user", "1dev");
        params.put("auth_pwd", "onetoall");
        params.put("phone_number", getUserName.toString());
        params.put("otp", tbOtpCode.getText().toString());
        params.put("action", "verify_otp");

        verifyOtpPost.post(apiService, params, new JsonHttpResponseHandler() {

            @Override
            public void onStart() {
                super.onStart();
                pDialog = new ProgressDialog(context);
                pDialog.setMessage("Please wait verify OTP code...");
                pDialog.setIndeterminate(false);
                pDialog.setCancelable(true);
                pDialog.show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.dismiss();
                }
                log.e("JSONArray Reponse", response.toString());
                if (response != null) {
                    try {
                        for(int i=0; i<response.length(); i++) {
                            JSONObject json_data = response.getJSONObject(i);
                            rsStatus = json_data.getString("status");
                            Toast.makeText(context,rsStatus, Toast.LENGTH_SHORT).show();
                        }
                        //3003:Success,
                        if (rsStatus.equals("7001")) {
                            Toast.makeText(context, "Verify successful.", Toast.LENGTH_SHORT).show();


                            ResultVerifyFragment fragmentRSVerify = new ResultVerifyFragment();
                            FragmentTransaction transaction =  getActivity().getSupportFragmentManager().beginTransaction();
                            transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
                            transaction.replace(R.id.rootLoginFrame,fragmentRSVerify, "Result");
                            transaction.addToBackStack(null);
                            transaction.commit();

                        } else {
                            Toast.makeText(context, "Can't verify OTP code.", Toast.LENGTH_SHORT).show();
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(context, "Error connected to service", Toast.LENGTH_LONG).show();
                pDialog.dismiss();
            }


            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Toast.makeText(context, "Error connected to service : " + responseString, Toast.LENGTH_LONG).show();
                log.e("Reponse Error", responseString.toString());
                pDialog.dismiss();
            }
        });

    }

    private void sendOtpPost() {

        APIOneToAll sendOTP = new APIOneToAll();

        final RequestParams params = new RequestParams();
        params.put("auth_user", "1dev");
        params.put("auth_pwd", "onetoall");
        params.put("phone_number", getUserName.toString());
        params.put("action", "send_OTP");

        sendOTP.post(apiService, params, new JsonHttpResponseHandler() {

            @Override
            public void onStart() {
                super.onStart();
                pDialog = new ProgressDialog(context);
                pDialog.setMessage("Loading...");
                pDialog.setIndeterminate(false);
                pDialog.setCancelable(true);
                pDialog.show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.dismiss();
                }
                log.e("JSONArray Reponse", response.toString());
                if (response != null) {
                    try {
                        for(int i=0; i<response.length(); i++) {
                            JSONObject json_data = response.getJSONObject(i);
                            rsStatus = json_data.getString("status");
                            Toast.makeText(context,rsStatus, Toast.LENGTH_SHORT).show();
                        }
                        //3003:Success,
                        if (rsStatus.equals("success")) {
                            Toast.makeText(context, "Re-send Complete.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Can't re-send OTP code.", Toast.LENGTH_SHORT).show();
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.dismiss();
                }
                //Toast.makeText(context, response.toString(), Toast.LENGTH_LONG).show();
                log.e("JSONObject Reponse", response.toString());
                if (response != null) {
                    try {
                        rsStatus = response.getString("status");
                        //3003:Success,
                        if (rsStatus.equals("001")) {

                        } else {
                            Toast.makeText(context, "CCan't re-send OTP code.", Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(context, "Error connected to service", Toast.LENGTH_LONG).show();
                pDialog.dismiss();
            }


            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Toast.makeText(context, "Error connected to service : " + responseString, Toast.LENGTH_LONG).show();
                log.e("Reponse Error", responseString.toString());
                pDialog.dismiss();
            }
        });

    }

    private void registerPost() {

        APIOneToAll registerPost = new APIOneToAll();

        final RequestParams params = new RequestParams();
        params.put("auth_user", "1dev");
        params.put("auth_pwd", "onetoall");
        params.put("phone_number", getUserName.toString());
        params.put("email_register", getEmail.toString());
        params.put("identification_number", getPasspostID.toString());
        params.put("user_name", getUserName.toString());
        params.put("password", getPassword.toString());
        params.put("otp", tbOtpCode.getText().toString());
        params.put("action", "register");

        registerPost.post(apiService, params, new JsonHttpResponseHandler() {

            @Override
            public void onStart() {
                super.onStart();
                pDialog = new ProgressDialog(context);
                pDialog.setMessage("Please wait verify OTP code...");
                pDialog.setIndeterminate(false);
                pDialog.setCancelable(true);
                pDialog.show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.dismiss();
                }
                log.e("JSONArray Reponse", response.toString());
                if (response != null) {
                    try {
                        for(int i=0; i<response.length(); i++) {
                            JSONObject json_data = response.getJSONObject(i);
                            rsStatus = json_data.getString("status");
                            //Toast.makeText(context,rsStatus, Toast.LENGTH_SHORT).show();
                        }
                        //3003:Success.
                        if (rsStatus.equals("3003")) {
                            Toast.makeText(context, "Verify successful.", Toast.LENGTH_SHORT).show();
                            ResultVerifyFragment fragmentRSVerify = new ResultVerifyFragment();
                            FragmentTransaction transaction =  getActivity().getSupportFragmentManager().beginTransaction();
                            transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
                            transaction.replace(R.id.rootLoginFrame,fragmentRSVerify, "ResultRegister");
                            transaction.addToBackStack(null);
                            transaction.commit();
                            //0111:Invalid identification.
                        } else if(rsStatus.equals("0111")){
                            Toast.makeText(context, "Can't verify OTP code : Invalid identification number, username, phoneNumber or Email.", Toast.LENGTH_SHORT).show();
                            //Other error.
                        } else {
                            Toast.makeText(context, "Can't verify OTP code : Please contact operation.", Toast.LENGTH_SHORT).show();
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(context, "Error connected to service", Toast.LENGTH_LONG).show();
                pDialog.dismiss();
            }


            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Toast.makeText(context, "Error connected to service : " + responseString, Toast.LENGTH_LONG).show();
                log.e("Reponse Error", responseString.toString());
                pDialog.dismiss();
            }
        });

    }
}
