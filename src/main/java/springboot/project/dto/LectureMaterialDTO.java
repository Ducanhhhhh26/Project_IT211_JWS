package springboot.project.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LectureMaterialDTO {
    private Long id;
    private String title;
    private String fileUrl;
    private Long courseId;
}
