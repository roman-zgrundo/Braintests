package by.zgrundo.braintests.util;


import by.zgrundo.braintests.models.User;
import by.zgrundo.braintests.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Objects;
import java.util.Optional;

@Component
public class UserValidator implements Validator {

    private final UserService userService;

    @Autowired
    public UserValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User userToBeChecked = (User) target;
        Optional<User> userFromDb = userService.findByName(userToBeChecked.getName());
        if (userFromDb.isPresent() && !Objects.equals(userFromDb.get().getId(), userToBeChecked.getId())) {
            errors.rejectValue("name", "", "Account with such name already exists");
        }
    }
}
