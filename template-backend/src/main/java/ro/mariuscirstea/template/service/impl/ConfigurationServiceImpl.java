package ro.mariuscirstea.template.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ro.mariuscirstea.template.entity.CFG_Configuration;
import ro.mariuscirstea.template.model.ConfigurationCategory;
import ro.mariuscirstea.template.repository.ConfigurationRepository;
import ro.mariuscirstea.template.service.ConfigurationService;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ConfigurationServiceImpl implements ConfigurationService {

    private final ConfigurationRepository configurationRepository;

    public ConfigurationServiceImpl(ConfigurationRepository configurationRepository) {
        this.configurationRepository = configurationRepository;
    }

    @Override
    public List<CFG_Configuration> getAll() {
        return StreamSupport.stream(configurationRepository.findAll().spliterator(), false)
                .sorted(Comparator.comparing(CFG_Configuration::getCreated).reversed())
                .collect(Collectors.toList());
    }

    private final List<String> allowedCfgConfigurationFieldNames = Arrays.stream(CFG_Configuration.class.getDeclaredFields()).map(Field::getName).collect(Collectors.toList());

    @Override
    public Page<CFG_Configuration> getFilteredConfigs(Specification<CFG_Configuration> specification, PageRequest pageRequest) {
        String sortBy = pageRequest.getSort().get().map(Sort.Order::getProperty).findFirst().orElseThrow(() -> new IllegalArgumentException("Sort by field not supplied"));

        if (!allowedCfgConfigurationFieldNames.contains(sortBy)) {
            throw new IllegalArgumentException("SortBy field " + sortBy + " not allowed for entity cfg_configuration!");
        }
        return configurationRepository.findAll(specification, pageRequest);
    }

    @Override
    public List<CFG_Configuration> getConfigurationsWithCategory(ConfigurationCategory configurationCategory) {
        return configurationRepository.findByCategory(configurationCategory.getValue());
    }

    @Override
    public List<CFG_Configuration> getConfigurationsWithName(String configName) {
        return configurationRepository.findByName(configName);
    }

    @Override
    public Integer getLatestVersion(String configName) {
        return configurationRepository.findLatestVersion(configName);
    }

    @Override
    public CFG_Configuration getConfigurationWithNameAndVersion(String configName, Integer version) {
        return configurationRepository.findByNameAndVersion(configName, version);
    }

    @Override
    public List<CFG_Configuration> getLatestConfigs() {
        return configurationRepository.getLatestConfigurations();
    }

    @Override
    public CFG_Configuration saveConfiguration(CFG_Configuration configuration) {
        return configurationRepository.save(configuration);
    }

    @Override
    public void deleteConfiguration(String configName, Integer version) {
        configurationRepository.deleteByNameAndVersion(configName, version);
    }

    @Override
    public void deleteConfiguration(Long configurationId) {
        configurationRepository.deleteById(configurationId);
    }
}
