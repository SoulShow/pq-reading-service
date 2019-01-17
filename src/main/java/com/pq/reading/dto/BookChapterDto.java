package com.pq.reading.dto;

public class BookChapterDto {
   private Long chapterId;

   private String name;

   public Long getChapterId() {
      return chapterId;
   }

   public void setChapterId(Long chapterId) {
      this.chapterId = chapterId;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }
}