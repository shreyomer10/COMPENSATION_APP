package com.example.compensation_app.screens.guard

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button

import androidx.compose.material3.ButtonDefaults

import androidx.compose.material3.Text

import androidx.compose.ui.graphics.Color

import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Phone
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.compensation_app.Backend.emp
import com.example.compensation_app.Navigation.NavigationScreens
import com.example.compensation_app.Navigation.SecureStorage
import com.example.compensation_app.Navigation.clearLoginStatus
import com.example.compensation_app.Navigation.logoutUser
import com.example.compensation_app.Navigation.saveLoginStatus
import com.example.compensation_app.R
import com.example.compensation_app.components.SignOut
import com.example.compensation_app.components.TokenCountdownDisplay
import com.example.compensation_app.components.TopAppBarOP
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
    var isLoading by remember { mutableStateOf(false) } // Loading state

    val viewModel: GuardViewModel = hiltViewModel()
    val mainViewModel: MainViewModel = hiltViewModel()
    val auth = FirebaseAuth.getInstance()
    val mobileNumber = auth.currentUser?.phoneNumber
    val formattedNumber = mobileNumber?.replace("+91", "") ?: ""
    Log.d("format", "$formattedNumber")
//    var gguard by remember {
//        mutableStateOf<emp>(emp(emp_id = "",
//            mobile_number = "",
//            Name = "",
//            Circle_CG = "",
//            Circle1 = "",
//            roll = "guard",
//            subdivision = "",
//            division = "", range_ = "", beat = ""))
//    }
    var gguard by remember {
        mutableStateOf<emp>(emp.default())
    }
    LaunchedEffect (Unit){
        mainViewModel.GetGuard {
            if (it != null) {
                gguard=it
            }
        }

    }

    Log.d("Final", "NewApplication:${gguard.emp_id} ")
    val guardJson = gson.toJson(gguard)
    val encodedGuardJson = URLEncoder.encode(guardJson, StandardCharsets.UTF_8.toString())
    // Handle Back Press
    BackHandler {
        showLogoutDialog = true // Show logout dialog instead of going back
    }
    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet(modifier = Modifier
                .fillMaxWidth(0.8f)
                .background(Color.White),
                //drawerContainerColor = Color(0xFFBB86FC)
            ) {
                Column(modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Logout",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp)
                            .clickable { showLogoutDialog = true },
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.error
                        )
                    )

                    Divider()

                    Spacer(modifier = Modifier.height(12.dp))

                    Column(modifier = Modifier.fillMaxWidth()) {
                        InfoRow(label = "User ID", value = gguard.emp_id)
                        InfoRow(label = "Mobile", value = gguard.mobile_number)
                        InfoRow(label = "Circle", value = gguard.Circle_CG)
                        InfoRow(label = "Division", value = gguard.division)
                        InfoRow(label = "SubDivision", value = gguard.subdivision)
                        InfoRow(label = "Range", value = gguard.range_)
                        InfoRow(label = "Circle 1", value = gguard.Circle1)
                        InfoRow(label = "Beat", value = gguard.beat.toString())

                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    TokenCountdownDisplay(context = context)
                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            isLoading = true
                            viewModel.refreshToken { response, error , code ->
                                isLoading = false
                                if (response != null) {


                                    Toast.makeText(context, response.message, Toast.LENGTH_SHORT).show()
                                    gguard = response.employee!!
                                    response.token?.let { SecureStorage.saveToken(context, it) }
                                    mainViewModel.deleteCompensationShortCache()
                                    mainViewModel.deleteComplaintShortCache()



                                } else {
                                    if(code==401 || code ==403){
                                        logoutUser(
                                            navController = navController,
                                            mainViewModel = mainViewModel,
                                            emp = gguard)
                                    }
                                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Re-Authenticate Employee", fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(24.dp))

                    // Contact Us Button
                    ElevatedButton(
                        onClick = {  navController.navigate(NavigationScreens.ContactUs.name)},
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.elevatedButtonColors(containerColor = Color(0xFF1976D2))
                    ) {
                        Icon(imageVector = Icons.Default.Phone, contentDescription = "Contact Us", tint = Color.White)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Contact Us", fontWeight = FontWeight.Bold, color = Color.White)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // About Us Button
                    ElevatedButton(
                        onClick = { navController.navigate(NavigationScreens.AboutUs.name) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.elevatedButtonColors(containerColor = Color(0xFF43A047))
                    ) {
                        Icon(imageVector = Icons.Default.Info, contentDescription = "About Us", tint = Color.White)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("About Us", fontWeight = FontWeight.Bold, color = Color.White)
                    }

                    Spacer(modifier = Modifier.height(90.dp))


                }
                if (showLogoutDialog) {
                    AlertDialog(
                        onDismissRequest = { showLogoutDialog = false },
                        title = {
                            Text(text = "Logout", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                        },
                        text = {
                            Text("Are you sure you want to logout?", textAlign = TextAlign.Center)
                        },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    showLogoutDialog = false
                                    logoutUser(navController = navController,
                                        mainViewModel=mainViewModel,
                                        emp = gguard)
//                                    clearLoginStatus(context = context)
//                                    SecureStorage.clearToken(context)
//                                    mainViewModel.deleteEmp(emp = gguard)
//                                    mainViewModel.deleteCompensationShortCache()
//                                    navController.navigate(NavigationScreens.LoginScreen.name) {
//                                        popUpTo(NavigationScreens.AppHome.name) { inclusive = true }
//                                    }
                                }
                            ) {
                                Text("Yes, Logout", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.error)
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { showLogoutDialog = false }) {
                                Text("Cancel")
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
            // TopAppBarOP with hamburger menu
            TopAppBarOP(
                navController = navController,
                greetings = "Welcome ${gguard.Name}",
                onMenuClick = { // Open the drawer on menu click
                    scope.launch {
                        drawerState.open() // Open the drawer
                    }
                }
            )
            Spacer(modifier = Modifier.height(16.dp))

            TokenCountdownDisplay(context)



            // Center the buttons vertically
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {


                Button(
                    onClick = { navController.navigate(NavigationScreens.NewApplicationScreen.name)/* Navigate to Submit New Application Screen */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    colors = ButtonDefaults.buttonColors(Color(0xFF0A66C2))
                ) {
                    Text(text = "Submit New Application ", color = Color.White)
                }
                Spacer(modifier = Modifier.height(16.dp))



                Button(
                    onClick = { navController.navigate(NavigationScreens.ComplaintApplicationGuard.name+"/$encodedGuardJson")/* Navigate to Submit New Application Screen */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    colors = ButtonDefaults.buttonColors(Color(0xFF0A66C2))
                ) {
                    Text(text = "Complaint Applications", color = Color.White)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { navController.navigate(NavigationScreens.DraftApplicationScreen.name+"/$encodedGuardJson")/* Navigate to Draft Applications Screen */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    colors = ButtonDefaults.buttonColors(Color(0xFF0A66C2))
                ) {
                    Text(text = "Draft Applications", color = Color.White)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { navController.navigate(NavigationScreens.PrevApplicationScreen.name+"/$encodedGuardJson")/* Navigate to Previous Applications Screen */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    colors = ButtonDefaults.buttonColors(Color(0xFF0A66C2))
                ) {
                    Text(text = "Previous Applications", color = Color.White)
                }
            }
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onBackground
            )
        )
    }
}