package com.pappt04.menzans.views.info

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TabPickerButton(
    globaltab: MutableState<Int>
) {
    // State to hold the selected tab
    var selectedTab = remember {
        when (globaltab.value) {
            0 -> mutableStateOf("Menu")
            else -> mutableStateOf("Info")
        }
    }

    // Function to handle tab selection
    fun selectTab(index: Int, tab: String) {
        selectedTab.value = tab
        globaltab.value = index
    }

    // Define the tabs
    val tabs = listOf("Menu", "Info")

    // Custom colors for the tab selector
    val selectedColor = MaterialTheme.colorScheme.tertiary // Green for selected
    val unselectedColor = MaterialTheme.colorScheme.tertiaryContainer// Light gray for unselected
    val textColor = Color.Black //color of the text

    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = unselectedColor,
        ),
    ) {
        // Container for the tabs, with a background and rounded corners
        Row(
            modifier = Modifier
                .padding(4.dp)
                .fillMaxWidth(), // Padding inside the background
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Absolute.SpaceEvenly
        ) {
            // Iterate through the tabs and create a TabItem for each
            tabs.forEachIndexed { i, tab ->
                TabItem(
                    tabName = tab,
                    isSelected = selectedTab.value == tab,
                    onTabSelected = { selectTab(i, tab) },
                    selectedColor = selectedColor,
                    unselectedColor = unselectedColor,
                    textColor = textColor
                )
            }
        }
    }
}

@Composable
fun TabItem(
    tabName: String,
    isSelected: Boolean,
    onTabSelected: () -> Unit,
    selectedColor: Color,
    unselectedColor: Color,
    textColor: Color
) {
    // Use a Surface for each tab item to get the rounded corners and background color
    Surface(
        modifier = Modifier
            .clickable { onTabSelected() } // Make the tab clickable
            .padding(4.dp), // Padding within each tab item
        color = if (isSelected) selectedColor else unselectedColor, // Background color based on selection
        shape = RoundedCornerShape(16.dp), // Rounded corners for each tab
    ) {
        // Content of the tab item (text)
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        {
            Text(
                text = tabName,
                style = TextStyle(
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = textColor // Text color
                ),
            )
        }
    }
}

// Preview the tab button
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    //TabPickerButton()
}
