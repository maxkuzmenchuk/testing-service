package com.kzumenchuk.testingservice.service;

import com.kzumenchuk.testingservice.repository.TagsRepository;
import com.kzumenchuk.testingservice.repository.model.TagEntity;
import org.springframework.stereotype.Service;

@Service
public class TagsService {
    private final TagsRepository tagsRepository;

    public TagsService(TagsRepository tagsRepository) {
        this.tagsRepository = tagsRepository;
    }

    public TagEntity saveOptions(TagEntity tagEntity) {
        return tagsRepository.save(tagEntity);
    }
}
