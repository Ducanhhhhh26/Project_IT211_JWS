package springboot.project.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springboot.project.dto.ApiResponseDTO;
import springboot.project.dto.CourseDTO;
import springboot.project.dto.CourseRequestDTO;
import springboot.project.dto.RegisterRequestDTO;
import springboot.project.dto.UserDTO;
import springboot.project.service.CourseService;
import springboot.project.service.UserService;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;
    private final CourseService courseService;

    // --- USER MANAGEMENT ---

    @GetMapping("/users")
    public ResponseEntity<ApiResponseDTO<Page<UserDTO>>> getUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search) {
        Page<UserDTO> users = userService.getAllUsers(page, size, search);
        return ResponseEntity.ok(new ApiResponseDTO<>(200, "Success", users));
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<ApiResponseDTO<UserDTO>> getUserById(@PathVariable Long id) {
        UserDTO user = userService.getUserById(id);
        return ResponseEntity.ok(new ApiResponseDTO<>(200, "Success", user));
    }

    @PostMapping("/users")
    public ResponseEntity<ApiResponseDTO<UserDTO>> createUser(@RequestBody RegisterRequestDTO request) {
        UserDTO user = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponseDTO<>(201, "User created", user));
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<ApiResponseDTO<UserDTO>> updateUser(@PathVariable Long id, @RequestBody UserDTO request) {
        UserDTO user = userService.updateUser(id, request);
        return ResponseEntity.ok(new ApiResponseDTO<>(200, "User updated", user));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<ApiResponseDTO<Void>> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(new ApiResponseDTO<>(200, "User deleted (deactivated)", null));
    }

    // --- COURSE MANAGEMENT ---

    @GetMapping("/courses")
    public ResponseEntity<ApiResponseDTO<Page<CourseDTO>>> getCourses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search) {
        Page<CourseDTO> courses = courseService.getAllCourses(page, size, search);
        return ResponseEntity.ok(new ApiResponseDTO<>(200, "Success", courses));
    }

    @PostMapping("/courses")
    public ResponseEntity<ApiResponseDTO<CourseDTO>> createCourse(@RequestBody CourseRequestDTO request) {
        CourseDTO course = courseService.createCourse(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponseDTO<>(201, "Course created", course));
    }

    @PutMapping("/courses/{id}")
    public ResponseEntity<ApiResponseDTO<CourseDTO>> updateCourse(@PathVariable Long id, @RequestBody CourseRequestDTO request) {
        CourseDTO course = courseService.updateCourse(id, request);
        return ResponseEntity.ok(new ApiResponseDTO<>(200, "Course updated", course));
    }

    @DeleteMapping("/courses/{id}")
    public ResponseEntity<ApiResponseDTO<Void>> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return ResponseEntity.ok(new ApiResponseDTO<>(200, "Course deleted", null));
    }
}
