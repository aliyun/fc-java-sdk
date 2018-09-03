package com.aliyuncs.fc.constants;

/**
 * TODO: add javadoc
 */
public class Const {

    public final static String SERVICE_PATH = "/%s/services";
    public final static String SINGLE_SERVICE_PATH = "/%s/services/%s";
    public final static String FUNCTION_PATH = "/%s/services/%s/functions";
    public final static String SINGLE_FUNCTION_PATH = "/%s/services/%s/functions/%s";
    public final static String CUSTOM_DOMAIN_PATH = "/%s/custom-domains";
    public final static String SINGLE_CUSTOM_DOMAIN_PATH = "/%s/custom-domains/%s";
    public final static String FUNCTION_CODE_PATH = "/%s/services/%s/functions/%s/code";
    public final static String TRIGGER_PATH = "/%s/services/%s/functions/%s/triggers";
    public final static String SINGLE_TRIGGER_PATH = "/%s/services/%s/functions/%s/triggers/%s";
    public final static String INVOKE_FUNCTION_PATH = "/%s/services/%s/functions/%s/invocations";
    public final static String HTTP_INVOKE_FUNCTION_PATH = "/%s/proxy/%s/%s/%s";
    public final static int CONNECT_TIMEOUT = 1000;
    public final static int READ_TIMEOUT = 100000;
    public final static String API_VERSION = "2016-08-15";
    public final static String INVOCATION_TYPE_ASYNC = "Async";
    public final static String INVOCATION_TYPE_HTTP = "http";
}
