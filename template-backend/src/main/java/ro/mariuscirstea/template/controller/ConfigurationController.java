package ro.mariuscirstea.template.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.mariuscirstea.template.entity.CFG_Configuration;
import ro.mariuscirstea.template.model.PagedResult;
import ro.mariuscirstea.template.security.IsAdmin;
import ro.mariuscirstea.template.service.ConfigurationService;
import ro.mariuscirstea.template.specifications.GenericSpecification;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController("configs")
@RequestMapping(path = "/configs")
@CrossOrigin(origins = {"http://localhost:4200"})
@Slf4j
public class ConfigurationController extends PagedControllerEntityImpl<CFG_Configuration> {

    private final ConfigurationService configurationService;

    public ConfigurationController(ConfigurationService configurationService, ObjectMapper objectMapper) {
        super(objectMapper);
        this.configurationService = configurationService;
    }

    @GetMapping("/public/getLatest")
    public ResponseEntity<Map<String, List<CFG_Configuration>>> getLatestConfigurations() {
        return ResponseEntity.ok(Collections.singletonMap("configs", configurationService.getLatestConfigs()));
    }

    @GetMapping("/public/getAll")
    public ResponseEntity<Map<String, List<CFG_Configuration>>> getAllConfigurations() {
        return ResponseEntity.ok(Collections.singletonMap("configs", configurationService.getAll()));
    }

    @IsAdmin
    @PostMapping("/saveConfiguration")
    public ResponseEntity<Map<String, CFG_Configuration>> saveConfiguration(@RequestBody CFG_Configuration cfgConfiguration) {
        boolean added = cfgConfiguration.getId() == null;
        CFG_Configuration savedCfg = configurationService.saveConfiguration(cfgConfiguration);
        if (added) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
        return ResponseEntity.ok(Collections.singletonMap("config", savedCfg));
    }

    @Override
    @GetMapping("/getPaged")
    public ResponseEntity<Map<String, PagedResult<CFG_Configuration>>> getPaged(@RequestParam(name = "pageSize", required = false) Integer pageSize,
                                                                                @RequestParam(name = "pageNo", required = false) Integer pageNo,
                                                                                @RequestParam(name = "sortBy", required = false) String sortBy,
                                                                                @RequestParam(name = "sortOrder", required = false) String sortOrder,
                                                                                @RequestParam(name = "filters", required = false) String filters) {
        PageRequest pageRequest = super.validateParams(pageSize, pageNo, sortBy, sortOrder);
        Map<String, String> filtersMap = super.parseFilters(filters);
        log.info("Filters map: {}", filtersMap);
        GenericSpecification<CFG_Configuration> cfg_configurationSpecification = new GenericSpecification<>(filtersMap);
        Page<CFG_Configuration> page = configurationService.getFilteredConfigs(cfg_configurationSpecification, pageRequest);
        PagedResult<CFG_Configuration> result = super.buildPagedResult(pageRequest, page);
        return ResponseEntity.ok(Collections.singletonMap("pagedResult", result));
    }
}
