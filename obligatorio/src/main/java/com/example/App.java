package com.example;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws InterruptedException
    {
        Process proc1 = new Process("Mortal Kombat X", 12, 0);
        Process proc2 = new Process("Angry Birds", 6, 0);
        Scheduler prueba = new Scheduler("FIFO", 5);
        prueba.AddProcess(proc1);
        prueba.AddProcess(proc2);
        prueba.Start();
    }
}
