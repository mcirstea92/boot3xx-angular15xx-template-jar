package ro.mariuscirstea.template.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import ro.mariuscirstea.template.entity.CFG_Configuration;
import ro.mariuscirstea.template.model.ConfigurationCategory;

import java.util.List;

public interface ConfigurationService {

    List<CFG_Configuration> getAll();

    Page<CFG_Configuration> getFilteredConfigs(Specification<CFG_Configuration> specification, PageRequest pageRequest);

    List<CFG_Configuration> getConfigurationsWithCategory(ConfigurationCategory configurationCategory);

    /**
     * @param configName the config name
     * @return a @{@link List} with {@link CFG_Configuration} - multiple versions are allowed for a given config name
     */
    List<CFG_Configuration> getConfigurationsWithName(String configName);

    Integer getLatestVersion(String configName);

    CFG_Configuration getConfigurationWithNameAndVersion(String configName, Integer version);

    List<CFG_Configuration> getLatestConfigs();

    CFG_Configuration saveConfiguration(CFG_Configuration configuration);

    void deleteConfiguration(String configName, Integer version);

    void deleteConfiguration(Long configurationId);

}
