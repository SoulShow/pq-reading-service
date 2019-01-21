package com.pq.reading.dto;

public class MyReadingDetailDto {
   private Long readingId;

   private String name;

   private String bookName;

   private String img;

   private String voiceUrl;

   private String createTime;

   private String duration;

   private String userName;
   private String className;
   private String avatar;
   private int playCount;
   private int praiseCount;
   private int commentCount;

   public String getUserName() {
      return userName;
   }

   public void setUserName(String userName) {
      this.userName = userName;
   }

   public String getClassName() {
      return className;
   }

   public void setClassName(String className) {
      this.className = className;
   }

   public String getAvatar() {
      return avatar;
   }

   public void setAvatar(String avatar) {
      this.avatar = avatar;
   }


   public int getPlayCount() {
      return playCount;
   }

   public void setPlayCount(int playCount) {
      this.playCount = playCount;
   }

   public int getPraiseCount() {
      return praiseCount;
   }

   public void setPraiseCount(int praiseCount) {
      this.praiseCount = praiseCount;
   }

   public int getCommentCount() {
      return commentCount;
   }

   public void setCommentCount(int commentCount) {
      this.commentCount = commentCount;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getBookName() {
      return bookName;
   }

   public void setBookName(String bookName) {
      this.bookName = bookName;
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

   public String getCreateTime() {
      return createTime;
   }

   public void setCreateTime(String createTime) {
      this.createTime = createTime;
   }

   public Long getReadingId() {
      return readingId;
   }

   public void setReadingId(Long readingId) {
      this.readingId = readingId;
   }

   public String getDuration() {
      return duration;
   }

   public void setDuration(String duration) {
      this.duration = duration;
   }
}