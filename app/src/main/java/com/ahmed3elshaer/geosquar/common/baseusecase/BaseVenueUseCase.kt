/*
 * *
 *  * Created by Ahmed Elshaer on 10/26/19 4:17 AM
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 10/26/19 12:05 AM
 *
 */

package com.ahmed3elshaer.geosquar.common.baseusecase

import com.ahmed3elshaer.geosquar.common.Repository
import com.ahmed3elshaer.geosquar.common.models.Venue
import com.ahmed3elshaer.geosquar.common.models.VenuesRequest
import com.ahmed3elshaer.geosquar.common.schedulers.BaseSchedulerProvider
import io.reactivex.Observable

abstract class BaseVenueUseCase(
    private val repository: Repository,
    private val schedulerProvider: BaseSchedulerProvider
) {
    fun getVenues(venuesRequest: VenuesRequest, shouldCache: Boolean): Observable<List<Venue>> =
            repository.exploreVenues(venuesRequest)
                    .subscribeOn(schedulerProvider.io())
                    .map { it.data.groups.first().venueItems.map { it.venue } }
                    .doOnNext {
                        if (shouldCache)
                            repository.cacheVenues(it)
                        repository.cacheLocation(venuesRequest.coordinates)
                    }
}
