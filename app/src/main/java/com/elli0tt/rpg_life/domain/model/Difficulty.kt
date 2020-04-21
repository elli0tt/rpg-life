package com.elli0tt.rpg_life.domain.model

enum class Difficulty(val xpIncrease: Int, val xpDecrease: Int, val procrastinationIncrease: Int, val procrastinationDecrease: Int) {
    VERY_EASY(100, 400, 1, 2),
    EASY(500, 800, 2, 4),
    NORMAL(1000, 2000, 3, 6),
    HARD(2000, 4000, 5, 10),
    VERY_HARD(5000, 20000, 10, 20),
    IMPOSSIBLE(10000, 60000, 30, 60),
    NOT_SET(0, 0, 0, 0);
}