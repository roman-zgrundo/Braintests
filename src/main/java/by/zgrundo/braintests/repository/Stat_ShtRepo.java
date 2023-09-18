package by.zgrundo.braintests.repository;

import by.zgrundo.braintests.models.Stat_Sht;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface Stat_ShtRepo extends CrudRepository<Stat_Sht, Long> {
    List<Stat_Sht> findAllByUserId(Long userId);
//    List<Stat_Sht> findByTop5ByUserId(Long userId);
}
