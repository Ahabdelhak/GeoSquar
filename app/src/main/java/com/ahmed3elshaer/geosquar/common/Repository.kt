package com.ahmed3elshaer.geosquar.common

import com.ahmed3elshaer.geosquar.common.SharedPrefWrapper.Companion.LOCATION_KEY
import com.ahmed3elshaer.geosquar.common.models.Venue
import com.ahmed3elshaer.geosquar.common.models.VenuesRequest
import com.ahmed3elshaer.geosquar.common.models.VenuesResponse
import com.ahmed3elshaer.geosquar.common.room.VenuesDao
import io.reactivex.Observable

class Repository(
    private val sharedPrefWrapper: SharedPrefWrapper,
    private val fourSquareApi: FourSquareApi,
    private val venuesDao: VenuesDao

) {

    fun exploreVenues(venuesRequest: VenuesRequest) = fourSquareApi.exploreVenues(venuesRequest)
    fun getVenueImages(venueId: String) = fourSquareApi.getVenuePhotos(venueId)
    fun getPhotoStream(url: String) = fourSquareApi.getPhotoStream(url)
    fun cacheLocation(coordinates: String) = sharedPrefWrapper.saveString(LOCATION_KEY, coordinates)
    fun getCachedLocation() = sharedPrefWrapper.getString(LOCATION_KEY)
    fun cacheVenues(venues: List<Venue>) {
        venuesDao.clearAll()
        venuesDao.insertAll(venues)
    }

    fun getVenuesCache(): Observable<List<Venue>> = venuesDao.allVenues()

}