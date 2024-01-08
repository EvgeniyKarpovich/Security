package by.karpovich.security.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Entity
@Getter
@Setter
@Builder
@Table(name = "users", schema = "security")
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String firstname;
    private String lastname;
    private String password;
    @Column(name = "password_confirmation")
    private String passwordConfirmation;
    private Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private List<Token> tokens = new ArrayList<>();
}
