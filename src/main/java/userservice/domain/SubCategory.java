package userservice.domain;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import userservice.global.BaseTimeEntity;
import userservice.vo.BaseCategoryEnumVo;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Entity
@Getter
@Table(name = "SubCategory")
public class SubCategory extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(length = 30)
    private String subCategoryName;

    @ColumnDefault("0")
    private Long count;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Category category;

    public static SubCategory createSubCategory(Category category, String subCategoryName) {
        SubCategory subCategory = SubCategory.builder()
                .category(category)
                .subCategoryName(subCategoryName)
                .build();
        category.updateCategory(new BaseCategoryEnumVo(category.getCategoryName(), true));
        category.addSubCategory(subCategory);
        return subCategory;
    }

    public void updateSubCategory(String subCategoryName) {
        this.subCategoryName = subCategoryName;
    }
}
