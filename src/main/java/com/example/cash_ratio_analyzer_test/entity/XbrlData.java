package com.example.cash_ratio_analyzer_test.entity;

public class XbrlData {
    private String name;
    private String contextRef;
    private String unitRef;
    private String value;

    public XbrlData(String name, String contextRef, String unitRef, String value) {
        this.name = name;
        this.contextRef = contextRef;
        this.unitRef = unitRef;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getContextRef() {
        return contextRef;
    }

    public String getUnitRef() {
        return unitRef;
    }

    public String getValue() {
        return value;
    }
}
