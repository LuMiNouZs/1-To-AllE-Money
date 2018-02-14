package com.project.onetoall.android.emoney.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.project.onetoall.android.emoney.MainActivity;
import com.project.onetoall.android.emoney.R;
import com.project.onetoall.android.emoney.api.APIOneToAll;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

import static com.loopj.android.http.AsyncHttpClient.log;

/**
 * Created by c.anupol on 11/12/17.
 */

public class LoginFragment extends Fragment implements View.OnClickListener {

    private static final String MY_PREFS = "my_prefs";
    private ImageView ivLogo;
    private Button btnLogin, btnClose, btnForgotPass, btnRegister;;
    private EditText tbUserLogin, tbPasswordLogin;
    private Context context;
    private ProgressDialog pDialog;
    private String apiService = "API_function.php";
    private String rsStatus, rsDetail, rsUsername, rsFirstname, rsLastname;
    private SharedPreferences sharedPreferences;

    public LoginFragment newInstance() {

        return new LoginFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_login, container, false);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = view.getContext();
        initUI(view);
        ivLogo.setImageDrawable(getResources().getDrawable(R.drawable.logo_emoney_red));
        btnLogin.setOnClickListener(this);
        btnClose.setOnClickListener(this);
        btnForgotPass.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
    }

    private void initUI(View view) {
        ivLogo = (ImageView) view.findViewById(R.id.ivLogo);
        tbUserLogin = (EditText) view.findViewById(R.id.tbLoginUsername);
        tbPasswordLogin = (EditText) view.findViewById(R.id.tbLoginPassword);
        btnLogin = (Button) view.findViewById(R.id.btnLogin);
        btnClose = (Button) view.findViewById(R.id.btnClose);
        btnForgotPass = (Button) view.findViewById(R.id.btnForgotPass);
        btnRegister = (Button) view.findViewById(R.id.btnRegister);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogin:
                if (tbUserLogin.getText().length() != 0 && tbPasswordLogin.getText().length() != 0) {
                    loginPost();
                } else {
                    Toast.makeText(context, "Please enter username or phonenumber and password.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnClose:
                getActivity().finish();
                getActivity().overridePendingTransition(R.anim.non_anima, R.anim.exit_to_right);
                break;
            case R.id.btnForgotPass:
                break;
            case R.id.btnRegister:
                RegisterFragment fragmentRegister = new RegisterFragment();
                FragmentTransaction transaction =  getActivity().getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
                transaction.replace(R.id.rootLoginFrame,fragmentRegister, "Register");
                transaction.addToBackStack(null);
                transaction.commit();
                break;
        }
    }

    private void loginPost() {

        APIOneToAll checkLogin = new APIOneToAll();

        final RequestParams params = new RequestParams();
        params.put("auth_user", "1dev");
        params.put("auth_pwd", "onetoall");
        params.put("user_name", tbUserLogin.getText().toString());
        params.put("password", tbPasswordLogin.getText().toString());
        params.put("action", "login");

        checkLogin.post(apiService, params, new JsonHttpResponseHandler() {

            @Override
            public void onStart() {
                super.onStart();
                pDialog = new ProgressDialog(context);
                pDialog.setMessage("Attempting login...");
                pDialog.setIndeterminate(false);
                pDialog.setCancelable(false);
                pDialog.show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.dismiss();
                }
                //Toast.makeText(context, response.toString(), Toast.LENGTH_LONG).show();
                log.e("Reponse Success", response.toString());
                if (response != null) {
                    try {
                        rsStatus = response.getString("status");
                        rsDetail = response.getString("detail");
                        rsUsername = response.getString("username");
                        rsFirstname = response.getString("firstname");
                        rsLastname = response.getString("lastname");
                        //3003:Success,
                        if (rsStatus.equals("3003")) {
                            //Toast.makeText(context, "Login Successful.", Toast.LENGTH_SHORT).show();

                            //Set preferences
                            sharedPreferences = getActivity().getSharedPreferences(MY_PREFS, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("user", rsUsername);
                            editor.putString("first_name", rsFirstname);
                            editor.putString("last_name", rsLastname);
                            editor.putBoolean("login", true);
                            editor.commit();

                            Intent inGotoMain = new Intent(context, MainActivity.class);
                            inGotoMain.putExtra("NewStartAct", true);
                            inGotoMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(inGotoMain);
                            getActivity().overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
                            //Close form login
                            getActivity().finish();
                        } else {
                            Toast.makeText(context, "Please check username and password.", Toast.LENGTH_SHORT).show();
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
