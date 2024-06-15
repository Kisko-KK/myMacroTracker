package com.example.mymacrotracker.view

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mymacrotracker.model.NavScreen

@Composable
fun BottomNavigationBar(
    tabs: List<NavScreen>,
    currentScreen: NavScreen,
    onTabSelected: (NavScreen) -> Unit
) {
    BottomAppBar(
        backgroundColor = Color(0xff5754F7),
        elevation = 8.dp,
    ) {
        Row(
            modifier = Modifier
                .height(56.dp)
                .padding(start = 16.dp, end = 16.dp),
        ) {
            tabs.forEach { screen ->
                BottomNavigationItem(
                    icon = {
                        Icon(
                            painter = painterResource(id = screen.icon),
                            contentDescription = screen.title,
                            modifier = Modifier.width(24.dp)
                        )
                    },
                    label = { Text(text = screen.title, fontSize = 12.sp) },
                    selected = currentScreen == screen,
                    onClick = {
                        onTabSelected(screen)
                    },
                    selectedContentColor = Color.Black,
                    unselectedContentColor = Color(0xff173b69),
                    modifier = Modifier.weight(1f),
                    alwaysShowLabel = true
                )
            }
        }
    }
}

