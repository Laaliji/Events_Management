package com.example.metier.entities;


import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private Date dateTime;

    @Enumerated(EnumType.STRING)
    private EventType type;

    public Event() {
    }

    public Event(String title, String description, Date dateTime, int durationInMinutes, EventType type) {
        this.title = title;
        this.description = description;
        this.dateTime = dateTime;
        this.type = type;
    }



        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public Date getDateTime() {
            return dateTime;
        }

        public void setDateTime(Date dateTime) {
            this.dateTime = dateTime;
        }

        public EventType getType() {
            return type;
        }

        public void setType(EventType type) {
            this.type = type;
        }
}
