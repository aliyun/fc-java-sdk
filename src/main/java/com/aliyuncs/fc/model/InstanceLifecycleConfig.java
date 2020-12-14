package com.aliyuncs.fc.model;

import com.google.gson.annotations.SerializedName;

public class InstanceLifecycleConfig {

    @SerializedName("preFreeze")
    private LifecycleHook preFreeze;

    @SerializedName("preStop")
    private LifecycleHook preStop;

    public InstanceLifecycleConfig() {
        this.preFreeze = new LifecycleHook();
        this.preStop = new LifecycleHook();
    }

    public InstanceLifecycleConfig(LifecycleHook preFreeze, LifecycleHook preStop) {
        this.preFreeze = preFreeze;
        this.preStop = preStop;
    }

    public LifecycleHook getPreFreezeHook(){
        if(preFreeze == null){
            preFreeze = new LifecycleHook();
        }
        return preFreeze;
    }

    public LifecycleHook getPreStopHook(){
        if(preStop == null){
            preStop = new LifecycleHook();
        }
        return preStop;
    }
}
