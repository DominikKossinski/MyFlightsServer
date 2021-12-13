package pl.kossa.myflightsserver.retrofit

import org.simpleframework.xml.convert.Registry
import org.simpleframework.xml.convert.RegistryStrategy
import org.simpleframework.xml.core.Persister
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Service
import pl.kossa.myflightsserver.retrofit.call.ApiResponseAdapterFactory
import pl.kossa.myflightsserver.retrofit.converters.*
import pl.kossa.myflightsserver.retrofit.models.*
import pl.kossa.myflightsserver.retrofit.services.SimbriefService
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory

@Service
class RetrofitService {

    private val registry = Registry().apply {
        bind(SimbriefAirport::class.java, SimbriefAirportConverter::class.java)
        bind(SimbriefAlternate::class.java, SimbriefAlternateConverter::class.java)
        bind(SimbriefAircraft::class.java, SimbriefAircraftConverter::class.java)
        bind(SimbriefCrew::class.java, SimbriefCrewConverter::class.java)
        bind(SimbriefFix::class.java, SimbriefFixConverter::class.java)
        bind(SimbriefFuel::class.java, SimbriefFuelConverter::class.java)
        bind(SimbriefImages::class.java, SimbriefImagesConverter::class.java)
        bind(SimbriefMap::class.java, SimbriefMapConverter::class.java)
        bind(SimbriefGeneral::class.java, SimbriefGeneralConverter::class.java)
        bind(SimbriefParams::class.java, SimbriefParamsConverter::class.java)
        bind(SimbriefOFP::class.java, SimbriefOFPConverter::class.java)
    }
    private val strategy = RegistryStrategy(registry)
    private val serializer = Persister(strategy)
    private val retrofit = Retrofit.Builder()
        .baseUrl(System.getenv("SIMBRIEF_URL"))
        .addCallAdapterFactory(ApiResponseAdapterFactory())
        .addConverterFactory(SimpleXmlConverterFactory.create(serializer))
        .build()

    @Bean
    fun simbriefService(): SimbriefService {
        return retrofit.create(SimbriefService::class.java)
    }

}