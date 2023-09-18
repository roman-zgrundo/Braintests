package by.zgrundo.braintests.repository;

import by.zgrundo.braintests.models.Stat_Stroop;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface Stat_StroopRepo extends CrudRepository<Stat_Stroop, Long> {
    List<Stat_Stroop> findAllByUserId(Long userId);
}
