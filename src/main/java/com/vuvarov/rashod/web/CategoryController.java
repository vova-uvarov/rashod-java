package com.vuvarov.rashod.web;

import com.vuvarov.rashod.model.Category;
import com.vuvarov.rashod.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController extends RestRepositoryController<Category, Long, CategoryRepository> {

    @GetMapping("/search")
    public Iterable<Category> search(@RequestParam String q, @PageableDefault Pageable pageable) {
        return repository.findAllByName("%" + q + "%", pageable);
    }

    @Override
    public Page<Category> findAll(@PageableDefault(sort = "name") Pageable pageable) {
        return super.findAll(pageable);
    }
}
