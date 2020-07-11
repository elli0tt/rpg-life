package com.elli0tt.rpg_life.presentation.screen.skills

import android.content.Context
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.elli0tt.rpg_life.data.repository.SkillsRepositoryImpl

class InsertEmptySkillWorker(private val context: Context, workerParameters: WorkerParameters)
    : Worker(context, workerParameters) {
    override fun doWork(): Result {
        val skillsRepository = SkillsRepositoryImpl(context)
        val outputData = Data.Builder()
                .putInt(Constants.KEY_SKILL_ID, skillsRepository.insertEmptySkill().toInt())
                .build()
        return Result.success(outputData)
    }
}