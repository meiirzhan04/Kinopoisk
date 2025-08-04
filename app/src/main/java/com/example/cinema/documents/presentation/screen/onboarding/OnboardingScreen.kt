package com.example.cinema.documents.presentation.screen.onboarding

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cinema.R

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(
    onSkip: () -> Unit,
) {
    val pages = listOf(
        OnboardingPage(
            imageRes = R.drawable.ic_onboardingfirst,
            title = "Узнавай\nо премьерах"
        ),
        OnboardingPage(
            imageRes = R.drawable.ic_onboardingsecond,
            title = "Создавай\nколлекции"
        ),
        OnboardingPage(
            imageRes = R.drawable.ic_onboardingthird,
            title = "Делись\nс друзьями"
        )
    )

    val pagerState = rememberPagerState(pageCount = { pages.size })

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 26.dp, vertical = 32.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                painter = painterResource(R.drawable.ic_text),
                contentDescription = "Text Icon"
            )
            Text(
                text = "Пропустить",
                color = Color(0xFFB5B5C9),
                fontSize = 14.sp,
                fontWeight = FontWeight.W500,
                modifier = Modifier.clickable { onSkip() }
            )
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxHeight(0.6f)
                .fillMaxWidth(),
            verticalAlignment = Alignment.Bottom
        ) { page ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally),
                contentAlignment = Alignment.BottomCenter,
            ) {
                Image(
                    painter = painterResource(pages[page].imageRes),
                    contentDescription = "Onboarding Image"
                )
            }
        }

        Spacer(modifier = Modifier.height(70.dp))

        Text(
            text = pages[pagerState.currentPage].title,
            style = MaterialTheme.typography.headlineMedium.copy(
                fontSize = 32.sp,
                lineHeight = 40.sp
            ),
            fontWeight = FontWeight.W500,
            color = Color.Black,
            modifier = Modifier.padding(horizontal = 26.dp)
        )

        Spacer(modifier = Modifier.height(56.dp))

        DotsIndicator(
            totalDots = pages.size,
            selectedIndex = pagerState.currentPage,
            selectedColor = Color.Black,
            unSelectedColor = Color(0xFFB5B5C9),
            modifier = Modifier.padding(horizontal = 26.dp)
        )

        Spacer(modifier = Modifier.height(40.dp))
    }
}

data class OnboardingPage(val imageRes: Int, val title: String)

