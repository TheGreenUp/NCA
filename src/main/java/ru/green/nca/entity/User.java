package ru.green.nca.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import ru.green.nca.enums.UserRole;

import java.time.Instant;

@Table(name = "users")
@Entity

@Slf4j
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String username;
    private String password;
    private String name;
    private String surname;
    private String parentName;
    @Column(name = "creation_date")
    @CreationTimestamp
    private Instant creationDate;
    @Column(name = "last_edit_date")
    @UpdateTimestamp
    private Instant lastEditDate;
    @Column(name = "id_role")
    private UserRole role;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

}
