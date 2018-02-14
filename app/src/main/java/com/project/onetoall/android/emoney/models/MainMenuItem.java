package com.project.onetoall.android.emoney.models;

/**
 * Created by c.anupol on 8/9/17.
 */

public class MainMenuItem {

    private String titleMainMenu;
    private int drawable;

    public MainMenuItem(String titleMainMenu, int drawable){
        super();
        this.titleMainMenu = titleMainMenu;
        this.drawable = drawable;
    }

    public String getTitleMainMenu() {
        return titleMainMenu;
    }

    public void setTitleMainMenu(String titleMainMenu) {
        this.titleMainMenu = titleMainMenu;
    }

    public int getDrawable() {
        return drawable;
    }

    public void setDrawable(int drawable) {
        this.drawable = drawable;
    }
}
