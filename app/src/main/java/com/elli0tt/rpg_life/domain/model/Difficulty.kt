package com.elli0tt.rpg_life.domain.model

enum class Difficulty(val xpIncrease: Int, val coins: Int) {
    VERY_EASY(100, 1),
    EASY(500, 5),
    NORMAL(1000, 10),
    HARD(2000, 20),
    VERY_HARD(5000, 50),
    IMPOSSIBLE(10000, 100),
    NOT_SET(0, 0);
}