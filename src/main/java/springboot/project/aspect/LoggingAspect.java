package springboot.project.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass());

    @org.aspectj.lang.annotation.Pointcut("within(springboot.project.controller..*) || within(springboot.project.service..*)")
    public void applicationPackagePointcut() {
        // Method is empty as this is just a Pointcut
    }

    @org.aspectj.lang.annotation.Around("applicationPackagePointcut()")
    public Object logExecutionTime(org.aspectj.lang.ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();

        Object result = joinPoint.proceed();

        long timeTaken = System.currentTimeMillis() - startTime;
        
        String className = joinPoint.getSignature().getDeclaringType().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        String vietnameseAction = getVietnameseAction(methodName);
        
        log.info("[Chức năng] {} ({}::{}) hoàn thành trong: {} ms", vietnameseAction, className, methodName, timeTaken);
        
        return result;
    }

    private String getVietnameseAction(String methodName) {
        switch (methodName) {
            case "login": return "Đăng nhập";
            case "logout": return "Đăng xuất";
            case "register": return "Đăng ký tài khoản";
            case "registerStudent": return "Đăng ký tài khoản sinh viên";
            case "refresh": 
            case "refreshToken": return "Làm mới Token";
            case "changePassword": return "Đổi mật khẩu";
            case "createCourse": return "Tạo khóa học mới";
            case "updateCourse": return "Cập nhật khóa học";
            case "deleteCourse": return "Xóa khóa học";
            case "getAllCourses": 
            case "getCourses": return "Lấy danh sách khóa học";
            case "registerToCourse": 
            case "registerStudentToCourse": return "Đăng ký tham gia khóa học";
            case "submitAssignment": return "Nộp bài tập/Đồ án";
            case "gradeSubmission": return "Chấm điểm bài nộp";
            case "uploadMaterial": return "Tải lên tài liệu bài giảng";
            case "createUser": return "Tạo người dùng mới";
            case "updateUser": return "Cập nhật người dùng";
            case "deleteUser": return "Xóa người dùng";
            case "getUserById": return "Lấy thông tin người dùng";
            case "getAllUsers": 
            case "getUsers": return "Lấy danh sách người dùng";
            default: return "Thực thi (" + methodName + ")";
        }
    }
}
