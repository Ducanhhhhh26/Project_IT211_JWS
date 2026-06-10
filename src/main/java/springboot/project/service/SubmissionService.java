package springboot.project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import springboot.project.dto.GradeRequestDTO;
import springboot.project.dto.SubmissionDTO;
import springboot.project.entity.Assignment;
import springboot.project.entity.Submission;
import springboot.project.entity.User;
import springboot.project.repository.AssignmentRepository;
import springboot.project.repository.SubmissionRepository;
import springboot.project.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class SubmissionService {

    private final SubmissionRepository submissionRepository;
    private final AssignmentRepository assignmentRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final CloudinaryService cloudinaryService;

    public SubmissionDTO submitAssignment(Long assignmentId, String username, org.springframework.web.multipart.MultipartFile file) {
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));
        User student = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        String reportUrl;
        try {
            reportUrl = cloudinaryService.uploadFile(file);
        } catch (java.io.IOException e) {
            throw new RuntimeException("Failed to upload file to Cloudinary", e);
        }

        Submission submission = new Submission();
        submission.setAssignment(assignment);
        submission.setStudent(student);
        submission.setReportUrl(reportUrl);
        submission.setStatus("SUBMITTED");
        
        submission = submissionRepository.save(submission);
        return mapToDTO(submission);
    }

    public SubmissionDTO gradeSubmission(Long submissionId, GradeRequestDTO request) {
        Submission submission = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new RuntimeException("Submission not found"));
        
        submission.setScore(request.getScore());
        submission.setFeedback(request.getFeedback());
        submission.setStatus("GRADED");
        
        submission = submissionRepository.save(submission);
        return mapToDTO(submission);
    }

    private SubmissionDTO mapToDTO(Submission submission) {
        return new SubmissionDTO(
                submission.getId(),
                submission.getAssignment() != null ? submission.getAssignment().getId() : null,
                submission.getStudent() != null ? userService.mapToDTO(submission.getStudent()) : null,
                submission.getReportUrl(),
                submission.getStatus(),
                submission.getScore(),
                submission.getFeedback()
        );
    }
}
