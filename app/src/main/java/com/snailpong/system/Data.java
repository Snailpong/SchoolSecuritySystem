package com.snailpong.system;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Data implements Serializable {
    private String id;
    private String name;
    private String parent;
    private String states;
    private String busdriver;
    private String busnumber;
    private String time;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getStates() {
        return states;
    }

    public void setStates(String states) {
        this.states = states;
    }

    public String getBusnumber() {
        return busnumber;
    }

    public void setBusnumber(String busnumber) {
        this.busnumber = busnumber;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getBusdriver() {
        return busdriver;
    }

    public void setBusdriver(String busdriver) {
        this.busdriver = busdriver;
    }
}
