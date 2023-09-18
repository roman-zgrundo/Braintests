package by.zgrundo.braintests.service;


import by.zgrundo.braintests.models.Role;
import by.zgrundo.braintests.models.User;
import by.zgrundo.braintests.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;



@Service
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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
    }
}
