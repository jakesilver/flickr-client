package com.jakesilver.takehome.scintillate.navigation

import android.app.Activity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.jakesilver.takehome.app.R
import com.jakesilver.takehome.scintillate.PhotoViewModel
import com.jakesilver.takehome.scintillate.compose.DetailScreen
import com.jakesilver.takehome.scintillate.compose.Home
import org.koin.androidx.compose.getViewModel

@Composable
fun ScintillateApp(
    photoViewModel: PhotoViewModel = getViewModel(),
) {
    val navController = rememberNavController()
    ScintillateNavHost(
        photoViewModel = photoViewModel,
        navController = navController,
    )
}

@Composable
fun ScintillateNavHost(
    photoViewModel: PhotoViewModel = getViewModel(),
    navController: NavHostController,
) {
    NavHost(navController = navController, startDestination = "photo_search") {
        composable("photo_search") {
            Home(
                photoViewModel = photoViewModel,
                onPhotoClick = { navController.navigate("photoDetails/${it.id}") },
                modifier = Modifier
                    .fillMaxSize()
                    .systemBarsPadding(),
            )
        }
        composable(
            route = "photoDetails/{photoId}",
            arguments = listOf(navArgument("photoId") {
                type = NavType.StringType
            })
        ) {
            DetailScreen(
                title = stringResource(id = R.string.photo_details_title),
                onUpClicked = { navController.navigateUp() },
                photoViewModel = photoViewModel,
                modifier = Modifier
                    .fillMaxSize()
                    .systemBarsPadding(),
            )
        }
    }
}