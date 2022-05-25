package com.kzumenchuk.testingservice.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import com.kzumenchuk.testingservice.exception.GeneratingPDFException;
import com.kzumenchuk.testingservice.exception.TestNotFoundException;
import com.kzumenchuk.testingservice.repository.IFileDataRepository;
import com.kzumenchuk.testingservice.repository.model.FileDataEntity;
import com.kzumenchuk.testingservice.repository.model.dto.TestDTO;
import com.kzumenchuk.testingservice.service.interfaces.IFileDataService;
import com.kzumenchuk.testingservice.util.EntityMapper;
import com.kzumenchuk.testingservice.util.FileDataUtil;
import com.kzumenchuk.testingservice.util.enums.FileKind;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class FileDataDataService implements IFileDataService {
    private final IFileDataRepository fileDataRepository;

    private final FileDataUtil fileDataUtil;

    @Override
    public FileDataEntity generateTestDataToPDF(Long id) {
        try {
            Document document = new Document();
            TestDTO testDTO = EntityMapper.fromEntityToDTO(fileDataUtil.getTest(id)); // get test entity

            Path sourceURL = fileDataUtil.returnSourceUrl(FileKind.TEST);
            String sourceURLString = sourceURL + "/" + testDTO.getTitle() + ".pdf";

            Path sourcePath = Paths.get(sourceURLString);
            PdfWriter.getInstance(document, Files.newOutputStream(sourcePath));

            // generating document
            document.open();
            document.addTitle(testDTO.getTitle());
            document.addCreationDate();

            Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLDITALIC, BaseColor.BLACK);
            Font infoFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, BaseColor.BLACK);
            Font textFont = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.NORMAL, BaseColor.BLACK);
            Font questionDescriptionFont = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL, BaseColor.BLACK);

            Paragraph testTitle = new Paragraph(testDTO.getTitle(), titleFont);
            testTitle.setAlignment(Element.ALIGN_CENTER);
            testTitle.setFont(titleFont);
            document.add(testTitle);

            Paragraph descriptionParagraph = new Paragraph("Description: ", infoFont);
            descriptionParagraph.add(testDTO.getDescription());
            document.add(descriptionParagraph);

            Paragraph categoryParagraph = new Paragraph("Category: ", infoFont);
            categoryParagraph.add(testDTO.getCategory());
            document.add(categoryParagraph);

            Paragraph tagsParagraph = new Paragraph("Tags: ", infoFont);
            StringBuilder tagsSB = new StringBuilder();
            testDTO.getTags()
                    .forEach(tag -> {
                        tagsSB.append(tag.getValue()).append(", "); // create string of tags from collection
                    });
            tagsSB.deleteCharAt(tagsSB.lastIndexOf(","));
            Chunk tagsValueChunk = new Chunk(tagsSB.toString());
            tagsParagraph.add(tagsValueChunk);
            document.add(tagsParagraph);

            // add questions to file
            AtomicInteger questionNum = new AtomicInteger(1);
            testDTO.getQuestions()
                    .forEach(questionEntity -> {
                        Paragraph questionTitleParagraph = new Paragraph(questionNum + ". ", textFont);
                        Paragraph questionDescription = new Paragraph(questionEntity.getDescription(), questionDescriptionFont);
                        questionTitleParagraph.add(questionEntity.getTitle());
                        questionTitleParagraph.setSpacingBefore(15f);

                        try {
                            document.add(questionTitleParagraph);
                            document.add(questionDescription);

                            // add question options to file
                            AtomicInteger optionNum = new AtomicInteger(1);
                            questionEntity.getOptions()
                                    .forEach(optionEntity -> {
                                        Paragraph optionTitleParagraph = new Paragraph("        " + optionNum + ". ", textFont);
                                        optionTitleParagraph.setExtraParagraphSpace(Element.ALIGN_RIGHT);
                                        optionTitleParagraph.add(optionEntity.getValue());

                                        try {
                                            document.add(optionTitleParagraph);
                                            optionNum.getAndIncrement();
                                        } catch (DocumentException e) {
                                            throw new GeneratingPDFException(e.getMessage());
                                        }
                                    });
                            questionNum.getAndIncrement();
                        } catch (DocumentException e) {
                            throw new GeneratingPDFException(e.getMessage());
                        }
                    });
            document.close();

            File newPdf = new File(sourceURLString);
            try {
                return storeFileData(newPdf, id); // store file data to database
            } catch (Exception exception) {
                Files.delete(sourcePath);
                throw new GeneratingPDFException("Exception while storing file data: " + exception.getMessage());
            }
        } catch (DocumentException | IOException | TestNotFoundException | EntityNotFoundException e) {
            throw new GeneratingPDFException(e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public FileDataEntity storeFileData(File file, Long testID) {
        String fileType = file.getPath().split("\\.")[1]; // get file extension
        FileDataEntity fileDataEntity = FileDataEntity.builder()
                .testID(testID)
                .fileName(file.getName())
                .fileType(fileType)
                .fileSourceUrl(file.getPath())
                .creatingDate(LocalDateTime.now())
                .build();
        return fileDataRepository.saveAndFlush(fileDataEntity);
    }

    @Override
    public FileDataEntity getFileByTestID(Long testID) throws FileNotFoundException {
        Optional<FileDataEntity> optionalFile = fileDataRepository.getFileDataEntityByTestID(testID);
        if (optionalFile.isPresent()) {
            File file = new File(optionalFile.get().getFileSourceUrl());

            if (!file.exists()) {
                // delete all file data from database if file not exists
                fileDataRepository.deleteAllById(Collections.singleton(optionalFile.get().getFileID()));
                return generateTestDataToPDF(testID);
            }

            return optionalFile.get();
        } else {
            return generateTestDataToPDF(testID);
        }
    }

    @Override
    public Resource returnFileAsResource(Path sourcePath) throws FileNotFoundException {
        Resource resource;
        try {
            resource = new UrlResource(sourcePath.toUri());
        } catch (MalformedURLException e) {
            throw new RuntimeException(String.format("File path is invalid: %s", sourcePath.toUri()));
        }

        if (!resource.exists()) {
            throw new FileNotFoundException(String.format("File not found: %s", sourcePath));
        }

        return resource;
    }

    @Override
    public void updateFileData(Long testID) {
        Optional<FileDataEntity> optionalFileData = fileDataRepository.getFileDataEntityByTestID(testID);

        if (optionalFileData.isPresent()) {
            generateTestDataToPDF(testID);
        }
    }

    @Override
    public void deleteFileData(Long testID) {
        fileDataRepository.deleteAllByTestID(testID);
    }
}


