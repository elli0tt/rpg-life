package com.elli0tt.rpg_life.presentation.worker

import android.content.Context
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.elli0tt.rpg_life.data.repository.QuestsRepositoryImpl

internal class InsertEmptySubQuestWorker(private val context: Context,
                                         workerParams: WorkerParameters
) : Worker(context, workerParams) {
    override fun doWork(): Result {
        val questsRepository = QuestsRepositoryImpl(context)

        val parentQuestId = inputData.getInt(Constants.KEY_PARENT_QUEST_ID, 0)
        val outputData = Data.Builder()
                .putInt(Constants.KEY_QUEST_ID, questsRepository.insertEmptySubQuestSync(parentQuestId).toInt())
                .build()
        return Result.success(outputData)
    }
}