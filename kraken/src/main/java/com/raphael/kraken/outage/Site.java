package com.raphael.kraken.outage;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Site {

    private final String id;
    private final String name;
    private final List<Device> devices;

    public Site(@JsonProperty("id") String id,
                  @JsonProperty("name") String name,
                  @JsonProperty("devices") List<Device> devices) {
        this.id = id;
        this.name = name;
        this.devices = devices;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Device> getDevices() {
        return devices;
    }

    @Override
    public String toString() {
        return "Site{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", devices=" + devices +
                '}';
    }
}
