package com.customview.keyboard;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;


/**
 * chenfeiyue 2015.7.9
 * 自定义密码输入框Dialog
 */
public class CustomPasswordDialog extends BaseDialog implements View.OnClickListener {
    private CustomPasswordEditText mEditText;

    private KeyboardHelper helper;

    private View rootView;

    // 点击确定
    private boolean onPositiveClick = false;
    private int passwordLength = 6;


    public CustomPasswordDialog(Context context) {
        super(context, R.style.ListDialog);
    }


    public CustomPasswordDialog(Context context, KeyboardHelper helper) {
        super(context, R.style.ListDialog);
        this.helper = helper;
    }

    public CustomPasswordDialog(Context context, int theme) {
        super(context, theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.custom_view_password);
        initKeyboard();
    }


    public CustomPasswordEditText getPasswordEditText() {
        return mEditText;
    }


    public View getRootView() {
        return rootView;
    }


    private void initKeyboard() {
        rootView = findViewById(R.id.root_view);

        mEditText = (CustomPasswordEditText) findViewById(R.id.password_text);

        passwordLength = mEditText.getPasswordLength();

        if (helper != null) {
            mEditText.setOnPasswordListener(helper);
        }

        findViewById(R.id.txt_cancel).setOnClickListener(this);

        final TextView okTextView = (TextView) findViewById(R.id.txt_ok);
        okTextView.setOnClickListener(this);
        changeTextView(okTextView, 0);

        mEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                int length = s.toString().length();

                changeTextView(okTextView, length);
            }
        });
        
        /* 
         * 获取圣诞框的窗口对象及参数对象以修改对话框的布局设置,
         * 可以直接调用getWindow(),表示获得这个Activity的Window
         * 对象,这样这可以以同样的方式改变这个Activity的属性.
         */
//        Window dialogWindow = this.getWindow();
//        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//        dialogWindow.setGravity(Gravity.LEFT | Gravity.TOP);

        /*
         * lp.x与lp.y表示相对于原始位置的偏移.
         * 当参数值包含Gravity.LEFT时,对话框出现在左边,所以lp.x就表示相对左边的偏移,负值忽略.
         * 当参数值包含Gravity.RIGHT时,对话框出现在右边,所以lp.x就表示相对右边的偏移,负值忽略.
         * 当参数值包含Gravity.TOP时,对话框出现在上边,所以lp.y就表示相对上边的偏移,负值忽略.
         * 当参数值包含Gravity.BOTTOM时,对话框出现在下边,所以lp.y就表示相对下边的偏移,负值忽略.
         * 当参数值包含Gravity.CENTER_HORIZONTAL时
         * ,对话框水平居中,所以lp.x就表示在水平居中的位置移动lp.x像素,正值向右移动,负值向左移动.
         * 当参数值包含Gravity.CENTER_VERTICAL时
         * ,对话框垂直居中,所以lp.y就表示在垂直居中的位置移动lp.y像素,正值向右移动,负值向左移动.
         * gravity的默认值为Gravity.CENTER,即Gravity.CENTER_HORIZONTAL |
         * Gravity.CENTER_VERTICAL.
         * 
         * 本来setGravity的参数值为Gravity.LEFT | Gravity.TOP时对话框应出现在程序的左上角,但在
         * 我手机上测试时发现距左边与上边都有一小段距离,而且垂直坐标把程序标题栏也计算在内了,
         * Gravity.LEFT, Gravity.TOP, Gravity.BOTTOM与Gravity.RIGHT都是如此,据边界有一小段距离
         */
//        lp.x = 100; // 新位置X坐标
//        lp.y = 100; // 新位置Y坐标
//        lp.width = 300; // 宽度
//        lp.height = 300; // 高度
//        lp.alpha = 0.7f; // 透明度

        // 当Window的Attributes改变时系统会调用此函数,可以直接调用以应用上面对窗口参数的更改,也可以用setAttributes
        // dialog.onWindowAttributesChanged(lp);
//        dialogWindow.setAttributes(lp);

        /*
         * 将对话框的大小按屏幕大小的百分比设置
         */
//        WindowManager m = getWindowManager();
//        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
//        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
//        p.height = (int) (d.getHeight() * 0.6); // 高度设置为屏幕的0.6
//        p.width = (int) (d.getWidth() * 0.65); // 宽度设置为屏幕的0.65
//        dialogWindow.setAttributes(p);


        // mKeyboardWindow = new
        // PopupWindow(mKeyboardView,ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        // mKeyboardWindow.setAnimationStyle(R.style.AnimationFade);
        // mKeyboardWindow.setOnDismissListener(new OnDismissListener() {
        //
        // @Override
        // public void onDismiss() {
        // // TODO Auto-generated method stub
        // if(scrolldis>0){
        // int temp=scrolldis;
        // scrolldis=0;
        // if(null != mContentView){
        // mContentView.scrollBy(0, -temp);
        // }
        // }
        // }
        // });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.txt_cancel) {
            if (helper != null) {
                helper.cancelDialog();
            }

        } else if (i == R.id.txt_ok) {
            onPositiveClick = true;

            if (listener != null) {
                listener.onPositiveClick(mEditText.getText().toString());
            }

            if (helper != null) {
                helper.cancelDialog();
            }
        }

    }

    @Override
    public void setOnDismissListener(DialogInterface.OnDismissListener listener) {

        super.setOnDismissListener(listener);
    }


    private ButtonOnClickListener listener;

    public interface ButtonOnClickListener {
        void onPositiveClick(String password);

        void onNegativeClick();
    }

    public void setOnButtonOnClickListener(ButtonOnClickListener listener) {
        this.listener = listener;
    }


    @Override
    public void dismiss() {
        super.dismiss();

        if (listener != null && !onPositiveClick) {
            listener.onNegativeClick();
        }
        onPositiveClick = false;
        mEditText.getEditableText().clear();
    }


    /**
     * 密码 0 或 6 位可用
     *
     * @param okTextView
     * @param length
     */
    private void changeTextView(TextView okTextView, int length) {

        if (length == 0) {
            okTextView.setEnabled(true);
        } else {
            okTextView.setEnabled(length < passwordLength ? false : true);
        }
    }
}
