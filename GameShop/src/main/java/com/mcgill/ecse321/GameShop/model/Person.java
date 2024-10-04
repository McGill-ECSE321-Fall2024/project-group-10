package com.mcgill.ecse321.GameShop.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity // all entity in hibernate classes need to havea d eafulat instructor. but it can be protected.
public class Person {
    @Id
    @GeneratedValue()
    private int id;

    protected Person() {
    }

    public int Pid(){
        return id;
    }
}
