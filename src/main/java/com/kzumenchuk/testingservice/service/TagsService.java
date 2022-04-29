package com.kzumenchuk.testingservice.service;

import com.kzumenchuk.testingservice.repository.TagsRepository;
import com.kzumenchuk.testingservice.repository.model.TagEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
public class TagsService {
    private final TagsRepository tagsRepository;

    public TagsService(TagsRepository tagsRepository) {
        this.tagsRepository = tagsRepository;
    }

    public TagEntity saveOptions(TagEntity tagEntity) {
        return tagsRepository.save(tagEntity);
    }

    public void editTags(Set<TagEntity> editedTags) {
        editedTags.stream()
                .filter(Objects::nonNull)
                .forEach((editedTag) -> {
                    Optional<TagEntity> databaseTagData = tagsRepository.findById(editedTag.getTagID());

                    if (databaseTagData.isPresent()) {
                        TagEntity tag = databaseTagData.get();


                        // TODO: CHECR STACKOOVERFLOW LINK FOR DYNAMIC UPDATE ENTITIES
                        // TODO: https://stackoverflow.com/questions/52162452/how-to-use-hibernate-dynamicupdate-with-spring-data-jpa
                        // TODO: TRY TO WRITE QUERY LIKE " ... SET :param1 = :param2 WHERE ..."

                        tag.setValue(editedTag.getValue());
                        tag.setUpdateDate(LocalDateTime.now());

                        tagsRepository.save(tag);
                    } // TODO: ADD ELSE BLOCK
                });
    }
}
