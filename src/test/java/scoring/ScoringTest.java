package com.example.scoringstrategy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class ScoringTest {
    @Test
    public void correctAdds() {
        CorrectAnswer s = new CorrectAnswer();
        assertEquals(100, s.calculateScore(100));
    }
    @Test
    public void wrongSubtracts() {
        WrongAnswer s = new WrongAnswer();
        assertEquals(-50, s.calculateScore(50));
    }
}
