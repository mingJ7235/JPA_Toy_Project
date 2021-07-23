package com.joshua.service;

import com.joshua.domain.Category;
import com.joshua.dto.CategoryDTO;
import com.joshua.repository.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class CategoryServiceTest {
    @Autowired
    CategoryService categoryService;
    @Autowired
    CategoryRepository categoryRepository;

    @Test
    public void 카테고리_저장_테스트 () {
        //given
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setBranch("TestBranch");
        categoryDTO.setCode("TestCode");
        categoryDTO.setName("TestName");

        //when

        Long saveId = categoryService.saveCategory(categoryDTO);

        //then

        Map<String, CategoryDTO> findCategory = categoryService
                .getCategoryByBranchAndName(categoryDTO.getBranch(), categoryDTO.getName());

        assertThat(saveId).isEqualTo(findCategory.get("TestName").getCategoryId());
    }

    @Test
    public void 카테고리_업데이트_테스트 () {
        //given
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setBranch("TestBranch");
        categoryDTO.setCode("TestCode");
        categoryDTO.setName("TestName");
        categoryService.saveCategory(categoryDTO);

        //when

        CategoryDTO targetCategory = categoryService
                .getCategoryByBranchAndName(categoryDTO.getBranch(), categoryDTO.getName()).get(categoryDTO.getName());
        targetCategory.setName("UpdateName");
        categoryService.updateCategory(targetCategory.getCategoryId(), targetCategory);

        //then
        assertThat(targetCategory.getName()).isEqualTo("UpdateName");
    }

    @Test
    public void 카테고리_삭제_테스트_대분류 () {
        //given
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setBranch("TestBranch");
        categoryDTO.setCode("TestCode");
        categoryDTO.setName("TestName");
        categoryService.saveCategory(categoryDTO);

        //when
//        CategoryDTO targetCategory = categoryService
//                .getCategoryByBranchAndName(categoryDTO.getBranch(), categoryDTO.getName()).get(categoryDTO.getName());
        Long categoryId = categoryDTO.getCategoryId();
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+categoryId);

        categoryService.deleteCategory(categoryId);
        //then

//        System.out.println(">>>>>>>>>>>>>>>>>>>>>"+targetCategory.getName());
//        System.out.println("parent : "+targetCategory.getParentCategoryName());
//        System.out.println("subcategory size: " + targetCategory.getChildren().size());

        assertThat(categoryDTO).isNull();


    }
}