package ro.mariuscirstea.template.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PagedResult<T> {

    private List<T> content;
    private int pageSize;
    private int pageNo;
    private int totalPages;
    private long totalElements;

}
