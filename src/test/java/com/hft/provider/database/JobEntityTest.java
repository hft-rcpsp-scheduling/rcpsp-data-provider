package com.hft.provider.database;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JobEntityTest {

    @Test
    void setSuccessors() {
        JobEntity entity = new JobEntity();
        entity.setSuccessors(List.of(1, 2, 3));
        assertEquals(List.of(1, 2, 3), entity.getSuccessors());
    }

    @Test
    void setPredecessors() {
        JobEntity entity = new JobEntity();
        entity.setPredecessors(List.of(1, 2, 3));
        assertEquals(List.of(1, 2, 3), entity.getPredecessors());
    }
}