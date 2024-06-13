package com.example;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.LinkedList;

public class SchedulerTest {

    @Test
    public void testSchedulerCreation() {
        Scheduler scheduler = new Scheduler("FIFO", 5);
        assertEquals("FIFO", scheduler.schedullingPolicy);
        assertEquals(5, scheduler.timeout);
        assertFalse(scheduler.isActive);
    }

    @Test
    public void testAddProcess() {
        Scheduler scheduler = new Scheduler("FIFO", 5);
        Process process = new Process("Test Process", 10, 0, new LinkedList<Resource>());
        scheduler.AddProcess(process);
        assertTrue(scheduler.processesList.contains(process));
    }

    @Test
    public void testAddResource() {
        Scheduler scheduler = new Scheduler("FIFO", 5);
        Resource resource = new Resource(1, "Test Resource");
        scheduler.addResource(resource);
        assertTrue(scheduler.resourcesList.contains(resource));
    }

/*     @Test
    public void testDispatchNext() throws InterruptedException {
        Scheduler scheduler = new Scheduler("FIFO", 5);
        LinkedList<Resource> resources = new LinkedList<>();
        Resource resource = new Resource(1, "Resource1");
        resources.add(resource);
        Process process = new Process("Test Process", 10, 0, resources);
        
        scheduler.addResource(resource);  // Add the necessary resource to scheduler
        scheduler.AddProcess(process);  // Add the process to scheduler
        
        // Ensure the process has the resource available
        process.getResAvaliables().add(resource);
        process.setStatus(Status.READY);  // Explicitly set process status to READY
        
        boolean dispatched = scheduler.DispatchNext();
        assertTrue(dispatched);
        assertEquals(process, scheduler.runningProcess);
    } */

    @Test
    public void testStartScheduler() throws InterruptedException {
        Scheduler scheduler = new Scheduler("FIFO", 5);
        LinkedList<Resource> resources = new LinkedList<>();
        Resource resource1 = new Resource(1, "Resource1");
        Resource resource2 = new Resource(2, "Resource2");
        resources.add(resource1);
        resources.add(resource2);
        Process process1 = new Process("Test Process 1", 10, 0, resources);
        Process process2 = new Process("Test Process 2", 5, 0, new LinkedList<Resource>());
        
        scheduler.addResource(resource1);
        scheduler.addResource(resource2);
        scheduler.AddProcess(process1);
        scheduler.AddProcess(process2);
        
        // Ensure the process1 has the resources available
        process1.getResAvaliables().add(resource1);
        process1.getResAvaliables().add(resource2);
        process1.setStatus(Status.READY);
        process2.setStatus(Status.READY);
        
        scheduler.Start();
        
        assertEquals(Status.FINISHED, process1.getStatus());
        assertEquals(Status.FINISHED, process2.getStatus());
        assertFalse(scheduler.isActive);
    }
}
