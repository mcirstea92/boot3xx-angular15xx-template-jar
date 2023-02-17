package ro.mariuscirstea.template.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ro.mariuscirstea.template.entity.CFG_Configuration;
import ro.mariuscirstea.template.entity.EVT_Event;

import java.util.List;

public interface ConfigurationRepository extends CrudRepository<CFG_Configuration, Long> {

    @Query("from cfg_configuration as cfg where cfg.category = ?1")
    List<CFG_Configuration> findByCategory(String category);

    @Query("from cfg_configuration as cfg where cfg.name = ?1")
    List<CFG_Configuration> findByName(String name);

    @Query("select max(cfg.version) from cfg_configuration as cfg where cfg.name = ?1")
    Integer findLatestVersion(String configName);

    @Query("from cfg_configuration as cfg where cfg.name = ?1 and cfg.version = ?2")
    CFG_Configuration findByNameAndVersion(String configName, Integer version);

    @Query("delete from cfg_configuration as cfg where cfg.name = ?1 and cfg.version = ?2")
    @Modifying
    void deleteByNameAndVersion(String name, Integer version);

    @Query(value = "SELECT * FROM cfg_configuration a INNER JOIN (SELECT name, MAX(version) version FROM cfg_configuration group by name) b ON b.name = a.name and b.version = a.version", nativeQuery = true)
    List<CFG_Configuration> getLatestConfigurations();

    List<CFG_Configuration> findAllBy(Pageable pageable);

    Page<CFG_Configuration> findAll(Specification<CFG_Configuration> specification, Pageable pageable);
}
