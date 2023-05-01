package com.raphael.kraken.outage;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FilteredOutage {

    private final String id;
    private String name;
    private final String begin;
    private final String end;

    public FilteredOutage(@JsonProperty("id") String id,
                  @JsonProperty("name") String name,
                  @JsonProperty("begin") String begin,
                  @JsonProperty("end") String outageEnd) {
        this.id = id;
        this.name = name;
        this.begin = begin;
        this.end = outageEnd;
    }

    public String getId() {
        return id;
    }

    public String getName() {return name;}

    public String getBegin() {
        return begin;
    }

    public String getEnd() {
        return end;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Outage{" +
                "outageId='" + id + '\'' +
                ", outageBegin='" + begin + '\'' +
                ", outageEnd='" + end + '\'' +
                '}';
    }
}

