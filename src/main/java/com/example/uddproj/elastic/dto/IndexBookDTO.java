package com.example.uddproj.elastic.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IndexBookDTO {
    private String fileName;
    private String title;
    private List<String> writers;
    private String genre;
    private Boolean isOpenAccess;

}
