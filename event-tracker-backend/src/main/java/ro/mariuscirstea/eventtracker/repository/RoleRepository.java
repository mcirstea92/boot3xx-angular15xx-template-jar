package ro.mariuscirstea.eventtracker.repository;

import org.springframework.data.repository.CrudRepository;
import ro.mariuscirstea.eventtracker.entity.Role;

import java.util.Optional;

public interface RoleRepository extends CrudRepository<Role, Long> {
    Optional<Role> findByName(String roleName);
}
