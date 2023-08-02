package com.creat.motiv

import android.content.Context
import com.firebase.ui.auth.AuthUI
import com.ilustris.motiv.base.service.CoverService
import com.ilustris.motiv.base.service.IconService
import com.ilustris.motiv.base.service.PreferencesService
import com.ilustris.motiv.base.service.QuoteService
import com.ilustris.motiv.base.service.RadioService
import com.ilustris.motiv.base.service.StyleService
import com.ilustris.motiv.base.service.UserService
import com.ilustris.motiv.base.service.helper.QuoteHelper
import com.ilustris.motiv.base.service.helper.RadioHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {


    @Singleton
    @Provides
    fun providesPreferencesService(@ApplicationContext context: Context) =
        PreferencesService(context)

    @Provides
    fun providesRadioHelper(
        @ApplicationContext context: Context,
        preferencesService: PreferencesService
    ) = RadioHelper(context)

    @Provides
    fun providesQuoteService() = QuoteService()

    @Provides
    fun providesRadioService(preferencesService: PreferencesService, radioHelper: RadioHelper) =
        RadioService(preferencesService, radioHelper)

    @Provides
    fun providesUserService() = UserService()

    @Provides
    fun providesIconService() = IconService()

    @Provides
    fun providesCoverService() = CoverService()

    @Provides
    fun providesStyleService() = StyleService()

    @Provides
    fun providesQuoteHelper(userService: UserService, styleService: StyleService) =
        QuoteHelper(userService, styleService)


    val loginProviders = listOf(
        AuthUI.IdpConfig.GoogleBuilder().build(),
        AuthUI.IdpConfig.EmailBuilder().build()
    )

}