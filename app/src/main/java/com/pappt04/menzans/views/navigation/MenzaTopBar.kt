package com.pappt04.menzans.views.navigation

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import com.pappt04.menzans.R
import com.pappt04.menzans.views.UserID
import com.pappt04.menzans.views.common.AutoResizedText
import com.pappt04.menzans.data.consts.DummyData
import com.pappt04.menzans.data.consts.UsefulLinks
import com.pappt04.menzans.ui.theme.megatitleFont

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenzaTopBar(firstWelcome: MutableState<Boolean>,waitTime: MutableIntState,drawerState: DrawerState,screenTitle: String) {
    val showtopbarpopup= remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    CenterAlignedTopAppBar(colors = topAppBarColors(
        titleContentColor = MaterialTheme.colorScheme.primary,
    ), title = {
        if (!firstWelcome.value) {
            Text(
                screenTitle,
                softWrap = false,
                fontSize = 42.sp,
                fontFamily = megatitleFont,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.basicMarquee()
            )
        }
    }, navigationIcon = {
        IconButton(onClick =
        {
            val sendIntent = Intent(Intent.ACTION_SEND).apply {
                putExtra(Intent.EXTRA_TEXT, "Hej! Video sam da ${waitTime.intValue} minuta treba čekati na menzu.")
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(sendIntent, null)

            startActivity(context, shareIntent, null)
        }) {
            Icon(imageVector = Icons.Default.Share, contentDescription = "Share")
        }

//        IconButton(onClick = {
//            scope.launch {
//                drawerState.open()
//            }
//        }) {
//            Icon(
//                imageVector = Icons.Default.Menu,
//                contentDescription = stringResource(R.string.menu_description)
//            )
//        }
    },
        actions = {
            Box{
                IconButton(
                    onClick = { showtopbarpopup.value = !showtopbarpopup.value }
                ) {
                    Icon(
                        Icons.Default.MoreVert,
                        stringResource(R.string.menu_description)
                    )
                }
            }
            TopBarPopup(showtopbarpopup.value,){
                showtopbarpopup.value = false
            }
        }
    )
}


@Composable
private fun TopBarPopup(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
) {
    val context= LocalContext.current
    val urlHandler= LocalUriHandler.current

    DropdownMenu(expanded,onDismissRequest) {
        UsefulLinks.topbarLinks.forEach { item ->
            DropdownMenuItem(text = {
                AutoResizedText(item.name.asString(context))
            }, onClick = {
                urlHandler.openUri(item.link)
                onDismissRequest()
            })
        }
        DropdownMenuItem(text = {
            AutoResizedText("Delete My Data")
        },
            onClick = {

                val intent = Intent(Intent.ACTION_SENDTO)
                intent.data = Uri.parse("mailto:")
                intent.putExtra(Intent.EXTRA_EMAIL, arrayOf("apollo4.labs@gmail.com"))
                intent.putExtra(Intent.EXTRA_SUBJECT, "Contact from ${UserID.userid}")
                intent.putExtra(Intent.EXTRA_TEXT, "Hello, please delete my data")
                try {
                    context.startActivity(intent)
                } catch (e: Exception) {
                    e.printStackTrace()

                }
            }
        )
        DropdownMenuItem(text = {
            AutoResizedText("Developer Contact")
        },
            onClick = {
                val intent = Intent(Intent.ACTION_SENDTO)
                intent.data = Uri.parse("mailto:")
                intent.putExtra(Intent.EXTRA_EMAIL, arrayOf("apollo4.labs@gmail.com"))
                intent.putExtra(Intent.EXTRA_SUBJECT, "Contact from ${UserID.userid}")
                intent.putExtra(Intent.EXTRA_TEXT, "")
                try {
                    context.startActivity(intent)
                } catch (e: Exception) {
                    e.printStackTrace()

                }
            }
        )

    }
}
