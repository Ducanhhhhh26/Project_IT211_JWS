package springboot.project.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import springboot.project.dto.CourseDTO;
import springboot.project.dto.CourseRequestDTO;
import springboot.project.entity.Course;
import springboot.project.repository.CourseRepository;
import springboot.project.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CourseService courseService;

    @Test
    void createCourse_Success() {
        // Arrange
        CourseRequestDTO requestDTO = new CourseRequestDTO();
        requestDTO.setName("Java Spring Boot");
        requestDTO.setDescription("Learn Spring Boot from scratch");
        requestDTO.setLecturerId(10L);

        springboot.project.entity.User mockLecturer = new springboot.project.entity.User();
        mockLecturer.setId(10L);

        Course savedCourse = new Course();
        savedCourse.setId(1L);
        savedCourse.setName("Java Spring Boot");
        savedCourse.setDescription("Learn Spring Boot from scratch");

        when(userRepository.findById(10L)).thenReturn(java.util.Optional.of(mockLecturer));
        when(courseRepository.save(any(Course.class))).thenReturn(savedCourse);

        // Act
        CourseDTO responseDTO = courseService.createCourse(requestDTO);

        // Assert
        assertNotNull(responseDTO);
        assertEquals(1L, responseDTO.getId());
        assertEquals("Java Spring Boot", responseDTO.getName());
        verify(userRepository, times(1)).findById(10L);
        verify(courseRepository, times(1)).save(any(Course.class));
    }
}
