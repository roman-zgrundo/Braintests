package by.zgrundo.braintests.repository;

import by.zgrundo.braintests.models.Role;
import by.zgrundo.braintests.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByName(String name);
    // Добавляем метод для поиска пользователей по роли ROLE_USER
    List<User> findByRole(Role role);
}
