package pl.kossa.myflightsserver.retrofit.services

import pl.kossa.myflightsserver.retrofit.call.ApiResponse
import pl.kossa.myflightsserver.retrofit.models.SimbriefOFP
import retrofit2.http.GET
import retrofit2.http.Path

interface SimbriefService {

    @GET("/ofp/flightplans/xml/{timestamp}_{md5}.xml")
    suspend fun getOFP(@Path("timestamp") timestamp: Long, @Path("md5") md5: String): ApiResponse<SimbriefOFP>
}