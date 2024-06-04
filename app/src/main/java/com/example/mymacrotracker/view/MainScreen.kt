import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.mymacrotracker.model.NavScreen
import com.example.mymacrotracker.view.BottomNavigationBar
import com.example.mymacrotracker.R
import com.example.mymacrotracker.view.DiaryScreen
import com.example.mymacrotracker.view.HomeScreen
import com.example.mymacrotracker.viewModel.DiaryViewModel
import com.example.mymacrotracker.viewModel.FirebaseViewModel
import com.example.mymacrotracker.viewModel.NotificationViewModel
import com.example.mymacrotracker.viewModel.SearchViewModel
import com.example.mymacrotracker.viewModel.StepCounterViewModel

@Composable
fun MainScreen(navController: NavController) {
    var currentScreen by remember { mutableStateOf(NavScreen.Search) }

    val searchViewModel = SearchViewModel()
    val firebaseViewModel = FirebaseViewModel(navController)
    val notificationViewModel = NotificationViewModel()
    val diaryViewModel = DiaryViewModel()

    val stepCounterViewModel = StepCounterViewModel(LocalContext.current)

    val tabs = listOf(
        NavScreen("Home", R.drawable.home),
        NavScreen("Search", R.drawable.outline_search_24),
        NavScreen("Diary", R.drawable.diary)
    )



    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                tabs = tabs,
                currentScreen = currentScreen,
                onTabSelected = { screen ->
                    currentScreen = screen
                }
            )
        },
        content = { padding ->
            Box(modifier = Modifier.padding(padding)){
                when (currentScreen) {
                    NavScreen.Home -> HomeScreen(firebaseViewModel, stepCounterViewModel,notificationViewModel)
                    NavScreen.Search -> SearchScreen(searchViewModel, navController)
                    NavScreen.Diary -> DiaryScreen(diaryViewModel,firebaseViewModel )
                }
            }
        },
        contentColor = Color.White
    )
}

