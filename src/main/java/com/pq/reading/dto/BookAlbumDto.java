package com.pq.reading.dto;

import com.pq.reading.entity.BookAlbum;

import java.util.List;

public class BookAlbumDto {
   private List<BookAlbum> list;

   public List<BookAlbum> getList() {
      return list;
   }

   public void setList(List<BookAlbum> list) {
      this.list = list;
   }
}