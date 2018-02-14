package com.project.onetoall.android.emoney.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.onetoall.android.emoney.R;
import com.project.onetoall.android.emoney.fragments.MainMenuFragment;

/**
 * Created by c.anupol on 8/9/17.
 */

public class MainMenuAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mImflater;
    // references to our images
    private int[] mThumbIds = {
            R.drawable.ic_action_balance, R.drawable.ic_action_topup, R.drawable.ic_action_transfer,
            R.drawable.ic_action_refill,R.drawable.ic_action_utility , R.drawable.ic_action_report
    };
    private int [] mThumbName = {
            R.string.main_menu_check_balance, R.string.main_menu_topup, R.string.main_menu_transfer,
            R.string.main_menu_refill_card, R.string.main_menu_utility, R.string.main_menu_report
    };


    public MainMenuAdapter(Context context){
        mContext = context;
    }

    @Override
    public int getCount() {
        return mThumbIds.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public class Holder{
        TextView txtTitleMainMenu;
        ImageView ivMainMenu;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Holder holder = new Holder();
        convertView = null;
        if(convertView == null){
            mImflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mImflater.inflate(R.layout.item_main_manu_list,parent,false);
            holder.txtTitleMainMenu = (TextView) convertView.findViewById(R.id.txtMainMenu);
            holder.ivMainMenu = (ImageView) convertView.findViewById(R.id.ivMainMenu);
            convertView.setTag(holder);
        }else{
            holder = (Holder) convertView.getTag();
        }

        holder.ivMainMenu.setImageResource(mThumbIds[position]);
        holder.txtTitleMainMenu.setText(mThumbName[position]);

        /*
        ImageView iv;
        //TextView txtTitle = null;
        if (convertView == null) {
            iv = new ImageView(mContext);
            iv.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 300));
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            iv.setPadding(2, 2, 2, 2);
            iv.setBackgroundColor(Color.WHITE);
        } else {
            iv = (ImageView) convertView;
        }

        iv.setImageResource(mThumbIds[position]);
        //txtTitle.setText(mThumbIds[position]);
        return iv;
        */
        return convertView;
    }
}
