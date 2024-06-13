package com.example;

import java.util.LinkedList;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws InterruptedException
    {
        Resource cd = new Resource(1, "MKX-CD");
        Resource samsung = new Resource(2, "Samsung Galaxy S7");
        Resource mando = new Resource(3, "Dualshock PS4");

        LinkedList<Resource> resMK = new LinkedList<>();
        resMK.add(cd);
        resMK.add(mando);

        LinkedList<Resource> resAngryBirds = new LinkedList<>();
        resAngryBirds.add(samsung);

        Process proc1 = new Process("Mortal Kombat X", 12, 0, resMK);
        Process proc2 = new Process("Angry Birds", 6, 0, resAngryBirds);
        
        Scheduler prueba = new Scheduler("FIFO", 5);
        prueba.AddProcess(proc1);
        prueba.AddProcess(proc2);
        prueba.addResource(cd);
        prueba.addResource(samsung);
        prueba.addResource(mando);
        prueba.Start();
    }
}


