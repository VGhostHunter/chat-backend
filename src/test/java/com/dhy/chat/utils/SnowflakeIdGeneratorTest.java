package com.dhy.chat.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SnowflakeIdGeneratorTest {

    private IdGenerator idGenerator;

    @BeforeEach
    public void setup() {
        idGenerator = new SnowflakeIdGenerator();
    }

    @Test
    void getId() {
        for (int i = 0; i < 20; i++) {
            new Thread(() -> {
                for (int j = 0; j < 10000; j++) {
                    long id = idGenerator.getId();
                    System.out.println(id);
                }
            }).start();
        }
    }
}