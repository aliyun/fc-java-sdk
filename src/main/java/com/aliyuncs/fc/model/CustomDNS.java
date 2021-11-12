package com.aliyuncs.fc.model;

import com.google.gson.annotations.SerializedName;

public class CustomDNS {

    @SerializedName("nameServers")
    private String[] nameServers;

    @SerializedName("searches")
    private String[] searches;

    @SerializedName("dnsOptions")
    private DNSOption[] dnsOptions;

    public String[] getNameServers() {
        return nameServers;
    }

    public void setNameServers(String[] nameServers) {
        this.nameServers = nameServers;
    }

    public String[] getSearches() {
        return searches;
    }

    public void setSearches(String[] searches) {
        this.searches = searches;
    }

    public DNSOption[] getDnsOptions() {
        return dnsOptions;
    }

    public void setDnsOptions(DNSOption[] dnsOptions) {
        this.dnsOptions = dnsOptions;
    }

}
