package springboot.project.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import springboot.project.dto.ApiResponseDTO;
import springboot.project.dto.SubmissionDTO;
import springboot.project.service.CourseService;
import springboot.project.service.SubmissionService;

@RestController
@RequestMapping("/api/v1/student")
@RequiredArgsConstructor
public class StudentController {

    private final CourseService courseService;
    private final SubmissionService submissionService;

    @PostMapping("/courses/{courseId}/register")
    public ResponseEntity<ApiResponseDTO<Void>> registerToCourse(@PathVariable Long courseId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        
        courseService.registerStudentToCourse(courseId, username);
        
        return ResponseEntity.ok(new ApiResponseDTO<>(200, "Registered to course successfully", null));
    }

    @PostMapping(value = "/assignments/{assignmentId}/submit", consumes = org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponseDTO<SubmissionDTO>> submitAssignment(
            @PathVariable Long assignmentId,
            @RequestParam("file") org.springframework.web.multipart.MultipartFile file) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        
        SubmissionDTO submission = submissionService.submitAssignment(assignmentId, username, file);
        return ResponseEntity.ok(new ApiResponseDTO<>(200, "Assignment submitted successfully", submission));
    }
}
