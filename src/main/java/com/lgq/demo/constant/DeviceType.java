package com.lgq.demo.constant;

/**
 * Created by lgq on 2015/7/1.
 */
public enum DeviceType {

    PC("PC"), MOBILE("MOBILE");

    private String name;

    DeviceType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public static void main(String[] args) {
		DeviceType.MOBILE.getName();
	}
}