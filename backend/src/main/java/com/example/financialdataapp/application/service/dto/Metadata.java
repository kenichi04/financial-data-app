package com.example.financialdataapp.application.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class Metadata {
    private String title;
    private Parameter parameter;
    @JsonProperty("resultset")
    private ResultSet resultSet;
    private String processDateTime;
    private String status;
    private String message;


    @Getter
    public static class Parameter {
        private String date;
        private String type;
    }

    @Getter
    public static class ResultSet {
        private int count;
    }
}
