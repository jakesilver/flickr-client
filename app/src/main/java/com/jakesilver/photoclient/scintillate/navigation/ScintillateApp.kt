package com.jakesilver.photoclient.scintillate.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.jakesilver.photoclient.app.R
import com.jakesilver.photoclient.scintillate.viewmodels.PhotoSearchViewModel
import com.jakesilver.photoclient.scintillate.compose.DetailScreen
import com.jakesilver.photoclient.scintillate.compose.Home
import org.koin.androidx.compose.koinViewModel

@Composable
fun ScintillateApp(
    photoSearchViewModel: PhotoSearchViewModel = koinViewModel(),
) {
    val navController = rememberNavController()
    ScintillateNavHost(
        photoSearchViewModel = photoSearchViewModel,
        navController = navController,
    )
}

@Composable
fun ScintillateNavHost(
    photoSearchViewModel: PhotoSearchViewModel = koinViewModel(),
    navController: NavHostController,
) {
    NavHost(navController = navController, startDestination = "photo_search") {
        composable("photo_search") {
            Home(
                photoSearchViewModel = photoSearchViewModel,
                onPhotoClick = { navController.navigate("photoDetails/${it.id}") },
                modifier = Modifier
                    .fillMaxSize()
                    .systemBarsPadding(),
            )
        }
        composable(
            route = "photoDetails/{photoId}",
            arguments = listOf(
                navArgument("photoId") {
                    type = NavType.StringType
                },
            ),
        ) {
            DetailScreen(
                title = stringResource(id = R.string.photo_details_title),
                onUpClicked = { navController.navigateUp() },
                modifier = Modifier
                    .fillMaxSize()
                    .systemBarsPadding()
                ,
            )
        }
    }
}
