package com.example.compensation_app.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button

import androidx.compose.material3.ButtonDefaults

import androidx.compose.material3.Text

import androidx.compose.ui.graphics.Color
import com.example.compensation_app.components.TopAppBar



import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch


@Composable
fun HomeScreen(navController: NavController) {
    // Drawer state
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope() // To open/close the drawer
    var showLogoutDialog by remember { mutableStateOf(false) }
    val contentColor = Color.White
    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet {
                Text(
                    text = "Menu Item 1",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "Menu Item 2",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "Menu Item 3",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.titleMedium
                )
                IconButton(onClick = { showLogoutDialog = true }) {
                    Icon(
                        imageVector = Icons.Filled.ExitToApp, // Changed to logout icon
                        contentDescription = "Logout",
                        tint = contentColor
                    )
                }
            }
        },
        drawerState = drawerState // Attach the DrawerState here
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // TopAppBar with hamburger menu
            TopAppBar(
                navController = navController,
                greetings = "Welcome",
                onMenuClick = { // Open the drawer on menu click
                    scope.launch {
                        drawerState.open() // Open the drawer
                    }
                }
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Center the buttons vertically
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = { /* Navigate to Submit New Application Screen */ },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(Color(0xFF4379FF))
                ) {
                    Text(text = "Submit New Application", color = Color.White)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { /* Navigate to Draft Applications Screen */ },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(Color(0xFF4379FF))
                ) {
                    Text(text = "Draft Applications", color = Color.White)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { /* Navigate to Previous Applications Screen */ },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(Color(0xFF5D8BFF))
                ) {
                    Text(text = "Previous Applications", color = Color.White)
                }
            }
        }
    }
}

@Composable
fun TopAppBar(navController: NavController, greetings: String, onMenuClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Hamburger menu icon
            IconButton(onClick = onMenuClick) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Menu",
                    tint = Color.Black
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = greetings,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }
    }
}