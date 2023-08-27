package ru.green.nca.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.green.nca.enums.UserRole;

import java.time.Instant;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private int id;
    private String username;
    private String password;
    private String name;
    private String surname;
    private String parentName;
    private Instant creationDate;
    private Instant lastEditDate;
    private UserRole role;

}
