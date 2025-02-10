package com.example.compensation_app.screens.guard

import android.util.Log
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

import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.compensation_app.Backend.emp
import com.example.compensation_app.Navigation.NavigationScreens
import com.example.compensation_app.Navigation.clearLoginStatus
import com.example.compensation_app.components.SignOut
import com.example.compensation_app.sqlite.MainViewModel

import com.example.compensation_app.viewmodel.GuardViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson

import kotlinx.coroutines.launch
import java.net.URLEncoder
import java.nio.charset.StandardCharsets


@Composable
fun HomeScreen(navController: NavController) {



    val context= LocalContext.current
    // Drawer state
    val gson = Gson()

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope() // To open/close the drawer
    var showLogoutDialog by remember { mutableStateOf(false) }
    val contentColor = Color.White

    val viewModel: GuardViewModel = hiltViewModel()
    val mainViewModel: MainViewModel = hiltViewModel()
    val auth = FirebaseAuth.getInstance()
    val mobileNumber = auth.currentUser?.phoneNumber
    val formattedNumber = mobileNumber?.replace("+91", "") ?: ""
    Log.d("format", "$formattedNumber")
    var gguard by remember {
        mutableStateOf<emp>(emp(emp_id = "",
            mobile_number = "",
            Name = "",
            Circle_CG = "",
            Circle1 = "",
            roll = "guard",
            subdivision = "",
            division = "", range_ = "", beat = 0))
    }
//    viewModel.searchByMobile(mobile = formattedNumber) {guard, Message->
//        if (guard != null) {
//            Log.d("Final 0", "NewApplication: $guard")
//            gguard=guard
//            println("Guard fetched: $guard")
//        } else {
//            println("Error: $Message")
//        }
//    }
    mainViewModel.GetGuard {
        if (it != null) {
            gguard=it
        }
    }

    Log.d("Final", "NewApplication:${gguard.emp_id} ")
    val guardJson = gson.toJson(gguard)
    val encodedGuardJson = URLEncoder.encode(guardJson, StandardCharsets.UTF_8.toString())

    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet {
                Text(
                    text = "Logout (लॉगआउट)",
                    modifier = Modifier
                        .padding(16.dp)
                        .clickable { showLogoutDialog = true },
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = if (showLogoutDialog) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary
                    )
                )

                // Guard details section
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "UserId: ${gguard.emp_id}",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = if (showLogoutDialog) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp)) // Space between details
                    Row {
                        Text(
                            text = "Mobile: ",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = if (showLogoutDialog) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary
                            )
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = gguard.mobile_number,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = if (showLogoutDialog) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary
                            )
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp)) // Space between details
                    Text(
                        text = "Circle: ${gguard.Circle_CG}",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = if (showLogoutDialog) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary
                        )
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Division :${gguard.division} ",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = if (showLogoutDialog) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary
                        )
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "SubDivision :${gguard.subdivision} ",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = if (showLogoutDialog) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp)) // Space between details
                    Row {
                        Text(
                            text = "Area: ",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = if (showLogoutDialog) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary
                            )
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${gguard.range_} ${gguard.Circle1} ${gguard.beat}",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = if (showLogoutDialog) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary
                            )
                        )
                    }
                }

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
                                    mainViewModel.deleteEmp(emp=gguard)
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
                    onClick = { navController.navigate(NavigationScreens.NewApplicationScreen.name+"/$encodedGuardJson")/* Navigate to Submit New Application Screen */ },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(Color(0xFF4379FF))
                ) {
                    Text(text = "Submit New Application (नई आवेदन प्रस्तुत करें)", color = Color.White)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { navController.navigate(NavigationScreens.DraftApplicationScreen.name+"/$encodedGuardJson")/* Navigate to Draft Applications Screen */ },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(Color(0xFF4379FF))
                ) {
                    Text(text = "Draft Applications (मसौदा आवेदन)", color = Color.White)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { navController.navigate(NavigationScreens.PrevApplicationScreen.name+"/$encodedGuardJson")/* Navigate to Previous Applications Screen */ },
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
    val backgroundColor = Color(0xFF4379FF)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .statusBarsPadding() // Ensures it is placed below the notch
            // .background(backgroundColor)
            .padding(horizontal = 16.dp)
            .padding(top = 8.dp),
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
