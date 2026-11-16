package com.krishiconnect.dto;

public class DurationDto {
    private Integer value = 1;
    private String unit = "Days";

    public DurationDto() {}

    public DurationDto(Integer value, String unit) {
        this.value = value;
        this.unit = unit;
    }

    public Integer getValue() { return value; }
    public void setValue(Integer value) { this.value = value; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
}
