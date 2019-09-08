package com.vuvarov.rashod.service;

import com.vuvarov.rashod.repository.OperationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlaceService implements IPlaceService {
    private final OperationRepository operationRepository;

    @Override
    public List<String> allPlaces() {
        return operationRepository.getAllPlaces();
    }
}
