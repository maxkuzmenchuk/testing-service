package com.kzumenchuk.testingservice.service;

import com.kzumenchuk.testingservice.repository.OptionsRepository;
import com.kzumenchuk.testingservice.repository.model.OptionEntity;
import com.kzumenchuk.testingservice.repository.model.QuestionEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
public class OptionsService {
    private final OptionsRepository optionsRepository;

    public OptionsService(OptionsRepository optionsRepository) {
        this.optionsRepository = optionsRepository;
    }

    public OptionEntity saveTags(OptionEntity optionEntity) {
        return optionsRepository.save(optionEntity);
    }

    public void updateOptions(QuestionEntity editedQuestion) {
        Set<OptionEntity> editedOptions = editedQuestion.getOptions();

        editedOptions.stream()
                .filter(Objects::nonNull)
                .forEach((entity) -> {
                    Optional<OptionEntity> databaseOptionData = optionsRepository.findById(entity.getOptionID());

                    if (databaseOptionData.isPresent()) {
                        OptionEntity optionEntity = databaseOptionData.get();

                        optionEntity.setValue(entity.getValue());
                        optionEntity.setCorrect(entity.isCorrect());
                        optionEntity.setUpdateDate(LocalDateTime.now());

                        optionsRepository.save(optionEntity);
                    } // TODO: ADD ELSE BLOCK
                });
    }
}
