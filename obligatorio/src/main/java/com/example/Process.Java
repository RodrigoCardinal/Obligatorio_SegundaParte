package com.example;

import java.util.LinkedList;
import java.util.Random;

public class Process 
{
    private String name;
    private long timeRequired;
    private int context;
    private LinkedList<Resource> resAvailables;
    private Status status;
    
    private enum Status
    {
        READY,
        RUNNING,
        BLOCKED
    }

    public Process(String name, long timeRequired, int initialContext){   
        this.name = name;
        this.timeRequired = timeRequired;
        this.context = initialContext;
        this.resAvailables = new LinkedList<>();
        this.status = Status.READY;
    }

    public String getName(){
        return this.name;
    }

    public int getTimer(){
        return this.timer;
    }

    public int getContext(){
        return this.context;
    }

    public long getTimeRequired()
    {
        return this.timeRequired;
    }

    public void Run(long seconds) throws InterruptedException{
        Random random = new Random();
        status = Status.RUNNING;
        System.out.println("Se está ejecutando el proceso: " + name + ", su contexto actual es: " + Integer.toString(context) + " y le faltan aproximadamente " + timeRequired + " segundos para terminar su ejecución.");
        Wait(seconds);
        context = random.nextInt(100);
        timeRequired = timeRequired - seconds < 0 ? 0 : timeRequired - seconds;
        System.out.println("Terminó la ejecución de: " + name + ", su contexto actual es: " + Integer.toString(context) + " y ahora le faltan aproximadamente " + timeRequired + " segundos para terminar su ejecución.");
        status = Status.READY;
    }

    public void Stop(){
        System.out.println("El proceso " + this.getNombre() + " se a detenido.");
    }

    public void Wait(long seconds) throws InterruptedException
    {
        Thread.sleep(seconds*1000);
    }
}
