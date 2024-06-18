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
        scheduler.addProcess(process);
        assertTrue(scheduler.processesList.contains(process));
    }

    @Test
    public void testAddResource() {
        Scheduler scheduler = new Scheduler("FIFO", 5);
        Resource resource = new Resource(1, "Test Resource");
        scheduler.addResource(resource);
        assertTrue(scheduler.resourcesList.contains(resource));
    }

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
        scheduler.addProcess(process1);
        scheduler.addProcess(process2);
        
        // Ensure the process1 has the resources available
        process1.getResAvaliables().add(resource1);
        process1.getResAvaliables().add(resource2);
        process1.setStatus(Status.READY);
        process2.setStatus(Status.READY);
        
        scheduler.start();
        
        assertEquals(Status.FINISHED, process1.getStatus());
        assertEquals(Status.FINISHED, process2.getStatus());
        assertFalse(scheduler.isActive);
    }

    @Test
    public void testFIFOExecution() throws InterruptedException {
        Scheduler scheduler = new Scheduler("FIFO", 1);
        LinkedList<Resource> resources = new LinkedList<>();
        resources.add(new Resource(1, "Recurso1"));
        
        Process process1 = new Process("Proceso1", 3, 0, resources);
        Process process2 = new Process("Proceso2", 2, 0, resources);
        
        scheduler.resourcesList.addAll(resources);
        scheduler.addProcess(process1);
        scheduler.addProcess(process2);
        
        scheduler.start();
        
        assertEquals(Status.FINISHED, process1.getStatus());
        assertEquals(Status.FINISHED, process2.getStatus());
    }

    @Test
    public void testRRExecution() throws InterruptedException {
        Scheduler scheduler = new Scheduler("RR", 1);
        LinkedList<Resource> resources = new LinkedList<>();
        resources.add(new Resource(1, "Recurso1"));
        
        Process process1 = new Process("Proceso1", 3, 0, resources);
        Process process2 = new Process("Proceso2", 2, 0, resources);
        
        scheduler.resourcesList.addAll(resources);
        scheduler.addProcess(process1);
        scheduler.addProcess(process2);
        
        scheduler.start();
        
        assertEquals(Status.FINISHED, process1.getStatus());
        assertEquals(Status.FINISHED, process2.getStatus());
    }

    @Test
    public void testRRExecutionWithTimeSlice() throws InterruptedException {
        Scheduler scheduler = new Scheduler("RR", 1);
        LinkedList<Resource> resources = new LinkedList<>();
        resources.add(new Resource(1, "Recurso1"));
        
        Process process1 = new Process("Proceso1", 5, 0, resources);
        Process process2 = new Process("Proceso2", 7, 0, resources);
        
        scheduler.resourcesList.addAll(resources);
        scheduler.addProcess(process1);
        scheduler.addProcess(process2);
        
        scheduler.start();
        
        assertEquals(Status.FINISHED, process1.getStatus());
        assertEquals(Status.FINISHED, process2.getStatus());
    }

    @Test
    public void testRRExecutionWithDifferentTimeSlices() throws InterruptedException {
        Scheduler scheduler = new Scheduler("RR", 2);
        LinkedList<Resource> resources = new LinkedList<>();
        resources.add(new Resource(1, "Recurso1"));
        
        Process process1 = new Process("Proceso1", 5, 0, resources);
        Process process2 = new Process("Proceso2", 7, 0, resources);
        
        scheduler.resourcesList.addAll(resources);
        scheduler.addProcess(process1);
        scheduler.addProcess(process2);
        
        scheduler.start();
        
        assertEquals(Status.FINISHED, process1.getStatus());
        assertEquals(Status.FINISHED, process2.getStatus());
    }

    @Test
    public void testProcessBlockingAndUnblocking() throws InterruptedException {
        LinkedList<Resource> resourcesNeeded = new LinkedList<>();
        Resource resource = new Resource(1, "Recurso1");
        resourcesNeeded.add(resource);

        Scheduler scheduler = new Scheduler("FIFO", 1);
        Process process1 = new Process("Proceso1", 5, 0, resourcesNeeded);
        Process process2 = new Process("Proceso2", 5, 0, new LinkedList<Resource>());

        scheduler.addProcess(process1);
        scheduler.addProcess(process2);

        // Manually blocking process1
        scheduler.blockedProcessesList.add(process1);
        process1.setStatus(Status.BLOCKED);

        // Making resource available and running scheduler to unblock process1
        scheduler.resourcesList.add(resource);
        scheduler.start();

        assertEquals(Status.FINISHED, process1.getStatus());
        assertEquals(Status.FINISHED, process2.getStatus());
    }

    /*     @Test
    public void testExecutionWithNoProcesses() throws InterruptedException {
        Scheduler scheduler = new Scheduler("FIFO", 1);
        scheduler.start();
        assertTrue(scheduler.processesList.isEmpty());
    }  */

    @Test
    public void testExecutionWithNoResources() throws InterruptedException {
        Scheduler scheduler = new Scheduler("FIFO", 1);
        Process process = new Process("Proceso1", 3, 0, new LinkedList<Resource>());
        scheduler.addProcess(process);
        
        scheduler.start();
        assertEquals(Status.FINISHED, process.getStatus());
    }

    /*     @Test 
    public void testExecutionWithBlockedProcess() throws InterruptedException {
        Scheduler scheduler = new Scheduler("FIFO", 1);
        LinkedList<Resource> resources = new LinkedList<>();
        resources.add(new Resource(1, "Recurso1"));
        
        Process process1 = new Process("Proceso1", 3, 0, resources);
        Process process2 = new Process("Proceso2", 2, 0, new LinkedList<Resource>());
        
        scheduler.addProcess(process1);
        scheduler.addProcess(process2);
        
        scheduler.start();
        
        assertEquals(Status.BLOCKED, process1.getStatus());
        assertEquals(Status.FINISHED, process2.getStatus());
    } */

    @Test
    public void testSchedulerEnd() throws InterruptedException {
        Scheduler scheduler = new Scheduler("FIFO", 1);
        scheduler.end();
        assertFalse(scheduler.isActive);
    }

}
