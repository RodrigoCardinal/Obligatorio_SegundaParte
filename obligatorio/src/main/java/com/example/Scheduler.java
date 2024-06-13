package com.example;

import java.util.LinkedList;

public class Scheduler implements IScheduler{

    boolean isActive;
    Process runningProcess;
    LinkedList<Process> processesList;
    LinkedList<Process> blockedProcessesList;
    LinkedList<Resource> resourcesList;
    String schedullingPolicy;
    long timeout;
    int delayLoop;

    public Scheduler(String schedullingPolicy, long initialTimeout)
    {
        this.isActive = false;
        this.runningProcess = null;
        this.processesList = new LinkedList<>();
        this.blockedProcessesList = new LinkedList<>();
        this.resourcesList = new LinkedList<>();
        this.schedullingPolicy = schedullingPolicy;
        this.timeout = initialTimeout;
        this.delayLoop = 500;
    }

    @Override
    public void Start() throws InterruptedException{
        boolean areReadyProcesses = false;
        boolean allFinished = false;
        isActive = true;
        System.out.println("Comenzó la ejecución del scheduler");

        //Búcle Principal del Scheduler
        while(isActive)
        {

            //Intenta dar recursos a los procesos bloqueados
            checkBlockedProcesses();

            //Comprobar si hay procesos listos
            for (Process process : processesList) {
                if (process.getStatus() == Status.READY)
                {
                    areReadyProcesses = true;
                }
            }   

            //Si no hay procesos listos espera un poco antes de volver a intentarlo.
            if(!areReadyProcesses)
            {
                Thread.sleep(delayLoop);
                continue;
            }

            //Despachar el siguiente proceso listo
            DispatchNext();


            

            
            //Muestra el estado actual del scheduler
            showActualStatus();


            //Chequea si todos los procesos están finalizados
            allFinished = true;
            for (Process process : processesList) {
                if(process.getStatus() != Status.FINISHED)
                {
                    allFinished = false;
                }
            }

            //Si todos los procesos están finalizados, termina la ejecución del scheduler
            if(allFinished) isActive = false;

            //Al final de la vuelta pongo en falso que hay procesos listos.
            areReadyProcesses = false;
        }
        End();
    }

    private void showActualStatus() {
        String blockedProcesses = "";
        for (Process process : blockedProcessesList) {
            blockedProcesses += process.getName() + ", ";
        }

        String resources = "";
        for (Resource res : resourcesList) {
            resources += res.getName() + ", ";
        }

        String processes = "";
        for (Process process : processesList) {
            processes += process.getName() + ", ";
        }

        String name = runningProcess == null ? "NINGUNO" : runningProcess.getName(); 

        System.out.println("ESTADO DEL SCHEDULER: \nProceso en Ejecución: " + name + " \nProcesos Bloqueados: " + blockedProcesses + " \nRecursos: " + resources + "\nProcesos: " + processes);
    }

    private void checkBlockedProcesses() {
        for (Process process : blockedProcessesList) {
            tryToUnlock(process);
        }        
    }

    private void tryToUnlock(Process proc)
    {
        LinkedList<Resource> resGiven = new LinkedList<>(); //Guarda los recursos que le voy dando

        for (Resource resource : proc.getResNeeded()) {
            if(resourcesList.contains(resource) && resource.owner == null)
            {
                //Le da el recurso al proceso
                resource.owner = proc;
                proc.getResAvaliables().add(resource);
                resGiven.add(resource);
            }
        }

        //Si ya tiene todos los recursos que necesita, se desbloquea
        if(proc.getResAvaliables().containsAll(proc.getResNeeded()))
        {
            proc.setStatus(Status.READY);
            blockedProcessesList.remove(proc);
            System.out.println("Se desbloqueó el proceso " + proc.getName() + " porque le dieron todos los recursos que precisaba.");
        }
        else
        {
            //Sino, devuelve los recursos que pidió
            for (Resource resource : resGiven) {
                resource.owner = null;
                proc.getResAvaliables().remove(resource);
            }
        }
    }

    @Override
    public void End() {
        System.out.println("Terminó la ejecución del scheduler");
    }

    @Override
    public boolean DispatchNext() throws InterruptedException
    {   
        if(processesList.isEmpty())
        {
            System.out.println("El Scheduler no puede hacer un despacho porque no tiene procesos.");
            return false;
        }
        switch(schedullingPolicy)
        {
            case "RR":
                RoundRobinDispatch();
                return true;
            case "FIFO":
                FIFODispatch();
                return true;
            default:
                System.out.println("Política de Scheduling Inválida");
                return false;
        }
    }

    public void RoundRobinDispatch() throws InterruptedException
    {
        Process next = processesList.getFirst();
        //Encuentra el primer proceso listo.
        while(next.getStatus() != Status.READY)
        {
            RemoveProcess(next);
            AddProcess(next);
            next = processesList.getFirst();   
        }

        RemoveProcess(next);
        runningProcess = next;
        next.Run(timeout);
        runningProcess = null;

        if (next.getStatus() == Status.BLOCKED) 
        {
            blockedProcessesList.add(next); // Lo pone en la lista de bloqueados si se bloqueó.
            AddProcess(next);
        } 
        else if (next.getStatus() == Status.READY) 
        {
            AddProcess(next); // Si aún no terminó lo vuelve a poner en la fila.
        }
    }


    public void FIFODispatch() throws InterruptedException
    {
        Process next = processesList.getFirst();
        //Encuentra el primer proceso listo.
        while(next.getStatus() != Status.READY)
        {
            RemoveProcess(next);
            AddProcess(next);
            next = processesList.getFirst();   
        }

        RemoveProcess(next);
        runningProcess = next;
        next.Run(next.getTimeRequired());
        runningProcess = null;

        if (next.getStatus() == Status.BLOCKED) 
        {
            blockedProcessesList.add(next); // Lo pone en la lista de bloqueados si se bloqueó.
            AddProcess(next);
        } 
    }

    @Override
    public void AddProcess(Process proc) {
        processesList.add(proc);
    }

    public void RemoveProcess(Process proc)
    {
        while(processesList.contains(proc))
        {
            processesList.removeFirstOccurrence(proc);   
        }
    }

    public void addResource(Resource res)
    {
        resourcesList.add(res);
    }

    public void RemoveResource(Resource res)
    {
        while(resourcesList.contains(res))
        {
            resourcesList.removeFirstOccurrence(res);   
        }
    }
}
