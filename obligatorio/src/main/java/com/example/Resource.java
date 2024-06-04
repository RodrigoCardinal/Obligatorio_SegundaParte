package com.example;

public class Resource {
    private int Id;
    private String name;
    boolean isAvailable;
    Process processOwner;

    public int getId() {
        return Id;
    }
    public String getName() {
        return name;
    }
    public boolean isAvailable() {
        return isAvailable;
    }
    public void setIsAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }
    public Process getProcessOwner() {
        return processOwner;
    }
    public void setProcessOwner(Process processOwner) {
        this.processOwner = processOwner;
    }



    
}