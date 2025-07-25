package jpabook.jpashop.domain;

import jakarta.persistence.Entity;

@Entity
public class Movie extends Item {
    private String director;
    private String actor;

    public String getActor() {
        return actor;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }
}
