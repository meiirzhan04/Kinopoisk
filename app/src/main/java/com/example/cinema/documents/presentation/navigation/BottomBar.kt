package com.example.cinema.documents.presentation.navigation

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.cinema.R

@Composable
fun BottomBar(
    currentDestination: String,
    onNavigate: (String) -> Unit
) {
    NavigationBar(
        containerColor = Color.White,
        modifier = Modifier
            .height(80.dp)
            .shadow(10.dp, shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
    ) {
        val items = listOf(
            Screen.Home to R.drawable.ic_home,
            Screen.Search to R.drawable.ic_search,
            Screen.Account to R.drawable.ic_account
        )

        items.forEach { (screen, icon) ->
            NavigationBarItem(
                selected = currentDestination == screen.route,
                onClick = { onNavigate(screen.route) },
                icon = {
                    Icon(
                        painter = painterResource(id = icon),
                        contentDescription = screen.route,

                        )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFF_3D3BFF),
                    unselectedIconColor = Color.Black,
                    indicatorColor = Color.White,
                )
            )
        }
    }
}
