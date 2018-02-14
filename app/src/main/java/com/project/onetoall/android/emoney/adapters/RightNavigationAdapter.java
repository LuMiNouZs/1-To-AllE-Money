package com.project.onetoall.android.emoney.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.onetoall.android.emoney.R;
import com.project.onetoall.android.emoney.models.RightNavItem;

import java.util.List;

/**
 * Created by c.anupol on 8/1/17.
 */

public class RightNavigationAdapter extends ArrayAdapter<RightNavItem>{

    private Context context;
    private int resLayout;
    List<RightNavItem> listNavItem;

    public RightNavigationAdapter(@NonNull Context context, @LayoutRes int resource, List<RightNavItem> listNavItem) {
        super(context, resource, listNavItem);

        this.context = context;
        this.resLayout = resource;
        this.listNavItem = listNavItem;
    }

    public View getView(int position, View convertView, ViewGroup parent){

        View v = View.inflate(context, resLayout, null);

        TextView txtTitle = (TextView) v.findViewById(R.id.txtRightNavTitle);
        ImageView ivTitle = (ImageView) v.findViewById(R.id.ivRightNavTitle);
        RightNavItem navItem = listNavItem.get(position);
        txtTitle.setText(navItem.getTitile());
        ivTitle.setImageResource(navItem.getDrwable());

        return v;
    }
}
