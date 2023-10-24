package by.zgrundo.braintests.service;


import by.zgrundo.braintests.models.Role;
import by.zgrundo.braintests.models.User;
import by.zgrundo.braintests.repository.BalanceHistoryRepo;
import by.zgrundo.braintests.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;


@Service
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final BalanceHistoryService BalanceHistoryService; // Инжектируйте сервис BalanceHistoryService
    private final Logger logger = Logger.getLogger(UserService.class.getName());


    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, BalanceHistoryService BalanceHistoryService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.BalanceHistoryService = BalanceHistoryService; // Инициализируйте сервис BalanceHistoryService

    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findOne(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public Optional<User> findByName(String name) {
        return userRepository.findByName(name);
    }

    @Transactional
    public void createUser(User user) {
        user.setActive(true);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.ROLE_USER);
        user.setTeacher(user.getTeacher());
//        user.setBalance(0);
        userRepository.save(user);
    }

    @Transactional
    public void update(Long id, User updatedUser) {
        User userToBeUpdated = userRepository.findById(id).get();
        updatedUser.setId(id);
        updatedUser.setActive(userToBeUpdated.isActive());
        updatedUser.setRole(userToBeUpdated.getRole());
        updatedUser.setPassword(userToBeUpdated.getPassword());
        userRepository.save(updatedUser);
    }

    @Transactional
    public void changeRole(Long id, Role role) {
        User user = userRepository.findById(id).get();
        user.setRole(role);
        userRepository.save(user);
    }

    @Transactional
    public void changeActive(Long id) {
        User user = userRepository.findById(id).get();
        user.setActive(!user.isActive());
        userRepository.save(user);
        logger.info("Deactivating ОК");
    }

    // Метод для проверки и обновления активности пользователей
    @Scheduled(cron = "0 01 00 * * ?")
    @Transactional
    public void checkAndDeactivateInactiveUsers() {
        logger.info("Running checkAndDeactivateInactiveUsers...");

        List<User> usersWithRoleUser = userRepository.findByRole(Role.ROLE_USER);

        for (User user : usersWithRoleUser) {
            logger.info("Checking user with ID: " + user.getId());

            LocalDateTime lastExpenseDate = BalanceHistoryService.findLastExpenseDateByUserId(user.getId());

            if (lastExpenseDate != null) {
                logger.info("Last expense date for user ID " + user.getId() + ": " + lastExpenseDate.toString());

                LocalDateTime now = LocalDateTime.now();
                Duration duration = Duration.between(lastExpenseDate, now);
                long daysBetween = duration.toDays();
                logger.info("Days since last expense: " + daysBetween);

                if (daysBetween > 7) {
                    logger.info("Deactivating user with ID: " + user.getId());
                    user.setActive(false);
                    userRepository.save(user);
                }
                else {
                    logger.info("Activating user with ID: " + user.getId());
                    user.setActive(true);
                    userRepository.save(user);
                }
            }
        }
    }




}
