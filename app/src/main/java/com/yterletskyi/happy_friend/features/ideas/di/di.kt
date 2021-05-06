package com.yterletskyi.happy_friend.features.ideas.di

import com.yterletskyi.happy_friend.features.ideas.data.IdeasDataSource
import com.yterletskyi.happy_friend.features.ideas.model.IdeasInteractor
import com.yterletskyi.happy_friend.features.ideas.model.IdeasInteractorImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent

@Module
@InstallIn(FragmentComponent::class)
class IdeasDi {

    @Provides
    fun provideIdeasInteractor(
        ideasDataSource: IdeasDataSource
    ): IdeasInteractor {
        return IdeasInteractorImpl(ideasDataSource)
    }

}
