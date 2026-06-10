package springboot.project.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "lecture_materials")
public class LectureMaterial {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    
    // Cloudinary or AWS S3 URL for the lecture material file
    private String fileUrl; 

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;
}
