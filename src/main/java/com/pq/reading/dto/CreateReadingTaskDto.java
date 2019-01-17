package com.pq.reading.dto;

import java.util.List;

public class CreateReadingTaskDto {
   private List<BookChapterDto> chapterList;

   private List<Long> classIdList;

   private String userId;

   public List<Long> getClassIdList() {
      return classIdList;
   }

   public void setClassIdList(List<Long> classIdList) {
      this.classIdList = classIdList;
   }

   public List<BookChapterDto> getChapterList() {
      return chapterList;
   }

   public void setChapterList(List<BookChapterDto> chapterList) {
      this.chapterList = chapterList;
   }

   public String getUserId() {
      return userId;
   }

   public void setUserId(String userId) {
      this.userId = userId;
   }
}