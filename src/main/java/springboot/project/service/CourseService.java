package springboot.project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import springboot.project.dto.CourseDTO;
import springboot.project.dto.CourseRequestDTO;
import springboot.project.entity.Course;
import springboot.project.entity.User;
import springboot.project.repository.CourseRepository;
import springboot.project.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseService {
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    public Page<CourseDTO> getAllCourses(int page, int size, String search) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Course> coursePage;
        if (search != null && !search.isEmpty()) {
            coursePage = courseRepository.findByNameContainingIgnoreCase(search, pageable);
        } else {
            coursePage = courseRepository.findAll(pageable);
        }

        List<CourseDTO> dtoList = coursePage.getContent().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());

        return new PageImpl<>(dtoList, pageable, coursePage.getTotalElements());
    }

    public CourseDTO createCourse(CourseRequestDTO requestDTO) {
        Course course = new Course();
        course.setName(requestDTO.getName());
        course.setDescription(requestDTO.getDescription());
        
        if (requestDTO.getLecturerId() != null) {
            User lecturer = userRepository.findById(requestDTO.getLecturerId())
                    .orElseThrow(() -> new RuntimeException("Lecturer not found"));
            course.setLecturer(lecturer);
        }
        
        course.setStudents(new ArrayList<>());
        return mapToDTO(courseRepository.save(course));
    }

    public CourseDTO updateCourse(Long id, CourseRequestDTO requestDTO) {
        Course course = courseRepository.findById(id).orElseThrow(() -> new RuntimeException("Course not found"));
        course.setName(requestDTO.getName());
        course.setDescription(requestDTO.getDescription());
        
        if (requestDTO.getLecturerId() != null) {
            User lecturer = userRepository.findById(requestDTO.getLecturerId())
                    .orElseThrow(() -> new RuntimeException("Lecturer not found"));
            course.setLecturer(lecturer);
        }
        return mapToDTO(courseRepository.save(course));
    }

    public void deleteCourse(Long id) {
        if (!courseRepository.existsById(id)) {
            throw new RuntimeException("Course not found");
        }
        courseRepository.deleteById(id);
    }

    public void registerStudentToCourse(Long courseId, String username) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        User student = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
                
        if (course.getStudents().contains(student)) {
            throw new RuntimeException("Student is already registered to this course");
        }
        
        course.getStudents().add(student);
        courseRepository.save(course);
    }

    private CourseDTO mapToDTO(Course course) {
        return new CourseDTO(
                course.getId(), 
                course.getName(), 
                course.getDescription(), 
                course.getLecturer() != null ? userService.mapToDTO(course.getLecturer()) : null
        );
    }
}
