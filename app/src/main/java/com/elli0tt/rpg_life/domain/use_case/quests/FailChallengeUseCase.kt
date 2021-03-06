package com.elli0tt.rpg_life.domain.use_case.quests

import com.elli0tt.rpg_life.domain.repository.QuestsRepository
import com.elli0tt.rpg_life.domain.repository.SkillsRepository
import javax.inject.Inject

class FailChallengeUseCase @Inject constructor(
        private val skillsRepository: SkillsRepository,
        private val questsRepository: QuestsRepository
) {
    fun invoke(challengeId: Int, dayNumber: Int, startXp: Int) {
        object : Thread() {
            override fun run() {
                super.run()
                val relatedSkills = questsRepository.getRelatedSkills(challengeId)
                for (relatedSkill in relatedSkills) {
                    val xpDecrease: Long = (startXp.toLong() * dayNumber + dayNumber * (dayNumber - 1) / 2 * 10) / 2
                    skillsRepository.updateSkillTotalXpById(relatedSkill.skillId, -xpDecrease)
                }
            }
        }.start()
    }
}
