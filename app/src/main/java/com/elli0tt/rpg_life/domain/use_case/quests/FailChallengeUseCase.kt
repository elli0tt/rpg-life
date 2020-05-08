package com.elli0tt.rpg_life.domain.use_case.quests

import com.elli0tt.rpg_life.domain.repository.QuestsRepository
import com.elli0tt.rpg_life.domain.repository.SkillsRepository
import com.elli0tt.rpg_life.domain.use_case.quests.load_data.GetRelatedSkillsUseCase
import com.elli0tt.rpg_life.domain.use_case.skills.update_data.UpdateSkillTotalXpByIdUseCase

class FailChallengeUseCase(private val skillsRepository: SkillsRepository, private val questsRepository: QuestsRepository) {
    private val updateSkillTotalXpByIdUseCase: UpdateSkillTotalXpByIdUseCase = UpdateSkillTotalXpByIdUseCase(skillsRepository)
    private val getRelatedSkillsUseCase = GetRelatedSkillsUseCase(questsRepository)

    fun invoke(challengeId: Int, dayNumber: Int, startXp: Int) {

        object : Thread() {
            override fun run() {
                super.run()
                val relatedSkills = getRelatedSkillsUseCase.invoke(challengeId)
                for (relatedSkill in relatedSkills){
                    val xpDecrease: Long = (startXp.toLong() * dayNumber + dayNumber * (dayNumber - 1) / 2 * 10) / 2;
                    updateSkillTotalXpByIdUseCase.invoke(relatedSkill.skillId, -xpDecrease);
                }
            }
        }.start()
    }
}
