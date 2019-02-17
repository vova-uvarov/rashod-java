package com.vuvarov.rashod.web;

import com.vuvarov.rashod.model.AppParam;
import com.vuvarov.rashod.repository.AppParamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/app/params")
@RequiredArgsConstructor
public class AppParamController extends RestRepositoryController<AppParam, Long, AppParamRepository> {
}
