package com.pq.reading.dto;

import java.util.ArrayList;
import java.util.List;

public class ChapterSearchListDto {
    private List<BookChapterDetailDto> chineseList = new ArrayList<>();
    private List<BookChapterDetailDto> readingList = new ArrayList<>();

    public List<BookChapterDetailDto> getChineseList() {
        return chineseList;
    }

    public void setChineseList(List<BookChapterDetailDto> chineseList) {
        this.chineseList = chineseList;
    }

    public List<BookChapterDetailDto> getReadingList() {
        return readingList;
    }

    public void setReadingList(List<BookChapterDetailDto> readingList) {
        this.readingList = readingList;
    }
}