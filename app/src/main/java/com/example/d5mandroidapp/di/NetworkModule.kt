package com.example.d5mandroidapp.di

import android.content.Context
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.network.okHttpClient
import com.example.d5mandroidapp.apollo.ApolloClientProvider
import com.example.d5mandroidapp.apollo.ApolloClientUpdater
import com.example.d5mandroidapp.apollo.ApolloConfig
import com.example.d5mandroidapp.apollo.AuthorizationInterceptor
import com.example.d5mandroidapp.data.network.ClientApiClient
import com.example.d5mandroidapp.data.network.ILocationService
import com.example.d5mandroidapp.data.network.OrderApiClient
import com.example.d5mandroidapp.data.network.PaymentApiClient
import com.example.d5mandroidapp.data.network.ProductTariffApiClient
import com.example.d5mandroidapp.data.network.RouteApiClient
import com.example.d5mandroidapp.data.network.UserApiClient
import com.example.d5mandroidapp.data.services.ClientService
import com.example.d5mandroidapp.data.services.LocationService
import com.example.d5mandroidapp.data.services.OrderService
import com.example.d5mandroidapp.data.services.PaymentService
import com.example.d5mandroidapp.data.services.ProductTariffService
import com.example.d5mandroidapp.data.services.RouteService
import com.example.d5mandroidapp.data.services.UserService
import com.example.d5mandroidapp.domain.GetAssignedGangsByUserIdUseCase
import com.example.d5mandroidapp.domain.GetAssignedZonesByUserIdUseCase
import com.example.d5mandroidapp.domain.GetClientByIdUseCase
import com.example.d5mandroidapp.domain.GetClientWithDebtByIdUseCase
import com.example.d5mandroidapp.domain.GetClientsFilteredUseCase
import com.example.d5mandroidapp.domain.GetDailyRoutesByCriteriaUseCase
import com.example.d5mandroidapp.domain.GetDiscountGiftsByProductsPurchasedUseCase
import com.example.d5mandroidapp.domain.GetGeneratedOrderUseCase
import com.example.d5mandroidapp.domain.GetOrdersWithDebtByClientIdUseCase
import com.example.d5mandroidapp.domain.GetProductGiftsByProductPurchasedIdUseCase
import com.example.d5mandroidapp.domain.GetProductGiftsByProductsPurchasedUseCase
import com.example.d5mandroidapp.domain.GetProductTariffsFilteredUseCase
import com.example.d5mandroidapp.domain.GetProductTariffsUseCase
import com.example.d5mandroidapp.domain.GetSimpleAddressesByClientIdUseCase
import com.example.d5mandroidapp.domain.GetUserByIdUseCase
import com.example.d5mandroidapp.domain.GetZonePointsByZoneIdUseCase
import com.example.d5mandroidapp.domain.GetZonesByGangIdUseCase
import com.example.d5mandroidapp.domain.RemoveRefreshTokenUseCase
import com.example.d5mandroidapp.domain.SaveOrderUseCase
import com.example.d5mandroidapp.domain.SavePaymentListUseCase
import com.example.d5mandroidapp.domain.UpdateWithoutOrderUseCase
import com.example.d5mandroidapp.domain.VerifyTokenUseCase
import com.example.d5mandroidapp.storage.TokenRepository
import com.example.d5mandroidapp.storage.TokenRepositoryImpl
import com.example.d5mandroidapp.storage.UserRepository
import com.example.d5mandroidapp.storage.UserRepositoryImpl
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideApolloClientWithAuthInterceptor(@ApplicationContext context: Context): ApolloClient {

        val tokenRepository = TokenRepositoryImpl(context)
        val token = tokenRepository.getToken()
        Log.d("D5MAP2"," ApolloClientWithAuthInterceptor token: ${token}")
        val authorizationInterceptor = AuthorizationInterceptor(token!!)

        return ApolloClient.Builder()
            .serverUrl(ApolloConfig.BASE_URL)
            .okHttpClient(
                OkHttpClient.Builder()
                    .addInterceptor(authorizationInterceptor)
                    .build()
            )
            .build()
    }

//    @Provides
//    @Singleton
//    fun provideApolloClientUpdater(
//        tokenRepository: TokenRepository,
//        @ApplicationContext context: Context
//    ): ApolloClientUpdater {
//        return ApolloClientUpdater(tokenRepository, context)
//    }

    @Provides
    @Singleton
    fun provideClientServiceClient(apolloClient: ApolloClient): ClientApiClient {
        return ClientService(apolloClient)
    }

    @Provides
    @Singleton
    fun provideProductTariffService(apolloClient: ApolloClient): ProductTariffApiClient {
        return ProductTariffService(apolloClient)
    }

    @Provides
    @Singleton
    fun provideOrderService(apolloClient: ApolloClient): OrderApiClient {
        return OrderService(apolloClient)
    }



    @Provides
    @Singleton
    fun provideRouteService(apolloClient: ApolloClient): RouteApiClient {
        return RouteService(apolloClient)
    }

    @Provides
    @Singleton
    fun provideUserService(apolloClient: ApolloClient): UserApiClient {
        return UserService(apolloClient)
    }

    @Provides
    @Singleton
    fun providePaymentService(apolloClient: ApolloClient): PaymentApiClient {
        return PaymentService(apolloClient)
    }

    @Provides
    @Singleton
    fun provideGetClientsFilteredUseCase(clientApiClient: ClientApiClient): GetClientsFilteredUseCase {
        return GetClientsFilteredUseCase(clientApiClient)
    }

    @Provides
    @Singleton
    fun provideGetSimpleAddressesByClientIdUseCase(clientApiClient: ClientApiClient): GetSimpleAddressesByClientIdUseCase {
        return GetSimpleAddressesByClientIdUseCase(clientApiClient)
    }

    @Provides
    @Singleton
    fun provideGetProductTariffsUseCase(productTariffApiClient: ProductTariffApiClient): GetProductTariffsUseCase {
        return GetProductTariffsUseCase(productTariffApiClient)
    }

    @Provides
    @Singleton
    fun provideGetProductTariffsFilteredUseCase(productTariffApiClient: ProductTariffApiClient): GetProductTariffsFilteredUseCase {
        return GetProductTariffsFilteredUseCase(productTariffApiClient)
    }

    @Provides
    @Singleton
    fun provideGetProductGiftsByProductPurchasedIdUseCase(productTariffApiClient: ProductTariffApiClient): GetProductGiftsByProductPurchasedIdUseCase {
        return GetProductGiftsByProductPurchasedIdUseCase(productTariffApiClient)
    }

    @Provides
    @Singleton
    fun provideGetProductGiftsByProductsPurchasedUseCase(productTariffApiClient: ProductTariffApiClient): GetProductGiftsByProductsPurchasedUseCase {
        return GetProductGiftsByProductsPurchasedUseCase(productTariffApiClient)
    }

    @Provides
    @Singleton
    fun provideSaveOrderUseCase(orderApiClient: OrderApiClient): SaveOrderUseCase {
        return SaveOrderUseCase(orderApiClient)
    }

//    @Provides
//    @Singleton
//    fun provideVerifyTokenUseCase(userApiClient: UserApiClient): VerifyTokenUseCase {
//        return VerifyTokenUseCase(userApiClient)
//    }

    @Provides
    @Singleton
    fun provideUpdateWithoutOrderUseCase(orderApiClient: OrderApiClient): UpdateWithoutOrderUseCase {
        return UpdateWithoutOrderUseCase(orderApiClient)
    }
    @Provides
    @Singleton
    fun provideGetGeneratedOrderUseCase(orderApiClient: OrderApiClient): GetGeneratedOrderUseCase {
        return GetGeneratedOrderUseCase(orderApiClient)
    }

    @Singleton
    @Provides
    fun provideLocationClient(
        @ApplicationContext context: Context
    ): ILocationService = LocationService(
        context,
        LocationServices.getFusedLocationProviderClient(context)
    )

    @Provides
    @Singleton
    fun provideGetAssignedZonesByUserIdUseCase(routeApiClient: RouteApiClient): GetAssignedZonesByUserIdUseCase {
        return GetAssignedZonesByUserIdUseCase(routeApiClient)
    }

    @Provides
    @Singleton
    fun provideGetZonePointsByZoneIdUseCase(routeApiClient: RouteApiClient): GetZonePointsByZoneIdUseCase {
        return GetZonePointsByZoneIdUseCase(routeApiClient)
    }

    @Provides
    @Singleton
    fun provideGetAssignedGangsByUserIdUseCase(routeApiClient: RouteApiClient): GetAssignedGangsByUserIdUseCase {
        return GetAssignedGangsByUserIdUseCase(routeApiClient)
    }

    @Provides
    @Singleton
    fun provideGetZonesByGangIdUseCase(routeApiClient: RouteApiClient): GetZonesByGangIdUseCase {
        return GetZonesByGangIdUseCase(routeApiClient)
    }

    @Provides
    @Singleton
    fun provideGetDailyRoutesByCriteriaUseCase(routeApiClient: RouteApiClient): GetDailyRoutesByCriteriaUseCase {
        return GetDailyRoutesByCriteriaUseCase(routeApiClient)
    }

    @Provides
    @Singleton
    fun provideGetUserByIdUseCase(userApiClient: UserApiClient): GetUserByIdUseCase {
        return GetUserByIdUseCase(userApiClient)
    }

    @Provides
    @Singleton
    fun provideGetClientWithDebtByIdUseCase(clientApiClient: ClientApiClient): GetClientWithDebtByIdUseCase {
        return GetClientWithDebtByIdUseCase(clientApiClient)
    }

    @Provides
    @Singleton
    fun provideGetOrdersWithDebtByClientIdUseCase(clientApiClient: ClientApiClient): GetOrdersWithDebtByClientIdUseCase {
        return GetOrdersWithDebtByClientIdUseCase(clientApiClient)
    }

    @Provides
    @Singleton
    fun provideGetDiscountGiftsByProductsPurchasedUseCase(productTariffApiClient: ProductTariffApiClient): GetDiscountGiftsByProductsPurchasedUseCase {
        return GetDiscountGiftsByProductsPurchasedUseCase(productTariffApiClient)
    }

    @Provides
    @Singleton
    fun provideSavePaymentListUseCase(paymentApiClient: PaymentApiClient): SavePaymentListUseCase {
        return SavePaymentListUseCase(paymentApiClient)
    }

    @Provides
    @Singleton
    fun provideRemoveRefreshTokenUseCase(userApiClient: UserApiClient): RemoveRefreshTokenUseCase {
        return RemoveRefreshTokenUseCase(userApiClient)
    }

    @Provides
    @Singleton
    fun provideTokenRepository(@ApplicationContext context: Context): TokenRepository {
        return TokenRepositoryImpl(context)
    }

    @Provides
    @Singleton
    fun provideUserRepository(@ApplicationContext context: Context): UserRepository {
        return UserRepositoryImpl(context)
    }

}