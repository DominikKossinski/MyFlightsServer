package pl.kossa.myflightsserver.retrofit.call

import retrofit2.Call
import retrofit2.CallAdapter
import java.lang.reflect.Type

class ApiCallAdapter<S : Any>(
    private val successType: Type
) : CallAdapter<S, Call<ApiResponse<S>>> {
    override fun responseType(): Type = successType

    override fun adapt(call: Call<S>): Call<ApiResponse<S>> {
        return ApiCall(call)
    }
}