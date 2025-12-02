package com.example.d5mandroidapp.ui.views

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.d5mandroidapp.ui.theme.D5MAndroidAppTheme

data class MyModel(
    val name: String = ""
)

private val dataList = listOf(
    MyModel("Alexander"),
    MyModel("Hamilton"),
    MyModel("Agatha")
)

@Composable
fun HomeScreen() {
    D5MAndroidAppTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            MyList(dataList = dataList)
        }
    }
}

@Composable
fun MyCard(
    data: MyModel,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { Log.d("MyCard", "You clicked ${data.name}") },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        ),
        shape = MaterialTheme.shapes.small
    ) {
        Text(
            text = data.name,
            modifier = Modifier.padding(all = 16.dp),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun MyList(
    dataList: List<MyModel>
) {
    LazyColumn(
        modifier = Modifier.padding(top = 70.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(dataList) { data ->
            MyCard(data = data)
        }
    }
}
