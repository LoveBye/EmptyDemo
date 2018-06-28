package com.example.app.rxjava.module.rxjava2.operators.item;

import android.util.Log;

import com.example.app.R;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * concat
 * <p>
 * 连接操作符，可接受Observable的可变参数，或者Observable的集合
 * <p>
 * Author: nanchen
 * Email: liushilin520@foxmail.com
 * Date: 2017-06-22  10:06
 */

public class RxConcatActivity extends RxOperatorBaseActivity {
    private static final String TAG = "RxConcatActivity";

    @Override
    protected String getSubTitle() {
        return getString(R.string.rx_concat);
    }

    @Override
    protected void doSomething() {
        Observable.concat(Observable.just(1, 2, 3), Observable.just(4, 5, 6))
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(@NonNull Integer integer) throws Exception {
                        mRxOperatorsText.append("concat : " + integer + "\n");
                        Log.e(TAG, "concat : " + integer + "\n");
                    }
                });
    }
}
