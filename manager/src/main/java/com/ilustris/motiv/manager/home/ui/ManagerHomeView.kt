@file:OptIn(ExperimentalFoundationApi::class)

package com.ilustris.manager.feature.home.ui

import android.graphics.Bitmap
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ilustris.motiv.base.data.model.QuoteDataModel
import com.ilustris.motiv.base.ui.component.MotivLoader
import com.ilustris.motiv.base.ui.component.QuoteCard
import com.ilustris.motiv.foundation.ui.presentation.QuoteActions
import com.ilustris.motiv.foundation.ui.theme.defaultRadius
import com.ilustris.motiv.foundation.ui.theme.quoteCardModifier
import com.ilustris.motiv.manager.home.presentation.ManagerViewModel
import com.silent.ilustriscore.core.model.ViewModelBaseState
import com.silent.ilustriscore.core.utilities.DateFormats
import com.silent.ilustriscore.core.utilities.format

@Composable
fun ManagerHomeView() {

    val managerViewModel = hiltViewModel<ManagerViewModel>()
    val state = managerViewModel.viewModelState.observeAsState().value
    val quotes = managerViewModel.quotes
    val quoteCount = managerViewModel.quoteCount.observeAsState().value ?: 0
    val quoteReportedCount = quotes.filter { it.quoteBean.reports.isNotEmpty() }.size
    LazyColumn(horizontalAlignment = Alignment.CenterHorizontally) {

        item {
            AnimatedVisibility(
                visible = state == ViewModelBaseState.LoadingState,
                enter = scaleIn(),
                exit = fadeOut()
            ) {
                MotivLoader(modifier = Modifier.padding(16.dp))
            }
        }

        stickyHeader {
            Text(
                text = "Home",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }

        item {
            Text(
                text = "Gerencie suas citações, atualmente o app possui $quoteCount posts e $quoteReportedCount reportados por usuários.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
            )
        }

        items(quotes.size) {
            val quoteData = quotes[it]
            QuoteCard(
                quoteData,
                animationEnabled = false,
                quoteActions = object : QuoteActions {
                    override fun onClickUser(uid: String) {}

                    override fun onLike(dataModel: QuoteDataModel) {}

                    override fun onShare(dataModel: QuoteDataModel, bitmap: Bitmap) {}

                    override fun onDelete(dataModel: QuoteDataModel) {
                        managerViewModel.deleteQuote(dataModel)
                    }

                    override fun onEdit(dataModel: QuoteDataModel) {}

                    override fun onReport(dataModel: QuoteDataModel) {}
                },
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .wrapContentSize()
                    .quoteCardModifier()
            )

            quoteData.quoteBean.reports.forEach { report ->

                Divider(
                    modifier = Modifier
                        .padding(horizontal = 24.dp)
                        .background(
                            MaterialTheme.colorScheme.surface,
                            RoundedCornerShape(defaultRadius)
                        )
                        .clip(RoundedCornerShape(defaultRadius))
                        .width(3.dp)
                        .height(25.dp)

                )
                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .background(
                            MaterialTheme.colorScheme.surface, RoundedCornerShape(
                                defaultRadius
                            )
                        )
                        .clip(RoundedCornerShape(defaultRadius))
                        .clickable {
                            managerViewModel.removeReport(quoteData, report)
                        }
                        .padding(8.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = report.reason,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        text = report.data?.toDate()?.format(DateFormats.DD_OF_MM_FROM_YYYY) ?: "-",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Light,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

        }

    }

    LaunchedEffect(Unit) {
        managerViewModel.getQuotes()
    }

}