package by.zgrundo.braintests.repository;


import by.zgrundo.braintests.models.SpeedAlphabetLetters;
import by.zgrundo.braintests.models.SpeedAlphabetTexts;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpeedAlphabetTextsRepo extends JpaRepository<SpeedAlphabetTexts, Long> {
    List<SpeedAlphabetTexts> findByLetter(SpeedAlphabetLetters letter);
}