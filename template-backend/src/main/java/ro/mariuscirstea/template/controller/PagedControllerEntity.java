package ro.mariuscirstea.template.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import ro.mariuscirstea.template.model.PagedResult;

import java.util.Map;

public interface PagedControllerEntity<T> {

    ResponseEntity<Map<String, PagedResult<T>>> getPaged(Integer pageSize, Integer pageNo, String sortBy, String sortOrder, String filters);

    PageRequest validateParams(Integer pageSize, Integer pageNo, String sortBy, String sortOrder);

    PagedResult<T> buildPagedResult(PageRequest pageRequest, Page<T> page);

    Map<String, String> parseFilters(String filters);
}
