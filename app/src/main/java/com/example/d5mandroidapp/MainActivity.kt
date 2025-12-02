package com.example.d5mandroidapp

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.example.d5mandroidapp.storage.TokenRepositoryImpl
import com.example.d5mandroidapp.ui.theme.D5MAndroidAppTheme
import com.example.d5mandroidapp.utils.TimerUtils
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.apollographql.apollo3.exception.ApolloException
import com.example.d5mandroidapp.apollo.apolloClient
import com.example.d5mandroidapp.navigation.Hosts
import com.example.d5mandroidapp.navigation.Screens
import com.example.d5mandroidapp.storage.UserRepositoryImpl
import dagger.hilt.android.AndroidEntryPoint

@RequiresApi(Build.VERSION_CODES.R)
@OptIn(ExperimentalMaterial3Api::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            D5MAndroidAppTheme {
                Surface(
                    color = MaterialTheme.colorScheme.background
                ) {

//                    val viewModel by viewModels<ProductViewModel>(factoryProducer = {
//                        object : ViewModelProvider.Factory {
//                            override fun <T : ViewModel> create(modelClass: Class<T>): T {
//                                return ProductViewModel(dao) as T
//                            }
//                        }
//                    })
                    RootNavHost()


                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "CoroutineCreationDuringComposition")
@ExperimentalMaterial3Api
@Composable
fun RootNavHost() {
    val navigationController: NavHostController = rememberNavController()
    val coroutineScope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
//    val userViewModel: UserViewModel = hiltViewModel()
//    val userState by userViewModel.state.collectAsState()
    var gesturesEnabled = !drawerState.isClosed
    var topBarState by rememberSaveable { (mutableStateOf(false)) }
    val context = LocalContext.current.applicationContext
    val scope = rememberCoroutineScope()
    val timerUtils = TimerUtils()
    val tokenRepository = TokenRepositoryImpl(context)
    val userRepository = UserRepositoryImpl(context)
    val token = tokenRepository.getToken()
//    Log.w("D5MAP2", "tokenRepository ${token}")
    scope.launch {
        val ok = token?.let { verifyingToken(context, it) }
        if (ok == false) {

            navigationController.navigate(Screens.Login.screen)
            tokenRepository.clearToken()
            tokenRepository.clearRefreshToken()
        }
    }
    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = gesturesEnabled,
        drawerContent = {
            ModalDrawerSheet(
                drawerContentColor = MaterialTheme.colorScheme.onSurface,
                drawerContainerColor = MaterialTheme.colorScheme.surface,
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surface),
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(
                        onClick = {
                            coroutineScope.launch {
                                drawerState.close()
                            }
                        },
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Cerrar drawer",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.surface)
                        .fillMaxWidth()
                        .height(250.dp)
                        .paint(
                            painterResource(id = R.drawable.bg15),
                            contentScale = ContentScale.FillBounds
                        )
                ) {
                    Column(
                        modifier = Modifier
                            .padding(all = 20.dp)
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.Bottom,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = "APLICACION 5M DISTRIBUCIONES",
                            fontFamily = FontFamily(Font(R.font.aeonik_light)),
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = "DESARROLLADA POR 4SOLUCIONES",
                            fontFamily = FontFamily(Font(R.font.aeonik_light)),
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

                NavigationDrawerItem(
                    label = {
                        Text(
                            text = "Perfil",
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    selected = false,
                    icon = {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "profile",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    },
                    onClick = {
                        coroutineScope.launch {
                            drawerState.close()
                        }
                        navigationController.navigate(Screens.Profile.screen) {
                            popUpTo(0)
                        }
                    }
                )

                NavigationDrawerItem(
                    label = {
                        Text(
                            text = "Clientes",
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    selected = false,
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    },
                    onClick = {
                        coroutineScope.launch {
                            drawerState.close()
                        }
                        navigationController.navigate(Screens.Client.screen) {
                            popUpTo(0)
                        }
                    }
                )

                NavigationDrawerItem(
                    label = {
                        Text(
                            text = "Mapa",
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    selected = false,
                    icon = {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    },
                    onClick = {
                        coroutineScope.launch {
                            drawerState.close()
                        }
                        navigationController.navigate(Screens.Route.screen) {
                            popUpTo(0)
                        }
                    }
                )

                NavigationDrawerItem(
                    label = {
                        Text(
                            text = "Ventas Generadas",
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    selected = false,
                    icon = {
                        Icon(
                            imageVector = Icons.Default.List,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    },
                    onClick = {
                        coroutineScope.launch {
                            drawerState.close()
                        }
                        navigationController.navigate(Screens.GeneratedOrders.screen) {
                            popUpTo(0)
                        }
                    }
                )

                NavigationDrawerItem(
                    label = {
                        Text(
                            text = "Cerrar sesión",
                            color = MaterialTheme.colorScheme.error,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    selected = false,
                    icon = {
                        Icon(
                            imageVector = Icons.Default.ExitToApp,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error
                        )
                    },
                    onClick = {
                        coroutineScope.launch {
                            drawerState.close()
                            tokenRepository.clearToken()
                            tokenRepository.clearRefreshToken()
                            userRepository.clearUserId()
                            userRepository.clearUserEmail()
                            Toast.makeText(context, "Cerraste sesión.", Toast.LENGTH_SHORT).show()
                            navigationController.navigate(Screens.Login.screen)
                        }
                    }
                )
            }
        },
    ) {
        Scaffold(

            topBar = {
                if (topBarState) {
//                    gesturesEnabled = true
//                    val coroutineScope2 = rememberCoroutineScope()



                    TopAppBar(
                        title = {
                            Text(
                                text = "5M Distribuciones",
                                style = MaterialTheme.typography.titleLarge
                            )
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            titleContentColor = MaterialTheme.colorScheme.onPrimary,
                            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        navigationIcon = {
                            IconButton(onClick = {
                                coroutineScope.launch {
                                    drawerState.open()
                                }
                            }) {
                                Icon(
                                    Icons.Rounded.Menu,
                                    contentDescription = "MenuButton"
                                )
                            }
                        }
                    )
                } else {
                    null
                }
            }
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                Hosts(navigationController = navigationController, { topBarState = it },
                    { gesturesEnabled = it })
            }
        }
    }


}

private suspend fun verifyingToken(context: Context, token: String): Boolean {
    val response = try {
        apolloClient.mutation(VerifyTokenMutation(token = token)).execute()
    } catch (e: ApolloException) {
        Toast.makeText(context, "No se pudo conectar", Toast.LENGTH_SHORT).show()
        return false
    }
    if (response.hasErrors()) {
        Toast.makeText(context, "Error al intentar recuperar token\n" +
                "${response.errors?.get(0)?.message}", Toast.LENGTH_SHORT).show()
        return false
    }
    val verifyToken = response.data?.verifyToken
    if (verifyToken == null) {
        Toast.makeText(context, "La firma ha caducado", Toast.LENGTH_LONG).show()
        return false
    }
    return true
}
private suspend fun logout(context: Context, userId: Int): Boolean {
    val response = try {
        apolloClient.mutation(LogoutMutation(userId = userId)).execute()
    } catch (e: ApolloException) {
        Toast.makeText(context, "No se pudo conectar", Toast.LENGTH_SHORT).show()
        return false
    }
    val verifyLogout = response.data?.logout?.success
    if (verifyLogout == null) {
        Toast.makeText(context, "No se pudo cerrar sesión", Toast.LENGTH_LONG).show()
        return false
    }
    return true
}
