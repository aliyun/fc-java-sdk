/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.aliyuncs.fc.request;

import com.aliyuncs.fc.constants.Const;
import com.aliyuncs.fc.exceptions.ClientException;
import com.aliyuncs.fc.http.HttpRequest;
import com.aliyuncs.fc.model.ListRequestUrlHelper;
import com.aliyuncs.fc.response.ListFunctionsResponse;
import com.aliyuncs.fc.response.ListInstancesResponse;
import com.google.common.base.Strings;
import com.google.gson.annotations.SerializedName;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static com.aliyuncs.fc.auth.AcsURLEncoder.encode;

/**
 * TODO: add javadoc
 */
public class InstanceExecRequest extends HttpRequest {

    private final String serviceName;
    private final String functionName;
    private final String instanceID;

    private String qualifier;
    private Boolean stdin;
    private Boolean stdout;
    private Boolean stderr;
    private Boolean tty;
    private String[] command;

    public InstanceExecRequest(String serviceName, String functionName, String instanceID) {
        this.serviceName = serviceName;
        this.functionName = functionName;
        this.instanceID = instanceID;

        // default params
        qualifier = "LATEST";
        stdin = true;
        stdout = true;
        stderr = true;
        tty = false;
        command = new String[] {};
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getFunctionName() {
        return functionName;
    }

    public String getInstanceID() {
        return instanceID;
    }

    public String getQualifier() {
        return qualifier;
    }

    public InstanceExecRequest setQualifier(String qualifier) {
        this.qualifier = qualifier;
        return this;
    }

    public Boolean getStdin() {
        return stdin;
    }

    public InstanceExecRequest setStdin(Boolean stdin) {
        this.stdin = stdin;
        return this;
    }

    public Boolean getStdout() {
        return stdout;
    }

    public InstanceExecRequest setStdout(Boolean stdout) {
        this.stdout = stdout;
        return this;
    }

    public Boolean getStderr() {
        return stderr;
    }

    public InstanceExecRequest setStderr(Boolean stderr) {
        this.stderr = stderr;
        return this;
    }

    public Boolean getTty() {
        return tty;
    }

    public InstanceExecRequest setTty(Boolean tty) {
        this.tty = tty;
        return this;
    }

    public String[] getCommand() {
        return command;
    }

    public InstanceExecRequest setCommand(String[] Command) {
        this.command = new String[Command.length];
        for (int i = 0; i < Command.length; i++) {
            this.command[i] = Command[i];
        }
        return this;
    }

    public String getPath() {
        if (Strings.isNullOrEmpty(qualifier)) {
            return String.format(Const.INSTANCE_EXEC_PATH, Const.API_VERSION, this.serviceName, this.functionName,this.instanceID);
        } else {
            return String.format(Const.INSTANCE_EXEC_WITH_QUALIFIER_PATH, Const.API_VERSION, this.serviceName,
                    this.qualifier, this.functionName,this.instanceID);
        }
    }

    public Map<String, String> getQueryParams() {
        Map<String, String> queries = new HashMap<String, String>();
        if (command != null && command.length > 0) {
            queries.put("command", command[0]);
        }
        queries.put("stdin", stdin ? "true" : "false");
        queries.put("stdout", stdout ? "true" : "false");
        queries.put("stderr", stderr ? "true" : "false");
        queries.put("tty", tty ? "true" : "false");
        return queries;
    }

    public String getQueries() throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder();
        if (command != null) {
            for (String cmd : command) {
                sb.append("command").append("=").append(encode(cmd)).append("&");
            }
        }
        Map<String, String> queries = getQueryParams();
        for (Map.Entry<String, String> entry : queries.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (!key.equals("command")) {
                sb.append(encode(key)).append("=").append(encode(value)).append("&");
            }
        }

        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    public byte[] getPayload() {
        return null;
    }

    public void validate() throws ClientException {
        if (Strings.isNullOrEmpty(serviceName)) {
            throw new ClientException("Service name cannot be blank");
        }
    }

    public Class<ListInstancesResponse> getResponseClass() {
        return ListInstancesResponse.class;
    }
}
