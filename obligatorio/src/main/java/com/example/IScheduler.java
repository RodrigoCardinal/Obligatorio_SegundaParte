package com.example;

public interface IScheduler {

    
    public void Start();
    public void End();
    public void AddProcess(Process proc);
    public void SuspendProcess(Process proc);
    public void ResumeProcess(Process proc);
    public void KillProcess(Process proc);
    //public void GiveResource(Resource res);
    //public void TakeResource(Resource res);


}
