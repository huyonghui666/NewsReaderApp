package com.example.newsreader.newsreaderlogin.ui.Screen

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.newsreader.newsreaderlogin.ui.viewmodel.MainViewModel

//@Composable
//fun AppNavigation(navController: NavHostController) {
//    NavHost(
//        navController = navController,
//        startDestination = "main"
//    ) {
//        composable(route = "main") {
//            val viewModel: MainViewModel = hiltViewModel()
//            MainScreen(navController = navController,viewModel=viewModel)
//        }
//
//        composable(
//            route = "agreement/{type}",
//            arguments = listOf(
//                navArgument("type") {
//                    type = NavType.StringType
//                    defaultValue = "user"
//                }
//            )
//        ) { backStackEntry ->
//            val agreementType = when(backStackEntry.arguments?.getString("type")) {
//                "user" -> AgreementType.USER_AGREEMENT
//                "privacy" -> AgreementType.PRIVACY_POLICY
//                else -> AgreementType.USER_AGREEMENT
//            }
//            AgreementScreen(
//                navController = navController,
//                agreementType = agreementType
//            )
//        }
//
//    }
//}