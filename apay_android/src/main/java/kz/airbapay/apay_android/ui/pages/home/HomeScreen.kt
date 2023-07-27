package kz.airbapay.apay_android.ui.pages.home

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.platform.LocalLifecycleOwner
import kotlinx.coroutines.CoroutineScope
import kz.airbapay.apay_android.ui.resources.ColorsSdk

@Composable
fun HomeScreen(
    delegate: HomeScreenDelegate,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    scrollState: ScrollState = rememberScrollState()
) {

    val owner = LocalLifecycleOwner.current
//    delegate.observeLifecycle(owner.lifecycle)

//    var airbaPayLoanDVO by remember { mutableStateOf<AirbaPayLoanDVO?>(null) }
//    var unfinishedOrderArgs by remember { mutableStateOf<UnfinishedOrderArgs?>(null) }

    with(delegate) {

        /*airbaPayLoanViewModel.navigateToLoanDialog.observe(owner) {
            airbaPayLoanDVO = it.peek().mapToAirbaPayLoanDVO()
        }

        unfinishedActiveOrderViewModel.needShowUnfinishedOrdersDialogEvent.observe(owner) {
            unfinishedOrderArgs = it.peek()
        }*/

        /*val pullRefreshState = rememberPullRefreshState(
            refreshing = homeViewModel.isRefreshing.value,
            onRefresh = {
                homeViewModel.isRefreshing.value = false
                update()
            }
        )*/

        Box(
//            modifier = Modifier.pullRefresh(pullRefreshState)
        ) {

            Column(
                modifier = Modifier
                    .background(ColorsSdk.bgMain)
                    .clipToBounds()
                    .fillMaxSize()
            ) {

              /*  ViewToolbar(
                    title = ,
                    actionBack = {

                    }
                )

                if (homeViewModel.hasInternet.value
                    && !homeViewModel.isError.value
                    && !homeViewModel.isLoading.value
                    && homeViewModel.layout.value.isNotEmpty()
                ) {
                    InitItems(
                        scrollState = scrollState,
                        coroutineScope = coroutineScope
                    )

                } else if (homeViewModel.isLoading.value
                    || homeViewModel.layout.value.isEmpty()
                ) {
                    ViewLoading()

                } else {
                    ViewLoadingError(actionUpdate = {
                        update()
                    })
                }
            }*/

            /*PullRefreshIndicator(
                refreshing = homeViewModel.isRefreshing.value,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter)
            )*/
        }

    }

/*    airbaPayLoanDVO?.let {
        AirbaPayCreditInfoDialog(
            loanDVO = it,
            onDismissRequest = { airbaPayLoanDVO = null }
        )
    }

    unfinishedOrderArgs?.let {
        UnfinishedActiveOrdersDialog(
            args = it,
            onDismissRequest = { unfinishedOrderArgs = null }
        )
    }*/
}
}



