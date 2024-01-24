package com.nobroker.payload;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private  long id ;

    private String name ;

    private String email ;

    private String password ;

    private String mobile;

    private boolean emailVerified;
}