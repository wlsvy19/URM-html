package com.ism.urm.vo;

import java.io.Serializable;
import java.util.List;

public class PagingResult<T extends Object> implements Serializable {

    private static final long serialVersionUID = 1L;

    private int pageSize;
    private int curPage;
    private long totalCount;

    private List<T> list;

    public int getPageSize() {
        return pageSize;
    }
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
    public List<T> getList() {
        return list;
    }
    public void setList(List<T> list) {
        this.list = list;
    }
    public int getCurPage() {
        return curPage;
    }
    public void setCurPage(int curPage) {
        this.curPage = curPage;
    }
    public long getTotalCount() {
        return totalCount;
    }
    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    @Override
    public String toString() {
        return "PagingResult [pageSize=" + pageSize + ", curPage=" + curPage + ", totalCount=" + totalCount + ", list="
                + list + "]";
    }

}
