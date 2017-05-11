fc-java-sdk
=======

[![maven version][mvn-image]][mvn-url]
[![build status][travis-image]][travis-url]

[mvn-image]: https://img.shields.io/maven-central/v/com.aliyun/aliyun-java-sdk-fc.svg?style=flat-square
[mvn-url]: http://search.maven.org/#search%7Cga%7C1%7Caliyun-java-sdk-fc
[travis-image]: https://img.shields.io/travis/aliyun/fc-java-sdk/master.svg?style=flat-square
[travis-url]: https://travis-ci.org/aliyun/fc-java-sdk.svg?branch=master

## Requirements

- `Java 1.6 and above`

## License

[MIT](LICENSE)

## Install

Add Maven dependencies into pom.xml

```xml
<dependency>
    <groupId>com.aliyun</groupId>
    <artifactId>aliyun-java-sdk-fc</artifactId>
    <version>1.0.0</version>
</dependency>
```

## Example

Create the code directory and write hello world nodejs code

```bash
mkdir /tmp/fc_code
cat <<EOF > /tmp/fc_code/hello_world.js
'use strict';

exports.handler = function(event, context, callback) {
  console.log('hello world');
  callback(null, 'hello world');
};
EOF
```

Run below with your own ENDPOINT, ACCESS_KEY/SECRET_KEY and ACCOUNT_ID environment variables

```Java
public class FcSample {
    private static final String CODE_DIR = "/tmp/fc_code";
    private static final String REGION = "cn-shanghai";
    private static final String SERVICE_NAME = "test_service";
    private static final String FUNCTION_NAME = "test_function";

    public static void main(final String[] args) throws IOException {
        String accessKey = System.getenv("ACCESS_KEY");
        String accessSecretKey = System.getenv("SECRET_KEY");
        String accountId = System.getenv("ACCOUNT_ID");
        String role = System.getenv("ROLE");

        // Initialize FC client
        FunctionComputeClient fcClient = new FunctionComputeClient(REGION, accountId, accessKey, accessSecretKey);

        // Create a service
        CreateServiceRequest csReq = new CreateServiceRequest();
        csReq.setServiceName(SERVICE_NAME);
        csReq.setDescription("FC test service");
        csReq.setRole(role);
        CreateServiceResponse csResp = fcClient.createService(csReq);
        System.out.println("Created service, request ID " + csResp.getRequestId());

        // Create a function
        CreateFunctionRequest cfReq = new CreateFunctionRequest(SERVICE_NAME);
        cfReq.setFunctionName(FUNCTION_NAME);
        cfReq.setDescription("Function for test");
        cfReq.setMemorySize(128);
        cfReq.setHandler("hello_world.handler");
        cfReq.setRuntime("nodejs4.4");
        Code code = new Code().setDir(CODE_DIR);
        cfReq.setCode(code);
        cfReq.setTimeout(10);
        CreateFunctionResponse cfResp = fcClient.createFunction(cfReq);
        System.out.println("Created function, request ID " + cfResp.getRequestId());

        // Invoke the function
        InvokeFunctionRequest invkReq = new InvokeFunctionRequest(SERVICE_NAME, FUNCTION_NAME);
        InvokeFunctionResponse invkResp = fcClient.invokeFunction(invkReq);
        System.out.println(new String(invkResp.getContent()));

        // Delete the function
        DeleteFunctionRequest dfReq = new DeleteFunctionRequest(SERVICE_NAME, FUNCTION_NAME);
        DeleteFunctionResponse dfResp = fcClient.deleteFunction(dfReq);
        System.out.println("Deleted function, request ID " + dfResp.getRequestId());

        // Delete the service
        DeleteServiceRequest dsReq = new DeleteServiceRequest(SERVICE_NAME);
        DeleteServiceResponse dsResp = fcClient.deleteService(dsReq);
        System.out.println("Deleted service, request ID " + dsResp.getRequestId());
    }
}
```

## API Spec

See: https://help.aliyun.com/document_detail/52877.html
