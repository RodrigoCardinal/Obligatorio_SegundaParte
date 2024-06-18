package com.example;

import java.util.LinkedList;

import com.example.Process;

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
    public void start() throws InterruptedException{
        boolean areReadyProcesses = false;
        boolean allFinished = false;
        isActive = true;
        System.out.println("Comenzó la ejecución del scheduler \n\n");

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
            if(!dispatchNext()) isActive = false;

            
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
        end();
    }

    private void showActualStatus() 
    {
        StringBuilder blockedProcessesBuilder = new StringBuilder();
        for (Process process : blockedProcessesList) {
            blockedProcessesBuilder.append(process.getName()).append(", ");
        }
        String blockedProcesses = blockedProcessesBuilder.length() > 0 ? blockedProcessesBuilder.substring(0, blockedProcessesBuilder.length() - 2) : "";
    
        StringBuilder resourcesBuilder = new StringBuilder();
        for (Resource res : resourcesList) {
            resourcesBuilder.append(String.format("%s (ID: %d, Dueño: %s)", res.getName(), res.getID(), res.getOwner() != null ? res.getOwner().getName() : "NINGUNO")).append(", ");
        }
        String resources = resourcesBuilder.length() > 0 ? resourcesBuilder.substring(0, resourcesBuilder.length() - 2) : "";
    
        StringBuilder processesBuilder = new StringBuilder();
        for (Process process : processesList) {
            processesBuilder.append(String.format("%s (Tiempo requerido: %d, Contexto: %d, Estado: %s, Recursos Necesarios: %s)", 
                                                  process.getName(), process.getTimeRequired(), process.getContext(), process.getStatus(), 
                                                  getProcessResources(process))).append(", ");
        }
        String processes = processesBuilder.length() > 0 ? processesBuilder.substring(0, processesBuilder.length() - 2) : "";
    
        String name = runningProcess == null ? "NINGUNO" : runningProcess.getName();
    
        System.out.printf(
            "========================================\n" +
            "          ESTADO DEL SCHEDULER          \n" +
            "========================================\n" +
            "Proceso en Ejecución: %s\n" +
            "========================================\n" +
            "Procesos Bloqueados: %s\n" +
            "========================================\n" +
            "Recursos: %s\n" +
            "========================================\n" +
            "Procesos: %s\n" +
            "========================================\n\n",
            name, blockedProcesses, resources, processes
        );
    }
    
    private String getProcessResources(Process process) {
        StringBuilder resourcesBuilder = new StringBuilder();
        for (Resource res : process.getResNeeded()) {
            resourcesBuilder.append(res.getName()).append(", ");
        }
        return resourcesBuilder.length() > 0 ? resourcesBuilder.substring(0, resourcesBuilder.length() - 2) : "";
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
            System.out.println("Se desbloqueó el proceso " + proc.getName() + " porque le dieron todos los recursos que precisaba. \n");
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
    public void end() {
        System.out.println("Terminó la ejecución del scheduler \n\n");
    }

    @Override
    public boolean dispatchNext() throws InterruptedException
    {   
        if(processesList.isEmpty())
        {
            System.out.println("ERROR: El Scheduler no puede hacer un despacho porque no tiene procesos. \n\n");
            return false;
        }
        switch(schedullingPolicy)
        {
            case "RR":
                roundRobinDispatch();
                return true;
            case "FIFO":
                FIFODispatch();
                return true;
            default:
                System.out.println("ERROR: Política de Scheduling Inválida \n\n");
                return false;
        }
    }

    public void roundRobinDispatch() throws InterruptedException
    {
        //Encuentra el primer proceso listo.

        Process next = null;
        for (Process process : processesList) {
            if(process.getStatus() == Status.READY)
            {
                next = process;
                break;
            }
        }

        if(next == null)
        {
            System.out.println("ERROR: Se intentó despachar y el scheduler no tiene procesos listos. \n\n");
            return;
        }
        else
        {
            removeProcess(next);
            runningProcess = next;
            next.run(timeout);
            next.returnAllResources(this);
            checkBlockedProcesses();

            if (next.getStatus() == Status.BLOCKED) 
            {
                blockedProcessesList.add(next); // Lo pone en la lista de bloqueados si se bloqueó.
                addProcess(next);
            } 
            else if (next.getStatus() == Status.READY) 
            {
                addProcess(next); // Si aún no terminó lo vuelve a poner en la fila.
            }
        }
    }


    public void FIFODispatch() throws InterruptedException
    {
        //Encuentra el primer proceso listo.

        Process next = null;
        for (Process process : processesList) {
            if(process.getStatus() == Status.READY)
            {
                next = process;
                break;
            }
        }

        if(next == null)
        {
            System.out.println("ERROR: Se intentó despachar y el scheduler no tiene procesos listos. \n\n");
            return;
        }
        else
        {
            removeProcess(next);
            runningProcess = next;
            next.run(next.getTimeRequired());
            next.returnAllResources(this); //Devuelve los recursos que tenía (agregue esto y creo q arregla el problema de los recursos que no se devuelven)
            runningProcess = null;

            if (next.getStatus() == Status.BLOCKED) 
            {
                blockedProcessesList.add(next); // Lo pone en la lista de bloqueados si se bloqueó.
                addProcess(next);
            } 
        }
    }

    @Override
    public void addProcess(Process proc) {
        if(!processesList.contains(proc))
        {
            //Le intentá dar los recursos que precisa.
            if(resourcesList.containsAll(proc.getResNeeded()))
            {
                for (Resource res : proc.getResNeeded()) {
                    if(!proc.getResAvaliables().contains(res))
                    {
                        proc.getResAvaliables().add(res);
                        resourcesList.remove(res);
                    }
                }


            }




            processesList.add(proc);
            System.out.println("Se agregó el proceso " + proc.getName() + " al scheduler con éxito. \n\n");
        }
        else
        {
            System.out.println("No se pudo añadir el proceso " + proc.getName() + " porque ya estaba en el scheduler. \n\n");
        }
    }

    public void removeProcess(Process proc)
    {
        if(processesList.removeFirstOccurrence(proc))
        {
            System.out.println("Se removió el proceso " + proc.getName() + " del scheduler \n\n");
        }
        else
        {
            System.out.println("El proceso " + proc.getName() + " no estaba en el scheduler \n\n");
        } 
    }

    public void addResource(Resource resource)
    {
        boolean puedeIngresar = true;
        Resource resMismoID = null;
        for (Resource res : resourcesList) {
            if(resource.getID() == res.getID())
            {
                puedeIngresar = false;
                resMismoID = res;
            }
        }
        if(puedeIngresar)
        {
            resourcesList.add(resource);
            System.out.println("Se agregó el recurso " + resource.getName() + " al scheduler con éxito. \n\n");
        }
        else
        {
            System.out.println("No se pudo añadir el recurso " + resource.getName() + " porque tiene el mismo ID que el recurso " + resMismoID.getName() + ". \n\n");
        }
        
    }

    public void removeResource(Resource res)
    {
        if(resourcesList.removeFirstOccurrence(res))
        {
            System.out.println("Se removió el recurso " + res.getName() + " del scheduler \n\n");
        }
        else
        {
            System.out.println("El recurso " + res.getName() + " no estaba en el scheduler \n\n");
        }
    }
}
