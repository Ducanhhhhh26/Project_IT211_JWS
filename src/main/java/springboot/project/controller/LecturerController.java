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

    @PostMapping("/materials")
    public ResponseEntity<ApiResponseDTO<LectureMaterialDTO>> uploadMaterial(
            @RequestParam Long courseId,
            @RequestParam String title,
            @RequestParam String fileUrl) {
        
        LectureMaterialDTO material = materialService.uploadMaterial(courseId, title, fileUrl);
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
