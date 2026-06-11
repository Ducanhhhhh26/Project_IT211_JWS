package springboot.project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import springboot.project.dto.LectureMaterialDTO;
import springboot.project.entity.Course;
import springboot.project.entity.LectureMaterial;
import springboot.project.repository.CourseRepository;
import springboot.project.repository.LectureMaterialRepository;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class LectureMaterialService {

    private final LectureMaterialRepository materialRepository;
    private final CourseRepository courseRepository;
    private final CloudinaryService cloudinaryService;

    public LectureMaterialDTO uploadMaterial(Long courseId, String title, MultipartFile file) throws IOException {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        
        String fileUrl = cloudinaryService.uploadFile(file);
        
        LectureMaterial material = new LectureMaterial();
        material.setTitle(title);
        material.setFileUrl(fileUrl);
        material.setCourse(course);
        
        material = materialRepository.save(material);
        
        return new LectureMaterialDTO(material.getId(), material.getTitle(), material.getFileUrl(), courseId);
    }
}
