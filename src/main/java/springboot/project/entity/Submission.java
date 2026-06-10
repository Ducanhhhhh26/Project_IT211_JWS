package springboot.project.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "submissions")
public class Submission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "assignment_id")
    private Assignment assignment;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private User student;

    private String reportUrl; // Cloudinary or AWS S3 URL
    private String status; // PENDING, SUBMITTED, LATE, GRADED

    private Double score;
    private String feedback;
}
