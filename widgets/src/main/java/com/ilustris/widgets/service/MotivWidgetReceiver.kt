package com.ilustris.widgets.service

import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import com.ilustris.motiv.base.service.QuoteService
import com.ilustris.motiv.base.service.StyleService
import com.ilustris.motiv.base.service.UserService
import com.ilustris.motiv.base.service.helper.QuoteHelper
import com.ilustris.widgets.ui.MotivWidget
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MotivWidgetReceiver : GlanceAppWidgetReceiver() {

    private var quoteService: QuoteService = QuoteService()
    private var styleService = StyleService()
    private var userService = UserService()
    private var quoteHelper: QuoteHelper = QuoteHelper(userService, styleService)

    override val glanceAppWidget: GlanceAppWidget = MotivWidget(quoteService, quoteHelper)

}