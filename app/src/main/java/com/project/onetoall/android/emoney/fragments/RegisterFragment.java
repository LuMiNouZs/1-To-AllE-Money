package com.project.onetoall.android.emoney.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.project.onetoall.android.emoney.MainActivity;
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

public class RegisterFragment extends Fragment implements View.OnClickListener {


    private EditText tbRegisterPasspostId,tbRegisterFirstname,tbRegisterLastname,tbRegisterUsername,tbRegisterPassword,
            tbRegisterPasswordConfirm, tbRegisterEmail;
    private Button btnRegisterNext;
    private Context context;
    private String apiService = "API_function.php";
    private ProgressDialog pDialog;
    private String rsStatus;

    public RegisterFragment newInstance() {

        return new RegisterFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_register, container, false);
        return v;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = view.getContext();
        initUI(view);
        btnRegisterNext.setOnClickListener(this);
        //testData();
    }

    private void initUI(View view) {
        tbRegisterPasspostId = (EditText) view.findViewById(R.id.tbRegisterPasspostId);
        tbRegisterFirstname = (EditText) view.findViewById(R.id.tbRegisterFirstname);
        tbRegisterLastname = (EditText) view.findViewById(R.id.tbRegisterLastname);
        tbRegisterUsername = (EditText) view.findViewById(R.id.tbRegisterUsername);
        tbRegisterPassword  = (EditText) view.findViewById(R.id.tbRegisterPassword);
        tbRegisterPasswordConfirm = (EditText) view.findViewById(R.id.tbRegisterPasswordConfirm);
        tbRegisterEmail = (EditText) view.findViewById(R.id.tbRegisterEmail);
        btnRegisterNext = (Button) view.findViewById(R.id.btnRegisterNext);
    }


    private void testData(){
        tbRegisterPasspostId.setText("1100701154346");
        tbRegisterFirstname.setText("Anupol");
        tbRegisterLastname.setText("Chantachua");
        tbRegisterUsername.setText("0922505777");
        tbRegisterPassword.setText("lkflyofko");
        tbRegisterPasswordConfirm.setText("lkflyofko");
        tbRegisterEmail.setText("c.anupol.zeen@gmail.com");
    }
    private void checkEmptyData(){
        if(tbRegisterPasspostId.length() < 10){
            Toast.makeText(context, "Please enter passpost ID not lass then 10.", Toast.LENGTH_SHORT).show();
        } else if(tbRegisterFirstname.length() <= 0){
            Toast.makeText(context, "Please enter firstname.", Toast.LENGTH_SHORT).show();
        }else if(tbRegisterLastname.length() <= 0){
            Toast.makeText(context, "Please enter lastname.", Toast.LENGTH_SHORT).show();
        }else if(tbRegisterUsername.length() < 10){
            Toast.makeText(context, "Please enter phonenumber 10 digit more.", Toast.LENGTH_SHORT).show();
        }else if(tbRegisterPassword.length() <= 6){
            Toast.makeText(context, "Please enter password.", Toast.LENGTH_SHORT).show();
        }else if(tbRegisterPasswordConfirm.getText().toString().compareTo(tbRegisterPassword.getText().toString()) != 0){
            Toast.makeText(context, "Please check re-password not match.", Toast.LENGTH_SHORT).show();
        }else if(tbRegisterEmail.length() <= 0){
            Toast.makeText(context, "Please enter e-mail.", Toast.LENGTH_SHORT).show();
        }else{
            sendOtpPost();
        }
    }

    @Override
    public void onClick(View v) {
            switch (v.getId()){
                case R.id.btnRegisterNext :
                    checkEmptyData();
                    break;
            }
    }


    private void sendOtpPost() {

        APIOneToAll sendOTP = new APIOneToAll();

        final RequestParams params = new RequestParams();
        params.put("auth_user", "1dev");
        params.put("auth_pwd", "onetoall");
        params.put("phone_number", tbRegisterUsername.getText().toString());
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
                            //Toast.makeText(context,rsStatus, Toast.LENGTH_SHORT).show();
                        }
                            //3003:Success,
                            if (rsStatus.equals("success")) {
                                //Toast.makeText(context, "Successful.", Toast.LENGTH_SHORT).show();

                                Bundle bundle = new Bundle();
                                bundle.putString("passpostId",tbRegisterPasspostId.getText().toString());
                                bundle.putString("firstName", tbRegisterFirstname.getText().toString());
                                bundle.putString("lastName", tbRegisterLastname.getText().toString());
                                bundle.putString("userName", tbRegisterUsername.getText().toString());
                                bundle.putString("password", tbRegisterPassword.getText().toString());
                                bundle.putString("email", tbRegisterEmail.getText().toString());

                                VerifyFragment fragmentVerify = new VerifyFragment();
                                fragmentVerify.setArguments(bundle);
                                FragmentTransaction transaction =  getActivity().getSupportFragmentManager().beginTransaction();
                                transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
                                transaction.replace(R.id.rootLoginFrame,fragmentVerify, "Verify");
                                transaction.addToBackStack(null);
                                transaction.commit();

                            } else {
                                Toast.makeText(context, "Can't register please check data.", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(context, "Successful.", Toast.LENGTH_SHORT).show();
                            VerifyFragment fragmentVerify = new VerifyFragment();
                            FragmentTransaction transaction =  getActivity().getSupportFragmentManager().beginTransaction();
                            transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
                            transaction.replace(R.id.rootLoginFrame,fragmentVerify, "Verify");
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



}
