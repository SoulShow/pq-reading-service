package com.pq.reading.entity;

import java.sql.Timestamp;

public class TaskReadingScoreLog {
    private Long id;

    private Long studentId;

    private Long readingRecordId;

    private String userId;

    private Integer score;

    private Timestamp createdTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Long getReadingRecordId() {
        return readingRecordId;
    }

    public void setReadingRecordId(Long readingRecordId) {
        this.readingRecordId = readingRecordId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Timestamp getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Timestamp createdTime) {
        this.createdTime = createdTime;
    }
}