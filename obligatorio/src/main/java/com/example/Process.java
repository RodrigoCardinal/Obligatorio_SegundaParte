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

    public void setName(String newName)
    {
        this.name = newName;
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

        //Revisa si tiene disponibles todos los recursos que necesita.
        if(resAvailables.containsAll(resNeeded))
        {
            Random random = new Random();

            //Comienza a ejecutar el proceso.
            status = Status.RUNNING;
            System.out.println("Comenzó a ejecutarse el proceso: " + name + ", su contexto actual es: " + Integer.toString(context) + " y para terminar aún le faltan " + timeRequired + " segundos.");
            context = random.nextInt(100);
            if(timeRequired - seconds <= 0)
            {
                //Transcurrió todo el tiempo que necesitaba el proceso, por lo que pasa a estar finalizado.
                Wait(timeRequired);
                System.out.println("El proceso " + name + " se ejecutó por " + timeRequired + " segundos y finalizó completamente, a continuación se retirará del scheduller.");
                timeRequired = 0;
                status = Status.FINISHED;
            }
            else
            {
                //Transcurrió el tiempo designado por el scheduler pero aún no finalizó el proceso. (TIMEOUT)
                Wait(seconds);
                timeRequired -= seconds;
                System.out.println("Se ejecutó el proceso: " + name + " durante " + seconds + " segundos, pero aún no finalizó, por lo que llegó al TIMEOUT, aún necesita ejecutar por " + timeRequired + " segundos para finalizar, por lo que volverá a la fila de procesos del scheduler,  su contexto actual es: " + Integer.toString(context) + ".");
                status = Status.READY;
            }
        }
        else
        {
            //Si no pudo ejecutar, se bloquea y muestra cuales recursos le faltaron.
            String resWaited = "";
            for (Resource resource : resNeeded) {
                if(!resAvailables.contains(resource))
                {
                    resWaited += resource.getName() + ", ";
                }
            }
            System.out.println("Se bloqueo el proceso " + name + " porque le faltaban los siguientes recursos: " + resWaited);
            status = Status.BLOCKED;
        }
    }

    public void Wait(long seconds) throws InterruptedException
    {
        Thread.sleep(seconds*1000);
    }
}
