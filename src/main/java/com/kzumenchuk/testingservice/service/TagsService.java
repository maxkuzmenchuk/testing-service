package com.kzumenchuk.testingservice.service;

import com.kzumenchuk.testingservice.exception.TagNotFoundException;
import com.kzumenchuk.testingservice.repository.ITagsRepository;
import com.kzumenchuk.testingservice.repository.model.TagEntity;
import com.kzumenchuk.testingservice.service.interfaces.ITagsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TagsService implements ITagsService {
    private final ITagsRepository ITagsRepository;
    private final UpdateLogService updateLogService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void editTags(Set<TagEntity> editedTags, Long updateUserID) {
        editedTags.stream()
                .filter(Objects::nonNull)
                .forEach((editedTag) -> {
                    Optional<TagEntity> databaseTagData = ITagsRepository.findById(editedTag.getTagID());

                    if (databaseTagData.isPresent()) {
                        TagEntity tag = databaseTagData.get();

                        if (!tag.equals(editedTag)) {
                            updateLogService.logTagsUpdate(updateUserID, editedTag, tag);

                            tag.setValue(editedTag.getValue());
                            tag.setUpdatingDate(LocalDateTime.now());

                            ITagsRepository.save(tag);
                        }
                    } else {
                        throw new TagNotFoundException("Tag (id = " + editedTag.getTagID() + ") not found");
                    }
                });
    }
}
