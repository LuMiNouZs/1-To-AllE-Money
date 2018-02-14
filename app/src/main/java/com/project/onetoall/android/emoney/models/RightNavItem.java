package com.project.onetoall.android.emoney.models;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

/**
 * Created by c.anupol on 8/1/17.
 */

public class RightNavItem {

    private int titile;
    private int drwable;

    public RightNavItem(int title, int drwable) {
        super();
        this.titile = title;
        this.drwable = drwable;
    }

    public int getTitile() {
        return titile;
    }

    public void setTitile(int titile) {
        this.titile = titile;
    }

    public int getDrwable() {
        return drwable;
    }

    public void setDrwable(int drwable) {
        this.drwable = drwable;
    }
}
