package com.vuvarov.rashod.web;

import com.vuvarov.rashod.model.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;

public abstract class RestRepositoryController<E extends Model, T extends Serializable, R extends PagingAndSortingRepository<E, T>> {

    @Autowired
    protected R repository;

    @GetMapping()
    public Page<E> findAll(@PageableDefault(sort = "id") Pageable pageable) {
        return repository.findAll(pageable);
    }

    @PostMapping
    public E save(@RequestBody E entity) {
        return repository.save(entity);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable T id) {
        repository.deleteById(id);
    }

    @GetMapping("/{id}")
    public E get(@PathVariable T id) {
        return repository.findById(id).orElse(null);
    }
}
