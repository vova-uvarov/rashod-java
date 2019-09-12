package com.vuvarov.rashod.util;

import com.vuvarov.rashod.model.Category;
import com.vuvarov.rashod.service.interfaces.ICategoryService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.IterableUtils;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class StatisticsUtil {
    private final ICategoryService categoryService;

    public String datasetName(Collection<Long> categoryIds){
        String categoryName = "Все";
        if (CollectionUtils.isNotEmpty(categoryIds)) {

            categoryName = IterableUtils.toList(categoryService.all(categoryIds))
                    .stream()
                    .map(Category::getName)
                    .collect(Collectors.joining(","));
        }
        return categoryName;
    }
}
