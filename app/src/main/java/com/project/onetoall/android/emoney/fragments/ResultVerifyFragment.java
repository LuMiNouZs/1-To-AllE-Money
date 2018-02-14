package com.project.onetoall.android.emoney.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.project.onetoall.android.emoney.MainActivity;
import com.project.onetoall.android.emoney.R;

/**
 * Created by c.anupol on 20/12/17.
 */

public class ResultVerifyFragment extends Fragment implements View.OnClickListener {


    private Button btnGotoHome;
    private Context context;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_otp_complete, container, false);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = view.getContext();
        initUI(view);
        btnGotoHome.setOnClickListener(this);
    }

    private void initUI(View view) {
        btnGotoHome = (Button) view.findViewById(R.id.btnGotoHome);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnGotoHome :
                Intent inGotoMain = new Intent(context, MainActivity.class);
                inGotoMain.putExtra("NewStartAct", true);
                inGotoMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(inGotoMain);
                getActivity().overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
                //Close sestion register and start new act.
                getActivity().finish();
                break;
        }
    }
}
