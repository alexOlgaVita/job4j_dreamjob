package ru.job4j.dreamjob.model;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

public class Candidate {

    public static final Map<String, String> COLUMN_MAPPING = Map.of(
            "id", "id",
            "title", "title",
            "description", "description",
            "creation_date", "creationDate",
            "city_id", "cityId",
            "file_id", "fileId"
    );

    private int id;

    private String title;

    private String description;

    private LocalDateTime creationDate = LocalDateTime.now();

    private int cityId;

    private int fileId;

    public Candidate() {
    }

    public Candidate(int id, String title, String description, LocalDateTime creationDate,
                     int cityId, int fileId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.creationDate = creationDate;
        this.cityId = cityId;
        this.fileId = fileId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public int getCityId() {
        return cityId;
    }

    public int getFileId() {
        return fileId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public void setFileId(int fileId) {
        this.fileId = fileId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Candidate candidate = (Candidate) o;
        return id == candidate.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
