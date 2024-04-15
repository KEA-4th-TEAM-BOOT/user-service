package userservice.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import userservice.global.BaseTimeEntity;
import userservice.vo.BaseCategoryEnumVo;

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

    public static Category createCategory(User user, String categoryName){
        Category category = Category.builder()
                .user(user)
                .categoryName(categoryName)
                .build();
        user.addCategory(category);
        return category;
    }

    public void updateCategory(BaseCategoryEnumVo baseCategoryEnumVo){
        this.categoryName = baseCategoryEnumVo.categoryName();
        this.existSubCategory = baseCategoryEnumVo.existSubCategory();
    }
}


// "categoryList": [ "string1", "string2" ]
// 네트워크
// 파이썬
// 카테고리3
    //카테고리 c++
    //카테고리 자바
// 운체

// category_id   categoryname subCategory
//      1            네트워크      네트워크
//      2            파이썬        자바
//      3            카테고리3      c++
//      4            카테고리3      자바
//      5            카테고리3      자바
//      6            운체          운체

//    public void updateCategory()
