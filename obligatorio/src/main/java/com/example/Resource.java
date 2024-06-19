package com.example;

public class Resource {
    private int ID;
    private String name;
    Process owner;

    /**
     * Constructor para la clase Resource.
     *
     * @param ID   el identificador del recurso
     * @param name el nombre del recurso
     */
    public Resource(int ID, String name) {
        this.ID = ID;
        this.name = name;
        this.owner = null;
    }
   
    /**
     * Obtiene el identificador del recurso.
     *
     * @return el identificador del recurso
     */
    public int getID() {
        return ID;
    }

    /**
     * Obtiene el nombre del recurso.
     *
     * @return el nombre del recurso
     */
    public String getName() {
        return name;
    }

    /**
     * Obtiene el proceso propietario del recurso.
     *
     * @return el proceso propietario del recurso
     */
    public Process getOwner() {
        return owner;
    }

    /**
     * Establece el proceso propietario del recurso.
     *
     * @param owner el proceso propietario del recurso
     */
    public void setOwner(Process owner) {
        this.owner = owner;
    }

    /**
     * Devuelve una representación en cadena del recurso.
     *
     * @return una cadena que representa el recurso
     */
    @Override
    public String toString() {
        return "Resource[ID=" + ID + ", Name=" + name + "]";
    }
}