package com.pappt04.menzans.views.welcome

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.GpsFixed
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.LocationSearching
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.pappt04.menzans.R
import com.pappt04.menzans.data.consts.GeofenceConstants
import com.pappt04.menzans.data.consts.PermissionData
import com.pappt04.menzans.geolocation.GeofenceManager
import com.pappt04.menzans.ui.theme.megatitleFont
import com.pappt04.menzans.views.common.AnimatedAppearance
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

private data class FeaturePageData(
    val icon: ImageVector,
    val titleRes: Int,
    val bodyRes: Int,
)

private val featurePages = listOf(
    FeaturePageData(Icons.Filled.CreditCard, R.string.onboarding_token_title, R.string.onboarding_token_body),
    FeaturePageData(Icons.Filled.AccessTime, R.string.onboarding_waittime_title, R.string.onboarding_waittime_body),
    FeaturePageData(Icons.Filled.Sensors, R.string.onboarding_location_title, R.string.onboarding_location_body),
    FeaturePageData(Icons.Filled.BarChart, R.string.onboarding_stats_title, R.string.onboarding_stats_body),
)

private val featurePageCount = 1 + featurePages.size  // hero + 4 features
private val totalPageCount = featurePageCount + 1      // + 1 permissions page

private fun permissionIcon(permission: String): ImageVector = when (permission) {
    Manifest.permission.ACCESS_COARSE_LOCATION -> Icons.Filled.LocationSearching
    Manifest.permission.ACCESS_FINE_LOCATION -> Icons.Filled.GpsFixed
    Manifest.permission.ACCESS_BACKGROUND_LOCATION -> Icons.Filled.LocationOn
    else -> Icons.Filled.Notifications
}

@Composable
fun WelcomeScreen(onCompleted: () -> Unit, innerpadding: PaddingValues) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = { totalPageCount })

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { _ ->
        val geofenceManager = GeofenceManager(context)
        for (geofence in GeofenceConstants.LANDMARKS) {
            geofenceManager.addGeofence(
                geofence.key,
                geofence.location,
                geofence.radiusInMeters,
                geofence.expirationTimeInMillis
            )
        }
        geofenceManager.registerGeofence()
        onCompleted()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerpadding),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f),
        ) { page ->
            when {
                page == 0 -> OnboardingHeroPage()
                page < featurePageCount -> {
                    val feature = featurePages[page - 1]
                    OnboardingFeaturePage(
                        icon = feature.icon,
                        title = stringResource(feature.titleRes),
                        body = stringResource(feature.bodyRes),
                    )
                }
                else -> OnboardingPermissionsPage()
            }
        }

        Spacer(Modifier.height(20.dp))
        PageDots(currentPage = pagerState.currentPage, totalPages = totalPageCount)
        Spacer(Modifier.height(24.dp))

        val isPermissionsPage = pagerState.currentPage == totalPageCount - 1
        val isLastFeaturePage = pagerState.currentPage == featurePageCount - 1

        if (isPermissionsPage) {
            Button(
                onClick = {
                    permissionLauncher.launch(PermissionData.permissionsNeeded.toTypedArray())
                },
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .height(52.dp)
            ) {
                Text(stringResource(R.string.onboarding_grant_all))
            }
            TextButton(
                onClick = { onCompleted() },
                modifier = Modifier.padding(top = 4.dp)
            ) {
                Text(stringResource(R.string.onboarding_skip))
            }
        } else {
            Button(
                onClick = {
                    scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) }
                },
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .height(52.dp)
            ) {
                Text(
                    if (isLastFeaturePage) stringResource(R.string.onboarding_get_started)
                    else stringResource(R.string.onboarding_next)
                )
                if (!isLastFeaturePage) {
                    Spacer(Modifier.width(8.dp))
                    Icon(Icons.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(18.dp))
                }
            }
            if (!isLastFeaturePage) {
                TextButton(
                    onClick = {
                        scope.launch { pagerState.animateScrollToPage(totalPageCount - 1) }
                    },
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    Text(stringResource(R.string.onboarding_skip_setup))
                }
            }
        }

        Spacer(Modifier.height(24.dp))
    }
}

@Composable
private fun OnboardingHeroPage() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        AnimatedAppearance(
            delay = 0.milliseconds,
            enter = fadeIn(tween(500)),
        ) {
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.size(128.dp),
            ) {
                Icon(
                    painter = painterResource(R.drawable.applogo_vector),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier
                        .padding(24.dp)
                        .fillMaxSize(),
                )
            }
        }

        Spacer(Modifier.height(32.dp))

        AnimatedAppearance(
            delay = 250.milliseconds,
            enter = fadeIn(tween(400)) + slideInVertically(tween(400)) { it / 3 },
        ) {
            Text(
                text = stringResource(R.string.welcome_title),
                style = MaterialTheme.typography.displaySmall,
                fontFamily = megatitleFont,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary,
            )
        }

        Spacer(Modifier.height(16.dp))

        AnimatedAppearance(
            delay = 450.milliseconds,
            enter = fadeIn(tween(400)) + slideInVertically(tween(400)) { it / 3 },
        ) {
            Text(
                text = stringResource(R.string.onboarding_tagline),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun OnboardingFeaturePage(icon: ImageVector, title: String, body: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        AnimatedAppearance(
            delay = 0.milliseconds,
            enter = fadeIn(tween(400)),
        ) {
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.size(112.dp),
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier
                        .padding(24.dp)
                        .fillMaxSize(),
                )
            }
        }

        Spacer(Modifier.height(36.dp))

        AnimatedAppearance(
            delay = 150.milliseconds,
            enter = fadeIn(tween(400)) + slideInVertically(tween(400)) { it / 3 },
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }

        Spacer(Modifier.height(16.dp))

        AnimatedAppearance(
            delay = 300.milliseconds,
            enter = fadeIn(tween(400)) + slideInVertically(tween(400)) { it / 3 },
        ) {
            Text(
                text = body,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun OnboardingPermissionsPage() {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Spacer(Modifier.height(16.dp))

        AnimatedAppearance(
            delay = 0.milliseconds,
            enter = fadeIn(tween(400)) + slideInVertically(tween(400)) { it / 3 },
        ) {
            Text(
                text = stringResource(R.string.onboarding_permissions_title),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }

        Spacer(Modifier.height(12.dp))

        AnimatedAppearance(
            delay = 150.milliseconds,
            enter = fadeIn(tween(400)),
        ) {
            Text(
                text = stringResource(R.string.onboarding_permissions_body),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        Spacer(Modifier.height(28.dp))

        PermissionData.permissionsNeeded.forEachIndexed { index, permission ->
            val explanation = PermissionData.permissionExplanations.getOrNull(index)
            AnimatedAppearance(
                delay = (200 + index * 100).milliseconds,
                enter = fadeIn(tween(350)) + slideInVertically(tween(350)) { it / 3 },
            ) {
                PermissionRow(
                    icon = permissionIcon(permission),
                    name = explanation?.name?.asString(context) ?: permission,
                    explanation = explanation?.explanation?.asString(context) ?: "",
                )
            }
            Spacer(Modifier.height(8.dp))
        }

        Spacer(Modifier.height(8.dp))
    }
}

@Composable
private fun PermissionRow(icon: ImageVector, name: String, explanation: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.secondaryContainer,
                modifier = Modifier.size(44.dp),
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSecondaryContainer,
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxSize(),
                )
            }
            Spacer(Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = explanation,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@Composable
private fun PageDots(currentPage: Int, totalPages: Int) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        repeat(totalPages) { index ->
            val isSelected = currentPage == index
            val dotColor by animateColorAsState(
                targetValue = if (isSelected) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.outlineVariant,
                animationSpec = tween(300),
                label = "dot_color_$index",
            )
            val dotWidth by animateDpAsState(
                targetValue = if (isSelected) 22.dp else 8.dp,
                animationSpec = tween(300),
                label = "dot_width_$index",
            )
            Box(
                modifier = Modifier
                    .height(8.dp)
                    .width(dotWidth)
                    .clip(CircleShape)
                    .background(dotColor),
            )
        }
    }
}
