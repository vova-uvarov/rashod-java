package com.vuvarov.rashod.web;

import com.vuvarov.rashod.service.interfaces.IPlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/places")
@RequiredArgsConstructor
public class PlaceController {

    private final IPlaceService placeService;

    @GetMapping
    public List<String> findAll() {
        return placeService.allPlaces();
    }
}
