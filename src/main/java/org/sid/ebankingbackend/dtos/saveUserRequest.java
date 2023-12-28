package org.sid.ebankingbackend.dtos;

import lombok.Data;

@Data
public class saveUserRequest {
    private String username;
    private String password;
    private String namecustomer;
    private String rolename;
}
