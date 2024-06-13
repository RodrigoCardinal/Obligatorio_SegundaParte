package com.example;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.LinkedList;

public class ResourceTest {

    @Test
    public void testResourceCreation() {
        Resource resource = new Resource(1, "Test Resource");
        assertEquals(1, resource.getID());
        assertEquals("Test Resource", resource.getName());
        assertNull(resource.getOwner());
    }

    @Test
    public void testResourceOwnerAssignment() {
        Resource resource = new Resource(2, "Another Resource");
        Process process = new Process("Test Process", 10, 0, new LinkedList<Resource>());
        resource.setOwner(process);
        assertEquals(process, resource.getOwner());
    }

    @Test
    public void testResourceOwnerRelease() {
        Resource resource = new Resource(3, "Resource to Release");
        Process process = new Process("Test Process", 10, 0, new LinkedList<Resource>());
        resource.setOwner(process);
        resource.setOwner(null);
        assertNull(resource.getOwner());
    }
}
