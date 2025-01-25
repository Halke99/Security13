package Users.Service;

import Users.Model.User;
import Users.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Create a new user
    public User createUser(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists!");
        }
        return userRepository.save(user);
    }

    // Authenticate a user
    public User authenticate(String email, String password) {
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (user.getPassword().equals(password)) {
                return user;
            } else {
                throw new IllegalArgumentException("Invalid password!");
            }
        } else {
            throw new IllegalArgumentException("User not found!");
        }
    }
}
