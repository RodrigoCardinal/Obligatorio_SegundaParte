package com.example;

import java.util.LinkedList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ProcessTest {

    @Test
    public void testProcessCreation() {
        LinkedList<Resource> resources = new LinkedList<>();
        resources.add(new Resource(1, "Resource1"));
        Process process = new Process("Test Process", 10, 0, resources);
        assertEquals("Test Process", process.getName());
        assertEquals(10, process.getTimeRequired());
        assertEquals(0, process.getContext());
        assertEquals(Status.READY, process.getStatus());
        assertEquals(resources, process.getResNeeded());
    }

    @Test
    public void testProcessContext() {
        Process process = new Process("Test Process", 10, 0, new LinkedList<Resource>());
        process.setContext(50);
        assertEquals(50, process.getContext());
    }

    @Test
    public void testProcessStatus() {
        Process process = new Process("Test Process", 10, 0, new LinkedList<Resource>());
        process.setStatus(Status.RUNNING);
        assertEquals(Status.RUNNING, process.getStatus());
    }
        @Test
    public void testSetOwner() {
        Resource resource = new Resource(1, "Recurso1");
        Process process = new Process("Proceso1", 10, 0, new LinkedList<Resource>());
        
        resource.setOwner(process);
        assertEquals(process, resource.getOwner());

        resource.setOwner(null);
        assertNull(resource.getOwner());
    }

    @Test
    public void testSetOwnerTwice() {
        Resource resource = new Resource(1, "Recurso1");
        Process process1 = new Process("Proceso1", 10, 0, new LinkedList<Resource>());
        Process process2 = new Process("Proceso2", 5, 0, new LinkedList<Resource>());
        
        resource.setOwner(process1);
        assertEquals(process1, resource.getOwner());

        resource.setOwner(process2);
        assertEquals(process2, resource.getOwner());
    }
    @Test
    public void testProcessRun() throws InterruptedException {
        LinkedList<Resource> resources = new LinkedList<>();
        resources.add(new Resource(1, "Recurso1"));
        Process process = new Process("Proceso1", 5, 0, resources);
        process.getResAvaliables().addAll(resources);
        
        process.run(3);
        assertEquals(2, process.getTimeRequired());
        assertEquals(Status.READY, process.getStatus());

        process.run(2);
        assertEquals(0, process.getTimeRequired());
        assertEquals(Status.FINISHED, process.getStatus());
    }

    @Test
    public void testProcessBlocking() throws InterruptedException {
        LinkedList<Resource> resources = new LinkedList<>();
        resources.add(new Resource(1, "Recurso1"));
        Process process = new Process("Proceso1", 5, 0, resources);

        process.run(3);
        assertEquals(Status.BLOCKED, process.getStatus());
    }

    @Test
    public void testProcessRunWithResources() throws InterruptedException {
        LinkedList<Resource> resources = new LinkedList<>();
        Resource resource = new Resource(1, "Resource1");
        resources.add(resource);
        Process process = new Process("Test Process", 5, 0, resources);
        process.getResAvaliables().add(resource);
        process.run(3);
        assertEquals(Status.READY, process.getStatus());
        assertEquals(2, process.getTimeRequired());
    }

    @Test
    public void testProcessRunWithoutResources() throws InterruptedException {
        LinkedList<Resource> resources = new LinkedList<>();
        resources.add(new Resource(1, "Resource1"));
        Process process = new Process("Test Process", 5, 0, resources);
        process.run(3);
        assertEquals(Status.BLOCKED, process.getStatus());
        assertEquals(5, process.getTimeRequired());
    }

    @Test
    public void testRunExactTimeRequired() throws InterruptedException {
        LinkedList<Resource> resources = new LinkedList<>();
        resources.add(new Resource(1, "Recurso1"));
        Process process = new Process("Proceso1", 5, 0, resources);
        process.getResAvaliables().addAll(resources);

        process.run(5);
        assertEquals(Status.FINISHED, process.getStatus());
        assertEquals(0, process.getTimeRequired());
    }

    @Test
    public void testRunWithMoreTimeThanRequired() throws InterruptedException {
        LinkedList<Resource> resources = new LinkedList<>();
        resources.add(new Resource(1, "Recurso1"));
        Process process = new Process("Proceso1", 5, 0, resources);
        process.getResAvaliables().addAll(resources);

        process.run(10);
        assertEquals(Status.FINISHED, process.getStatus());
        assertEquals(0, process.getTimeRequired());
    }
}
