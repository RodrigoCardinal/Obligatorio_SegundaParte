package com.example;

import java.util.LinkedList;

public class Scheduler implements IScheduler{

    boolean isActive;
    Process runningProcess;
    LinkedList<Process> processesList;
    LinkedList<Resource> resourcesList;
    String schedullingPolicy;
    long timeout;

    public Scheduler(String schedullingPolicy, long initialTimeout)
    {
        this.isActive = false;
        this.runningProcess = null;
        this.processesList = new LinkedList<>();
        this.resourcesList = new LinkedList<>();
        this.schedullingPolicy = schedullingPolicy;
        this.timeout = initialTimeout;
    }

    @Override
    public void Start() throws InterruptedException{
        isActive = true;
        System.out.println("Comenzó la ejecución del scheduler");
        while(isActive)
        {
            System.out.println("El scheduler sigue activo, actualmente está corriendo el proceso: " + (runningProcess == null ? "NULO" : runningProcess.getName()));
            if(!DispatchNext())
            {
                isActive = false;
                System.out.println("Se rompió la ejecución del scheduler por un error al realizar un despacho.");
            }
        }
        End();
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
        RemoveProcess(next);
        runningProcess = next;
        next.Run(timeout);
        if(next.getTimeRequired() != 0)
        {
            AddProcess(next);
        }
    }


    public void FIFODispatch() throws InterruptedException
    {
        Process next = processesList.getFirst();
        RemoveProcess(next);
        runningProcess = next;
        next.Run(next.getTimeRequired());
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

    @Override
    public void SuspendProcess(Process proc) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'SuspendProcess'");
    }

    @Override
    public void ResumeProcess(Process proc) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'ResumeProcess'");
    }

    @Override
    public void KillProcess(Process proc) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'KillProcess'");
    }

    
    @Override
    public void GiveResource(Resource res) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'GiveResource'");
    }

    @Override
    public void TakeResource(Resource res) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'TakeResource'");
    }
}
