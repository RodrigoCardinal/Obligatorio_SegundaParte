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
        Scheduler prueba = new Scheduler("RR", 5);
        prueba.AddProcess(proc1);
        prueba.Start();
    }
}
