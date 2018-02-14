package com.project.onetoall.android.emoney.fragments;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.project.onetoall.android.emoney.MainActivity;
import com.project.onetoall.android.emoney.R;

/**
 * Created by c.anupol on 10/4/17.
 */

public class ReceiptFragment extends Fragment implements View.OnClickListener, View.OnKeyListener {

    private Context context;
    private TextView txtTransectionRef, txtTransectionResult ,txtTransectionFrom, TxtTransectionFromRef1,
            txtTransectionTo, txtTransectionToRef1, txtTransectionAmount, txtTransectionDate, txtTransectionFee, txtTransectionStatus;
    private Button btnHome;
    private String getAction, getStatus, getAmount, getFee, getDate, getTransectionRef, getFromUser, getFromNumber, getTo, getMessage  ;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_topup_receipt,container, false);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = view.getContext();
        getBundleData();
        initUI(view);
        ((MainActivity) getActivity()).setNovigationText(R.string.title_receipt, View.TEXT_ALIGNMENT_TEXT_START);
        setDataReceipt();
        btnHome.setOnClickListener(this);

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(this);
    }

    private void initUI(View view){
        //txtTransectionStatus = (TextView) view.findViewById(R.id.txtReceiptTitle);
        txtTransectionResult = (TextView) view.findViewById(R.id.txtReceiptTitle);
        txtTransectionRef = (TextView) view.findViewById(R.id.txtReceiptRef);
        txtTransectionDate = (TextView) view.findViewById(R.id.txtReceiptDate);
        txtTransectionFrom = (TextView) view.findViewById(R.id.txtReceiptFrom);
        TxtTransectionFromRef1 = (TextView) view.findViewById(R.id.txtReceiptFomRef1);
        txtTransectionTo = (TextView) view.findViewById(R.id.txtReceiptTo);
        txtTransectionToRef1 = (TextView) view.findViewById(R.id.txtReceiptToRef1);
        txtTransectionAmount = (TextView) view.findViewById(R.id.txtReceiptAmount);
        txtTransectionFee = (TextView) view.findViewById(R.id.txtReceiptFee);
        btnHome = (Button) view.findViewById(R.id.btnReceipToHome);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnReceipToHome:
                Intent inGotoHome = new Intent(context,MainActivity.class);
                startActivity(inGotoHome);
                getActivity().finish();
                break;
        }
    }

    private void setDataReceipt(){
        if(getMessage.equals("success") && getAction.equals("topup") ){
            txtTransectionResult.setText("Topup Success");
            txtTransectionResult.setTextColor(Color.parseColor("#2ECC71"));
        }else{
            txtTransectionResult.setText("Fail");
            txtTransectionResult.setTextColor(Color.parseColor("#F4350F"));
        }

        txtTransectionDate.setText(getDate);
        txtTransectionRef.setText(getTransectionRef);
        txtTransectionFrom.setText(getFromNumber);
        TxtTransectionFromRef1.setText(getFromUser);
        txtTransectionTo.setText(getTo);
        txtTransectionToRef1.setText("");
        txtTransectionAmount.setText(getAmount);
        txtTransectionFee.setText(getFee);
    }

    public void getBundleData() {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            //int myInt = bundle.getInt(provider, defaultValue);
            getAction = bundle.getString("action");
            getStatus = bundle.getString("status");
            getMessage = bundle.getString("message");
            getAmount = bundle.getString("amount");
            getFee = bundle.getString("fee");
            getFromUser = bundle.getString("fromUser");
            getFromNumber = bundle.getString("fromNumber");
            getTo = bundle.getString("to");
            getTransectionRef = bundle.getString("transectionRef");
            getDate = bundle.getString("date");

        }
    }


    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                //Toast.makeText(getActivity(), "Back Pressed", Toast.LENGTH_SHORT).show();
                return true;
            }
        }
        return false;
    }
}
