package com.example.prueba

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.prueba.ui.theme.LabFinalTheme
import com.example.prueba.presentation.list.AssetsScreen
import com.example.prueba.presentation.profile.AssetDetailScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LabFinalTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(title = { Text("Crypto App") })
        }
    ) { innerPadding ->
        SetupNavGraph(
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
fun SetupNavGraph(navController: NavController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = "assets_screen",
        modifier = modifier
    ) {
        composable("assets_screen") {
            AssetsScreen(onAssetClick = { assetId ->
                navController.navigate("asset_detail/$assetId")
            })
        }
        composable("asset_detail/{assetId}") { backStackEntry ->
            val assetId = backStackEntry.arguments?.getString("assetId") ?: return@composable
            AssetDetailScreen(assetId = assetId)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    LabFinalTheme {
        MainScreen()
    }
}
