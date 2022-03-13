package com.yterletskyi.happy_friend.features.ideas.di

import com.yterletskyi.happy_friend.features.ideas.data.IdeasDataSource
import com.yterletskyi.happy_friend.features.ideas.domain.IdeasInteractor
import com.yterletskyi.happy_friend.features.ideas.domain.IdeasInteractorImpl
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
    ): IdeasInteractor = IdeasInteractorImpl(ideasDataSource)

}
