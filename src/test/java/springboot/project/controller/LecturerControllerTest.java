package springboot.project.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import springboot.project.dto.GradeRequestDTO;
import springboot.project.dto.SubmissionDTO;
import springboot.project.security.JwtUtil;
import springboot.project.service.AssignmentService;
import springboot.project.service.LectureMaterialService;
import springboot.project.service.SubmissionService;
import org.springframework.security.core.userdetails.UserDetailsService;
import springboot.project.repository.TokenBlacklistRepository;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LecturerController.class)
@AutoConfigureMockMvc(addFilters = false)
class LecturerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LectureMaterialService materialService;

    @MockitoBean
    private SubmissionService submissionService;
    
    @MockitoBean
    private AssignmentService assignmentService;

    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @MockitoBean
    private TokenBlacklistRepository tokenBlacklistRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "LECTURER")
    void gradeSubmission_Returns200OK() throws Exception {
        GradeRequestDTO requestDTO = new GradeRequestDTO();
        requestDTO.setScore(9.5);
        requestDTO.setFeedback("Good job!");

        SubmissionDTO responseDTO = new SubmissionDTO();
        responseDTO.setId(100L);
        responseDTO.setScore(9.5);
        responseDTO.setFeedback("Good job!");

        when(submissionService.gradeSubmission(eq(100L), any(GradeRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(put("/api/v1/lecturer/submissions/100/grade")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data.score").value(9.5));
    }
}
