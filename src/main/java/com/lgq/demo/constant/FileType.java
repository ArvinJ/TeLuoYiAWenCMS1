package com.lgq.demo.constant;

/**
 * Created by lgq on 2015/7/1.
 */
public enum FileType {

    SCORM("SCORM包"),
    COURSE_PACK("课程包"),
    VIDEO("视频"),
    DOCUMENT("文档"),
    DOCUMENT_PPT("PPT文档"),
    DOCUMENT_WORD("WORD文档")
    ,DOCUMENT_EXCEL("EXCEL文档"),
    VIDEO_LINK("视频链接"),
    RECORD_COURSE("录播课程"),
    HTML("WEB页面");

    private String name;

    FileType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
