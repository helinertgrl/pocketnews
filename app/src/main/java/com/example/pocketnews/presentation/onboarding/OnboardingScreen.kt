package com.example.pocketnews.presentation.onboarding

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.pocketnews.presentation.Screen
import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Switch
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.google.firebase.logger.Logger
import kotlin.contracts.contract

@Composable
fun OnboardingScreen(
    viewModel: OnboardingViewModel = hiltViewModel(),
    onOnboardingComplete: () -> Unit = {},
    navController: NavController
) {
    val state = viewModel.uiState
    val context = LocalContext.current

    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        viewModel.onNotificationPermissionResult(isGranted)
        if (isGranted){
            Log.d("OnboardScreen", "Bildirim izni verildi")
        }else{
            Log.d("OnboardingScreen","Bildirim izni reddedildi")
        }
    }

    //İlk açılışta izin (android 13+)
    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            val hasPermission = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED

            if (!hasPermission){
                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            } else{
                viewModel.onNotificationPermissionResult(true)
            }
        }else{
            //Android 12- izin oto var ama kullanıcı karar verecek.
            viewModel.onNotificationPermissionResult(true)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "POCKET NEWS",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            "Pocket News, seçtiğiniz kategorileri sizin yerinize takip eder ve yeni bir gelişme olduğunda sizi anında bilgilendirir." ,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(
                start = 8.dp,
                end = 8.dp,
                bottom = 24.dp
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("Hangi kategorideki haberleri takip etmek istersiniz?")

            Column(
                modifier = Modifier.fillMaxWidth()
            ){
                state.categories.forEach { option ->
                    Row (
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {viewModel.onCategorySelected(option)}
                    ) {
                        RadioButton(
                            selected = (state.selectedCategory == option),
                            onClick = {viewModel.onCategorySelected(option)}
                        )
                        Text(option)
                    }
                }
            }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Ne sıklıkla kontrol edilsin?")

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                state.intervals.forEach { option ->
                    Row (
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable{viewModel.onIntervalSelected(option)}
                    ){
                        RadioButton(
                            selected = (state.selectedInterval == option),
                            onClick = {viewModel.onIntervalSelected(option)}
                        )
                        Text("$option saat")
                    }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        HorizontalDivider(
            modifier = Modifier.padding(vertical = 16.dp),
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Bildirimler",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "Yeni haberlerden haberdar ol",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Switch(
                checked = state.notificationsEnabled,
                onCheckedChange = {viewModel.onNotificationToggled(it)}
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                viewModel.saveSelection()
                onOnboardingComplete()
                navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.Onboarding.route) {inclusive = true}
                }
            },
            enabled = state.selectedCategory.isNotEmpty() && state.selectedInterval != null,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("BAŞLAYALIM")
        }
    }
}