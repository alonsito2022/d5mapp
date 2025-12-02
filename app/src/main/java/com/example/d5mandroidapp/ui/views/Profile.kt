package com.example.d5mandroidapp.ui.views

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.d5mandroidapp.R
import com.example.d5mandroidapp.ui.theme.D5MAndroidAppTheme
import com.example.d5mandroidapp.ui.viewmodels.ProfileViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Profile() {
    val viewModel: ProfileViewModel = hiltViewModel()
    val state by viewModel.state.collectAsState()
    
    D5MAndroidAppTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.primary
                )
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp, vertical = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Imagen de cabecera
                    Image(
                        painter = painterResource(id = R.drawable.bg10),
                        contentDescription = "Imagen de perfil",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Saludo
                    Text(
                        text = "¡Bienvenido(a) ${state.selectedUser.firstName}!",
                        modifier = Modifier.fillMaxWidth(),
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Nos alegra que estés aquí.",
                        modifier = Modifier.fillMaxWidth(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    // Ícono de perfil
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "Ícono de perfil",
                            modifier = Modifier.size(60.dp),
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Card con información del perfil
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 2.dp
                        ),
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp)
                        ) {
                            Text(
                                text = "Tu Perfil",
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            Spacer(modifier = Modifier.height(16.dp))
                            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                            Spacer(modifier = Modifier.height(16.dp))

                            // Correo
                            ProfileInfoItem(
                                label = "CORREO",
                                value = state.selectedUser.username
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            // Cargo
                            ProfileInfoItem(
                                label = "CARGO",
                                value = state.selectedUser.roleReadable
                            )

                            // Celular (condicional)
                            if (state.selectedUser.phone.isNotEmpty() && 
                                state.selectedUser.phone.lowercase() != "null") {
                                Spacer(modifier = Modifier.height(16.dp))
                                ProfileInfoItem(
                                    label = "CELULAR",
                                    value = state.selectedUser.phone
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // DNI
                            ProfileInfoItem(
                                label = "DNI",
                                value = state.selectedUser.document
                            )

                            // Dirección (condicional)
                            if (state.selectedUser.address.isNotEmpty() && 
                                state.selectedUser.address.lowercase() != "null") {
                                Spacer(modifier = Modifier.height(16.dp))
                                ProfileInfoItem(
                                    label = "DIRECCIÓN",
                                    value = state.selectedUser.address
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

@Composable
private fun ProfileInfoItem(
    label: String,
    value: String
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            overflow = TextOverflow.Ellipsis,
            maxLines = 2
        )
    }
}
