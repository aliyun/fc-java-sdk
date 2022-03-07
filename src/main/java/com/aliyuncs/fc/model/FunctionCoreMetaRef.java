package com.aliyuncs.fc.model;

import com.google.gson.annotations.SerializedName;
import java.util.Map;

/**
 * TODO: add javadoc
 */
public class FunctionCoreMetaRef {

    @SerializedName("serviceName")
    private String serviceName;

    @SerializedName("functionName")
    private String functionName;

    @SerializedName("qualifier")
    private String qualifier;


    public FunctionCoreMetaRef(){}

    public FunctionCoreMetaRef(String serviceName, String functionName, String qualifier) {
        this.serviceName = serviceName;
        this.functionName = functionName;
        this.qualifier = qualifier;
    }
    public String getFunctionName() {
        return functionName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getQualifier() {
        return qualifier;
    }

    public FunctionCoreMetaRef setServiceName(String serviceName) {
        this.serviceName = serviceName;
        return this;
    }
    public FunctionCoreMetaRef setFunctionName(String functionName) {
        this.functionName = functionName;
        return this;
    }
    public FunctionCoreMetaRef setQualifier(String qualifier) {
        this.qualifier = qualifier;
        return this;
    }

}
