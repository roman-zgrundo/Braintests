package by.zgrundo.braintests.repository;

import by.zgrundo.braintests.models.WordsForGestures;
import org.springframework.data.repository.CrudRepository;

public interface Gestures_wordsRepo extends CrudRepository<WordsForGestures, Long> {
}
