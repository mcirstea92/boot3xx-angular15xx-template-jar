package ro.mariuscirstea.template.entity;

import jakarta.persistence.*;
import ro.mariuscirstea.template.model.ERole;

@Entity(name = "rol_role")
@Table(name = "rol_role")
public class ROL_Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ERole name;

    public ROL_Role() {
        this.name = ERole.ROLE_DEMO;
    }

    public ROL_Role(ERole name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ERole getName() {
        return name;
    }

    public void setName(ERole role) {
        this.name = role;
    }

}