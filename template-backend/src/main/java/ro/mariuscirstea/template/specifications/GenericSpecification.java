package ro.mariuscirstea.template.specifications;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GenericSpecification<T> implements Specification<T> {

    private final Map<String, String> criteria;

    public GenericSpecification(Map<String, String> criteria) {
        this.criteria = criteria;
    }

    @Override
    public Specification<T> and(Specification<T> other) {
        return Specification.super.and(other);
    }

    @Override
    public Specification<T> or(Specification<T> other) {
        return Specification.super.or(other);
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        Set<String> keys = criteria.keySet();
        List<Predicate> filters = new ArrayList<>();
        if (criteria.size() != 0) {
            for (String key : keys) {
                String filterValue = criteria.get(key);
                if (filterValue != null) {
                    filters.add(criteriaBuilder.like(criteriaBuilder.upper(root.get(key)), "%" + filterValue.toUpperCase() + "%"));
                }
            }
        }
        return criteriaBuilder.and(filters.toArray(new Predicate[0]));
    }

}
