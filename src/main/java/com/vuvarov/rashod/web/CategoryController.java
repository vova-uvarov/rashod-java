package com.vuvarov.rashod.web;

import com.vuvarov.rashod.model.Category;
import com.vuvarov.rashod.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController extends RestRepositoryController<Category, Long, CategoryRepository> {

    @GetMapping("/search")
    public Iterable<Category> search(@RequestParam String q) {
        return repository.findAllByName("%" + q + "%");
    }
}
