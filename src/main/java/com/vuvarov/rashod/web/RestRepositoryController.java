package com.vuvarov.rashod.web;

import com.vuvarov.rashod.model.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public abstract class RestRepositoryController<E extends Model, T extends Serializable, R extends CrudRepository<E, T>> {

    @Autowired
    protected R repository;

    @GetMapping()
    public List<E> findAll() {
        return StreamSupport
                .stream(repository.findAll().spliterator(), false)
                .collect(Collectors.toList());
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
