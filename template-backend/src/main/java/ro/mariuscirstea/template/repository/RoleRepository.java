package ro.mariuscirstea.template.repository;

import org.springframework.data.repository.CrudRepository;
import ro.mariuscirstea.template.entity.ROL_Role;
import ro.mariuscirstea.template.model.ERole;

import java.util.Optional;

public interface RoleRepository extends CrudRepository<ROL_Role, Long> {
    Optional<ROL_Role> findByName(String roleName);

    Optional<ROL_Role> findByName(ERole roleName);
}
