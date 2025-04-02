package org.springframework.samples.petclinic.customers.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link PetType}.
 */
public class PetTypeTest {

    private PetType petType;
    private static final Integer TEST_ID = 1;
    private static final String TEST_NAME = "Dog";

    @BeforeEach
    void setUp() {
        petType = new PetType();
    }

    @Test
    void testGetAndSetId() {
        // Verify initial state
        assertNull(petType.getId(), "Id should be null initially");

        // Set and verify id
        petType.setId(TEST_ID);
        assertEquals(TEST_ID, petType.getId(), "Id should match the value we set");
    }

    @Test
    void testGetAndSetName() {
        // Verify initial state
        assertNull(petType.getName(), "Name should be null initially");

        // Set and verify name
        petType.setName(TEST_NAME);
        assertEquals(TEST_NAME, petType.getName(), "Name should match the value we set");
    }

    @Test
    void testSetNameWithNullValue() {
        // Set name initially
        petType.setName(TEST_NAME);

        // Set name to null
        petType.setName(null);
        assertNull(petType.getName(), "Name should be null after setting to null");
    }

    @Test
    void testSetNameWithEmptyString() {
        // Set name to empty string
        petType.setName("");
        assertEquals("", petType.getName(), "Name should be empty string after setting to empty string");
    }
}
