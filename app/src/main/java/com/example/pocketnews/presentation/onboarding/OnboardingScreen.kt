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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.pocketnews.presentation.Screen

@Composable
fun OnboardingScreen(
    viewModel: OnboardingViewModel = hiltViewModel(),
    onOnboardingComplete: () -> Unit = {},
    navController: NavController
) {
    val state = viewModel.uiState

    Column(
        modifier = Modifier.fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("POCKET NEWS",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp))
        Text("Pocket News, seçtiğiniz kategorileri sizin yerinize takip eder ve yeni bir gelişme olduğunda sizi anında bilgilendirir." ,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(
                start = 8.dp,
                end = 8.dp,
                bottom = 24.dp
            ))

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

        Button(
            onClick = {
                viewModel.saveSelection()
                onOnboardingComplete()
                navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.Onboarding.route) {inclusive = true}
                }
        },
            enabled = state.selectedCategory != null && state.selectedInterval !=null
        ) {
            Text("BAŞLAYALIM")
        }

    }
}