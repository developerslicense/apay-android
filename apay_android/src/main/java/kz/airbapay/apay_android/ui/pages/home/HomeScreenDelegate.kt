package kz.airbapay.apay_android.ui.pages.home

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel

internal class HomeScreenDelegate(
//    val homeViewModel: HomeConstructorViewModel,
) : ViewModel(), DefaultLifecycleObserver {

    private var isDataLoaded = false

//    val needShowLangDialog = mutableStateOf(checkNeedLangDialog())

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        if (!isDataLoaded) {
            isDataLoaded = true
//            homeViewModel.loadFromDb()
        }
    }

    override fun onResume(
        owner: LifecycleOwner
    ) {
        super.onResume(owner)
//        NavigationObserver.referrer.value = Referrer.HOME
//        AnalyticsTracker.sendAmplitudeEvent(EVENT_VIEW_LANDING_SCREEN)
//        partUpdate()
//        needShowLangDialog.value = checkNeedLangDialog()
    }

    fun update() {
        /*homeViewModel.loadFromNet(
            type = "main",
            name = null
        )

        partUpdate()*/
    }

 /*   private fun partUpdate() {
        unfinishedActiveOrderViewModel.checkForUnfinishedOrders()
        cartCoreDelegate.cartCoreViewModel.loadProductInCart()
        favoritesCoreDelegate.favoritesCoreViewModel.getFavorites()
        notificationViewModel.getNotifications()
    }*/
}