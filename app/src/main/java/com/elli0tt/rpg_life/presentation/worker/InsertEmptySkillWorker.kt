package com.elli0tt.rpg_life.presentation.worker

import android.content.Context
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.elli0tt.rpg_life.data.repository.SkillsRepositoryImpl
import com.elli0tt.rpg_life.presentation.screen.skills.SkillsConstants

class InsertEmptySkillWorker(private val context: Context, workerParameters: WorkerParameters)
    : Worker(context, workerParameters) {
    override fun doWork(): Result {
        val skillsRepository = SkillsRepositoryImpl(context)
        val outputData = Data.Builder()
                .putInt(SkillsConstants.KEY_SKILL_ID, skillsRepository.insertEmptySkill().toInt())
                .build()
        return Result.success(outputData)
    }
}