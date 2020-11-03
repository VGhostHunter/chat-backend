package com.dhy.chat.dto;

import javax.validation.constraints.Min;

public class PageSortAndFilterDto {

    @Min(1)
    private int pageSize = 10;

    @Min(1)
    private int pageNum = 1;

    private String sort;

    private String filter;

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }
}