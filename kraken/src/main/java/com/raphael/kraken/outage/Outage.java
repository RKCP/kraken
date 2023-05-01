package com.raphael.kraken.outage;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Outage {

    private final String id;
    private final String begin;
    private final String end;

    public Outage(@JsonProperty("id") String id,
                  @JsonProperty("begin") String begin,
                  @JsonProperty("end") String outageEnd) {
        this.id = id;
        this.begin = begin;
        this.end = outageEnd;
    }

    public String getId() {
        return id;
    }

    public String getBegin() {
        return begin;
    }

    public String getEnd() {
        return end;
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
