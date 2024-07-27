package com.example.d5mandroidapp.ui.views
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.d5mandroidapp.apollo.ApolloClientProvider
import com.example.d5mandroidapp.TruckListQuery
import com.example.d5mandroidapp.storage.TokenRepositoryImpl
import com.example.d5mandroidapp.ui.theme.GreenJC
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

data class Truck(val id: String, val licensePlate: String, val isEnabled: Boolean) // Data model for trucks

@Composable
fun TruckList() {

    val context = LocalContext.current

    // State variable to store trucks data
    var trucks by remember { mutableStateOf<List<Truck>?>(null) }

    LaunchedEffect(Unit) {
//        val tokenRepository = TokenRepositoryImpl(context)
//        val apolloClientProvider = tokenRepository.getToken()?.let { ApolloClientProvider(it) }
//        val response = apolloClientProvider?.apolloClientWithAuthorizationInterceptor?.query(TruckListQuery())?.execute()
//        if (response != null) {
//            trucks = response.data?.allTrucks?.map {
//                Truck(it?.id!!, it.licensePlate!!, it.isEnabled)
//            }
//            Log.d("truckList", response.toString())
//        }
        val tokenRepository = TokenRepositoryImpl(context)
        val apolloClientProvider = tokenRepository.getToken()?.let { ApolloClientProvider(it) }
        if (apolloClientProvider != null) {
            val apolloClient = apolloClientProvider.apolloClientWithAuthorizationInterceptor
            val trucksFlow: Flow<List<Truck>?> = flow {
                val response = apolloClient.query(TruckListQuery()).execute()
                val data = response.data?.allTrucks?.map { truck ->
                    Truck(truck?.id ?: "", truck?.licensePlate ?: "", truck?.isEnabled ?: false)
                }
                emit(data)
            }

            trucksFlow.collect { data ->
                // Actualiza el estado de 'trucks' con la data
                trucks = data
            }
        } else {
            Log.w("TruckList", "No token found; unable to fetch trucks")
        }

    }

    // Display the list of trucks if data is available
    if (trucks != null) {

        Log.w("TruckList", trucks!!.size.toString())

        LazyColumn  (
                modifier = Modifier.padding(top = 70.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                items(trucks!!) { truck ->
//                TruckListItem(truck = truck)
//                this@LazyColumn.item {
//                    Spacer(modifier = Modifier.height(8.dp)) // Espaciado vertical entre Cards
//                }

                        Text(text = "Item ${truck.licensePlate}",
                            modifier = Modifier.fillMaxWidth(),
                            fontSize = 30.sp,
                            color = GreenJC)

                }
            }


    }

    else {
        // Show a loading indicator or error message while fetching data
        Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }
}

@Composable
fun TruckListItem(truck: Truck) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        )
    ) {
        Row(modifier = Modifier.clickable(onClick = { /* Handle truck click */ })) {
            Text(
                text = truck.licensePlate,
                modifier = Modifier.padding(16.dp),
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = if (truck.isEnabled) "Enabled" else "Disabled",
                modifier = Modifier.padding(16.dp),
                color = if (truck.isEnabled) GreenJC else Color.Red
            )
        }
    }
}