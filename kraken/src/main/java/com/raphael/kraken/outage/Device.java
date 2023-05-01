package com.raphael.kraken.outage;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Device {

    private final String id;
    private final String name;

    public Device(@JsonProperty("id") String id,
                  @JsonProperty("name") String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Device{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
