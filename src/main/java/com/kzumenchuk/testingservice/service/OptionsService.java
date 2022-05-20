package com.kzumenchuk.testingservice.service;

import com.kzumenchuk.testingservice.exception.OptionNotFoundException;
import com.kzumenchuk.testingservice.repository.IOptionsRepository;
import com.kzumenchuk.testingservice.repository.model.OptionEntity;
import com.kzumenchuk.testingservice.repository.model.QuestionEntity;
import com.kzumenchuk.testingservice.service.interfaces.IOptionsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class OptionsService implements IOptionsService {
    private final IOptionsRepository IOptionsRepository;

    private final UpdateLogService updateLogService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateOptions(QuestionEntity editedQuestion, Long updateUserID) {
        Set<OptionEntity> editedOptions = editedQuestion.getOptions();

        editedOptions.stream()
                .filter(Objects::nonNull)
                .forEach((editedOption) -> {
                    Optional<OptionEntity> databaseOptionData = IOptionsRepository.findById(editedOption.getOptionID());

                    if (databaseOptionData.isPresent()) {
                        OptionEntity optionEntity = databaseOptionData.get();

                        if (!editedOption.equals(optionEntity)) {

                            updateLogService.logOptionUpdate(optionEntity, editedOption, updateUserID);

                            optionEntity.setValue(editedOption.getValue());
                            optionEntity.setCorrect(editedOption.isCorrect());
                            optionEntity.setUpdatingDate(LocalDateTime.now());

                            IOptionsRepository.save(optionEntity);
                        }
                    } else {
                        throw new OptionNotFoundException("Option (id = " + editedOption.getOptionID() + ") not found");
                    }
                });
    }
}
