package com.example;

public class Resource {
    private int ID;
    private String name;
    Process owner;

    public Resource(int ID, String name)
    {
        this.ID = ID;
        this.name = name;
        this.owner = null;
    }
   
    public int getID() {
        return ID;
    }
    public String getName() {
        return name;
    }
    public Process getOwner() {
        return owner;
    }
    public void setOwner(Process owner) {
        this.owner = owner;
    }

    @Override
    public String toString() {
        return "Resource[ID=" + ID + ", Name=" + name + "]";
    }

}
