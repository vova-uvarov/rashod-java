package com.vuvarov.rashod.service.interfaces;

import com.vuvarov.rashod.model.Category;

import java.util.Collection;
import java.util.List;

public interface ICategoryService {
    List<Category> all(Collection<Long> ids);

    List<Category> findByName(String name);
}
