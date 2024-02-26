/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.lunchtray

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.lunchtray.ui.EntreeMenuScreen
import com.example.lunchtray.ui.OrderViewModel
import com.example.lunchtray.ui.StartOrderScreen
import com.example.lunchtray.datasource.DataSource
import com.example.lunchtray.model.OrderUiState
import com.example.lunchtray.ui.AccompanimentMenuScreen
import com.example.lunchtray.ui.CheckoutScreen
import com.example.lunchtray.ui.SideDishMenuScreen

// TODO: Screen enum
// in order to make the app bar responsive we need to modify
// the enum class a bit, add string resource title
enum class LunchTrayScreens(@StringRes val title: Int) {
    Start(title = R.string.app_name),
    Entree(title = R.string.choose_entree),
    SideDish(title = R.string.choose_side_dish),
    Accompaniment(title = R.string.choose_accompaniment),
    Checkout(title = R.string.order_checkout)
}

// TODO: AppBar
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LunchTrayAppBar(
    // add param currentScreen to display the current screen in the app bar
    currentScreen: LunchTrayScreens,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        // we add dynamic title using our selected string resource name id
        title = { Text(stringResource(id = currentScreen.title)) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LunchTrayApp(
    viewModel: OrderViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
) {
    // TODO: Create Controller and initialization
    // create a back stack controller to go back pages
    val backStackEntry by navController.currentBackStackEntryAsState()

    // now make a variable that holds the current screen state as a LunchTrayScreen enum position
    val currentScreen = LunchTrayScreens.valueOf(
        backStackEntry?.destination?.route ?: LunchTrayScreens.Start.name
    )
    // Create ViewModel
    val viewModel: OrderViewModel = viewModel()

    Scaffold(
        topBar = {
            // TODO: AppBar
            LunchTrayAppBar(
                // to make sure we can navigate back to the previous screen, we make a conditional
                // statement for the canNavigateBack param, this is a logic of our custom element,
                // to display or don't display our back button if not null, can navigate back else,
                // can't navigate back
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = {
                    /*TODO*/
                    navController.navigateUp()
                },
                currentScreen = currentScreen,
            )
        }
    ) { innerPadding ->
        val uiState by viewModel.uiState.collectAsState()

        // TODO: Navigation host
        // created navigation controller
        NavHost(
            navController = navController,
            startDestination = LunchTrayScreens.Start.name,
            modifier = Modifier
                .padding(innerPadding)
        ) {

            // now creating the first composable for the start page
            // on first stage it is just displaying the screen with no actions implemented
            // but the buttons are being added as the practice excercise advances
            composable(route = LunchTrayScreens.Start.name) {
                StartOrderScreen(onStartOrderButtonClicked = {
                    /*TODO*/
                        navController.navigate(LunchTrayScreens.Entree.name)
                    },
                    // the modifier
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(dimensionResource(id = R.dimen.padding_medium))
                    )
            }
            
            // make the choose entree menu screen route
            composable(route = LunchTrayScreens.Entree.name) {
                // we need to have context to call classes and resources specific to our app
                val context = LocalContext.current
                EntreeMenuScreen(
                    options = DataSource.entreeMenuItems,
                    onCancelButtonClicked = {
                        /*TODO*/
                        navController.navigate(LunchTrayScreens.Start.name)
                    },
                    onNextButtonClicked = {
                        /*TODO*/
                        navController.navigate(LunchTrayScreens.SideDish.name)
                    },
                    onSelectionChanged = {
                        /*TODO*/
                        viewModel.updateEntree(it)
                    },
                    modifier = Modifier
                        .padding(dimensionResource(R.dimen.padding_medium))
                        .verticalScroll(rememberScrollState())
                )
            }

            // make the choose side dish menu route
            composable(route = LunchTrayScreens.SideDish.name) {
                SideDishMenuScreen(
                    options = DataSource.sideDishMenuItems,
                    onCancelButtonClicked = {
                        /*TODO*/
                        navController.navigate(LunchTrayScreens.Start.name)
                    },
                    onNextButtonClicked = {
                        /*TODO*/
                        navController.navigate(LunchTrayScreens.Accompaniment.name)
                    },
                    onSelectionChanged = {
                        /*TODO*/
                        viewModel.updateSideDish(it)
                    },
                    modifier = Modifier
                        .padding(dimensionResource(R.dimen.padding_medium))
                        .verticalScroll(rememberScrollState())
                )
            }

            // make the choose accompaniment menu route
            composable(route = LunchTrayScreens.Accompaniment.name) {
                AccompanimentMenuScreen(
                    options = DataSource.accompanimentMenuItems,
                    onCancelButtonClicked = {
                        /*TODO*/
                        navController.navigate(LunchTrayScreens.Start.name)
                    },
                    onNextButtonClicked = {
                        /*TODO*/
                        navController.navigate(LunchTrayScreens.Checkout.name)
                    },
                    onSelectionChanged = {
                        /*TODO*/
                        viewModel.updateAccompaniment(it)
                    },
                    modifier = Modifier
                        .padding(dimensionResource(R.dimen.padding_medium))
                        .verticalScroll(rememberScrollState())
                )
            }

            // make the checkout menu route
            composable(route = LunchTrayScreens.Checkout.name) {
                CheckoutScreen(
//                    orderUiState = OrderUiState(
//                        entree = DataSource.entreeMenuItems[0],
//                        sideDish = DataSource.sideDishMenuItems[0],
//                        accompaniment = DataSource.accompanimentMenuItems[0],
//                        itemTotalPrice = 15.00,
//                        orderTax = 1.00,
//                        orderTotalPrice = 16.00
//                    ),
                    orderUiState = uiState,
                    onNextButtonClicked = {
                        /*TODO*/
                        navController.navigate(LunchTrayScreens.Start.name)
                    },
                    onCancelButtonClicked = {
                        /*TODO*/
                        navController.navigate(LunchTrayScreens.Start.name)
                    },
                    modifier = Modifier
                        .padding(dimensionResource(R.dimen.padding_medium))
                        .verticalScroll(rememberScrollState())
                )
            }
        }
    }
}
