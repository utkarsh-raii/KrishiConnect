package com.krishiconnect.dto;

public class PaginationMeta {
    private long total;
    private int page;
    private int limit;
    private int totalPages;
    private boolean hasNextPage;
    private boolean hasPrevPage;

    public PaginationMeta() {}

    public PaginationMeta(long total, int page, int limit) {
        this.total = total;
        this.page = page;
        this.limit = limit;
        this.totalPages = limit > 0 ? (int) Math.ceil((double) total / limit) : 0;
        this.hasNextPage = page < totalPages;
        this.hasPrevPage = page > 1;
    }

    public long getTotal() { return total; }
    public void setTotal(long total) { this.total = total; }

    public int getPage() { return page; }
    public void setPage(int page) { this.page = page; }

    public int getLimit() { return limit; }
    public void setLimit(int limit) { this.limit = limit; }

    public int getTotalPages() { return totalPages; }
    public void setTotalPages(int totalPages) { this.totalPages = totalPages; }

    public boolean isHasNextPage() { return hasNextPage; }
    public void setHasNextPage(boolean hasNextPage) { this.hasNextPage = hasNextPage; }

    public boolean isHasPrevPage() { return hasPrevPage; }
    public void setHasPrevPage(boolean hasPrevPage) { this.hasPrevPage = hasPrevPage; }
}
