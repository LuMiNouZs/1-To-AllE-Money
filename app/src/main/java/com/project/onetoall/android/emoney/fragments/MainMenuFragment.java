package com.project.onetoall.android.emoney.fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.project.onetoall.android.emoney.MainActivity;
import com.project.onetoall.android.emoney.R;
import com.project.onetoall.android.emoney.adapters.ContentNewsAdapter;
import com.project.onetoall.android.emoney.adapters.MainMenuAdapter;
import com.project.onetoall.android.emoney.api.APIOneToAll;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import cz.msebera.android.httpclient.Header;

import static com.loopj.android.http.AsyncHttpClient.log;

/**
 * Created by c.anupol on 8/9/17.
 */
public class MainMenuFragment extends Fragment implements AdapterView.OnItemClickListener, View.OnClickListener {

    private GridView gvMainMenu;
    private static final String TAG_MENU_TOPUP_FRAGMENT = "menu_topup_fragment";
    private String apiService = "API_function.php";
    private Context context;
    private ViewPager vpContentNews;
    private static final String MY_PREFS = "my_prefs";
    private Dialog dialog;
    private ProgressDialog pDialog;
    SharedPreferences sharedPreferences;
    private String rsStatus, rsBalance;

    public MainMenuFragment newInstance() {

        return new MainMenuFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main_menu, container, false);
        return v;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUI(view);

        context = view.getContext();
        //((MainActivity) getActivity()).setNovigationText("Welcome to E-Money by 1-To-All",View.TEXT_ALIGNMENT_CENTER);
        ContentNewsAdapter contentNewsAdapter = new ContentNewsAdapter(context);
        vpContentNews.setAdapter(contentNewsAdapter);

        gvMainMenu.setAdapter(new MainMenuAdapter(view.getContext()));
        gvMainMenu.setOnItemClickListener(this);

        try {
            Timer timer = new Timer();
            timer.scheduleAtFixedRate(new ContentNewsTimeTask(), 2000, 4000);
        }catch (Exception e){

        }


    }

    private void initUI(View view) {
        gvMainMenu = (GridView) view.findViewById(R.id.gvMainmenu);
        vpContentNews = (ViewPager) view.findViewById(R.id.vpContentNews);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (((MainActivity) getActivity()).checkSeestionLogin() == true) {
            switch (position) {
                case 0://check balance
                    checkBalancePost();
                    break;
                case 1://top-up
                    Fragment fragment = new MenuTopupFragment().newInstance();
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
                    transaction.replace(R.id.rootFrame, fragment, TAG_MENU_TOPUP_FRAGMENT);
                    transaction.addToBackStack("MenuTopup");
                    transaction.commit();
                    break;
                case 2://transfer
                    break;
                case 3://refill card
                    break;
                case 4://utility
                    break;
                case 5://report
                    break;
            }
        } else {
            //Toast.makeText(context, "Please login before choose function.", Toast.LENGTH_SHORT).show();
            showDialogNotify("Please login before choose function.");
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnDialogNotifyClose:
                dialog.dismiss();
                break;
            case R.id.btnDialogBalanceClose:
                dialog.dismiss();
                break;
        }
    }

    private void showDialogBalance(String txtResultBalance) {
        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.custom_dialog_check_balance);
        dialog.setCancelable(false);
        //dialog.setTitle("Notification");

        //dialog
        TextView txtDes = (TextView) dialog.findViewById(R.id.txtDialogDesBalance);
        TextView txtBalance = (TextView) dialog.findViewById(R.id.txtDialogCreditBalance);
        txtBalance.setText(txtResultBalance);
        Button btnClose = (Button) dialog.findViewById(R.id.btnDialogBalanceClose);

        btnClose.setOnClickListener(this);

        dialog.show();

    }

    private void checkBalancePost() {
        sharedPreferences = getActivity().getSharedPreferences(MY_PREFS, Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("user", "N/A");

        APIOneToAll cehckBalanceService = new APIOneToAll();

        RequestParams params = new RequestParams();
        params.put("auth_user", "1dev");
        params.put("auth_pwd", "onetoall");
        params.put("user_name", username);
        params.put("action", "balance");
        log.d("CheckParams", params.toString());
        cehckBalanceService.post(apiService, params, new JsonHttpResponseHandler() {

            @Override
            public void onStart() {
                super.onStart();
                pDialog = new ProgressDialog(context);
                pDialog.setMessage("Check Balance...");
                pDialog.setIndeterminate(false);
                pDialog.setCancelable(true);
                pDialog.show();
            }
/*
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
                        rsBalance = response.getString("balance");
                        //3003:Success,
                        if (rsStatus.equals("3003")) {
                            showDialogBalance(rsBalance);
                            //Toast.makeText(context, "Your Balance :" + rsBalance, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Cannot check balnce please contact admin.", Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }

*/
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.dismiss();
                }
                //Toast.makeText(context, response.toString(), Toast.LENGTH_LONG).show();
                log.e("Reponse Success", response.toString());
                if (response != null) {
                    try {
                        rsStatus = response.getJSONObject(0).getString("status");
                        rsBalance = response.getJSONObject(0).getString("balance");
                        //3003:Success,
                        if (rsStatus.equals("3003")) {
                            showDialogBalance(rsBalance);
                            //Toast.makeText(context, "Your Balance :" + rsBalance, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Cannot check balnce please contact admin.", Toast.LENGTH_SHORT).show();
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

    public  class ContentNewsTimeTask extends TimerTask {

        @Override
        public void run() {

            if(getActivity() == null)
                return;

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    switch (vpContentNews.getCurrentItem()){
                        case 0:
                            vpContentNews.setCurrentItem(1);
                            break;
                        case 1:
                            vpContentNews.setCurrentItem(2);
                            break;
                        case 2:
                            vpContentNews.setCurrentItem(0);
                            break;
                    }
                }
            });
        }
    }

    private  void showDialogNotify(String txtDialog){
        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_notify);
        dialog.setCancelable(false);
        TextView txtDetail = (TextView) dialog.findViewById(R.id.txtDialogNotify);
        txtDetail.setText(txtDialog);
        Button btnClose = (Button) dialog.findViewById(R.id.btnDialogNotifyClose);
        btnClose.setOnClickListener(this);

        dialog.show();

    }
}
