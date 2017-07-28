package com.customview.keyboard;

import android.content.Context;
import android.graphics.Typeface;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.Keyboard.Key;
import android.inputmethodservice.KeyboardView;
import android.os.IBinder;
import android.text.Editable;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;

import java.util.List;

/**
 * chenfeiyue 2015.7.9
 * 自定义键盘帮助类
 */
public class KeyboardHelper implements KeyboardView.OnKeyboardActionListener,
        CustomKeyboardEditText.PasswordListener {
    private Context context;

    private CustomPasswordDialog dialog;

    private CustomKeyboardView mKeyboardView;
    private PopupWindow mKeyboardWindow;

    /**
     * dialog Y轴偏移量
     */
    private static final float OFFSET_Y = 100f;

    private int screenw;

    public KeyboardHelper(Context context) {
        this.context = context;
        initScreenParams(context);
    }


    /**
     * 展示密码输入框
     */
    public void showPasswordDialog() {
        showPasswordDialog(true);
    }


    /**
     * 展示密码输入框
     */
    public void showPasswordDialog(boolean isShowKeyboard) {
        if (dialog == null) {
            dialog = new CustomPasswordDialog(context, this);
            dialog.setCanceledOnTouchOutside(false);
        }

        if (dialog.isShowing()) {
            return;
        }
        dialog.show();
        restore();


        if(isShowKeyboard){
            dialog.getRootView().post(new Runnable() {
                @Override
                public void run() {
                    showKeyboard();
                }
            });
        }
    }




    private void resize() {

        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams layoutParams = dialogWindow.getAttributes();

        layoutParams.x = 0;
        layoutParams.y -= dpToPx(context, OFFSET_Y);
        dialogWindow.setAttributes(layoutParams);
    }


    /**
     * 恢复默认位置
     */
    private void restore() {
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams layoutParams = dialogWindow.getAttributes();

        layoutParams.x = 0;
        layoutParams.y = 0;

        dialogWindow.setAttributes(layoutParams);
    }

    /**
     * 获取屏幕宽高
     * @param context
     */
    private void initScreenParams(Context context) {
        DisplayMetrics dMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        display.getMetrics(dMetrics);
        screenw = dMetrics.widthPixels;
    }

    /**
     * 密度转换为像素值
     *
     * @param dp
     * @return
     */
    public int dpToPx(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    /**
     * 初始化键盘PopupWindow
     */
    private void initKeyPWindow() {
        if (mKeyboardWindow == null) {
            View view = LayoutInflater.from(context).inflate(
                    R.layout.custom_keyboardview, null);
            mKeyboardView = (CustomKeyboardView) view
                    .findViewById(R.id.keyboardview);
            mKeyboardView.setTypeFace(Typeface.createFromAsset(context.getAssets(), "Roboto_LightItalic.ttf"));
            mKeyboardView.setOnKeyboardActionListener(this);

            mKeyboardWindow = new PopupWindow(view, screenw,
                    ViewGroup.LayoutParams.WRAP_CONTENT);

            mKeyboardWindow.setAnimationStyle(R.style.AnimationFade);
            // mKeyboardWindow.setBackgroundDrawable(new BitmapDrawable());
            // mKeyboardWindow.setOutsideTouchable(true);
            mKeyboardWindow.setOnDismissListener(new OnDismissListener() {

                @Override
                public void onDismiss() {

                    restore();
                }
            });

            mKeyboardWindow.setOutsideTouchable(false);
        }
    }

    /**
     * 展示键盘
     */
    private void showKeyboard() {
        initKeyPWindow();
        if (null != mKeyboardWindow && !mKeyboardWindow.isShowing()) {
            resize();
            mKeyboardView.randomKeys();

            // mKeyboardWindow.showAtLocation(this.mDecorView, Gravity.BOTTOM,
            // 0, 0);
            // mKeyboardWindow.update();
            mKeyboardWindow.getContentView().measure(0, 0);
            int height = mKeyboardWindow.getContentView().getMeasuredHeight();
            // 3、调用showAtLocation方法时，第一个参数传Dialog上的View，比如edit，调用方法如下：
            // showAtLocation(edit,Gravity.BOTTOM,0,-height

            mKeyboardWindow.showAtLocation(dialog.getRootView(),
                    Gravity.BOTTOM, 0, -1000);

            mKeyboardWindow.update();

        }
    }

    /**
     * 隐藏系统键盘
     */
    private void hideSysInput() {
        IBinder binder = dialog.getPasswordEditText().getWindowToken();
        if (binder != null) {
            InputMethodManager imm = (InputMethodManager) context
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(binder,
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public void onPress(int primaryCode) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onRelease(int primaryCode) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
        // TODO Auto-generated method stub

        EditText passwordEText = dialog.getPasswordEditText();
        if (passwordEText == null) {
            return;
        }
        Editable editable = passwordEText.getText();
        int start = passwordEText.getSelectionStart();
        if (primaryCode == Keyboard.KEYCODE_CANCEL) {// 隐藏键盘
            hideKeyboard();
        } else if (primaryCode == Keyboard.KEYCODE_DELETE) {// 回退
            if (editable != null && editable.length() > 0) {
                if (start > 0) {
                    editable.delete(start - 1, start);
                }
            }
        } else if (0x0 <= primaryCode && primaryCode <= 0x7f) {
            // 可以直接输入的字符(如0-9,.)，他们在键盘映射xml中的keycode值必须配置为该字符的ASCII码
            editable.insert(start, Character.toString((char) primaryCode));
        } else if (primaryCode > 0x7f) {
            Key mkey = getKeyByKeyCode(primaryCode);
            // 可以直接输入的字符(如0-9,.)，他们在键盘映射xml中的keycode值必须配置为该字符的ASCII码
            editable.insert(start, mkey.label);

        } else {
            // 其他一些暂未开放的键指令，如next到下一个输入框等指令
        }
    }

    @Override
    public void onText(CharSequence text) {
        // TODO Auto-generated method stub

    }

    @Override
    public void swipeLeft() {
        // TODO Auto-generated method stub

    }

    @Override
    public void swipeRight() {
        // TODO Auto-generated method stub

    }

    @Override
    public void swipeDown() {
        // TODO Auto-generated method stub

    }

    @Override
    public void swipeUp() {
        // TODO Auto-generated method stub

    }

    private void hideKeyboardWindow() {
        if (null != mKeyboardWindow && mKeyboardWindow.isShowing()) {
            mKeyboardWindow.dismiss();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        hideSysInput();
        showKeyboard();
        return true;
    }

    @Override
    public void hideKeyboard() {
        hideKeyboardWindow();
    }

    private Key getKeyByKeyCode(int keyCode) {
        Keyboard mKeyboard = mKeyboardView.getKeyboard();
        if (null != mKeyboard) {
            List<Key> mKeys = mKeyboard.getKeys();
            for (int i = 0, size = mKeys.size(); i < size; i++) {
                Key mKey = mKeys.get(i);

                int codes[] = mKey.codes;

                if (codes[0] == keyCode) {
                    return mKey;
                }
            }
        }

        return null;
    }

    /**
     * 返回键处理
     */
    @Override
    public boolean onKeyDownBack() {
        if (null != mKeyboardWindow && mKeyboardWindow.isShowing()) {
            mKeyboardWindow.dismiss();
            return true;
        }
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            return true;
        }
        return false;
    }

    /**
     * 隐藏dialog和输入键盘
     */
    public void cancelDialog() {
        hideKeyboardWindow();
        hideDialog();
    }

    /**
     * 隐藏Dialog
     */
    private void hideDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    /**
     * 确定和取消的监听
     * @param listener
     */
    public void setOnButtonOnClickListener(CustomPasswordDialog.ButtonOnClickListener listener) {
        if (dialog != null) {
            dialog.setOnButtonOnClickListener(listener);
        }
    }
}
