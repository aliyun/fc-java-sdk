package com.aliyuncs.fc.constants;

/**
 * TODO: add javadoc
 */
public class Const {

    public final static String SERVICE_PATH = "/%s/services";
    public final static String SINGLE_SERVICE_PATH = SERVICE_PATH + "/%s";
    public final static String FUNCTION_PATH = SINGLE_SERVICE_PATH + "/functions";
    public final static String SINGLE_FUNCTION_PATH = FUNCTION_PATH + "/%s";
    public final static String CUSTOM_DOMAIN_PATH = "/%s/custom-domains";
    public final static String SINGLE_CUSTOM_DOMAIN_PATH = CUSTOM_DOMAIN_PATH + "/%s";
    public final static String SERVICE_VERSION_PATH = SINGLE_SERVICE_PATH + "/versions";
    public final static String SINGLE_VERSION_PATH = SERVICE_VERSION_PATH + "/%s";
    public final static String ALIAS_PATH = SINGLE_SERVICE_PATH + "/aliases";
    public final static String SINGLE_ALIAS_PATH = ALIAS_PATH + "/%s";


    public final static String FUNCTION_CODE_PATH = SINGLE_FUNCTION_PATH + "/code";
    public final static String TRIGGER_PATH = SINGLE_FUNCTION_PATH + "/triggers";
    public final static String SINGLE_TRIGGER_PATH = TRIGGER_PATH + "/%s";
    public final static String INVOKE_FUNCTION_PATH = SINGLE_FUNCTION_PATH + "/invocations";
    public final static String HTTP_INVOKE_FUNCTION_PATH = "/%s/proxy/%s/%s/%s";

    public final static String SINGLE_SERVICE_WITH_QUALIFIER_PATH = SERVICE_PATH + "/%s$%s";
    public final static String FUNCTION_WITH_QUALIFIER_PATH =
        SINGLE_SERVICE_WITH_QUALIFIER_PATH + "/functions";
    public final static String SINGLE_FUNCTION_WITH_QUALIFIER_PATH =
        FUNCTION_WITH_QUALIFIER_PATH + "/%s";
    public final static String FUNCTION_CODE_WITH_QUALIFIER_PATH =
        SINGLE_FUNCTION_WITH_QUALIFIER_PATH + "/code";
    public final static String INVOKE_FUNCTION_WITH_QUALIFIER_PATH =
        SINGLE_FUNCTION_WITH_QUALIFIER_PATH + "/invocations";

    /**
     * 3 seconds
     *
     * Used for http request connect timeout
     */
    public final static int CONNECT_TIMEOUT = 60 * 1000;

    /**
     * 10 minutes 3 seconds
     *
     * Used for http request read timeout
     */
    public final static int READ_TIMEOUT = 10 * 60 * 1000 + 3000;

    public final static String API_VERSION = "2016-08-15";
    public final static String INVOCATION_TYPE_ASYNC = "Async";
    public final static String INVOCATION_TYPE_HTTP = "http";
    public final static String IF_MATCH_HEADER = "If-Match";
}
