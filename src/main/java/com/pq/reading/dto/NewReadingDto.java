package com.pq.reading.dto;

public class NewReadingDto {

   private Long taskId;

   private String name;

   private String className;

   private String userName;

   private String avatar;

   private String createTime;

   private int readingState;

   private int unCommitCount;

   public Long getTaskId() {
      return taskId;
   }

   public void setTaskId(Long taskId) {
      this.taskId = taskId;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getClassName() {
      return className;
   }

   public void setClassName(String className) {
      this.className = className;
   }

   public String getUserName() {
      return userName;
   }

   public void setUserName(String userName) {
      this.userName = userName;
   }

   public String getAvatar() {
      return avatar;
   }

   public void setAvatar(String avatar) {
      this.avatar = avatar;
   }

   public String getCreateTime() {
      return createTime;
   }

   public void setCreateTime(String createTime) {
      this.createTime = createTime;
   }

   public int getUnCommitCount() {
      return unCommitCount;
   }

   public void setUnCommitCount(int unCommitCount) {
      this.unCommitCount = unCommitCount;
   }

   public int getReadingState() {
      return readingState;
   }

   public void setReadingState(int readingState) {
      this.readingState = readingState;
   }
}