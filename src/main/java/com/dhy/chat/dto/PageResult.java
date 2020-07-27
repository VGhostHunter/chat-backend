package com.dhy.chat.dto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author vghosthunter
 */
public class PageResult<T> implements Serializable {

    private static final long serialVersionUID = -668012717745708249L;

    private List<T> items = new ArrayList<>();
    private long totalCount;
    private int totalPages;
    private int pageNumber;
    private int numberOfElements;
    private int pageSize;
    private boolean hasNext;

    public PageResult (List<T> data, long totalElements, boolean hasNext, int pageNumber, int pageSize, int totalPages, int numberOfElements) {
        this.items = data;
        this.totalCount = totalElements;
        this.hasNext = hasNext;
        this.pageNumber = pageNumber;
        this.totalPages = totalPages;
        this.pageSize = pageSize;
        this.numberOfElements = numberOfElements;
    }

    public PageResult (List<T> data, boolean hasNext, int pageNumber, int pageSize, int numberOfElements) {
        this.items = data;
        this.hasNext = hasNext;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.numberOfElements = numberOfElements;
    }

    public static<T> PageResult<T> convertPage(Page<T> page) {
        return new PageResult<T>(page.getContent(), page.getTotalElements(), page.hasNext(), page.getNumber() + 1, page.getSize(), page.getTotalPages(), page.getNumberOfElements());
    }

    public static<T> PageResult<T> convertSlice(Slice<T> slice) {
        return new PageResult<T>(slice.getContent(), slice.hasNext(),slice.getNumber() + 1, slice.getSize(), slice.getNumberOfElements());
    }

    public List<T> getItems() {
        return items;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getNumberOfElements() {
        return numberOfElements;
    }

    public void setNumberOfElements(int numberOfElements) {
        this.numberOfElements = numberOfElements;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public boolean isHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }
}