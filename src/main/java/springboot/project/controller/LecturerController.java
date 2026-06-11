package springboot.project.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springboot.project.dto.ApiResponseDTO;
import springboot.project.dto.GradeRequestDTO;
import springboot.project.dto.LectureMaterialDTO;
import springboot.project.dto.SubmissionDTO;
import springboot.project.service.LectureMaterialService;
import springboot.project.service.SubmissionService;

@RestController
@RequestMapping("/api/v1/lecturer")
@RequiredArgsConstructor
public class LecturerController {

    private final LectureMaterialService materialService;
    private final SubmissionService submissionService;
    private final springboot.project.service.AssignmentService assignmentService;

    @PostMapping("/assignments")
    public ResponseEntity<ApiResponseDTO<springboot.project.dto.AssignmentDTO>> createAssignment(@RequestBody springboot.project.dto.AssignmentRequestDTO request) {
        springboot.project.dto.AssignmentDTO assignment = assignmentService.createAssignment(request);
        return ResponseEntity.status(org.springframework.http.HttpStatus.CREATED)
                .body(new ApiResponseDTO<>(201, "Assignment created successfully", assignment));
    }

    @PostMapping(value = "/materials", consumes = org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponseDTO<LectureMaterialDTO>> uploadMaterial(
            @RequestParam("courseId") Long courseId,
            @RequestParam("title") String title,
            @RequestParam("file") org.springframework.web.multipart.MultipartFile file) throws java.io.IOException {
        
        LectureMaterialDTO material = materialService.uploadMaterial(courseId, title, file);
        return ResponseEntity.ok(new ApiResponseDTO<>(200, "Material uploaded successfully", material));
    }

    @PutMapping("/submissions/{id}/grade")
    public ResponseEntity<ApiResponseDTO<SubmissionDTO>> gradeSubmission(
            @PathVariable Long id,
            @RequestBody GradeRequestDTO request) {
        
        SubmissionDTO submission = submissionService.gradeSubmission(id, request);
        return ResponseEntity.ok(new ApiResponseDTO<>(200, "Submission graded successfully", submission));
    }
}
