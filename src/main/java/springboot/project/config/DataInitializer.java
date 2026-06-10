package springboot.project.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import springboot.project.entity.Course;
import springboot.project.entity.User;
import springboot.project.repository.CourseRepository;
import springboot.project.repository.UserRepository;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            // Seed Admin
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole("ROLE_ADMIN");
            admin.setActive(true);
            userRepository.save(admin);

            // Seed Lecturer
            User lecturer = new User();
            lecturer.setUsername("lecturer");
            lecturer.setPassword(passwordEncoder.encode("lecturer123"));
            lecturer.setRole("ROLE_LECTURER");
            lecturer.setActive(true);
            userRepository.save(lecturer);

            // Seed Course
            Course course = new Course();
            course.setName("Java Spring Boot");
            course.setDescription("Advanced Backend Development");
            course.setLecturer(lecturer);
            courseRepository.save(course);
        }
    }
}
