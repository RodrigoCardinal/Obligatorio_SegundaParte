package com.example;

public interface IScheduler {

    
    public void Start() throws InterruptedException;
    public void End();
    public boolean DispatchNext() throws InterruptedException;
    public void AddProcess(Process proc);
    public void SuspendProcess(Process proc);
    public void ResumeProcess(Process proc);
    public void KillProcess(Process proc);
    //public void GiveResource(Resource res);
    //public void TakeResource(Resource res);


}
