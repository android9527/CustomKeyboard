package com.baseui.vidget;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.customview.keyboard.CustomPasswordDialog;
import com.customview.keyboard.KeyboardHelper;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private KeyboardHelper keyboardHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.button_show).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_show:
                showPasswordDialog();
                break;
        }
    }

    private void showPasswordDialog() {
        if (keyboardHelper == null) {
            keyboardHelper = new KeyboardHelper(MainActivity.this);
        }

        keyboardHelper.showPasswordDialog();
        keyboardHelper.setOnButtonOnClickListener(new CustomPasswordDialog.ButtonOnClickListener() {
            @Override
            public void onPositiveClick(final String password) {
                Log.d(TAG, "native password" + password);
            }

            @Override
            public void onNegativeClick() {
                Log.d(TAG, "dialog cancel");
            }
        });
    }
}
