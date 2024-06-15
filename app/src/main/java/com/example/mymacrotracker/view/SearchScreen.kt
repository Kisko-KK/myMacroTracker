import android.os.Bundle
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mymacrotracker.viewModel.SearchViewModel
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.navigation.NavController
import com.example.mymacrotracker.ui.theme.BackgroundMain
import com.example.mymacrotracker.ui.theme.BackgroundSecondary


@Composable
fun SearchScreen(viewModel: SearchViewModel, navController : NavController) {
    var searchText by remember { mutableStateOf("") }
    val foodList by viewModel.foodList.observeAsState()
    val context = LocalContext.current

    var isLoading by remember { mutableStateOf(false) }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundMain)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Search bar
                OutlinedTextField(
                    value = searchText,
                    onValueChange = { text -> searchText = text },
                    label = { Text("Search for food", color = Color.White) },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { viewModel.searchFood(searchText, context); }),
                    modifier = Modifier.weight(1f),
                    textStyle = MaterialTheme.typography.body1.copy(color = Color.White)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = Color.White
                )
            } else {
                Text(
                    text = "Search Results",
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            foodList?.let { list ->

                LazyColumn {
                    items(list) { foodItem ->
                        Spacer(modifier = Modifier.height(5.dp))
                        Box(
                            modifier = Modifier
                                .clickable {
                                    navController.currentBackStackEntry?.savedStateHandle?.set("foodItem", foodItem)
                                    navController.navigate("foodItemScreen")
                                }
                                .fillMaxWidth()
                                .height(80.dp)
                                .background(
                                    color = BackgroundSecondary,
                                    shape = RoundedCornerShape(10.dp)
                                )
                                .padding(horizontal = 16.dp),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Column(
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = viewModel.getTitle(foodItem),
                                    style = MaterialTheme.typography.subtitle1,
                                    color = Color.White
                                )
                                Text(
                                    text = viewModel.getFoodTitleSearch(foodItem),
                                    style = MaterialTheme.typography.subtitle2,
                                    fontWeight = FontWeight.Normal,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}