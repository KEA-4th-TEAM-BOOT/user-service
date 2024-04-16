package userservice.domain;


import jakarta.persistence.*;
import lombok.*;
import userservice.global.BaseTimeEntity;

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

    private String subCategoryName;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Category category;

    public static SubCategory createSubCategory(Category category, String subCategoryName){
        SubCategory subCategory = SubCategory.builder()
                .category(category)
                .subCategoryName(subCategoryName)
                .build();

        category.addSubCategory(subCategory);
        return subCategory;
    }

    public void updateSubCategory(String subCategoryName){
        this.subCategoryName = subCategoryName;
    }
}
