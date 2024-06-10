package com.example;

import java.util.LinkedList;

public class Process 
{
    private String name;
    private int timeRequired;
    private int context;
    private LinkedList<Resource> resAvailables;

    public Process(String name, int timeRequired, int initialContext){   
        this.name = name;
        this.timeRequired = timeRequired;
        this.context = initialContext;
        this.resAvailables = new LinkedList<>();
    }

    public void Run(){
        System.out.println("Se está ejecutando el proceso: " + name);
    }

    public void Stop(){
        System.out.println("El proceso " + this.getNombre() + " se a detenido.");
        timer=0;
        this.setRecursoHabilitado(false);
    }

    public void saveContext(){
        System.out.println("Guardando el contexto del proceso " + this.getNombre() + "." );
        contexto = timer;
    }

    public void loadContext(){
        System.out.println("El contexto del proceso" + this.getNombre() + " es " + this.getContexto());
    }

    public String getNombre(){
        return this.nombre;
    }

    public int getTimer(){
        return this.timer;
    }

    public int getContexto(){
        return this.contexto;
    }

    public boolean getRecursoHabilitado(){
        return this.recursoHabilitado;
    }

    public boolean setRecursoHabilitado(boolean recurso){
        return this.recursoHabilitado = recurso;
    }
}
