package springboot.project.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssignmentRequestDTO {
    private String title;
    private String description;
    private Long courseId;
}
