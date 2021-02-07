package com.aliyuncs.fc.response;

import com.aliyuncs.fc.http.HttpResponse;
import com.aliyuncs.fc.model.ALBSettings;

public class GetALBSettingsResponse extends HttpResponse {

    private ALBSettings albSettings;

    public ALBSettings getAlbSettings() {
        return albSettings;
    }

    public void setAlbSettings(ALBSettings albSettings) {
        this.albSettings = albSettings;
    }
}