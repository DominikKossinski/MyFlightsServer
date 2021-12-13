package pl.kossa.myflightsserver.retrofit.call

import okhttp3.Request
import okio.Timeout
import pl.kossa.myflightsserver.retrofit.exceptions.ApiServerException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.TimeUnit

class ApiCall<S : Any>(
    private val delegate: Call<S>
) : Call<ApiResponse<S>> {

    override fun enqueue(callback: Callback<ApiResponse<S>>) {
        return delegate.enqueue(object : Callback<S> {
            override fun onResponse(call: Call<S>, response: Response<S>) {
                if (response.isSuccessful) {
                    callback.onResponse(
                        this@ApiCall,
                        Response.success(ApiResponse(response.body()))
                    )
                } else {
                    callback.onFailure(this@ApiCall, ApiServerException(response.code()))
                }
            }

            override fun onFailure(call: Call<S>, t: Throwable) {
                callback.onFailure(this@ApiCall, t)
            }

        })
    }


    override fun isExecuted() = delegate.isExecuted

    override fun clone() = ApiCall(delegate.clone())

    override fun isCanceled() = delegate.isCanceled

    override fun cancel() = delegate.cancel()

    override fun execute(): Response<ApiResponse<S>> {
        throw UnsupportedOperationException("Api does not support execute")
    }

    override fun request(): Request = delegate.request()

    override fun timeout(): Timeout = Timeout().timeout(2, TimeUnit.SECONDS)
}