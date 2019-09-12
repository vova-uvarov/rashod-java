package com.vuvarov.rashod.service;

import com.vuvarov.rashod.model.Category;
import com.vuvarov.rashod.repository.CategoryRepository;
import com.vuvarov.rashod.service.interfaces.ICategoryService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.IterableUtils;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService {
    private final CategoryRepository repository;

    @Override
    public List<Category> all(Collection<Long> ids) {
        return IterableUtils.toList(repository.findAllById(ids));
    }
}
