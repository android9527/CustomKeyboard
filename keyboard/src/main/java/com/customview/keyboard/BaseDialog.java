package com.customview.keyboard;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;

/**
 *
 */
public class BaseDialog extends Dialog {

	private Context mContext;

	public BaseDialog(Context context) {
		super(context);
		mContext = context;
	}

	public BaseDialog(Context context, int theme) {
		super(context, theme);
		mContext = context;
	}

	@Override
	public void show() {
		if (!isActivityValid()) {
			return;
		}

		try {
			super.show();
		} catch (Exception e) {
		}
	}

	private boolean isActivityValid() {

		if (null != mContext && mContext instanceof Activity) {
			Activity at = (Activity) mContext;
			if (at.isFinishing()) {
				// /< 是 activity，但已finish
				return false;
			} else {
				// /< 是activity，还在运行中...
				return true;
			}
		}

		/**
		 * 由于有些Dialog的Context非Activity，例如MobogenieService里面的垃圾提示Dialog，因此默认返回true吧
		 */
		return true;
	}

	@Override
	public void dismiss() {
		if (!isActivityValid()) {
			return;
		}
		try {
			super.dismiss();
		} catch (Exception e) {
		}
	}

}
