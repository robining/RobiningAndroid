package com.github.robining.uiimpl.toast;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.robining.uiimpl.R;

/**
 * @create date:2013-7-16
 * @class describe:解决Toast重复弹出
 */
class ToastUtil {
    private enum Type {
        TYPE_TOAST,//普通Toast风格
        TYPE_TIPS,//普通消息类型
        TYPE_ERROR,//错误类型
        TYPE_WARN,//警告类型
        TYPE_SUCCESS//成功类型
    }


    private static String oldMsg;
    private static Type oldType;
    protected static Toast toast = null;
    private static long oneTime = 0;
    private static long twoTime = 0;
    private static Dialog toastDialog = null;

    private static void show(Context context, String s, Type type) {
        if(TextUtils.isEmpty(s)){
            return;
        }

        context = context.getApplicationContext();
        if(toastDialog != null && toastDialog.isShowing()){
            toastDialog.dismiss();
        }
        if (toast == null) {
            toast = Toast.makeText(context, s, Toast.LENGTH_SHORT);
            setToastStyleByType(context, toast, type, s);
            show(context, toast);
            oneTime = System.currentTimeMillis();
        } else {
            twoTime = System.currentTimeMillis();
            if (s.equals(oldMsg) && oldType == type) {
                if (twoTime - oneTime > Toast.LENGTH_SHORT) {
                    toast.show();
                }
            } else {
                setToastStyleByType(context, toast, type, s);
                show(context, toast);
            }
        }
        oneTime = twoTime;
        oldMsg = s;
        oldType = type;
    }

    private static void setToastStyle(Context context, Toast toast, String content) {
        View view = LayoutInflater.from(context).inflate(R.layout.toast_type_toast, null);
        TextView textView = (TextView) view.findViewById(R.id.textContent);
        textView.setText(content);
        toast.setView(view);
        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 100);
    }

    private static void setTipsStyle(Context context, Toast toast, Type type, String content) {
        View view;
        if (oldType != null && type != Type.TYPE_TOAST && oldType != Type.TYPE_TOAST) {
            view = toast.getView();
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.toast_type_tips, null);
        }

        View container = view.findViewById(R.id.toastContainer);
        WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);

        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) container.getLayoutParams();
        lp.width = outMetrics.widthPixels;
        lp.height = outMetrics.heightPixels;
        lp.setMargins(0,0,0,0);
        container.setLayoutParams(lp);

        setTipsViewStyle(view, type, content);
        toast.setView(view);
        toast.setGravity(Gravity.FILL, 0, 0);
    }

    private static void setTipsViewStyle(View view, Type type, String content) {
        TextView textView = (TextView) view.findViewById(R.id.textContent);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageIcon);
        textView.setText(content);
        switch (type) {
            case TYPE_ERROR:
                imageView.setImageResource(R.mipmap.bounced_state_failure);
                break;
            case TYPE_TIPS:
                imageView.setImageResource(R.mipmap.bounced_state_prompt);
                break;
            case TYPE_WARN:
                imageView.setImageResource(R.mipmap.bounced_state_warning);
                break;
            case TYPE_SUCCESS:
                imageView.setImageResource(R.mipmap.bounced_state_successfu);
                break;
            default:
                break;
        }
    }

    private static void setToastStyleByType(Context context, Toast toast, Type type, String content) {
        if (type == Type.TYPE_TOAST) {
            setToastStyle(context, toast, content);
        } else {
            setTipsStyle(context, toast, type, content);
        }
    }

    private static void dialogShow(Context context, String content, Type type) {
        if(toast != null){
            toast.cancel();
        }

        if (toastDialog != null) {
            toastDialog.dismiss();
        }
        toastDialog = new Dialog(context, R.style.ThemeDialog);
        View view = LayoutInflater.from(context).inflate(R.layout.toast_type_dialog_tips, null);
        setTipsViewStyle(view, type, content);
        toastDialog.setContentView(view);
        toastDialog.setCancelable(true);
        toastDialog.setCanceledOnTouchOutside(true);
        toastDialog.show();
    }

    private static void show(Context context, Toast toast) {
        toast.show();
    }

    public static void toast(Context context, String s) {
        show(context, s, Type.TYPE_TOAST);
    }

    public static void toast(Context context, int resId) {
        toast(context, context.getString(resId));
    }

    public static void error(Context context, String s) {
        show(context, s, Type.TYPE_ERROR);
    }

    public static void error(Context context, int resId) {
        error(context, context.getString(resId));
    }

    public static void dialogError(Context context, String s) {
        dialogShow(context, s, Type.TYPE_ERROR);
    }

    public static void dialogError(Context context, int resId) {
        dialogError(context, context.getString(resId));
    }


    public static void tips(Context context, String s) {
        show(context, s, Type.TYPE_TIPS);
    }

    public static void tips(Context context, int resId) {
        tips(context, context.getString(resId));
    }

    public static void dialogTips(Context context, String s) {
        dialogShow(context, s, Type.TYPE_TIPS);
    }

    public static void dialogTips(Context context, int resId) {
        dialogTips(context, context.getString(resId));
    }

    public static void warn(Context context, String s) {
        show(context, s, Type.TYPE_WARN);
    }

    public static void warn(Context context, int resId) {
        warn(context, context.getString(resId));
    }

    public static void dialogWarn(Context context, String s) {
        dialogShow(context, s, Type.TYPE_WARN);
    }

    public static void dialogWarn(Context context, int resId) {
        dialogWarn(context, context.getString(resId));
    }

    public static void success(Context context, String s) {
        show(context, s, Type.TYPE_SUCCESS);
    }

    public static void success(Context context, int resId) {
        success(context, context.getString(resId));
    }

    public static void dialogSuccess(Context context, String s) {
        dialogShow(context, s, Type.TYPE_SUCCESS);
    }

    public static void dialogSuccess(Context context, int resId) {
        dialogSuccess(context, context.getString(resId));
    }
}