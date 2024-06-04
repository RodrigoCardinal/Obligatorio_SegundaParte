package com.example;

public class Proceso 
{
    private String nombre;
    private int timer;
    private int contexto;
    private boolean recursoHabilitado;

    public Proceso(String nombre, int timer, int contexto, boolean recursoHabilitado){   
        this.nombre = nombre;
        this.timer = timer;
        this.contexto = contexto;
        this.recursoHabilitado = recursoHabilitado;
    }

    public void Run(){
        if(recursoHabilitado){
            System.out.println("Se esta ejecutando el proceso " + this.getNombre() + ".");
            timer++;
        }else{
            System.out.println("No se pudo ejectuar el proceso " + this.getNombre() + ".");
        }
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
