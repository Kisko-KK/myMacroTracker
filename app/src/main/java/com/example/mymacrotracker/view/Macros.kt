package com.example.mymacrotracker.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp

@Composable
fun Macros(fat: Double, protein: Double, carbs: Double) {

    val style = LocalTextStyle.current.copy(
        color = Color.White,
        fontWeight = FontWeight.Medium,
        fontStyle = FontStyle.Italic,
        letterSpacing = 0.1.em,
        fontSize = 16.sp
    )


    Column(
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Carbs",
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                style = style.copy(fontSize = 20.sp)
            )
            Text(
                text = "Fat",
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                style = style.copy(fontSize = 20.sp)
            )
            Text(
                text = "Protein",
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                style = style.copy(fontSize = 20.sp)

            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "${"%.1f".format(carbs).replace(',', '.').toDouble()} g",
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                style = style.copy(fontWeight = FontWeight.Light)
            )
            Text(
                text = "${"%.1f".format(fat).replace(',', '.').toDouble()} g",
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                style = style.copy(fontWeight = FontWeight.Light)
            )
            Text(
                text = "${"%.1f".format(protein).replace(',', '.').toDouble()} g",
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                style = style.copy(fontWeight = FontWeight.Light)
            )
        }
    }
}

