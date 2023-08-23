package ru.green.nca.dto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;
@Data
@AllArgsConstructor
public class UserDto {
    private int id;
    private String username;
    private String password;
    private String name;
    private String surname;
    private String parentName;
    private Instant creationDate;
    private Instant lastEditDate;
    private int roleId;

}
