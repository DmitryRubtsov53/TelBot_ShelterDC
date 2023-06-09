package com.devsteam.getname.telbot_shelterdc.service;

import com.devsteam.getname.telbot_shelterdc.dto.PetDTO;
import com.devsteam.getname.telbot_shelterdc.dto.PetOwnerDTO;
import com.devsteam.getname.telbot_shelterdc.dto.ReportDTO;
import com.devsteam.getname.telbot_shelterdc.exception.NoSuchEntityException;
import com.devsteam.getname.telbot_shelterdc.exception.ReportListIsEmptyException;
import com.devsteam.getname.telbot_shelterdc.model.*;
import com.devsteam.getname.telbot_shelterdc.repository.OwnerRepository;
import com.devsteam.getname.telbot_shelterdc.repository.PetRepository;
import com.devsteam.getname.telbot_shelterdc.repository.ReportRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.devsteam.getname.telbot_shelterdc.model.Gender.MALE;
import static com.devsteam.getname.telbot_shelterdc.model.Kind.CAT;
import static com.devsteam.getname.telbot_shelterdc.model.Kind.DOG;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ReportServiceTest {

    @Autowired
    ReportService reportService;
    @Autowired
    ReportRepository reportRepository;
    @Autowired
    PetOwnerService petOwnerService;
    @Autowired
    OwnerRepository ownerRepository;
    @Autowired
    PetService petService;
    @Autowired
    PetRepository petRepository;

    @BeforeEach
    public void setUp() {
        PetDTO pet1 = new PetDTO(0L, 2020, "Chad", "mixed", "feisty", Color.SPOTTED, Status.FREE, 0, CAT, MALE);
        PetDTO pet2 = new PetDTO(0L, 2021, "Dach", "labr", "feisty", Color.BLACK, Status.FREE, 0, CAT, MALE);
        petService.addPet(pet1);
        petService.addPet(pet2);
        PetOwnerDTO owner1 = new PetOwnerDTO(0L, 405441405L, "fullName", "phone", "address", StatusOwner.PROBATION, LocalDate.now(), 1L);
        PetOwnerDTO owner2 = new PetOwnerDTO(0L, 999L, "fullName", "phone", "address", StatusOwner.PROBATION, LocalDate.now(), 2L);
        petOwnerService.creatPetOwner(owner1);
        petOwnerService.creatPetOwner(owner2);
    }

    @AfterEach
    public void clearDB() {
        ownerRepository.deleteAll();
        reportRepository.deleteAll();
    }

    @Test
    public void addingReportWithCorrectParametersCreatesReportAndAddsItToDB() {
        long chatId = 405441405;
        ReportDTO reportDTO = reportService.addReport(chatId, "mealsAndStuff", new byte[1]);
        assertTrue(reportService.getReportsByChatId(chatId).contains(reportService.getReportByReportId(reportDTO.id())));
    }

    @Test
    public void addingReportWithNonExistentChatIdThrowsNoSuchEntityException() {
        Exception exception = assertThrows(NoSuchEntityException.class, () -> reportService
                .addReport(0, "string", new byte[1]));
        String expectedMessage = "No owner with such chat ID";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void addingReportWithEmptyMealsWellBeingAndAdaptationBehaviourChangesThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> reportService
                .addReport(405441405, "", new byte[1]));
    }

    @Test
    public void addingReportWithBlankMealsWellBeingAndAdaptationBehaviourChangesThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> reportService
                .addReport(405441405, " ", new byte[1]));
    }

    @Test
    public void addingReportWithNullPhotoByteArrayThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> reportService
                .addReport(405441405, "1", null));
    }

    @Test
    public void addingReportWithZeroBytesPhotoArrayThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> reportService
                .addReport(405441405, "1", new byte[0]));
    }

    @Test
    public void gettingReportByExistingReportIdReturnsCorrectReportDTO() {
        long chatId = 405441405;
        ReportDTO reportDTO = reportService.addReport(chatId, "mealsAndStuff", new byte[1]);
        assertEquals(reportService.getReportByReportId(reportDTO.id()), reportDTO);
    }

    @Test
    public void gettingReportByNonExistingReportIdThrowsNoSuchEntityException() {
        assertThrows(NoSuchEntityException.class, () -> reportService.getReportByReportId(0));
    }

    @Test
    public void gettingReportsByExistingPetIdReturnsCorrectReportDTOList() {
        long chatId = 405441405;
        ReportDTO reportDTO = reportService.addReport(chatId, "mealsAndStuff", new byte[1]);
        List<ReportDTO> reportDTOS = new ArrayList<>();
        reportDTOS.add(reportDTO);
        assertEquals(reportDTOS, reportService.getReportsByPetId(reportDTO.petId()));
    }

    @Test
    public void gettingReportsByNonExistingPetIdThrowsNoSuchEntityException() {
        assertThrows(NoSuchEntityException.class, () -> reportService.getReportsByPetId(0));
    }

    @Test
    public void gettingReportsByExistingDateAndKindReturnsCorrectReportDTOList() {
        long chatId = 405441405;
        ReportDTO reportDTO = reportService.addReport(chatId, "mealsAndStuff", new byte[1]);
        List<ReportDTO> reportDTOS = new ArrayList<>();
        reportDTOS.add(reportDTO);
        Kind kind = petService.getPet(reportDTO.petId()).kind();
        LocalDate date = reportDTO.reportDate();
        assertEquals(reportDTOS, reportService.getReportsByDate(date, kind));
    }

    @Test
    public void gettingReportsByNonExistingInDBDateAndKindThrowsNoSuchEntityException() {
        long chatId = 405441405;
        ReportDTO reportDTO = reportService.addReport(chatId, "mealsAndStuff", new byte[1]);
        List<ReportDTO> reportDTOS = new ArrayList<>();
        reportDTOS.add(reportDTO);
        Kind kind = petService.getPet(reportDTO.petId()).kind();
        LocalDate date = reportDTO.reportDate();
        assertThrows(NoSuchEntityException.class, () -> reportService.getReportsByDate(date.plusDays(1), kind));
    }

    @Test
    public void gettingReportsByNonExistingInDBKindAndExistingDateThrowsNoSuchEntityException() {
        PetDTO pet2 = new PetDTO(0L, 2021, "Lara", "Labrador", "kind", Color.WHITE, Status.FREE, 0, DOG, MALE);
        long chatId = 405441405;
        ReportDTO reportDTO = reportService.addReport(chatId, "mealsAndStuff", new byte[1]);
        List<ReportDTO> reportDTOS = new ArrayList<>();
        reportDTOS.add(reportDTO);
        LocalDate date = reportDTO.reportDate();
        assertThrows(NoSuchEntityException.class, () -> reportService.getReportsByDate(date, pet2.kind()));
    }

    @Test
    public void gettingReportsByExistingDateAndPetIdReturnsCorrectReportDTOList() {
        long chatId = 405441405;
        ReportDTO reportDTO = reportService.addReport(chatId, "mealsAndStuff", new byte[1]);
        List<ReportDTO> reportDTOS = new ArrayList<>();
        reportDTOS.add(reportDTO);
        long petId = petService.getPet(reportDTO.petId()).id();
        LocalDate date = reportDTO.reportDate();
        assertEquals(reportDTOS, reportService.getReportsByDateAndPetId(date, petId));
    }

    @Test
    public void gettingReportsByNonExistingInDBDateAndExistingPetIdThrowsNoSuchEntityException() {
        long chatId = 405441405;
        ReportDTO reportDTO = reportService.addReport(chatId, "mealsAndStuff", new byte[1]);
        List<ReportDTO> reportDTOS = new ArrayList<>();
        reportDTOS.add(reportDTO);
        long petId = petService.getPet(reportDTO.petId()).id();
        LocalDate date = reportDTO.reportDate();
        assertThrows(NoSuchEntityException.class, () -> reportService.getReportsByDateAndPetId(date.plusDays(1), petId));
    }

    @Test
    public void gettingReportsByNonExistingInDBPetIdAndExistingDateThrowsNoSuchEntityException() {
        PetDTO pet2 = new PetDTO(0L, 2021, "Lara", "Labrador", "kind", Color.WHITE, Status.FREE, 0, DOG, MALE);
        long chatId = 405441405;
        ReportDTO reportDTO = reportService.addReport(chatId, "mealsAndStuff", new byte[1]);
        List<ReportDTO> reportDTOS = new ArrayList<>();
        reportDTOS.add(reportDTO);
        LocalDate date = reportDTO.reportDate();
        assertThrows(NoSuchEntityException.class, () -> reportService.getReportsByDateAndPetId(date, pet2.id()));
    }

    @Test
    public void gettingReportsByNonExistingInDBPetIdAndNonExistingDateThrowsNoSuchEntityException() {
        PetDTO pet2 = new PetDTO(0L, 2021, "Lara", "Labrador", "kind", Color.WHITE, Status.FREE, 0, DOG, MALE);
        long chatId = 405441405;
        ReportDTO reportDTO = reportService.addReport(chatId, "mealsAndStuff", new byte[1]);
        List<ReportDTO> reportDTOS = new ArrayList<>();
        reportDTOS.add(reportDTO);
        LocalDate date = reportDTO.reportDate();
        assertThrows(NoSuchEntityException.class, () -> reportService.getReportsByDateAndPetId(date.plusDays(1), pet2.id()));
    }

    @Test
    public void gettingAllReportsByKindReturnsCorrectReportDTOList() {
        long chatId = 405441405;
        ReportDTO reportDTO = reportService.addReport(chatId, "mealsAndStuff", new byte[1]);
        List<ReportDTO> reportDTOS = new ArrayList<>();
        reportDTOS.add(reportDTO);
        Kind kind = petService.getPet(reportDTO.petId()).kind();
        assertEquals(reportDTOS, reportService.getAllReports(kind));
    }

    @Test
    public void gettingAllReportsByNonExistingInDBKindThrowsReportListIsEmptyException() {
        PetDTO pet2 = new PetDTO(0L, 2021, "Lara", "Labrador", "kind", Color.WHITE, Status.FREE, 0, DOG, MALE);
        long chatId = 405441405;
        ReportDTO reportDTO = reportService.addReport(chatId, "mealsAndStuff", new byte[1]);
        List<ReportDTO> reportDTOS = new ArrayList<>();
        reportDTOS.add(reportDTO);
        assertThrows(ReportListIsEmptyException.class, () -> reportService.getAllReports(pet2.kind()));
    }

    @Test
    public void gettingReportsByExistingChatIdReturnsCorrectReportDTOList() {
        long chatId = 405441405;
        ReportDTO reportDTO = reportService.addReport(chatId, "mealsAndStuff", new byte[1]);
        List<ReportDTO> reportDTOS = new ArrayList<>();
        reportDTOS.add(reportDTO);
        assertEquals(reportDTOS, reportService.getReportsByChatId(chatId));
    }

    @Test
    public void gettingReportsByNonExistingChatIdThrowsReportListIsEmptyException() {
        assertThrows(ReportListIsEmptyException.class, () -> reportService.getReportsByChatId(0));
    }

    @Test
    public void settingReportByReportIdAsCompleteSetsReportIsCompleteFieldAsTrueInExistingReport() {
        long chatId = 405441405;
        ReportDTO reportDTO = reportService.addReport(chatId, "mealsAndStuff", new byte[1]);
        reportService.setReportAsIncomplete(reportDTO.id());
        reportService.setReportAsComplete(reportDTO.id());
        assertEquals(reportService.getReportByReportId(reportDTO.id()), reportDTO);
    }

    @Test
    public void settingReportByNonExistingReportIdAsCompleteThrowsNoSuchEntityException() {
        assertThrows(NoSuchEntityException.class, () -> reportService.setReportAsComplete(0));
    }

    @Test
    public void settingReportAsIncompleteByReportIdSetsReportIsCompleteFieldAsFalseInExistingReport() {
        long chatId = 405441405;
        ReportDTO reportDTO = reportService.addReport(chatId, "mealsAndStuff", new byte[1]);
        reportService.setReportAsIncomplete(reportDTO.id());
        reportDTO.reportIsComplete();
        assertFalse(reportService.getReportByReportId(reportDTO.id()).reportIsComplete());
    }

    @Test
    public void settingReportAsIncompleteByNonExistingReportIdThrowsNoSuchEntityException() {
        assertThrows(NoSuchEntityException.class, () -> reportService.setReportAsIncomplete(0));
    }

    @Test
    public void settingReportByReportIdAsInspectedSetsReportIsInspectedFieldAsTrueInExistingReport() {
        long chatId = 405441405;
        ReportDTO reportDTO = reportService.addReport(chatId, "mealsAndStuff", new byte[1]);
        reportService.setReportAsInspected(reportDTO.id());
        assertTrue(reportService.getReportByReportId(reportDTO.id()).reportIsInspected());
    }

    @Test
    public void settingReportByNonExistingReportIdAsInspectedThrowsNoSuchEntityException() {
        assertThrows(NoSuchEntityException.class, () -> reportService.setReportAsComplete(0));
    }

    @Test
    public void deletingReportByExistingReportIdDeletesCorrectReport() {
        reportService.addReport(999, "string", new byte[1]);
        long chatId = 405441405;
        ReportDTO reportDTO = reportService.addReport(chatId, "mealsAndStuff", new byte[1]);
        reportService.deleteReportByReportId(reportService.getReportsByChatId(chatId).stream().findFirst().get().id());
        assertFalse(reportService.getAllReports(petService.getPet(reportDTO.petId()).kind()).contains(reportDTO));
    }

    @Test
    public void deletingReportByNonExistingReportIdThrowsNoSuchEntityException() {
        assertThrows(NoSuchEntityException.class, () -> reportService.deleteReportByReportId(0));
    }

    @Test
    public void deletingReportsByExistingPetIdDeletesAllReportsWithGivenPetId() {
        long chatId = 405441405;
        reportService.addReport(chatId, "mealsAndStuff", new byte[1]);
        long petId = reportService.getReportsByChatId(chatId).stream().findFirst().get().petId();
        reportService.deleteReportsByPetId(petId);
        assertThrows(NoSuchEntityException.class, () -> reportService.getReportsByPetId(petId));
    }

    @Test
    public void deletingReportsByNonExistingPetIdThrowsNoSuchEntityException() {
        assertThrows(NoSuchEntityException.class, () -> reportService.deleteReportsByPetId(1));
    }


}
