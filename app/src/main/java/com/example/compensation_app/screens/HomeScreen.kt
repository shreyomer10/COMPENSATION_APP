package com.example.compensation_app.screens

import android.widget.Button
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat.recreate
import com.example.compensation_app.Backend.Guard
import com.example.compensation_app.Navigation.NavigationScreens
import com.example.compensation_app.Navigation.clearLoginStatus
import com.example.compensation_app.R
import com.example.compensation_app.components.SignOut
import com.example.compensation_app.ui.theme.LanguageManager
import com.example.compensation_app.ui.theme.LanguageSwitchScreen
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson

import kotlinx.coroutines.launch
import java.net.URLEncoder


@Composable
fun HomeScreen(navController: NavController) {
    val context= LocalContext.current
    // Drawer state
    val gson = Gson()

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope() // To open/close the drawer
    var showLogoutDialog by remember { mutableStateOf(false) }
    val contentColor = Color.White
    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet {
                Text(
                    text = "Logout (लॉगआउट)",
                    modifier = Modifier.padding(16.dp)
                        .clickable { showLogoutDialog = true },
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = if (showLogoutDialog) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary
                    )
                )

                if (showLogoutDialog) {
                    AlertDialog(
                        onDismissRequest = { showLogoutDialog = false },
                        title = {
                            Text(
                                text = "Logout (लॉगआउट)",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                        },
                        text = {
                            Text(
                                "Are you sure you want to logout? (क्या आप सुनिश्चित हैं कि आप लॉगआउट करना चाहते हैं?)",
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    showLogoutDialog = false
                                    SignOut(navController)
                                    clearLoginStatus(context = context)
                                }
                            ) {
                                Text("Yes, Logout (हां, लॉगआउट करें)")
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { showLogoutDialog = false }) {
                                Text("Cancel (रद्द करें)")
                            }
                        }
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
                greetings = "Welcome (स्वागत है)",
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
                    onClick = { navController.navigate(NavigationScreens.NewApplicationScreen.name)/* Navigate to Submit New Application Screen */ },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(Color(0xFF4379FF))
                ) {
                    Text(text = "Submit New Application (नई आवेदन प्रस्तुत करें)", color = Color.White)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { /* Navigate to Draft Applications Screen */ },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(Color(0xFF4379FF))
                ) {
                    Text(text = "Draft Applications (मसौदा आवेदन)", color = Color.White)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { navController.navigate(NavigationScreens.PrevApplicationScreen.name)/* Navigate to Previous Applications Screen */ },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(Color(0xFF5D8BFF))
                ) {
                    Text(text = "Previous Applications (पिछले आवेदन)", color = Color.White)
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
                    contentDescription = "Menu (मेनू)",
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
