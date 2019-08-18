package com.elli0tt.rpg_life.domain.interactor;

import com.elli0tt.rpg_life.domain.repository.CharacteristicsRepository;

public class CharacteristicsInteractor {
    private CharacteristicsRepository characteristicsRepository;

    public CharacteristicsInteractor(CharacteristicsRepository characteristicsRepository) {
        this.characteristicsRepository = characteristicsRepository;
    }


}
