package com.jdd.android.jdd.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;
import com.jdd.android.jdd.R;
import com.thinkive.android.app_engine.utils.StringUtils;

/**
 * 描述：弹出组件封装类
 *
 * @author 徐鑫炎
 * @version 1.0
 * @corporation
 * @date 2016-02-26
 * @last_edit 2016-02-26
 * @since 1.0
 */
public class PopUpComponentUtil {
    public static Dialog createLoadingProgressDialog(
            Context context, String text,
            boolean canceledOnTouchOutside, boolean setBackgroundTransparent) {
        Dialog dialog = new Dialog(context);
        if (setBackgroundTransparent) {
            dialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
        }
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_loading);
        if (canceledOnTouchOutside) {
            dialog.setCanceledOnTouchOutside(true);
        }
        ((TextView) dialog.findViewById(R.id.tv_loading)).setText(text);

        return dialog;
    }

    public static void showShortToast(Context context, String text) {
        if (StringUtils.isEmpty(text)) {
            return;
        }
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    public static void showLongToast(Context context, String text) {
        if (StringUtils.isEmpty(text)) {
            return;
        }
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }
}
