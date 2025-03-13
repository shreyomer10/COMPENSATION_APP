package com.example.compensation_app.screens.deputyRanger






import android.net.Uri
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
import com.example.compensation_app.Backend.RetrivalForm
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
fun DeputyHomeScreen(navController: NavController) {



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
    var emp by remember {
        mutableStateOf<emp>(emp(emp_id = "",
            mobile_number = "",
            Name = "",
            Circle_CG = "",
            Circle1 = "",
            roll = "guard",
            subdivision = "",
            division = "", range_ = "", beat = 0))
    }
    mainViewModel.GetGuard {
        if (it != null) {
            emp=it
        }
    }
    var Forms by remember { mutableStateOf<List<RetrivalForm>>(emptyList()) }
    var PendingForYou by remember { mutableStateOf<List<RetrivalForm>>(emptyList()) }
    var Pending by remember { mutableStateOf<List<RetrivalForm>>(emptyList()) }
    var Accepted by remember { mutableStateOf<List<RetrivalForm>>(emptyList()) }
    var Rejected by remember { mutableStateOf<List<RetrivalForm>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }  // Track loading state



    LaunchedEffect(emp.emp_id) {
        if (emp.emp_id.isNotEmpty()) {
            viewModel.getFormsByDeptRangerID(deptRangerId = emp.emp_id) { forms, message ->
                isLoading=false
                if (!forms.isNullOrEmpty()) {
                    Forms = forms
                    PendingForYou = forms.filter { it.status == "2" }
                    Pending = forms.filter { it.status in arrayOf("3", "4", "5") }
                    Accepted = forms.filter { it.status == "6" }
                    Rejected = forms.filter { it.status == "-1" }
                } else {
                    Log.d("Forms", "No forms found or error: $message")
                }
            }
        }
    }
    Log.d("ALL Forms ", "DeputyHomeScreen: $Forms")
    Log.d("Pendingg for you", "DeputyHomeScreen: $PendingForYou")
    Log.d("Rejected maal", "DeputyHomeScreen: $Rejected")
    Log.d("Pending maal", "DeputyHomeScreen: $Pending")


    val encodedPending = URLEncoder.encode(gson.toJson(Pending), StandardCharsets.UTF_8.toString())
    val encodedPendingForYou = URLEncoder.encode(gson.toJson(PendingForYou), StandardCharsets.UTF_8.toString())
    val encodedAccepted = URLEncoder.encode(gson.toJson(Accepted), StandardCharsets.UTF_8.toString())
    val encodedRejected = URLEncoder.encode(gson.toJson(Rejected), StandardCharsets.UTF_8.toString())

    val encodedEmpJson = URLEncoder.encode(gson.toJson(emp), StandardCharsets.UTF_8.toString())
    Log.d("encoded", "DeputyHomeScreen: $encodedPendingForYou")




    Log.d("Final", "NewApplication:${emp.emp_id} ")
    val empJson = gson.toJson(emp)
   // val encodedEmpJson = URLEncoder.encode(empJson, StandardCharsets.UTF_8.toString())

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
                        text = "UserId: ${emp.emp_id}",
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
                            text = emp.mobile_number,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = if (showLogoutDialog) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary
                            )
                        )

                    }
                    Spacer(modifier = Modifier.height(8.dp)) // Space between details
                    Text(
                        text = "Circle: ${emp.Circle_CG}",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = if (showLogoutDialog) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Division :${emp.division} ",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = if (showLogoutDialog) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "SubDivision :${emp.subdivision} ",
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
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${emp.range_} ${emp.Circle1} ",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = if (showLogoutDialog) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary
                            )
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp)) // Space between details
                    Row {
                        Text(
                            text = "Area: ",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = if (showLogoutDialog) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${emp.division} ${emp.range_} ${emp.beat}",
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
                                    mainViewModel.deleteEmp(emp=emp)
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
            if(isLoading){
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(color = Color.Blue)

                    }
                }
            }
            else{
                Spacer(modifier = Modifier.height(32.dp))

                // Center the buttons vertically
                // Center the buttons vertically
                Button(
                    onClick = { navController.navigate(NavigationScreens.PendingForYouScreen.name + "/$encodedEmpJson/$encodedPendingForYou") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(Color(0xFF4379FF))
                ) {
                    Text(text = "Pending (For You)", color = Color.White)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { navController.navigate(NavigationScreens.PendingScreen.name + "/$encodedEmpJson/$encodedPending") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(Color(0xFF4379FF))
                ) {
                    Text(text = "Pending", color = Color.White)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { navController.navigate(NavigationScreens.AcceptedScreen.name + "/$encodedEmpJson/$encodedAccepted") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(Color(0xFF5D8BFF))
                ) {
                    Text(text = "Accepted", color = Color.White)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { navController.navigate(NavigationScreens.RejectedScreen.name + "/$encodedEmpJson/$encodedRejected") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(Color(0xFFFF4C4C))
                ) {
                    Text(text = "Rejected", color = Color.White)
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
