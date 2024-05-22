package userservice.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import userservice.global.BaseTimeEntity;
import userservice.vo.BaseCategoryEnumVo;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@DynamicInsert
@Entity
@Getter
@Builder
@Table(name = "Category")
public class Category extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 30)
    private String categoryName;

    @ColumnDefault("False")
    private boolean existSubCategory;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SubCategory> subCategoryList = new ArrayList<>();

    public static Category createCategory(User user, String categoryName) {
        Category category = Category.builder()
                .user(user)
                .categoryName(categoryName)
                .build();
        user.addCategory(category);
        return category;
    }

    public void updateCategory(BaseCategoryEnumVo baseCategoryEnumVo) {
        this.categoryName = baseCategoryEnumVo.categoryName();
        this.existSubCategory = baseCategoryEnumVo.existSubCategory();
    }

    public void addSubCategory(SubCategory subCategory) {
        this.subCategoryList.add(subCategory);
    }
}



