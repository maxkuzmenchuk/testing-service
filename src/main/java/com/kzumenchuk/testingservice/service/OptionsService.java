package com.kzumenchuk.testingservice.service;

import com.kzumenchuk.testingservice.repository.OptionsRepository;
import com.kzumenchuk.testingservice.repository.model.OptionEntity;
import org.springframework.stereotype.Service;

@Service
public class OptionsService {
    private final OptionsRepository optionsRepository;

    public OptionsService(OptionsRepository optionsRepository) {
        this.optionsRepository = optionsRepository;
    }

    public OptionEntity saveTags(OptionEntity optionEntity) {
        return optionsRepository.save(optionEntity);
    }
}
