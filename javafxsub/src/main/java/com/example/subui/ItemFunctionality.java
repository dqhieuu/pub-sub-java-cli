package com.example.subui;

public interface ItemFunctionality {
    void setTopic(String topic);
    void setLocation(String location);
    void setSensor(String sensor);
    <T> void setState(T state);
}
