package springboot.project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import springboot.project.dto.AssignmentDTO;
import springboot.project.dto.AssignmentRequestDTO;
import springboot.project.entity.Assignment;
import springboot.project.entity.Course;
import springboot.project.repository.AssignmentRepository;
import springboot.project.repository.CourseRepository;

@Service
@RequiredArgsConstructor
public class AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final CourseRepository courseRepository;

    public AssignmentDTO createAssignment(AssignmentRequestDTO request) {
        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course not found"));

        Assignment assignment = new Assignment();
        assignment.setTitle(request.getTitle());
        assignment.setDescription(request.getDescription());
        assignment.setCourse(course);

        assignment = assignmentRepository.save(assignment);
        
        return new AssignmentDTO(assignment.getId(), assignment.getTitle(), assignment.getDescription(), course.getId());
    }
}
