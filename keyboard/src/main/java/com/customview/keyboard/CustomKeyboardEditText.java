/**
 *
 */
package com.customview.keyboard;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.IBinder;
import android.util.AttributeSet;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * 自定义键盘，有按下效果
 * chenfeiyue
 */
@SuppressLint("AppCompatCustomView")
public class CustomKeyboardEditText extends EditText {


    private Context mContext;

    /**
     * @param context
     * @param attrs
     */
    public CustomKeyboardEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initAttributes(context);
    }

    /**
     * @param context
     * @param attrs
     * @param defStyle
     */
    public CustomKeyboardEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        initAttributes(context);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        requestFocus();
        requestFocusFromTouch();
        if (listener != null) {
            listener.onTouchEvent(event);
        }
        return true;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (listener != null) {
                return listener.onKeyDownBack();
            }
        }

        return super.onKeyDown(keyCode, event);
    }

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        hideSysInput();
    }

    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        if (listener != null) {
            listener.hideKeyboard();
        }
    }

    private void hideSysInput() {
        IBinder binder = getWindowToken();
        if (binder != null) {
            InputMethodManager imm = (InputMethodManager) mContext
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(binder,
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private void initAttributes(Context context) {
        // initScreenParams(context);
        this.setLongClickable(false);
        this.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
        removeCopyAbility();

        if (this.getText() != null) {
            this.setSelection(this.getText().length());
        }

        this.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                if (!hasFocus) {
                    if (listener != null) {
                        listener.hideKeyboard();
                    }
                }
            }
        });

    }

    @TargetApi(11)
    private void removeCopyAbility() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            this.setCustomSelectionActionModeCallback(new ActionMode.Callback() {

                @Override
                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                    return false;
                }

                @Override
                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                    return false;
                }

                @Override
                public void onDestroyActionMode(ActionMode mode) {

                }

                @Override
                public boolean onActionItemClicked(ActionMode mode,
                                                   MenuItem item) {
                    return false;
                }
            });
        }
    }

    private PasswordListener listener;

    public interface PasswordListener {
        boolean onTouchEvent(MotionEvent event);

        void hideKeyboard();

        boolean onKeyDownBack();
    }

    public void setOnPasswordListener(PasswordListener listener) {
        this.listener = listener;
    }

}
