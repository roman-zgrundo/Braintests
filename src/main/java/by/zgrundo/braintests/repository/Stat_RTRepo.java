package by.zgrundo.braintests.repository;

import by.zgrundo.braintests.models.Stat_RT;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface Stat_RTRepo extends CrudRepository<Stat_RT, Long> {
    List<Stat_RT> findAllByUserId(Long userId);
}
