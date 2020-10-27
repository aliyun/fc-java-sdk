package com.aliyuncs.fc.model;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import static com.aliyuncs.fc.model.TracingType.JAEGERTYPE;

/**
 * TracingConfig is used in tracing feature.
 * Type: Implementation of opentracing or other tracing protocal.
 * Supported: Jaeger.
 * Params: Parameters needed for the implementation of the specified type.
 */
public class TracingConfig {
    @SerializedName("type")
    private TracingType type;

    @SerializedName("params")
    private Object params;

    public TracingConfig() {
    }

    public TracingConfig(TracingType type, Object params) {
        this.type = type;
        this.params = params;
    }

    public TracingConfig setType(TracingType type) {
        this.type = type;
        return this;
    }

    public TracingConfig setParams(Object params) {
        this.params = params;
        return this;
    }

    public TracingType getType() {
        return type;
    }

    public Object getParams() {
        return params;
    }

    public TracingConfig setJaegerConfig(JaegerConfig jaegerConfig) {
        this.type = JAEGERTYPE;
        this.params = jaegerConfig;
        return this;
    }

    public JaegerConfig getJaegerConfig() {
        Gson gson = new Gson();
        JaegerConfig jaegerConfig = gson.fromJson(gson.toJson(params), JaegerConfig.class);
        return jaegerConfig;
    }
}
