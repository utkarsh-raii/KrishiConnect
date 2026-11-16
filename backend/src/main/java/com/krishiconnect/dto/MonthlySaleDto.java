package com.krishiconnect.dto;

import java.math.BigDecimal;

public class MonthlySaleDto {
    private int year;
    private int month;
    private BigDecimal total;
    private long count;

    public MonthlySaleDto() {}

    public MonthlySaleDto(int year, int month, BigDecimal total, long count) {
        this.year = year;
        this.month = month;
        this.total = total;
        this.count = count;
    }

    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }

    public int getMonth() { return month; }
    public void setMonth(int month) { this.month = month; }

    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }

    public long getCount() { return count; }
    public void setCount(long count) { this.count = count; }
}
