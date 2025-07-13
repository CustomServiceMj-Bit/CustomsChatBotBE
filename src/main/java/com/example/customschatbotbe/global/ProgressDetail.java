package com.example.customschatbotbe.global;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProgressDetail {
    private String datetime;
    private String status;
    private String comment;
}