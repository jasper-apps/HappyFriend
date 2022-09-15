package com.jasperapps.happyfriend.features.ideas.di

import com.jasperapps.happyfriend.features.ideas.data.IdeasDataSource
import com.jasperapps.happyfriend.features.ideas.domain.IdeasInteractor
import com.jasperapps.happyfriend.features.ideas.domain.IdeasInteractorImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class IdeasDi {

    @Provides
    fun provideIdeasInteractor(
        ideasDataSource: IdeasDataSource
    ): IdeasInteractor = IdeasInteractorImpl(ideasDataSource)
}
