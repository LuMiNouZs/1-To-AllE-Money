package com.project.onetoall.android.emoney.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.onetoall.android.emoney.MainActivity;
import com.project.onetoall.android.emoney.R;
import com.project.onetoall.android.emoney.adapters.MenuTopupAdapter;

/**
 * Created by c.anupol on 8/2/17.
 */

public class MenuTopupFragment extends Fragment implements AdapterView.OnItemClickListener {

    private GridView gvMenuTopup;
    private ImageView ivBack;
    private TextView txtNavigation;
    private String resultChooseTopup;
    private static final String TAG_TOPUP_FRAGMENT = "topup_fragment";
    public MenuTopupFragment newInstance() {
        return new MenuTopupFragment();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_menu_topup,container, false);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUI(view);
        //Set title navigation
        ((MainActivity) getActivity()).setNovigationText(R.string.title_topup,View.TEXT_ALIGNMENT_TEXT_START);
        //((MainActivity) getActivity()).setInvisibleBackButton(true);
        //((MainActivity) getActivity()).setInvisibleContentNews(false);
        gvMenuTopup.setAdapter(new MenuTopupAdapter(view.getContext()));
        gvMenuTopup.setOnItemClickListener(this);
    }

    private void initUI(View view){
        gvMenuTopup = (GridView) view.findViewById(R.id.gvMenuTopup);
        ivBack = (ImageView) view.findViewById(R.id.ivBack);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position){
            case 0:
                resultChooseTopup = "Metfone";
                break;
            case 1:
                resultChooseTopup = "AIS";
                break;
            case 2:
                resultChooseTopup = "DTAC";
                break;
            case 3:
                resultChooseTopup = "TRUE";
                break;
        }



        Bundle bundle=new Bundle();
        bundle.putString("provider", resultChooseTopup);
        TopupFragment fragment = new TopupFragment();
        fragment.setArguments(bundle);



        FragmentTransaction transaction =  getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        transaction.replace(R.id.rootFrame, fragment, TAG_TOPUP_FRAGMENT);
        transaction.addToBackStack(null);
        transaction.commit();

    }


}
