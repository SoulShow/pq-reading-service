package com.pq.reading.dto;

import java.util.List;

public class AgencyStudentListDto {
    private int count;
    private List<AgencyStudentDto> list;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<AgencyStudentDto> getList() {
        return list;
    }

    public void setList(List<AgencyStudentDto> list) {
        this.list = list;
    }
}