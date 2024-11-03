package com.example.telegram_botjob;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobVacancy {


    private Long id;


    private String companyName;


    private String vacancyName;


    private String offer;


    private String description;


    private LocalDateTime createdDate;

    private RemoteWork remoteWork;

    private String alternateUrl;

}

