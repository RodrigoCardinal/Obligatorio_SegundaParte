package com.example;

import java.util.LinkedList;

public class Scheduler implements IScheduler{

    Process runningProcess;
    LinkedList<Process> processesList;
    //LinkedList<Resource> resourcesList;
    String schedullingPolicy;

    @Override
    public void Start() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'Start'");
    }

    @Override
    public void End() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'End'");
    }

    @Override
    public void DispatchNext()
    {   
        switch(schedullingPolicy)
        {
            case "RR":
                RoundRobinDispatch();
                break;
            case "FIFO":
                FIFODispatch();
                break;
            default:
                System.out.println("Invalid Schedulling Policy");
        }
        
    }

    @Override
    public void AddProcess(Process proc) {
        processesList.add(proc);
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

    /* 
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
    */
    
    public void FIFODispatch()
    {
        Process next = processesList.getFirst();
    }

    public void RoundRobinDispatch()
    {

    }

}
