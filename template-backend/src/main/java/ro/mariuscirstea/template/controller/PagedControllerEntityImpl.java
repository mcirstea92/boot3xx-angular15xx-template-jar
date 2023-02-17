package ro.mariuscirstea.template.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ro.mariuscirstea.template.model.PagedResult;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class PagedControllerEntityImpl<T> implements PagedControllerEntity<T> {

    private final List<String> allowedSortOrderKeywords = Arrays.asList("asc", "desc");
    private final ObjectMapper objectMapper;

    protected PagedControllerEntityImpl(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public PageRequest validateParams(Integer pageSize, Integer pageNo, String sortBy, String sortOrder) {
        if (pageSize == null) {
            pageSize = 20;
        }
        if (pageSize > 100) {
            throw new IllegalArgumentException("Page size is bigger than max allowed value [100]!");
        }
        if (pageNo == null) {
            pageNo = 0;
        }
        if (sortBy == null) {
            sortBy = "id";
        }
        if (sortOrder == null) {
            sortOrder = "desc";
        }
        if (!allowedSortOrderKeywords.contains(sortOrder)) {
            throw new IllegalArgumentException("SortOrder field " + sortOrder + " not allowed!");
        }
        return PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.fromString(sortOrder), sortBy));
    }

    @Override
    public PagedResult<T> buildPagedResult(PageRequest pageRequest, Page<T> page) {
        PagedResult<T> result = new PagedResult<>();
        result.setContent(page.getContent());
        result.setPageNo(pageRequest.getPageNumber());
        result.setPageSize(pageRequest.getPageSize());
        result.setTotalPages(page.getTotalPages());
        result.setTotalElements(page.getTotalElements());
        return result;
    }

    @Override
    public Map<String, String> parseFilters(String filters) {
        Map<String, String> filtersMap = null;
        if (filters == null || filters.trim().length() == 0) {
            filtersMap = new HashMap<>();
        } else {
            try {
                filtersMap = objectMapper.readValue(filters, Map.class);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return filtersMap;
    }

}
