package com.example;

public interface IScheduler {

    
    public void Start() throws InterruptedException;
    public void End();
    public boolean DispatchNext() throws InterruptedException;
    public void AddProcess(Process proc);


}
