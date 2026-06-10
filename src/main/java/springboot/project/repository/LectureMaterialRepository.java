package springboot.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import springboot.project.entity.LectureMaterial;

@Repository
public interface LectureMaterialRepository extends JpaRepository<LectureMaterial, Long> {
}
