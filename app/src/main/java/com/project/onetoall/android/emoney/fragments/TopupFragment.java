package com.project.onetoall.android.emoney.fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.project.onetoall.android.emoney.MainActivity;
import com.project.onetoall.android.emoney.R;
import com.project.onetoall.android.emoney.api.APIOneToAll;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

import static com.loopj.android.http.AsyncHttpClient.log;

/**
 * Created by c.anupol on 8/11/17.
 */

public class TopupFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {


    private Context context;
    private Button btnNext;
    private EditText tbPhoneNumberTopup;
    private Spinner spnValueTopup;
    private ImageView ivBack, ivTopupProvider;
    private String getProvider, getAmount;
    private ArrayAdapter<CharSequence> adapter;
    private String apiService = "API_function.php";
    private String apiTypeService = "";
    private String rsAction, rsStatus, rsAmount, rsFee, rsDate, rsTransectionRef, rsFromUser, rsFromNumber, rsTo, rsMessage;
    private ProgressDialog pDialog;
    private static final String MY_PREFS = "my_prefs";
    private String payProvider;
    SharedPreferences sharedPreferences;
    FragmentManager fm;
    private Dialog dialog;

    public TopupFragment newInstance() {
        return new TopupFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_topup, container, false);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = view.getContext();
        getBundleData();
        iniUI(view);
        checkProvider(getProvider);

        //ivBack.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        spnValueTopup.setOnItemSelectedListener(this);
    }

    private void iniUI(View view) {
        btnNext = (Button) view.findViewById(R.id.btnNextTopup);
        tbPhoneNumberTopup = (EditText) view.findViewById(R.id.tbPhoneNumberTopup);
        spnValueTopup = (Spinner) view.findViewById(R.id.spnValueTopup);
        //ivBack = (ImageView) findViewById(R.id.ivBack);
        ivTopupProvider = (ImageView) view.findViewById(R.id.ivTopupProvider);
        //txtNavigation = (TextView) findViewById(R.id.txtNavigationName);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnNextTopup:
                if (tbPhoneNumberTopup.getText().toString().equals("")) {
                    Toast.makeText(context, "Plese enter phonenumber your need to topup.", Toast.LENGTH_SHORT).show();
                } else if (tbPhoneNumberTopup.getText().length() < 10) {
                    Toast.makeText(context, "Wrong format phonenumber, Plese check. ", Toast.LENGTH_SHORT).show();
                } else {
                    showDialog(getAmount + " credits to " + tbPhoneNumberTopup.getText().toString() + " ?");
                }
                break;
            case R.id.btnDialogTopupYes:
                topupPost();
                dialog.dismiss();
                break;
            case R.id.btnDialogTopupNo:
                dialog.dismiss();
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        getAmount = spnValueTopup.getItemAtPosition(position).toString();
        //Toast.makeText(context,getAmount,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        getAmount = "";
    }

    public void getBundleData() {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            //int myInt = bundle.getInt(provider, defaultValue);
            getProvider = bundle.getString("provider");
        }
    }

    public void checkProvider(String provider) {
        switch (provider) {
            case "Metfone":
                //txtNavigation.setText(" Topup > AIS");
                ((MainActivity) getActivity()).setNovigationText(R.string.title_topup_metfone, View.TEXT_ALIGNMENT_TEXT_START);
                ivTopupProvider.setImageResource(R.drawable.logo_metfone);
                if (adapter == null) {
                    adapter = ArrayAdapter.createFromResource(this.getActivity(), R.array.ais_array, android.R.layout.simple_spinner_item);
                }
                //apiTypeService = "metfone";
                payProvider = "metfone";
                break;
            case "AIS":
                //txtNavigation.setText(" Topup > AIS");
                ((MainActivity) getActivity()).setNovigationText(R.string.title_topup_ais, View.TEXT_ALIGNMENT_TEXT_START);
                ivTopupProvider.setImageResource(R.drawable.logo_ais);
                if (adapter == null) {
                    adapter = ArrayAdapter.createFromResource(this.getActivity(), R.array.ais_array, android.R.layout.simple_spinner_item);
                }
                //apiTypeService = "wepay";
                payProvider = "ais";
                break;
            case "DTAC":
                //txtNavigation.setText(" Topup > DTAC");
                ((MainActivity) getActivity()).setNovigationText(R.string.title_topup_dtac, View.TEXT_ALIGNMENT_TEXT_START);
                ivTopupProvider.setImageResource(R.drawable.logo_dtac);
                if (adapter == null) {
                    adapter = ArrayAdapter.createFromResource(context, R.array.dtac_array, android.R.layout.simple_spinner_item);
                }
                //apiTypeService = "wepay";
                payProvider = "dtac";
                break;
            case "TRUE":
                //txtNavigation.setText(" Topup > TRUE");
                ((MainActivity) getActivity()).setNovigationText(R.string.title_topup_true, View.TEXT_ALIGNMENT_TEXT_START);
                ivTopupProvider.setImageResource(R.drawable.logo_true);
                if (adapter == null) {
                    adapter = ArrayAdapter.createFromResource(context, R.array.true_array, android.R.layout.simple_spinner_item);
                }
                //apiTypeService = "wepay";
                payProvider = "truemh";
                break;
        }
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnValueTopup.setAdapter(adapter);
    }

    private void topupPost() {
        sharedPreferences = getActivity().getSharedPreferences(MY_PREFS, Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("user", "N/A");

        APIOneToAll topupService = new APIOneToAll();

        RequestParams params = new RequestParams();
        params.put("auth_user", "1dev");
        params.put("auth_pwd", "onetoall");
        params.put("user_name", username);
        //params.put("api_service", apiTypeService);
        params.put("pay_provider", payProvider);
        params.put("pay_amount", getAmount);
        params.put("pay_phone_number", tbPhoneNumberTopup.getText().toString());
        params.put("action", "topup");
        log.d("CheckParams", params.toString());
        topupService.post(apiService, params, new JsonHttpResponseHandler() {

            @Override
            public void onStart() {
                super.onStart();
                pDialog = new ProgressDialog(context);
                pDialog.setMessage("Topup request...");
                pDialog.setIndeterminate(false);
                pDialog.setCancelable(false);
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
                                    rsMessage = response.getString("msg_status");
                                    rsAmount = response.getString("pay_amount");
                                    rsFrom = response.getString("username");
                                    rsTo = response.getString("pay_phone_number");
                                    rsTransectionRef = response.getString("dest_ref");
                                    rsDate = response.getString("log_date");
                                    rsFee = response.getString("fee_amount");
                                    //3003:Success,
                                    if (rsStatus.equals("3003")) {
                                        Toast.makeText(context, "Topup Successful.", Toast.LENGTH_SHORT).show();
                                        Bundle bundle = new Bundle();
                                        bundle.putString("status", rsStatus);
                                        ReceiptFragment receiptFrag = new ReceiptFragment();
                                        receiptFrag.setArguments(bundle);

                                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                                        transaction.replace(R.id.rootFrame, receiptFrag, "Receipt");
                                        transaction.addToBackStack(null);
                                        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                                        transaction.commit();

                                    } else {
                                        Toast.makeText(context, "Please check your balance.", Toast.LENGTH_SHORT).show();
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

                        //response.getJSONObject(0).getString("name");
                        //JSONArray jsonarray = new JSONArray(response);
                        //for (int i = 0; i < jsonarray.length(); i++) {
                        //JSONObject jsonObject = jsonarray.getJSONObject(0);
                        rsAction = response.getJSONObject(0).getString("action");
                        rsStatus = response.getJSONObject(0).getString("status");
                        rsMessage = response.getJSONObject(0).getString("msg_status");
                        rsAmount = response.getJSONObject(0).getString("pay_amount");
                        rsFromUser = response.getJSONObject(0).getString("username");
                        rsFromNumber = response.getJSONObject(0).getString("phone_number_client");
                        rsTo = response.getJSONObject(0).getString("pay_phone_number");
                        rsTransectionRef = response.getJSONObject(0).getString("dest_ref");
                        rsDate = response.getJSONObject(0).getString("log_date");
                        rsFee = response.getJSONObject(0).getString("fee_amount");
                        //}
                        //3003:Success,
                        if (rsStatus.equals("3003")) {
                            Toast.makeText(context, "Topup Successful.", Toast.LENGTH_SHORT).show();
                            Bundle bundle = new Bundle();
                            bundle.putString("action",rsAction);
                            bundle.putString("status", rsStatus);
                            bundle.putString("message", rsMessage);
                            bundle.putString("amount", rsAmount);
                            bundle.putString("fromUser", rsFromUser);
                            bundle.putString("fromNumber", rsFromNumber);
                            bundle.putString("to", rsTo);
                            bundle.putString("transectionRef", rsTransectionRef);
                            bundle.putString("date", rsDate);
                            bundle.putString("fee", rsFee);

                            ReceiptFragment receiptFrag = new ReceiptFragment();
                            receiptFrag.setArguments(bundle);

                            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                            transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
                            transaction.replace(R.id.rootFrame, receiptFrag, "Receipt");
                            transaction.addToBackStack(null);
                            //transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                            transaction.commit();

                        } else {

                            Toast.makeText(context, "Please check your balance.", Toast.LENGTH_SHORT).show();
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

    private void showDialog(String txtDialog) {
        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_topup_notify);
        //dialog.setTitle("Notification");

        //dialog
        TextView txtDes = (TextView) dialog.findViewById(R.id.txtDialogTopupDescription);
        TextView txtDetail = (TextView) dialog.findViewById(R.id.txtDialogTopupDetail);
        txtDetail.setText(txtDialog);
        Button btnYes = (Button) dialog.findViewById(R.id.btnDialogTopupYes);
        Button btnNo = (Button) dialog.findViewById(R.id.btnDialogTopupNo);

        btnYes.setOnClickListener(this);
        btnNo.setOnClickListener(this);
        dialog.show();

    }



}
