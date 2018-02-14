package com.project.onetoall.android.emoney;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.project.onetoall.android.emoney.api.APIOneToAll;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

import static com.loopj.android.http.AsyncHttpClient.log;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnLogin, btnClose;
    private EditText tbUserLogin, tbPasswordLogin;
    private Context context;
    private ProgressDialog pDialog;
    private String apiService = "API_function.php";
    private String rsStatus, rsDetail, rsUsername, rsFirstname, rsLastname;
    private static final String MY_PREFS = "my_prefs";
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = this;
        initUI();

    }

    private void initUI() {
        tbUserLogin = (EditText) findViewById(R.id.tbLoginUsername);
        tbPasswordLogin = (EditText) findViewById(R.id.tbLoginPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnClose = (Button) findViewById(R.id.btnClose);


        btnLogin.setOnClickListener(this);
        btnClose.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogin:
                if (tbUserLogin.getText().length() != 0 && tbPasswordLogin.getText().length() != 0) {
                    loginPost();
                } else {
                    Toast.makeText(context, "Please enter username and password.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnClose:
                finish();
                overridePendingTransition(R.anim.non_anima, R.anim.exit_to_right);
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
                pDialog.setCancelable(true);
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
                            Toast.makeText(context, "Login Successful.", Toast.LENGTH_SHORT).show();

                            //Set preferences
                            sharedPreferences = getSharedPreferences(MY_PREFS, Context.MODE_PRIVATE);
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
                            overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
                            //Close form login
                            finish();
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
