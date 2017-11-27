package com.github.robining.config.utils.proxy;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.SparseArray;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

/**
 * 功能描述:代理的Fragment,支持回调替代startActivityForResult,支持requestCode自动管理
 * Created by LuoHaifeng on 2017/5/23.
 * Email:496349136@qq.com
 */

public class ProxyFragment extends Fragment {
    private SparseArray<ProxyRequestBean> requestCallbacks = new SparseArray<>();
    private final Lock requestCodeLock = new ReentrantLock();
    private int autoRequestCode = 0;

    /**
     * 自动生成requestCode
     *
     * @return requestCode
     */
    private synchronized int getAutoRequestCode() {
        synchronized (requestCodeLock) {
            return ++autoRequestCode;
        }
    }

    /**
     * 自动生成requestCode
     * Activity.RESULT_OK 作为resultCode
     * @param intent 请求内容
     * @return 请求结果
     */
    public Observable<ProxyResultBean> startActivityForResultProxy(@NonNull Intent intent) {
        return startActivityForResultProxy(Activity.RESULT_OK, intent);
    }

    /**
     * 自动生成requestCode
     * 自定义resultCode
     * @param resultCode resultCode
     * @param intent 请求内容
     * @return 请求结果
     */
    public Observable<ProxyResultBean> startActivityForResultProxy(int resultCode, @NonNull Intent intent) {
        return startActivityForResultProxy(getAutoRequestCode(), resultCode, intent);
    }

    /**
     * @param requestCode 指定requestCode
     * @param resultCode 指定resultCode
     * @param intent 请求内容
     * @return 请求结果
     * @deprecated 可能和自动生成的requestCode重复
     */
    @Deprecated
    private Observable<ProxyResultBean> startActivityForResultProxy(final int requestCode, final int resultCode, @NonNull final Intent intent) {
        return Observable.create(new ObservableOnSubscribe<ProxyResultBean>() {

            @Override
            public void subscribe(ObservableEmitter<ProxyResultBean> e) throws Exception {
                ProxyRequestBean prb = new ProxyRequestBean().setRequestCode(requestCode).setEmitter(e);
                requestCallbacks.put(requestCode, prb);
                startActivityForResult(intent, requestCode);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //分发结果
        ProxyRequestBean requestBean = requestCallbacks.get(requestCode, null);
        if (requestBean != null) {
            requestBean.getEmitter().onNext(new ProxyResultBean().setResultCode(resultCode).setData(data));
            requestBean.getEmitter().onComplete();
            requestCallbacks.remove(requestCode);
        }
    }
}
