package com.pappt04.menzans

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pappt04.menzans.ui.theme.MenzaNSTheme


/*
    surname: String,
    name: String,
    faculty: String,
    birth: String,
    issued: String,
    valid: String,
    index: String,
    cardnumber: String,
    isicnumber: String,
    context: Context
 */

@Composable
fun MenzaCard(cardData: List<String>) {
    OutlinedCard(
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Cyan,
        ),
        border = BorderStroke(1.dp, Color.Black),
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Column {
            Row {
                Image(
                    painter = painterResource(id = R.drawable.isic_logo),
                    contentDescription = "ISIC Logo",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .padding(4.dp)
                        .weight(1f)
                )
                Text(
                    text = stringResource(R.string.isic_card_number)+ cardData[8],
                    modifier = Modifier
                        .padding(8.dp)
                )
            }
            Row {
                Icon(
                    imageVector = Icons.Filled.Email,
                    contentDescription = "Symbolic Chip in card",
                    tint = Color.Yellow,
                    modifier = Modifier
                        .weight(0.6f)
                        .height(70.dp)
                )
                Image(
                    painter = painterResource(id = R.drawable.eyca_logo),
                    contentDescription = "EYC logo",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .padding(4.dp)
                        .weight(0.6f)
                )
                Text(
                    "Europen \n Youth card",
                    textAlign = TextAlign.Left,
                    color = Color.Red,
                    style = MaterialTheme.typography.bodyMedium,
                )
                Image(
                    painter = painterResource(id = R.drawable.coat_of_arms_of_serbia_small),
                    contentDescription = "Coat of arms ",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .padding(4.dp)
                        .weight(0.6f)
                )
                Icon(
                    imageVector = Icons.Filled.AccountBox,
                    contentDescription = "Profile photo",
                    tint = Color.Black,
                    modifier = Modifier
                        .size(80.dp)
                        .weight(1f)
                )
            }
            OutlinedTextField(
                value = cardData[2],
                onValueChange = { print("Clicked") },
                label = { Text(text = "Studies at") },
                readOnly = true,
                textStyle = LocalTextStyle.current.copy(
                    fontSize = 22.sp
                ),
                modifier = Modifier
                    .padding(4.dp)
            )
            Row()
            {
                OutlinedTextField(
                    value = cardData[1] + " " + cardData[0],
                    onValueChange = { print("Clicked") },
                    label = { Text(text = "Cardholder name") },
                    readOnly = true,
                    modifier = Modifier
                        .padding(4.dp)
                        .weight(1.5f),
                )
                OutlinedTextField(
                    value = cardData[4],
                    onValueChange = { print("Clicked") },
                    label = { Text(text = "Issued") },
                    readOnly = true,

                    modifier = Modifier
                        .padding(4.dp)
                        .weight(1f),
                )
                OutlinedTextField(
                    value = cardData[6],
                    onValueChange = { print("Clicked") },
                    label = { Text(text = "Index") },
                    readOnly = true,
                    modifier = Modifier
                        .padding(4.dp)
                        .weight(1f),
                )
            }
            Row()
            {
                OutlinedTextField(
                    value = cardData[3],
                    onValueChange = { print("Clicked") },
                    label = { Text(text = "Date of birth") },
                    readOnly = true,
                    modifier = Modifier
                        .padding(4.dp)
                        .weight(1.5f),
                )
                OutlinedTextField(
                    value = cardData[5],
                    onValueChange = { print("Clicked") },
                    label = { Text(text = "Valid until") },
                    readOnly = true,

                    modifier = Modifier
                        .padding(4.dp)
                        .weight(1f),
                )
                OutlinedTextField(
                    value = cardData[8],
                    onValueChange = { print("Clicked") },
                    label = { Text(text = "Card number") },
                    readOnly = true,
                    modifier = Modifier
                        .padding(4.dp)
                        .weight(1f),
                )
            }
        }
    }
}

@Preview(name = "Light Mode")
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    name = "Dark Mode"
)
@Composable
fun PreviewMenzaCard() {
    MenzaNSTheme {
        MenzaCard(
            listOf(
                "Papp",
                "Tamas",
                "FTN",
                "2004 01 30",
                "2024 10 5",
                "2024 10 31",
                "RA4/2022",
                "212301047",
                "S381000235412P"
            )
        )
    }
}
