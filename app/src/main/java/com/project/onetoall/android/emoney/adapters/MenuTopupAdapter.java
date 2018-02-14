package com.project.onetoall.android.emoney.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.project.onetoall.android.emoney.R;

/**
 * Created by c.anupol on 7/20/17.
 */

public class MenuTopupAdapter extends BaseAdapter {

    private Context mContext;
    // references to our images
    private Integer[] mThumbIds = {
            R.drawable.logo_metfone, R.drawable.logo_ais, R.drawable.logo_dtac , R.drawable.logo_true
    };
    private String [] mThumbName = {
            "Metfone", "AIS", "Dtac", "True"
    };

    public MenuTopupAdapter(Context context) {
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView iv;
        if (convertView == null) {
            iv = new ImageView(mContext);
            iv.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 300));
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            //iv.setPadding(1, 1, 1, 1);
            iv.setBackgroundColor(Color.WHITE);
        } else {
            iv = (ImageView) convertView;
        }

        iv.setImageResource(mThumbIds[position]);
        return iv;
    }


}
