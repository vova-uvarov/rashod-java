package com.vuvarov.rashod.web;

import com.vuvarov.rashod.model.param.AppParam;
import com.vuvarov.rashod.model.param.ParamGroup;
import com.vuvarov.rashod.model.param.ParamKey;
import com.vuvarov.rashod.repository.AppParamRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.IterableUtils;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/app/params")
@RequiredArgsConstructor
public class AppParamController {
    private final AppParamRepository repository;

    @GetMapping
    public List<AppParam> params() {
        return IterableUtils.toList(repository.findAll());
    }

    @GetMapping("/groups")
    public List<ParamGroup> getGroups() {
        return Arrays.asList(ParamGroup.values());
    }

    @GetMapping("/keys")
    public List<ParamKey> getKeys() {
        return Arrays.asList(ParamKey.values());
    }

    @GetMapping("/{id}")
    public AppParam get(@PathVariable Long id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException("param not found with id: " + id));
    }

    @PostMapping
    public AppParam save(@RequestBody AppParam param) {
        return repository.save(param);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        repository.deleteById(id);
    }
}
