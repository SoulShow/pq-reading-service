package com.pq.reading.dto;

import java.util.List;

public class AgencyStudentDto {
    private Long studentId;
    private String avatar;
    private String name;
    private int sex;
    private Long classId;
    private String className;
    private Long readingId;
    private List<ParentDto> parentList;


    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Long getReadingId() {
        return readingId;
    }

    public void setReadingId(Long readingId) {
        this.readingId = readingId;
    }

    public List<ParentDto> getParentList() {
        return parentList;
    }

    public void setParentList(List<ParentDto> parentList) {
        this.parentList = parentList;
    }

    public Long getClassId() {
        return classId;
    }

    public void setClassId(Long classId) {
        this.classId = classId;
    }
}