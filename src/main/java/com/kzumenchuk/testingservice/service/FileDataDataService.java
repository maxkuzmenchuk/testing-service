package com.kzumenchuk.testingservice.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import com.kzumenchuk.testingservice.exception.GeneratingPDFException;
import com.kzumenchuk.testingservice.exception.TestNotFoundException;
import com.kzumenchuk.testingservice.repository.IFileDataRepository;
import com.kzumenchuk.testingservice.repository.model.FileDataEntity;
import com.kzumenchuk.testingservice.repository.model.dto.TestDTO;
import com.kzumenchuk.testingservice.repository.model.dto.TestResultDTO;
import com.kzumenchuk.testingservice.service.interfaces.IFileDataService;
import com.kzumenchuk.testingservice.util.EntityMapper;
import com.kzumenchuk.testingservice.util.FileDataUtil;
import com.kzumenchuk.testingservice.util.PDFFonts;
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
import java.time.format.DateTimeFormatter;
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

            Paragraph testTitle = new Paragraph(testDTO.getTitle(), PDFFonts.TITLE_FONT);
            testTitle.setAlignment(Element.ALIGN_CENTER);
            document.add(testTitle);

            Paragraph descriptionParagraph = new Paragraph("Description: ", PDFFonts.INFO_FONT);
            descriptionParagraph.add(testDTO.getDescription());
            document.add(descriptionParagraph);

            Paragraph categoryParagraph = new Paragraph("Category: ", PDFFonts.INFO_FONT);
            categoryParagraph.add(testDTO.getCategory());
            document.add(categoryParagraph);

            Paragraph tagsParagraph = new Paragraph("Tags: ", PDFFonts.INFO_FONT);
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
                        Paragraph questionTitleParagraph = new Paragraph(questionNum + ". ", PDFFonts.TEXT_FONT);
                        Paragraph questionDescription = new Paragraph(questionEntity.getDescription(), PDFFonts.QUESTION_DESCRIPTION_FONT);
                        questionTitleParagraph.add(questionEntity.getTitle());
                        questionTitleParagraph.setSpacingBefore(15f);

                        try {
                            document.add(questionTitleParagraph);
                            document.add(questionDescription);

                            // add question options to file
                            AtomicInteger optionNum = new AtomicInteger(1);
                            questionEntity.getOptions()
                                    .forEach(optionEntity -> {
                                        Paragraph optionTitleParagraph = new Paragraph("        " + optionNum + ". ", PDFFonts.TEXT_FONT);
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
                return storeFileData(newPdf, id, FileKind.TEST); // store file data to database
            } catch (Exception exception) {
                Files.delete(sourcePath);
                throw new GeneratingPDFException("Exception while storing file data: " + exception.getMessage());
            }
        } catch (DocumentException | IOException | TestNotFoundException | EntityNotFoundException e) {
            throw new GeneratingPDFException(e.getMessage());
        }
    }

    @Override
    public FileDataEntity generateTestResultToPDF(Long resultID) {
        try {
            Document document = new Document();
            TestResultDTO testResultDTO = EntityMapper.fromEntityToDTO(fileDataUtil.getTestResult(resultID)); // get test entity
            TestDTO testDTO = EntityMapper.fromEntityToDTO(fileDataUtil.getTest(testResultDTO.getTestID())); // get test entity

            Path sourceURL = fileDataUtil.returnSourceUrl(FileKind.RESULT);
            String sourceURLString = sourceURL + "/" + resultID + "_" + testDTO.getTitle() + "_result.pdf";

            Path sourcePath = Paths.get(sourceURLString);
            PdfWriter.getInstance(document, Files.newOutputStream(sourcePath));

            // generating document
            document.open();
            document.addTitle(testDTO.getTitle() + " results");
            document.addCreationDate();

            Paragraph testTitle = new Paragraph(testDTO.getTitle() + " results", PDFFonts.TITLE_FONT);
            testTitle.setAlignment(Element.ALIGN_CENTER);
            document.add(testTitle);

            Paragraph descriptionParagraph = new Paragraph("Description: ", PDFFonts.INFO_FONT);
            descriptionParagraph.add(testDTO.getDescription());
            document.add(descriptionParagraph);

            Paragraph categoryParagraph = new Paragraph("Category: ", PDFFonts.INFO_FONT);
            categoryParagraph.add(testDTO.getCategory());
            document.add(categoryParagraph);

            Paragraph tagsParagraph = new Paragraph("Tags: ", PDFFonts.INFO_FONT);
            StringBuilder tagsSB = new StringBuilder();
            testDTO.getTags()
                    .forEach(tag -> {
                        tagsSB.append(tag.getValue()).append(", "); // create string of tags from collection
                    });
            tagsSB.deleteCharAt(tagsSB.lastIndexOf(","));
            Chunk tagsValueChunk = new Chunk(tagsSB.toString());
            tagsParagraph.add(tagsValueChunk);
            document.add(tagsParagraph);

            // add results to file
            String correctAnswersCount = "Correct answers: " + testResultDTO.getCorrectAnswersCount() + "/" + testDTO.getQuestions().size();
            Paragraph correctAnswersCountParagraph = new Paragraph(correctAnswersCount, PDFFonts.INFO_FONT);
            document.add(correctAnswersCountParagraph);

            Paragraph correctAnswersPercentageParagraph = new Paragraph("Correct answer percentage: " + testResultDTO.getCorrectAnswersPercentage() + "%", PDFFonts.INFO_FONT);
            document.add(correctAnswersPercentageParagraph);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

            Paragraph testingDateParagraph = new Paragraph("Testing date: " + testResultDTO.getTestingDate().format(formatter), PDFFonts.INFO_FONT);
            document.add(testingDateParagraph);

            document.close();

            File newPdf = new File(sourceURLString);
            try {
                return storeFileData(newPdf, testResultDTO.getTestID(), FileKind.RESULT); // store file data to database
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
    public FileDataEntity storeFileData(File file, Long testID, FileKind fileKind) {
        String fileType = file.getPath().split("\\.")[1]; // get file extension
        FileDataEntity fileDataEntity = FileDataEntity.builder()
                .testID(testID)
                .fileKind(fileKind)
                .fileName(file.getName())
                .fileType(fileType)
                .fileSourceUrl(file.getPath())
                .creatingDate(LocalDateTime.now())
                .build();
        return fileDataRepository.saveAndFlush(fileDataEntity);
    }

    @Override
    public FileDataEntity getTestFile(Long testID, FileKind fileKind) throws FileNotFoundException {
        Optional<FileDataEntity> optionalFile = fileDataRepository.getTestFile(testID, fileKind);

        if (optionalFile.isPresent()) {
            File file = new File(optionalFile.get().getFileSourceUrl());

            if (!file.exists()) {
                // delete all file data from database if file not exists
                deleteTestFileData(optionalFile.get().getTestID(), FileKind.TEST);
                return generateTestDataToPDF(testID);
            }

            return optionalFile.get();
        } else {
            return generateTestDataToPDF(testID);
        }
    }

    @Override
    public FileDataEntity getTestResultFile(Long resultID) throws FileNotFoundException {
        Optional<FileDataEntity> optionalFile = fileDataRepository.findById(resultID);

        if (optionalFile.isPresent()) {
            File file = new File(optionalFile.get().getFileSourceUrl());

            if (!file.exists()) {
                // delete all file data from database if file not exists
                deleteTestResultFileData(resultID);
                return generateTestResultToPDF(resultID);
            }

            return optionalFile.get();
        } else {
            return generateTestResultToPDF(resultID);
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
    public void updateTestFileData(Long testID) {
        Optional<FileDataEntity> optionalFileData = fileDataRepository.getTestFile(testID, FileKind.TEST);

        if (optionalFileData.isPresent()) {
            generateTestDataToPDF(testID);

        }
    }

    @Override
    public void deleteTestFileData(Long testID, FileKind fileKind) {
        fileDataRepository.deleteTestFile(testID, fileKind);
    }

    @Override
    public void deleteTestResultFileData(Long resultID) {
        fileDataRepository.deleteById(resultID);
    }
}


