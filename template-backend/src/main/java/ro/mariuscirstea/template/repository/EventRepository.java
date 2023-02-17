package ro.mariuscirstea.template.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import ro.mariuscirstea.template.entity.EVT_Event;

import java.util.List;

public interface EventRepository extends JpaRepository<EVT_Event, Long> {

    @Query("from evt_event e order by e.created desc")
    List<EVT_Event> findLatestSortedByCreatedTimestamp(Pageable pageable);

    Page<EVT_Event> findAll(Specification<EVT_Event> specification, Pageable pageable);

}
