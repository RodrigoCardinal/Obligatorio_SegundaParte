package com.example;

import java.util.LinkedList;
import java.util.Random;

public class Process 
{
    private String name;
    private long timeRequired;
    private int context;
    private LinkedList<Resource> resNeeded;
    private LinkedList<Resource> resAvailables;
    private Status status;

    public Process(String name, long timeRequired, int initialContext, LinkedList<Resource> resNeeded){   
        this.name = name;
        this.timeRequired = timeRequired;
        this.context = initialContext;
        this.resNeeded = resNeeded;
        this.resAvailables = new LinkedList<>();
        this.status = Status.READY;
    }

    public String getName(){
        return this.name;
    }

    public int getContext(){
        return this.context;
    }

    public void setContext(int newContext)
    {
        this.context = newContext;
    }

    public long getTimeRequired()
    {
        return this.timeRequired;
    }

    public LinkedList<Resource> getResNeeded()
    {
        return resNeeded;
    }

    public LinkedList<Resource> getResAvaliables()
    {
        return resAvailables;
    }

    public Status getStatus()
    {
        return status;
    }

    public void setStatus(Status newStatus)
    {
        this.status = newStatus;
    }

    public void Run(long seconds) throws InterruptedException{
        if(resAvailables.containsAll(resNeeded))
        {
            Random random = new Random();
            status = Status.RUNNING;
            System.out.println("Se está ejecutando el proceso: " + name + ", su contexto actual es: " + Integer.toString(context) + " y le faltan aproximadamente " + timeRequired + " segundos para terminar su ejecución.");
            context = random.nextInt(100);
            if(timeRequired - seconds <= 0)
            {
                Wait(timeRequired);
                System.out.println("El proceso " + getName() + " se ejecutó por " + timeRequired + " segundos y finalizó completamente, a continuación se retirará del scheduller.");
                timeRequired = 0;
                status = Status.FINISHED;
            }
            else
            {
                Wait(seconds);
                timeRequired -= seconds;
                System.out.println("Terminó la ejecución de: " + name + ", su contexto actual es: " + Integer.toString(context) + " y ahora le faltan aproximadamente " + timeRequired + " segundos para terminar su ejecución.");
                status = Status.READY;
            }
        }
        else
        {
            String resWaited = "";
            for (Resource resource : resNeeded) {
                if(!resAvailables.contains(resource))
                {
                    resWaited += resource.getName() + ", ";
                }
            }
            System.out.println("Se bloqueo el proceso " + getName() + " porque le faltaban los siguientes recursos: " + resWaited);
            status = Status.BLOCKED;
        }
    }

    public void Wait(long seconds) throws InterruptedException
    {
        Thread.sleep(seconds*1000);
    }
}
