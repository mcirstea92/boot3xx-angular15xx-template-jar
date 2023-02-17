package ro.mariuscirstea.template.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.HashSet;
import java.util.Set;

@Entity(name = "usr_user")
@Table(name = "usr_user", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "email"
        }),
        @UniqueConstraint(columnNames = {
                "username"
        })
})
public class USR_User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;

    private String dateOfBirth;

    private String nickname;

    private String username;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "uro_user_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id", table = "usr_user"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id", table = "rol_role"))
    private Set<ROL_Role> roles = new HashSet<>();

    public String getPassword() {
        return password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dob) {
        this.dateOfBirth = dob;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nick) {
        this.nickname = nick;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<ROL_Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<ROL_Role> roles) {
        this.roles = roles;
    }

}