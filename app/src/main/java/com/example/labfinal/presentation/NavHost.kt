package com.example.prueba.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.prueba.presentation.list.AssetsScreen
import com.example.prueba.presentation.profile.AssetDetailScreen
import com.example.prueba.presentation.profile.AssetDetailViewModel
import com.example.prueba.presentation.profile.AssetDetailViewModelFactory
import com.example.prueba.di.ServiceLocator

sealed class Screen(val route: String) {
    object Assets : Screen("assets")
    object AssetDetail : Screen("assetDetail/{assetId}") {
        fun createRoute(assetId: String) = "assetDetail/$assetId"
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
            AssetsScreen(onAssetClick = { asset ->
                navController.navigate(Screen.AssetDetail.createRoute(asset.id))
            })
        }
        composable(
            route = Screen.AssetDetail.route,
            arguments = listOf(navArgument("assetId") { defaultValue = "" })
        ) { backStackEntry ->
            val assetId = backStackEntry.arguments?.getString("assetId") ?: ""
            val repository = ServiceLocator.provideCryptoRepository(navController.context)
            val factory = AssetDetailViewModelFactory(repository, assetId)
            val viewModel = androidx.lifecycle.ViewModelProvider(
                navController.currentBackStackEntry!!,
                factory
            )[AssetDetailViewModel::class.java]

            // Pasar el viewModel al AssetDetailScreen
            AssetDetailScreen(viewModel = viewModel)
        }
    }
}
