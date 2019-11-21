package com.aliyuncs.fc.core;

import com.aliyuncs.fc.client.FcCallback;

import java.util.LinkedList;
import java.util.List;

abstract class AbstractWatchableCallback<Req, Res> implements FcCallback<Req, Res> {
    protected List<FcCallback<Req, Res>> callbacks = new LinkedList();

    public AbstractWatchableCallback<Req, Res> addCallback(FcCallback<Req, Res> callback) {
        callbacks.add(callback);
        return this;
    }
}

