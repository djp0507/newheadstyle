package com.feiyou.headstyle.model;

import android.content.Context;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.feiyou.headstyle.api.TestInfoServiceApi;
import com.feiyou.headstyle.api.TopicDataServiceApi;
import com.feiyou.headstyle.base.BaseModel;
import com.feiyou.headstyle.base.BaseTestModel;
import com.feiyou.headstyle.base.IBaseRequestCallBack;
import com.feiyou.headstyle.bean.TestResultInfoRet;
import com.feiyou.headstyle.bean.TestResultParams;
import com.feiyou.headstyle.bean.TopicInfoRet;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by iflying on 2018/1/9.
 */

public class TestResultInfoModelImp extends BaseTestModel implements TestResultInfoModel<TestResultInfoRet> {

    private Context context = null;
    private TestInfoServiceApi testInfoServiceApi;
    private CompositeSubscription mCompositeSubscription;

    public TestResultInfoModelImp(Context mContext) {
        super();
        context = mContext;
        testInfoServiceApi = mRetrofit.create(TestInfoServiceApi.class);
        mCompositeSubscription = new CompositeSubscription();
    }

    @Override
    public void createImage(TestResultParams testResultParams, IBaseRequestCallBack<TestResultInfoRet> iBaseRequestCallBack) {
        JSONObject params = new JSONObject();
        try {
            params.put("id", testResultParams.getId());
            params.put("test_type", testResultParams.getTestType());
            params.put("nickname", testResultParams.getNickname());
            params.put("headimg", testResultParams.getHeadimg());
            params.put("sex", testResultParams.getSex());
            params.put("result_id", testResultParams.getResultId());
            params.put("is_app", "1");
            params.put("app_id", "37");
            params.put("user_id",testResultParams.getUserId());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), params.toString());
        mCompositeSubscription.add(testInfoServiceApi.createImage(requestBody)  //将subscribe添加到subscription，用于注销subscribe
                .observeOn(AndroidSchedulers.mainThread())//指定事件消费线程
                .subscribeOn(Schedulers.io())  //指定 subscribe() 发生在 IO 线程
                .subscribe(new Subscriber<TestResultInfoRet>() {

                    @Override
                    public void onStart() {
                        super.onStart();
                        //onStart它总是在 subscribe 所发生的线程被调用 ,如果你的subscribe不是主线程，则会出错，则需要指定线程;
                        iBaseRequestCallBack.beforeRequest();
                    }

                    @Override
                    public void onCompleted() {
                        //回调接口：请求已完成，可以隐藏progress
                        iBaseRequestCallBack.requestComplete();
                    }

                    @Override
                    public void onError(Throwable e) {
                        //回调接口：请求异常
                        iBaseRequestCallBack.requestError(e);
                    }

                    @Override
                    public void onNext(TestResultInfoRet testResultInfoRet) {
                        //回调接口：请求成功，获取实体类对象
                        iBaseRequestCallBack.requestSuccess(testResultInfoRet);
                    }
                }));
    }

}
