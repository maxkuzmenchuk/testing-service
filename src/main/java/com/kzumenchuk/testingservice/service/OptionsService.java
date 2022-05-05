package com.kzumenchuk.testingservice.service;

import com.kzumenchuk.testingservice.repository.OptionsRepository;
import com.kzumenchuk.testingservice.repository.model.OptionEntity;
import com.kzumenchuk.testingservice.repository.model.QuestionEntity;
import com.kzumenchuk.testingservice.repository.model.UpdateLogEntity;
import com.kzumenchuk.testingservice.util.EntityType;
import com.kzumenchuk.testingservice.util.UpdateLogUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
public class OptionsService {
    private final OptionsRepository optionsRepository;

    private final UpdateLogService updateLogService;

    public OptionsService(OptionsRepository optionsRepository, UpdateLogService updateLogService) {
        this.optionsRepository = optionsRepository;
        this.updateLogService = updateLogService;
    }

    public OptionEntity saveTags(OptionEntity optionEntity) {
        return optionsRepository.save(optionEntity);
    }

    public void updateOptions(QuestionEntity editedQuestion, Long updateUserID) {
        Set<OptionEntity> editedOptions = editedQuestion.getOptions();

        editedOptions.stream()
                .filter(Objects::nonNull)
                .forEach((editedOption) -> {
                    Optional<OptionEntity> databaseOptionData = optionsRepository.findById(editedOption.getOptionID());

                    if (databaseOptionData.isPresent()) {
                        OptionEntity optionEntity = databaseOptionData.get();

                        if (!editedOption.equals(optionEntity)) {

                            logUpdateOption(optionEntity, editedOption, updateUserID);

                            optionEntity.setValue(editedOption.getValue());
                            optionEntity.setCorrect(editedOption.isCorrect());
                            optionEntity.setUpdateDate(LocalDateTime.now());

                            optionsRepository.save(optionEntity);
                        }
                    } // TODO: ADD ELSE BLOCK
                });
    }

    private void logUpdateOption(OptionEntity oldOption, OptionEntity newOption, Long updateUserID) {
        if (!oldOption.getValue().equalsIgnoreCase(newOption.getValue())) {
            UpdateLogEntity tagLog = UpdateLogUtil.createLogEntity(oldOption.getOptionID(), EntityType.OPTION,
                    "U", "value", oldOption.getValue(), newOption.getValue(), updateUserID);
            updateLogService.saveLog(tagLog);
        }

        if (oldOption.isCorrect() != newOption.isCorrect()) {
            UpdateLogEntity tagLog = UpdateLogUtil.createLogEntity(oldOption.getOptionID(), EntityType.OPTION,
                    "U", "isCorrect", String.valueOf(oldOption.isCorrect()), String.valueOf(newOption.isCorrect()), updateUserID);
            updateLogService.saveLog(tagLog);
        }
    }
}
