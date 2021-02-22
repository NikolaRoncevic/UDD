package com.example.uddproj.elastic.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IndexReviewerDTO {

    private String reviewerUsername;
    private String reviewerFirstName;
    private String reviewerLastName;
    private long lat;
    private long lng;
}
