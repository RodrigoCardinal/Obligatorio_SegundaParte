package com.example;

import java.util.LinkedList;

import static org.junit.Assert.assertEquals;
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
}
