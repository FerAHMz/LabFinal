package com.example.labfinal.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.labfinal.presentation.list.AssetsScreen
import com.example.labfinal.presentation.profile.AssetDetailScreen

sealed class Screen(val route: String) {
    object Assets : Screen("assets")
    data class AssetDetail(val assetId: String) : Screen("assetDetail/{assetId}") {
        fun createRoute() = "assetDetail/$assetId"
    }
}

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Assets.route,
        modifier = modifier
    ) {
        composable(Screen.Assets.route) {
            AssetsScreen(onAssetClick = { assetId ->
                navController.navigate(Screen.AssetDetail(assetId).createRoute())
            })
        }

        composable(
            route = Screen.AssetDetail("{assetId}").route,
            arguments = listOf(navArgument("assetId") { defaultValue = "" })
        ) { backStackEntry ->
            val assetId = backStackEntry.arguments?.getString("assetId").orEmpty()
            AssetDetailScreen(assetId = assetId, navController = navController)
        }
    }
}
