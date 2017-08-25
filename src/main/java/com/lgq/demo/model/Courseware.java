package com.lgq.demo.model;

import org.springframework.jdbc.core.RowMapper;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 * Created by liguoqing on 2016/1/11.
 */
public class Courseware implements Serializable,RowMapper<Courseware> {


    private String id;
    private String code;
    private String name;
    private int duration;
    private String deviceType;
    private String fileType;
    private boolean open;
    private String transformStatus;
    private String createTime;
    private String updateTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public String getTransformStatus() {
        return transformStatus;
    }

    public void setTransformStatus(String transformStatus) {
        this.transformStatus = transformStatus;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public Courseware mapRow(ResultSet rs, int i) throws SQLException {
        Courseware courseware = new Courseware();
        courseware.setId(rs.getString("id"));
        courseware.setCode(rs.getString("code"));
        courseware.setName(rs.getString("name"));
        courseware.setDuration(rs.getInt("duration"));
        courseware.setFileType(rs.getString("file_type"));
        courseware.setDeviceType(rs.getString("device_type"));
        courseware.setOpen(rs.getBoolean("open"));
        courseware.setTransformStatus(rs.getString("transform_status"));
        courseware.setCreateTime(rs.getString("create_time"));
        courseware.setUpdateTime(rs.getString("update_time"));
        return courseware;
    }
}
