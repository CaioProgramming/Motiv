@file:OptIn(ExperimentalAnimationApi::class)

package com.ilustris.widgets.ui

import android.content.Context
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.glance.GlanceId
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import com.ilustris.motiv.base.data.model.Quote
import com.ilustris.motiv.base.data.model.QuoteDataModel
import com.ilustris.motiv.base.service.QuoteService
import com.ilustris.motiv.base.service.helper.QuoteHelper
import com.ilustris.widgets.ui.component.MotivWidgetLoader
import com.ilustris.widgets.ui.component.QuoteCardWidget
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MotivWidget(private val quoteService: QuoteService, private val quoteHelper: QuoteHelper) :
    GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            var randomQuote by remember {
                mutableStateOf<QuoteDataModel?>(null)
            }
            val coroutineScope = rememberCoroutineScope()


            if (randomQuote == null) {
                MotivWidgetLoader()
            } else {
                QuoteCardWidget(quoteDataModel = randomQuote!!)
            }

            suspend fun mapQuote(quote: Quote) {
                coroutineScope.launch {
                    quoteHelper.mapQuoteToQuoteDataModel(quote).run {
                        if (isSuccess) {
                            randomQuote = this.success.data
                        }
                    }
                }

            }

            fun fetchRandomQuote() {
                runBlocking {
                    launch {
                        quoteService.getAllData(10, "data").run {
                            if (isSuccess) {
                                mapQuote((success.data as List<Quote>).random())
                            }
                        }
                    }
                }
            }



            LaunchedEffect(Unit) {
                if (randomQuote == null) {
                    fetchRandomQuote()
                }
            }
        }
    }


}