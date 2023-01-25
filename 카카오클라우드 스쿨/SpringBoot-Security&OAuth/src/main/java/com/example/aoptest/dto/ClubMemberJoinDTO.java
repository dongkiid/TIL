package com.example.aoptest.dto;

import lombok.Data;

@Data
public class ClubMemberJoinDTO {
    private String mid;
    private String mpw;
    private String email;
    private String name;
    private boolean del;
    private boolean social;

}
