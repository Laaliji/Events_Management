package com.example.jeebackend.Entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "programs")
public class Program {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @ElementCollection
    @CollectionTable(name = "program_schedule", joinColumns = @JoinColumn(name = "program_id"))
    @Column(name = "session_time")
    private List<LocalDateTime> schedules;

    @Transient
    public List<LocalDateTime> getSchedulesAsLocalDateTime() {
        return schedules;
    }

    @ElementCollection
    @CollectionTable(name = "program_speakers", joinColumns = @JoinColumn(name = "program_id"))
    @Column(name = "speaker_name")
    private List<String> speakers;

    @ElementCollection
    @CollectionTable(name = "program_topics", joinColumns = @JoinColumn(name = "program_id"))
    @Column(name = "topic_name")
    private List<String> topics;

    @ElementCollection
    @CollectionTable(name = "program_images", joinColumns = @JoinColumn(name = "program_id"))
    @Column(name = "image_url")
    private List<String> images;

    @Column(name="details",columnDefinition = "TEXT")
    private String details;

    @ManyToOne(fetch = FetchType.LAZY)
    private Event event;

    public Program() {
    }

    public Program(List<String> schedules, List<String> speakers, List<String> topics, List<String> images, String details, Event event) {
        this.schedules = schedules.stream()
                .map(dateTimeString -> LocalDateTime.parse(dateTimeString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS")))
                .collect(Collectors.toList());
        this.speakers = speakers;
        this.topics = topics;
        this.images = images;
        this.details = details;
        this.event = event;
    }

    public List<LocalDateTime> getSchedules() {
        return schedules;
    }


    public List<String> getSpeakers() {
        return speakers;
    }

    public List<String> getTopics() {
        return topics;
    }

    public List<String> getImages() {
        return images;
    }

    public String getDetails() {
        return details;
    }

    public Event getEvent() {
        return event;
    }

    public void setSchedules(List<LocalDateTime> schedules) {
        this.schedules = schedules;
    }

    public void setSpeakers(List<String> speakers) {
        this.speakers = speakers;
    }

    public void setTopics(List<String> topics) {
        this.topics = topics;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public void setEvent(Event event) {
        this.event = event;
    }
}
