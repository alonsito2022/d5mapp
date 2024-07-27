package com.example.d5mandroidapp.ui.views.clients

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.ContentAlpha
import com.example.d5mandroidapp.data.states.UserState
import com.example.d5mandroidapp.ui.theme.Purple200
import com.example.d5mandroidapp.ui.theme.Purple40
import com.example.d5mandroidapp.ui.theme.Purple500
import kotlin.reflect.KFunction1

@Composable
fun SearchClient(
    searchText: String,
    searchBy: String,
    onTextChangeTextField: KFunction1<String, Unit>,
    cleanTextField: (code: String) -> Unit,
    updateSearchBy: (code: String) -> Unit,
    onClickButtonSearchClient: () -> Unit,
    onClickButtonToggleSearchPanel: () -> Unit,
    userState: UserState
) {
    var expanded by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    var hasFocus by remember{ mutableStateOf(false) }
    val controller = LocalSoftwareKeyboardController.current
    Row(modifier = Modifier
        .fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
//                    .wrapContentSize(Alignment.TopEnd)
                .weight(1f)

        ){
            IconButton(onClick = { expanded = !expanded }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "More",
                    modifier = Modifier.size(60.dp),
                    tint = Purple200
                )
            }
            DropdownMenu(
                modifier = Modifier
                    .wrapContentSize()
                    .background(Color.White),
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                DropdownMenuItem(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White),
                    onClick = {
                        expanded = false
                        updateSearchBy("names")
                    },
                    text = {
                        Text(text = "BUSCAR POR NOMBRE")
                    },
                )
                HorizontalDivider()
                DropdownMenuItem(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White),
                    onClick = {
                        expanded = false
                        updateSearchBy("code")
                    },
                    text = {
                        Text(text = "BUSCAR POR CODIGO")
                    },
                )
                HorizontalDivider()
                DropdownMenuItem(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White),
                    onClick = {
                        expanded = false
                        updateSearchBy("document")
                    },
                    text = {
                        Text(text = "BUSCAR POR DOCUMENTO")
                    },
                )
            }
        }
        OutlinedTextField(
//            enabled = !userState.isLoading,
            value = searchText,
            onValueChange = onTextChangeTextField,
            singleLine = true,
            modifier = Modifier
                .weight(7f)
                .padding(4.dp)
                .background(Color.White, shape = RoundedCornerShape(5.dp))
                .focusRequester(focusRequester)
                .onFocusChanged { hasFocus = it.hasFocus },
            placeholder = {
                if (searchBy=="names") {
                    Text(text = "por nombre", color = Color.Gray, fontSize = 13.sp)
                    cleanTextField("")
                }
                if (searchBy=="code") {
                    Text(text = "por codigo", color = Color.Gray, fontSize = 13.sp)
                    cleanTextField("")
                }
                if (searchBy=="document") {
                    Text(text = "por documento", color = Color.Gray, fontSize = 13.sp)
                    cleanTextField("")
                }
            },
//            leadingIcon = {
//                Icon(
//                    imageVector = Icons.Filled.Search,
//                    contentDescription = "Search Icon",
//                    tint = Purple40.copy(
//                        alpha = ContentAlpha.medium
//                    )
//                )
//            },
            trailingIcon = {
                IconButton(onClick = {
                    cleanTextField("")
                    focusRequester.requestFocus()
                }) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Close Icon",
                        modifier = Modifier.size(60.dp),
                        tint = Purple200
                    )
                }
            },
        )

        IconButton(
            modifier = Modifier.weight(1f),
            onClick = {
                if(!hasFocus){
                    focusRequester.requestFocus()
                }else{
                    focusManager.clearFocus()
                }
                onClickButtonSearchClient()
                controller?.hide()
            }) {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = "Search Icon",
                modifier = Modifier.size(60.dp),
                tint = Purple200
            )
        }

        IconButton(
            modifier = Modifier.weight(1f),
            onClick = {
                onClickButtonToggleSearchPanel()
            }) {
            Icon(
                imageVector = if(userState.showSearchPanel){Icons.Filled.KeyboardArrowUp}else{Icons.Filled.KeyboardArrowDown},
                contentDescription = "Arrow Icon",
                modifier = Modifier.size(60.dp),
                tint = Purple200
            )
        }

    }
}