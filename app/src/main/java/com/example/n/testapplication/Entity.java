package com.example.n.testapplication;

import android.media.Image;

import java.util.Date;

/**
 * Created by N on 04.04.2017.
 */

public class Entity {
    private String name;
    private String surname;
    private Date birthDate;
    private Date deathDate;
    private String biography;
    private String[] popular;
    private int age;
    private Image photo;

    @Override
    public String toString() {
        return "Painter{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                '}';
    }

    public Entity(String name, String surname, Date birthDate, Date deathDate, String biography, String[] popular, Image photo) {
        this.name = name;
        this.surname = surname;
        this.birthDate = birthDate;
        this.deathDate = deathDate;
        this.biography = biography;
        this.popular = popular;
        this.photo = photo;
    }

    public int getAge(){
        int result = 0;
        int birth = this.birthDate.getYear();
        int death = this.deathDate.getYear();
        if (death==0){
            int date = new Date(System.currentTimeMillis()).getYear();
            result = date - birth;
        } else {
            result = death - birth;
        }
        return result;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public Date getDeathDate() {
        return deathDate;
    }

    public String getBiography() {
        return biography;
    }

    public String[] getPopular() {
        return popular;
    }
}
