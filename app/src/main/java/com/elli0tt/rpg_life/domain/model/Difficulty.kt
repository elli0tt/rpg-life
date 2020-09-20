package com.elli0tt.rpg_life.domain.model

enum class Difficulty(val xpIncrease: Int, val coins: Int) {
    VERY_EASY(100, 1),
    EASY(250, 3),
    NORMAL(500, 5),
    HARD(1000, 10),
    VERY_HARD(2000, 20),
    IMPOSSIBLE(10000, 100),
    NOT_SET(0, 0);
}