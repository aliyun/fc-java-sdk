package com.aliyuncs.fc.client;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.fc.config.Config;
import com.aliyuncs.fc.exceptions.ClientException;
import com.aliyuncs.fc.exceptions.ServerException;
import com.aliyuncs.fc.request.OpenFunctionComputeRequest;
import com.aliyuncs.fc.response.OpenFunctionComputeResponse;
import com.aliyuncs.fc_open.model.v20200310.OpenFcServiceRequest;
import com.aliyuncs.fc_open.model.v20200310.OpenFcServiceResponse;
import com.aliyuncs.profile.DefaultProfile;
import org.apache.commons.lang.StringUtils;

public class PopClient {

    private static final String OPEN_FC_SERVICE_REGION = "cn-hangzhou";

    private static final String OPEN_FC_SERVICE_ENDPOINT = "fc-open.cn-hangzhou.aliyuncs.com";

    public static final String ERROR_CODE_ORDER_OPENED = "ORDER.OPENED";

    public OpenFunctionComputeResponse openFCService(Config config, OpenFunctionComputeRequest request) throws ClientException, ServerException {
        request.validate();

        try {
            String accessKeyId = config.getAccessKeyID();
            String accessSecret = config.getAccessKeySecret();

            DefaultProfile profile = null;
            if (StringUtils.isBlank(config.getSecurityToken())) {
                profile = DefaultProfile.getProfile(OPEN_FC_SERVICE_REGION, accessKeyId, accessSecret);
            } else {
                String sToken = config.getSecurityToken();
                profile = DefaultProfile.getProfile(OPEN_FC_SERVICE_REGION, accessKeyId, accessSecret, sToken);
            }

            OpenFcServiceRequest openFCServiceRequest = new OpenFcServiceRequest();
            IAcsClient client = new DefaultAcsClient(profile);
            openFCServiceRequest.setSysEndpoint(OPEN_FC_SERVICE_ENDPOINT);

            OpenFcServiceResponse openFCServiceResponse = client.getAcsResponse(openFCServiceRequest);
            OpenFunctionComputeResponse response = new OpenFunctionComputeResponse();
            response.setRequestId(openFCServiceResponse.getRequestId());
            response.setOrderId(openFCServiceResponse.getOrderId());
            return response;
        } catch (com.aliyuncs.exceptions.ClientException e) {
            if (StringUtils.contains(e.getErrMsg(), "已开通")) {
                OpenFunctionComputeResponse response = new OpenFunctionComputeResponse();
                response.setRequestId(e.getRequestId());
                response.setCode(ERROR_CODE_ORDER_OPENED);
                response.setMsg("You have subscribed FunctionCompute, please proceed to FC console and start using it.");
                return response;
            }
            throw new ClientException(e.getErrCode(), e.getErrMsg(), e.getRequestId());
        }
    }
}
