package com.customview.keyboard;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.Keyboard.Key;
import android.inputmethodservice.KeyboardView;
import android.os.SystemClock;
import android.util.AttributeSet;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * chenfeiyue 2015.7.9
 * 自定义数字键盘
 */
public class CustomKeyboardView extends KeyboardView
{
    
    private Drawable mKeybgDrawable;
    
//    private Drawable mOpKeybgDrawable;
    
    private Resources res;
    
    private Keyboard mKeyboard;
    
    private boolean randomkeys;

    private Paint paint;

    
    public CustomKeyboardView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        initResources(context, attrs);
        // TODO Auto-generated constructor stub
    }
    
    public CustomKeyboardView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        initResources(context, attrs);
        // TODO Auto-generated constructor stub
    }
    
    private void initResources(Context context, AttributeSet attrs)
    {
        res = context.getResources();
        
        mKeybgDrawable = res.getDrawable(R.drawable.custom_keyboard_key);
//        mOpKeybgDrawable = res.getDrawable(R.drawable.btn_keyboard_opkey);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(50);
        paint.setColor(res.getColor(R.color.black));
        initKeyboard(context, attrs);
    }
    
    private void initKeyboard(Context context, AttributeSet attrs)
    {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.keyboard);

        if (a.hasValue(R.styleable.keyboard_xml))
        {
            int xmlid = a.getResourceId(R.styleable.keyboard_xml, 0);
            
            mKeyboard = new Keyboard(context, xmlid);
            
            if (a.hasValue(R.styleable.keyboard_randomkeys))
            {
                boolean random = a.getBoolean(R.styleable.keyboard_randomkeys, false);
                randomkeys = random;
                
                if (random)
                {
                    randomdigkey(mKeyboard);
                }
            }
            
            setKeyboard(mKeyboard);
            setEnabled(true);
            setPreviewEnabled(false);
        }
        a.recycle();
        
    }


    public void setTypeFace(Typeface keyBoardFont){
        paint.setTypeface(keyBoardFont);
        invalidate();
    }
    
    @Override
    public void onDraw(Canvas canvas)
    {
        List<Key> keys = getKeyboard().getKeys();
        for (Key key : keys)
        {
            canvas.save();
            
            int offsety = 0;
            if (key.y == 0)
            {
                offsety = 1;
            }
            
            int initdrawy = key.y + offsety;
            
            Rect rect = new Rect(key.x, initdrawy, key.x + key.width, key.y + key.height);
            
            canvas.clipRect(rect);
            
//            int primaryCode = -1;
//            if (null != key.codes && key.codes.length != 0)
//            {
//                primaryCode = key.codes[0];
//            }
//            
            Drawable dr = null;
//            if (primaryCode == -3 || key.codes[0] == -5)
//            {
//                dr = mKeybgDrawable;
//            }
//            else if (-1 != primaryCode)
//            {
                dr = mKeybgDrawable;
//            }
            
            if (null != dr)
            {
                int[] state = key.getCurrentDrawableState();
                
                dr.setState(state);
                dr.setBounds(rect);
                dr.draw(canvas);
            }
            

            
            if (key.label != null)
            {
                canvas.drawText(key.label.toString(),
                    key.x + (key.width / 2),
                    initdrawy + (key.height + paint.getTextSize() - paint.descent()) / 2,
                    paint);
            }
            else if (key.icon != null)
            {
                int intriWidth = key.icon.getIntrinsicWidth();
                int intriHeight = key.icon.getIntrinsicHeight();
                
                final int drawableX = key.x + (key.width - intriWidth) / 2;
                final int drawableY = initdrawy + (key.height - intriHeight) / 2;
                
                key.icon.setBounds(drawableX, drawableY, drawableX + intriWidth, drawableY + intriHeight);
                
                key.icon.draw(canvas);
            }
            
            canvas.restore();
        }
        
    }
    
    private void randomdigkey(Keyboard mKeyboard)
    {
        if (mKeyboard == null)
        {
            return;
        }
        
        List<Key> keyList = mKeyboard.getKeys();
        
        // 查找出0-9的数字键
        List<Key> newkeyList = new ArrayList<Key>();
        for (int i = 0, size = keyList.size(); i < size; i++)
        {
            Key key = keyList.get(i);
            CharSequence label = key.label;
            if (label != null && isNumber(label.toString()))
            {
                newkeyList.add(key);
            }
        }
        
        int count = newkeyList.size();
        
        List<KeyModel> resultList = new ArrayList<KeyModel>();
        
        LinkedList<KeyModel> temp = new LinkedList<KeyModel>();
        
        for (int i = 0; i < count; i++)
        {
            temp.add(new KeyModel(48 + i, i + ""));
        }
        
        Random rand = new SecureRandom();
        rand.setSeed(SystemClock.currentThreadTimeMillis());
        
        for (int i = 0; i < count; i++)
        {
            int num = rand.nextInt(count - i);
            KeyModel model = temp.get(num);
            resultList.add(new KeyModel(model.getCode(), model.getLable()));
            temp.remove(num);
        }
        
        for (int i = 0, size = newkeyList.size(); i < size; i++)
        {
            Key newKey = newkeyList.get(i);
            KeyModel resultmodle = resultList.get(i);
            newKey.label = resultmodle.getLable();
            newKey.codes[0] = resultmodle.getCode();
        }
        
    }
    
    class KeyModel
    {
        
        private Integer code;
        
        private String label;
        
        public KeyModel(Integer code, String lable)
        {
            this.code = code;
            this.label = lable;
        }
        
        public Integer getCode()
        {
            return code;
        }
        
        public void setCode(Integer code)
        {
            this.code = code;
        }
        
        public String getLable()
        {
            return label;
        }
        
        public void setLabel(String lable)
        {
            this.label = lable;
        }
        
    }
    
    private boolean isNumber(String str)
    {
        String wordstr = "0123456789";
        if (wordstr.indexOf(str) > -1)
        {
            return true;
        }
        return false;
    }
    
    
    public void randomKeys(){
        if (randomkeys)
        {
            randomdigkey(mKeyboard);
        }
        
        setKeyboard(mKeyboard);
    }
    
}
