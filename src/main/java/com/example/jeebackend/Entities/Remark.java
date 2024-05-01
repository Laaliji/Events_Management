package com.example.jeebackend.Entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

@Entity
@Table(name = "remarks")
public class Remark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "remark_id", nullable = false)
    @JsonProperty("remark_id")
    private Long id;

    @Column(name = "remark_text", nullable = false)
    private String description;

    @Column(name = "priority")
    private String priority;

    @Column(name = "status")
    private String status;

    @Enumerated(EnumType.STRING)
    private RemarkType type;


    @ManyToOne(fetch = FetchType.LAZY)
    private Event event;

    public Remark() {
    }

    public Remark(String description, String priority, String status, RemarkType type, Event event) {
        this.description = description;
        this.priority = priority;
        this.status = status;
        this.type = type;
        this.event = event;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public RemarkType getType() {
        return type;
    }

    public void setType(RemarkType type) {
        this.type = type;
    }


    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }
}
