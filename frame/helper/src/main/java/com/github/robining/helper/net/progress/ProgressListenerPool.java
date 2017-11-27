package com.github.robining.helper.net.progress;

import android.support.annotation.NonNull;

import com.github.robining.helper.ILifeCycleProvider;
import com.trello.rxlifecycle2.LifecycleProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

/**
 * 功能描述:进度监听器的管理器,负责申请和取消注册监听器
 * Created by LuoHaifeng on 2017/5/24.
 * Email:496349136@qq.com
 */

public class ProgressListenerPool {
    private HashMap<String, FlowableEmitter<ProgressEntity>> progressListenerPool = new HashMap<>();
    private List<String> ids = new ArrayList<>();
    private final Lock regLock = new ReentrantLock();
    private static ProgressListenerPool instance = new ProgressListenerPool();

    private ProgressListenerPool() {
    }

    public static ProgressListenerPool getInstance() {
        return instance;
    }

    /**
     * 生成随机监听器ID
     *
     * @return id
     */
    private String requestRandomId() {
        String id = System.currentTimeMillis() + "" + getRandomString(10);
        if (ids.contains(id)) {//如果id重复,重新申请
            return requestRandomId();
        }
        return id;
    }

    public static String getRandomString(int length) {
        StringBuilder buffer = new StringBuilder("0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ");
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        int range = buffer.length();
        for (int i = 0; i < length; i++) {
            sb.append(buffer.charAt(random.nextInt(range)));
        }
        return sb.toString();
    }

    /**
     * 注册监听器
     *
     * @param lifeCycleProvider 生命周期控制器
     * @return 监听器
     */
    public ProgressListener registerListener(ILifeCycleProvider lifeCycleProvider) {
        synchronized (regLock) {
            final String id = requestRandomId();
            ids.add(id);//预定该id,以免重复创建
            Flowable<ProgressEntity> listener = Flowable.create(new FlowableOnSubscribe<ProgressEntity>() {
                @Override
                public void subscribe(FlowableEmitter<ProgressEntity> e) throws Exception {
                    progressListenerPool.put(id, e);
                }
            }, BackpressureStrategy.LATEST);

            if (lifeCycleProvider != null) {
                listener = listener.compose(lifeCycleProvider.<ProgressEntity>bindLifecycle());
            }

            listener.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doAfterTerminate(new Action() {
                        @Override
                        public void run() throws Exception {
                            unregisterListener(id);//当请求结束自动卸载监听器
                        }
                    });
            return new ProgressListener().setId(id).setListener(listener);
        }
    }

    /**
     * 卸载监听器
     *
     * @param id 监听器id
     */
    public void unregisterListener(@NonNull String id) {
        synchronized (regLock) {
            if (ids.contains(id)) {
                ids.remove(id);
            }
            if (progressListenerPool.containsKey(id)) {
                progressListenerPool.remove(id);
            }
        }
    }

    /**
     * 通过id获取进度发射器
     *
     * @param id 监听器id
     * @return 发射器
     */
    public FlowableEmitter<ProgressEntity> getListener(@NonNull String id) {
        if (progressListenerPool.containsKey(id)) {
            return progressListenerPool.get(id);
        }
        return null;
    }

    public int getSize() {
        return progressListenerPool.size();
    }
}
