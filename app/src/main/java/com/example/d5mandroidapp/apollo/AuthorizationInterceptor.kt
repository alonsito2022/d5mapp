package com.example.d5mandroidapp.apollo
import okhttp3.Interceptor
import okhttp3.Response

class AuthorizationInterceptor(val token: String) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .apply {
                addHeader("Authorization", "JWT $token")
            }
            .build()
        return chain.proceed(request)
    }
}