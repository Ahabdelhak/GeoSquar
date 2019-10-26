package com.ahmed3elshaer.geosquar.home

import androidx.lifecycle.MutableLiveData
import com.ahmed3elshaer.geosquar.common.BaseViewModel
import com.ahmed3elshaer.geosquar.common.Event

class HomeViewModel(
    private val exploreVenuesRealtimeUseCase: ExploreVenuesRealtimeUseCase,
    private val exploreVenuesSingleUseCase: ExploreVenuesSingleUseCase,
    private val exploreVenuesCacheUseCase: ExploreVenuesCacheUseCase
) : BaseViewModel<Event<HomeViewState>>() {
    fun checkForCachedVenues() {
        add {
            exploreVenuesCacheUseCase()
                    .compose(applySchedulers())
                    .doOnSubscribe { post(previousValue()?.copy(isLoading = true)) }
                    .subscribe({
                        post(previousValue()?.copy(isLoading = false, venues = it))
                    }, {
                        post(previousValue()?.copy(isLoading = false, error = it))
                    })
        }
    }

    fun exploreVenues(location: Location, realtime: Boolean) {
        if (realtime)
            exploreRealtime(location)
        else
            exploreSingle(location)


    }

    private fun exploreSingle(location: Location) {
        add {
            exploreVenuesSingleUseCase(VenuesRequest("${location.latitude},${location.longitude}"))
                    .compose(applySchedulers())
                    .doOnSubscribe { post(previousValue()?.copy(isLoading = true)) }
                    .subscribe({
                        post(previousValue()?.copy(isLoading = false, venues = it))
                    }, {
                        post(previousValue()?.copy(isLoading = false, error = it))
                    })
        }
    }

    private fun exploreRealtime(location: Location) {
        add {
            exploreVenuesRealtimeUseCase(VenuesRequest("${location.latitude},${location.longitude}"))
                    .compose(applySchedulers())
                    .doOnSubscribe { post(previousValue()?.copy(isLoading = true)) }
                    .subscribe({
                        post(previousValue()?.copy(isLoading = false, venues = it))
                    }, {
                        post(previousValue()?.copy(isLoading = false, error = it))
                    })
        }
    }
    override val _viewState = MutableLiveData<Event<HomeViewState>>()
}