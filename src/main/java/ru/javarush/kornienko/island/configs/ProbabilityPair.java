package ru.javarush.kornienko.island.configs;

import java.io.Serializable;

public class ProbabilityPair implements Serializable {
    private String from;
    private String to;
    private byte percent;

    public ProbabilityPair() {
    }

    public ProbabilityPair(String from, String to, byte percent) {
        this.from = from;
        this.to = to;
        this.percent = percent;
    }

    public byte getPercent() {
        return percent;
    }

    public void setPercent(byte percent) {
        this.percent = percent;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }
}
