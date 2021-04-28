package com.aliyuncs.fc.constants;


/**
 * Contains shared constant request header String keys
 */
public class HeaderKeys {

    public static final String REQUEST_ID = "X-Fc-Request-Id";
    public static final String INVOCATION_TYPE = "X-Fc-Invocation-Type";
    public static final String INVOCATION_LOG_TYPE = "X-Fc-Log-Type";
    public static final String INVOCATION_LOG_RESULT = "X-Fc-Log-Result";
    public static final String OPENTRACING_SPANCONTEXT = "X-Fc-Tracing-Opentracing-Span-Context";
    public static final String OPENTRACING_SPANCONTEXT_BAGGAGE_PREFIX = "X-Fc-Tracing-Opentracing-Span-Context-Baggage-";
    public static final String STATEFUL_ASYNC_INVOCATIONID = "X-Fc-Stateful-Async-Invocation-Id";

}
