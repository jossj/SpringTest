package com.example.springtest.service;

import com.example.springtest.model.*;
import com.example.springtest.repository.ClassRoomRepository;
import com.example.springtest.repository.RewardRepository;
import com.example.springtest.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BulkUploadService {

    private final StudentRepository studentRepository;
    private final RewardRepository rewardRepository;
    private final ClassRoomRepository classRoomRepository;

    public record UploadResult(
            int studentsCreated,
            int rewardsCreated,
            List<String> errors
    ) {}

    @Transactional
    public UploadResult processUpload(MultipartFile file) throws IOException {
        List<String> errors = new ArrayList<>();
        int studentsCreated = 0;
        int rewardsCreated = 0;

        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet studentsSheet = findSheet(workbook, "Students");
            Sheet rewardsSheet  = findSheet(workbook, "Rewards");

            if (studentsSheet == null) {
                errors.add("Workbook is missing a sheet named 'Students'.");
            }
            if (rewardsSheet == null) {
                errors.add("Workbook is missing a sheet named 'Rewards'.");
            }
            if (!errors.isEmpty()) {
                return new UploadResult(0, 0, errors);
            }

            studentsCreated = importStudents(studentsSheet, errors);
            rewardsCreated  = importRewards(rewardsSheet, errors);
        }

        return new UploadResult(studentsCreated, rewardsCreated, errors);
    }

    // ── Students sheet: firstName | lastName | email | className | yearLevel ──

    private int importStudents(Sheet sheet, List<String> errors) {
        int created = 0;
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (isBlankRow(row)) continue;

            String firstName = cellString(row, 0);
            String lastName  = cellString(row, 1);
            String email     = cellString(row, 2);
            String className = cellString(row, 3);
            String yearStr   = cellString(row, 4);

            if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty()) {
                errors.add("Students row " + (i + 1) + ": firstName, lastName, and email are required.");
                continue;
            }

            if (studentRepository.findByEmail(email).isPresent()) {
                errors.add("Students row " + (i + 1) + ": student with email '" + email + "' already exists — skipped.");
                continue;
            }

            ClassRoom classRoom = null;
            if (!className.isEmpty()) {
                YearLevel yearLevel = parseYearLevel(yearStr);
                if (yearLevel == null && !yearStr.isEmpty()) {
                    errors.add("Students row " + (i + 1) + ": unrecognised yearLevel '" + yearStr + "' — student saved without classroom.");
                } else if (yearLevel != null) {
                    classRoom = classRoomRepository
                            .findByNameAndYearLevel(className, yearLevel)
                            .orElseGet(() -> classRoomRepository.save(
                                    ClassRoom.builder().name(className).yearLevel(yearLevel).build()));
                }
            }

            studentRepository.save(Student.builder()
                    .firstName(firstName)
                    .lastName(lastName)
                    .email(email)
                    .classRoom(classRoom)
                    .build());
            created++;
        }
        return created;
    }

    // ── Rewards sheet: studentEmail | title | description | points | type ──

    private int importRewards(Sheet sheet, List<String> errors) {
        int created = 0;
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (isBlankRow(row)) continue;

            String studentEmail = cellString(row, 0);
            String title        = cellString(row, 1);
            String description  = cellString(row, 2);
            String pointsStr    = cellString(row, 3);
            String typeStr      = cellString(row, 4);

            if (studentEmail.isEmpty() || title.isEmpty()) {
                errors.add("Rewards row " + (i + 1) + ": studentEmail and title are required.");
                continue;
            }

            Optional<Student> studentOpt = studentRepository.findByEmail(studentEmail);
            if (studentOpt.isEmpty()) {
                errors.add("Rewards row " + (i + 1) + ": no student found with email '" + studentEmail + "' — skipped.");
                continue;
            }

            int points = 0;
            try {
                points = Integer.parseInt(pointsStr);
            } catch (NumberFormatException e) {
                errors.add("Rewards row " + (i + 1) + ": invalid points value '" + pointsStr + "', defaulting to 0.");
            }

            RewardType rewardType = null;
            if (!typeStr.isEmpty()) {
                try {
                    rewardType = RewardType.valueOf(typeStr.toUpperCase());
                } catch (IllegalArgumentException e) {
                    errors.add("Rewards row " + (i + 1) + ": unrecognised reward type '" + typeStr + "' — saved without type.");
                }
            }

            rewardRepository.save(Reward.builder()
                    .student(studentOpt.get())
                    .title(title)
                    .description(description.isEmpty() ? null : description)
                    .points(points)
                    .type(rewardType)
                    .build());
            created++;
        }
        return created;
    }

    // ── helpers ───────────────────────────────────────────────────────────────

    private Sheet findSheet(Workbook workbook, String name) {
        Sheet sheet = workbook.getSheet(name);
        if (sheet != null) return sheet;
        // case-insensitive fallback
        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
            if (workbook.getSheetName(i).equalsIgnoreCase(name)) {
                return workbook.getSheetAt(i);
            }
        }
        return null;
    }

    private String cellString(Row row, int col) {
        if (row == null) return "";
        Cell cell = row.getCell(col, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
        if (cell == null) return "";
        cell.setCellType(CellType.STRING);
        String val = cell.getStringCellValue();
        return val == null ? "" : val.trim();
    }

    private boolean isBlankRow(Row row) {
        if (row == null) return true;
        for (int c = row.getFirstCellNum(); c < row.getLastCellNum(); c++) {
            Cell cell = row.getCell(c);
            if (cell != null && cell.getCellType() != CellType.BLANK) return false;
        }
        return true;
    }

    private YearLevel parseYearLevel(String raw) {
        if (raw == null || raw.isBlank()) return null;
        String upper = raw.toUpperCase().trim();
        try {
            return YearLevel.valueOf(upper);
        } catch (IllegalArgumentException ignored) {}
        // accept "5", "05", "Year 5", "Year5" etc.
        String digits = upper.replaceAll("[^0-9]", "");
        if (!digits.isEmpty()) {
            try {
                return YearLevel.valueOf("YEAR_" + Integer.parseInt(digits));
            } catch (IllegalArgumentException ignored) {}
        }
        return null;
    }
}
