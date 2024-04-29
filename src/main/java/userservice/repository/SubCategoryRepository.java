package userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import userservice.domain.SubCategory;

@Repository
public interface SubCategoryRepository extends JpaRepository<SubCategory, Long> {
    long countByCategoryId(Long categoryId);
}
