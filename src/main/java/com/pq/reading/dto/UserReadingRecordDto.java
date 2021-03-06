package com.pq.reading.dto;

public class UserReadingRecordDto {

   private Long taskId;

   private String name;

   private String img;

   private Long userAlbumId;

   private String voiceUrl;

   private int isPrivate = 1;

   private String oneToOneUserId;

   private String userId;

   private Long studentId;

   private Long classId;

   private Long chapterId;

   private String duration;

   private String base64;

   private String suffix;

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

   public String getImg() {
      return img;
   }

   public void setImg(String img) {
      this.img = img;
   }

   public String getVoiceUrl() {
      return voiceUrl;
   }

   public void setVoiceUrl(String voiceUrl) {
      this.voiceUrl = voiceUrl;
   }

   public String getOneToOneUserId() {
      return oneToOneUserId;
   }

   public void setOneToOneUserId(String oneToOneUserId) {
      this.oneToOneUserId = oneToOneUserId;
   }

   public int getIsPrivate() {
      return isPrivate;
   }

   public void setIsPrivate(int isPrivate) {
      this.isPrivate = isPrivate;
   }

   public String getUserId() {
      return userId;
   }

   public void setUserId(String userId) {
      this.userId = userId;
   }

   public Long getStudentId() {
      return studentId;
   }

   public void setStudentId(Long studentId) {
      this.studentId = studentId;
   }

   public Long getUserAlbumId() {
      return userAlbumId;
   }

   public void setUserAlbumId(Long userAlbumId) {
      this.userAlbumId = userAlbumId;
   }

   public Long getChapterId() {
      return chapterId;
   }

   public void setChapterId(Long chapterId) {
      this.chapterId = chapterId;
   }

   public String getDuration() {
      return duration;
   }

   public void setDuration(String duration) {
      this.duration = duration;
   }

   public Long getClassId() {
      return classId;
   }

   public void setClassId(Long classId) {
      this.classId = classId;
   }

   public String getBase64() {
      return base64;
   }

   public void setBase64(String base64) {
      this.base64 = base64;
   }

   public String getSuffix() {
      return suffix;
   }

   public void setSuffix(String suffix) {
      this.suffix = suffix;
   }
}