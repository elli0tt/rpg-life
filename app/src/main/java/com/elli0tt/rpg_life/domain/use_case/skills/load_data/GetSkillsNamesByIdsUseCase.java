package com.elli0tt.rpg_life.domain.use_case.skills.load_data;

import com.elli0tt.rpg_life.domain.repository.SkillsRepository;

import java.util.List;

public class GetSkillsNamesByIdsUseCase {
    private SkillsRepository repository;

    public GetSkillsNamesByIdsUseCase(SkillsRepository repository) {
        this.repository = repository;
    }

    public List<String> invoke(List<Integer> ids) {
        return repository.getSkillsNamesByIds(ids);
    }
}
