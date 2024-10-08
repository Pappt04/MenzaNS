package com.pappt04.menzans

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.pappt04.menzans.ui.theme.MenzaNSTheme


@Composable
fun BalanceDialog(
    onDismissRequest: () -> Unit,
    balance: MutableState<Int>,
    context: Context,
    filename: String
) {
    var mText by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.add_to_balance),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier
                        .fillMaxWidth()
                )
                Row {
                    TextField(
                        value = mText,
                        onValueChange = { mText = it },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        textStyle = LocalTextStyle.current.copy(
                            textAlign = TextAlign.Right,
                            fontSize = 22.sp
                        ),
                        prefix = {
                            Text(text = "+",
                                fontSize = 22.sp
                            )
                        },
                        suffix = {
                            Text("rsd",
                                fontSize = 22.sp
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = {
                            Toast.makeText(
                                context,
                                context.getString(R.string.your_balance_is_still_rsd, balance.value.toString()),
                                Toast.LENGTH_SHORT
                            ).show()
                            saveToFile(context, filename, balance.value,false)
                            onDismissRequest()
                        },
                    ) {
                        Text(stringResource(R.string.dismiss))
                    }
                    Button(onClick = {
                        try {
                            balance.value += mText.toInt()

                            if (balance.value < 0)
                                balance.value = 0

                            saveToFile(context, filename, balance.value,false)
                        } catch (_: Exception) {
                        }
                        Toast.makeText(
                            context,
                            context.getString(R.string.your_balance_is_now_rsd, balance.value.toString()),
                            Toast.LENGTH_SHORT
                        ).show()
                        onDismissRequest()
                    }) {
                        Text(stringResource(R.string.confirm))
                    }
                }
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
fun PreviewBalanceDialog() {
    MenzaNSTheme {
        //BalanceDialog(onDismissRequest = {}, 1500, LocalContext.current, "breakfast")
    }
}
