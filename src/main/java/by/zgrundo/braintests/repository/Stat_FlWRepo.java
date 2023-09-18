package by.zgrundo.braintests.repository;

import by.zgrundo.braintests.models.Stat_FlW;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface Stat_FlWRepo extends CrudRepository<Stat_FlW, Long> {
    List<Stat_FlW> findAllByUserId(Long userId);
}
