package com.yterletskyi.happyfriend.features.ideas.di

import com.yterletskyi.happyfriend.features.ideas.data.IdeasDataSource
import com.yterletskyi.happyfriend.features.ideas.domain.IdeasInteractor
import com.yterletskyi.happyfriend.features.ideas.domain.IdeasInteractorImpl
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
