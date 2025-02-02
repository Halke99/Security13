package Tasks.Service;

import Tasks.Model.Task;
import Tasks.Repository.TaskRepository;
import Users.Model.User;
import Users.Repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskService(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    public List<Task> getUserTasks() {
        User user = getAuthenticatedUser();
        return taskRepository.findByUserId(user.getId());
    }

    public Task createTask(Task task) {
        User user = getAuthenticatedUser();
        task.setUser(user);
        return taskRepository.save(task);
    }

    private User getAuthenticatedUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return Optional.ofNullable(userRepository.findByUsername(((UserDetails) principal).getUsername()))
                    .orElseThrow(() -> new RuntimeException("User not found"));
        }
        throw new RuntimeException("Authentication error");
    }
}

