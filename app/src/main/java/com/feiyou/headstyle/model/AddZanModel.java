package com.feiyou.headstyle.model;

import com.feiyou.headstyle.base.IBaseRequestCallBack;

import java.util.List;

/**
 * Created by iflying on 2018/1/9.
 */

public interface AddZanModel<T> {
    void addZan(int type, String userId, String messageId, String commentId, String repeatId, IBaseRequestCallBack<T> iBaseRequestCallBack);
}
