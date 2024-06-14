package com.example;

public interface IScheduler {
    public void start() throws InterruptedException;
    public void end();
    public boolean dispatchNext() throws InterruptedException;
    public void addProcess(Process proc);
}
