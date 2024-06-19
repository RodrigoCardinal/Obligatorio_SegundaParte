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

    /**
     * Constructor para la clase Process.
     *
     * @param name           el nombre del proceso
     * @param timeRequired   el tiempo requerido para que el proceso se complete
     * @param initialContext el contexto inicial del proceso
     * @param resNeeded      la lista de recursos necesarios para el proceso
     */
    public Process(String name, long timeRequired, int initialContext, LinkedList<Resource> resNeeded){   
        this.name = name;
        this.timeRequired = timeRequired;
        this.context = initialContext;
        this.resNeeded = resNeeded;
        this.resAvailables = new LinkedList<>();
        this.status = Status.READY;
    }

    /**
     * Obtiene el nombre del proceso.
     *
     * @return el nombre del proceso
     */
    public String getName(){
        return this.name;
    }

    /**
     * Establece un nuevo nombre para el proceso.
     *
     * @param newName el nuevo nombre del proceso
     */
    public void setName(String newName)
    {
        this.name = newName;
    }

    /**
     * Obtiene el contexto del proceso.
     *
     * @return el contexto del proceso
     */
    public int getContext(){
        return this.context;
    }

    /**
     * Establece un nuevo contexto para el proceso.
     *
     * @param newContext el nuevo contexto del proceso
     */
    public void setContext(int newContext)
    {
        this.context = newContext;
    }

    /**
     * Obtiene el tiempo requerido para que el proceso se complete.
     *
     * @return el tiempo requerido
     */
    public long getTimeRequired()
    {
        return this.timeRequired;
    }

    /**
     * Obtiene la lista de recursos necesarios para el proceso.
     *
     * @return la lista de recursos necesarios
     */
    public LinkedList<Resource> getResNeeded()
    {
        return resNeeded;
    }

     /**
     * Obtiene la lista de recursos disponibles para el proceso.
     *
     * @return la lista de recursos disponibles
     */
    public LinkedList<Resource> getResAvaliables()
    {
        return resAvailables;
    }

    /**
     * Obtiene el estado actual del proceso.
     *
     * @return el estado del proceso
     */
    public Status getStatus()
    {
        return status;
    }

     /**
     * Establece un nuevo estado para el proceso.
     *
     * @param newStatus el nuevo estado del proceso
     */
    public void setStatus(Status newStatus)
    {
        this.status = newStatus;
    }

     /**
     * Ejecuta el proceso durante un periodo de tiempo especificado.
     *
     * @param seconds el tiempo en segundos durante el cual se ejecutará el proceso
     * @throws InterruptedException si la ejecución del hilo es interrumpida
     */
    public void run(long seconds) throws InterruptedException{

        //Revisa si tiene disponibles todos los recursos que necesita.
        if(resAvailables.containsAll(resNeeded))
        {
            Random random = new Random();

            //Comienza a ejecutar el proceso.
            status = Status.RUNNING;
            System.out.println("Comenzó a ejecutarse el proceso: " + name + ", su contexto actual es: " + Integer.toString(context) + " y para terminar aún le faltan " + timeRequired + " segundos.");
            context = random.nextInt(1000000);
            if(timeRequired - seconds <= 0)
            {
                //Transcurrió todo el tiempo que necesitaba el proceso, por lo que pasa a estar finalizado.
                execute(timeRequired);
                System.out.println("El proceso " + name + " se ejecutó por " + timeRequired + " segundos y finalizó completamente, a continuación se retirará del scheduller.");
                timeRequired = 0;
                status = Status.FINISHED;
            }
            else
            {
                //Transcurrió el tiempo designado por el scheduler pero aún no finalizó el proceso. (TIMEOUT)
                execute(seconds);
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

    /**
     * Simula la ejecución del proceso durante un tiempo especificado.
     *
     * @param seconds el tiempo en segundos durante el cual se simula la ejecución
     * @throws InterruptedException si la ejecución del hilo es interrumpida
     */
    public void execute(long seconds) throws InterruptedException
    {
        Thread.sleep(seconds*1000);
    }

    /**
     * Devuelve todos los recursos que el proceso tiene asignados al scheduler.
     *
     * @param scheduler el scheduler al que se devolverán los recursos
     */
    public void returnAllResources(Scheduler scheduler)
    {
        String returnedNames = "";
        LinkedList<Resource> resReturned = new LinkedList<>();
        for (Resource res : resAvailables) {
            resReturned.add(res);
            scheduler.resourcesList.add(res);
            res.setOwner(null);
            returnedNames += res.getName() + ", ";
        }
        resAvailables.removeAll(resReturned);
        System.out.println("El proceso " + name + " devolvió los recursos: " + returnedNames);
    }
    @Override
    public String toString()
    {
        return "Process[Name=" + name + ", TimeRequired=" + timeRequired + ", InitialContext=" + context + ", Resources=" + resNeeded + "]";
    }
}
