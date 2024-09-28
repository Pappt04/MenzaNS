package com.pappt04.menzans

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Icon
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun InfoScreen(innerpadding: PaddingValues) {
    val context = LocalContext.current
    LazyColumn(
        modifier = Modifier
            .padding(innerpadding)
    ) {
        item {
            FacultyCard(context)
        }
        item {
            Card(
                modifier = Modifier
                    .padding(10.dp)
            ) {
                Column {
//                    items(DummyData.usefullLinks) { smartlink: DummyData.linkContainer ->
//                        LinkButton(context, weblink = smartlink)
//                    }
                    for (smartlink in DummyData.usefullLinks)
                        LinkButton(context, weblink = smartlink)
                }
            }
        }
    }
}


@Composable
fun LinkButton(context: Context, weblink: DummyData.linkContainer) {
    ElevatedButton(modifier = Modifier
        .padding(10.dp)
        .fillMaxWidth(),
        onClick = {
            openUrl(context, weblink.link)
        }) {
        Text(
            text = weblink.name.asString(context),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.SemiBold,
            fontStyle = FontStyle.Italic,
            fontSize = 16.sp,
            modifier = Modifier
                .padding(6.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FacultyCard(context: Context) {
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier
            .padding(8.dp)
            .clickable { isExpanded = !isExpanded }
    )
    {
        Column(
            modifier = Modifier
                .padding(10.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.unslogo_k100_transparent),
                contentDescription = stringResource(R.string.university_of_novi_sad),
                modifier = Modifier
                    .height(80.dp)
                    .fillMaxWidth()
                    .padding(8.dp)
            )
            Text(
                text = stringResource(R.string.university_of_novi_sad),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(8.dp)
            )
            RotatingIcon(isExpanded)
        }
        AnimatedVisibility(
            isExpanded,
            modifier = Modifier
                .animateContentSize(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioLowBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                )
        ) {
            Column {
                for (smartlink in DummyData.allUnsAcRswebsites)
                    LinkButton(context, weblink = smartlink)
            }
        }
    }
}

@ExperimentalMaterial3Api
@Composable
fun RotatingIcon(expanded: Boolean) {
    Icon(
        Icons.Filled.ArrowDropDown,
        null,
        Modifier.rotate(if (expanded) 180f else 0f)
    )
}

fun openUrl(context: Context, link: String) {
    var uri = Uri.parse(link)
    val intent = Intent(Intent.ACTION_VIEW, uri)

    context.startActivity(intent)
}