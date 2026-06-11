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
import springboot.project.dto.CourseDTO;
import springboot.project.dto.CourseRequestDTO;
import springboot.project.dto.UserDTO;
import springboot.project.security.JwtUtil;
import springboot.project.service.CourseService;
import springboot.project.service.UserService;
import org.springframework.security.core.userdetails.UserDetailsService;
import springboot.project.repository.TokenBlacklistRepository;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdminController.class)
@AutoConfigureMockMvc(addFilters = false)
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private CourseService courseService;

    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @MockitoBean
    private TokenBlacklistRepository tokenBlacklistRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "ADMIN")
    void getUserById_Returns200OK() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setUsername("admin1");
        userDTO.setRole("ROLE_ADMIN");

        when(userService.getUserById(1L)).thenReturn(userDTO);

        mockMvc.perform(get("/api/v1/admin/users/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data.username").value("admin1"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createCourse_Returns201Created() throws Exception {
        CourseRequestDTO requestDTO = new CourseRequestDTO();
        requestDTO.setName("Java API");

        CourseDTO responseDTO = new CourseDTO();
        responseDTO.setId(10L);
        responseDTO.setName("Java API");

        when(courseService.createCourse(any(CourseRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/v1/admin/courses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value(201))
                .andExpect(jsonPath("$.data.id").value(10));
    }
}
