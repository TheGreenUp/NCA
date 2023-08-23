package ru.green.nca.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;

@Table(name = "users")
@Entity

@Slf4j
@Data
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
    private Instant creationDate;
    @Column(name = "last_edit_date")
    private Instant lastEditDate;
    @Column(name = "id_role")
    private int roleId;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @PrePersist
    public void prePersist() {
        creationDate = Instant.now();
        lastEditDate = Instant.now();
    }

    @PreUpdate
    public void preUpdate() {
        lastEditDate = Instant.now();
    }
}
