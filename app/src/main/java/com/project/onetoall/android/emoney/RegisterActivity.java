package com.project.onetoall.android.emoney;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by c.anupol on 3/11/17.
 */

public class RegisterActivity extends Activity{

    private Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);
        context = this;
        initUI();

    }


    private void initUI(){

    }
}
