package com.example;

public class Resource {
    private int Id;
    private String name;
    Process owner;
   
    public int getId() {
        return Id;
    }
    public String getName() {
        return name;
    }
    public Process getOwner() {
        return owner;
    }
    public void setOwner(Process processOwner) {
        this.processOwner = processOwner;
    }
    
}
