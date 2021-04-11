package com.elli0tt.rpg_life.di

import android.content.Context
import com.elli0tt.rpg_life.di.modules.AppModule
import com.elli0tt.rpg_life.di.modules.RepositoryModule
import com.elli0tt.rpg_life.di.modules.ViewModelModule
import com.elli0tt.rpg_life.di.modules.WorkManagerModule
import com.elli0tt.rpg_life.presentation.screen.add_category_to_skill.di.AddCategoryToSkillComponent
import com.elli0tt.rpg_life.presentation.screen.add_category_to_skill.di.AddCategoryToSkillModule
import com.elli0tt.rpg_life.presentation.screen.add_edit_challenge.di.AddEditChallengeComponent
import com.elli0tt.rpg_life.presentation.screen.add_edit_challenge.di.AddEditChallengeModule
import com.elli0tt.rpg_life.presentation.screen.add_edit_characteristic.di.AddEditCharacteristicComponent
import com.elli0tt.rpg_life.presentation.screen.add_edit_characteristic.di.AddEditCharacteristicModule
import com.elli0tt.rpg_life.presentation.screen.add_edit_quest.di.AddEditQuestComponent
import com.elli0tt.rpg_life.presentation.screen.add_edit_quest.di.AddEditQuestModule
import com.elli0tt.rpg_life.presentation.screen.add_edit_skill.di.AddEditSkillComponent
import com.elli0tt.rpg_life.presentation.screen.add_edit_skill.di.AddEditSkillModule
import com.elli0tt.rpg_life.presentation.screen.add_edit_skill_category.di.AddEditSkillsCategoryComponent
import com.elli0tt.rpg_life.presentation.screen.add_edit_skill_category.di.AddEditSkillsCategoryModule
import com.elli0tt.rpg_life.presentation.screen.add_skills_to_quest.di.AddSkillsToQuestComponent
import com.elli0tt.rpg_life.presentation.screen.add_skills_to_quest.di.AddSkillsToQuestModule
import com.elli0tt.rpg_life.presentation.screen.character.di.CharacterComponent
import com.elli0tt.rpg_life.presentation.screen.character.di.CharacterModule
import com.elli0tt.rpg_life.presentation.screen.countdown_timer.di.CountDownComponent
import com.elli0tt.rpg_life.presentation.screen.countdown_timer.di.CountDownModule
import com.elli0tt.rpg_life.presentation.screen.quests.di.QuestsComponent
import com.elli0tt.rpg_life.presentation.screen.quests.di.QuestsModule
import com.elli0tt.rpg_life.presentation.screen.rewards_shop.di.RewardsShopComponent
import com.elli0tt.rpg_life.presentation.screen.rewards_shop.di.RewardsShopModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AppModule::class,
    RepositoryModule::class,
    ViewModelModule::class,
    WorkManagerModule::class,
    AddCategoryToSkillModule::class,
    AddEditChallengeModule::class,
    AddEditCharacteristicModule::class,
    AddEditQuestModule::class,
    AddEditSkillModule::class,
    AddEditSkillsCategoryModule::class,
    AddSkillsToQuestModule::class,
    CharacterModule::class,
    CountDownModule::class,
    QuestsModule::class,
    RewardsShopModule::class
])
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }

    fun addCategoryToSkillComponentFactory(): AddCategoryToSkillComponent.Factory
    fun addEditChallengeComponentFactory(): AddEditChallengeComponent.Factory
    fun addEditCharacteristicComponentFactory(): AddEditCharacteristicComponent.Factory
    fun addEditQuestComponentFactory(): AddEditQuestComponent.Factory
    fun addEditSkillComponentFactory(): AddEditSkillComponent.Factory
    fun addEditSkillCategoryComponentFactory(): AddEditSkillsCategoryComponent.Factory
    fun addSkillsToQuestComponentFactory(): AddSkillsToQuestComponent.Factory
    fun characterComponentFactory(): CharacterComponent.Factory
    fun countDownComponentFactory(): CountDownComponent.Factory
    fun questsComponentFactory(): QuestsComponent.Factory
    fun rewardsShopComponentFactory(): RewardsShopComponent.Factory
}