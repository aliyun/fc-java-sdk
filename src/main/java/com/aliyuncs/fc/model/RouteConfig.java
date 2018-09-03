package com.aliyuncs.fc.model;

public class RouteConfig {
    private PathConfig[] routes;

    public RouteConfig(PathConfig[] routes) {
        this.routes = routes;
    }

    public PathConfig[] getRoutes() {
        return routes;
    }

    public RouteConfig setRoutes(PathConfig[] routes) {
        this.routes = routes;
        return this;
    }
}
