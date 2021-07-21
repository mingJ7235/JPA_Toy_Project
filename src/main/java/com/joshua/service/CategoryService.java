package com.joshua.service;

import com.joshua.domain.Category;
import com.joshua.domain.Reply;
import com.joshua.dto.CategoryDTO;
import com.joshua.dto.ReplyDTO;
import com.joshua.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;


    public Map<String, Category> saveCategory (CategoryDTO categoryDTO) {

        Category category = categoryDTO.toEntity();

        if(categoryDTO.getParent_category_id() == null || categoryDTO.getParent_category_id() ==0) {
            category.setLevel(1);
        } else {
            Long parentCategoryId = categoryDTO.getParent_category_id();
            Category parentCategory = categoryRepository.findById(parentCategoryId)
                    .orElseThrow(() -> new IllegalArgumentException("부모 카테고리 없음 예외"));

            if (!parentCategory.isLive()) {
                throw new RuntimeException("부모 카테고리를 찾을 수 없습니다.");
            }

            category.setLevel(parentCategory.getLevel() + 1);
            category.setParentCategory(parentCategory);
            parentCategory.getSubCategory().add(category);
        }

        category.setLive(true);
        Long categoryId = categoryRepository.save(category).getId();

        Map<String, Category> categoryMap = new HashMap<>();

        categoryMap.put(category.getCode(), category);

        return categoryMap;
    }


//    public CategoryDTO createCategoryRoot() {
//        Map<Long, List<CategoryDTO>> groupingByParent = categoryRepository.findAll()
//                .stream()
//                .map(ce -> new CategoryDTO(ce.getId(), ce.getCode(), ce.getName(), ce.getParent))
//                .collect(groupingBy(cd -> cd.getParentId()));
//
//        CategoryDto rootCategoryDto = new CategoryDto(0l, "ROOT", null);
//        addSubCategories(rootCategoryDto, groupingByParent);
//
//        return rootCategoryDto;
}