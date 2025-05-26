package com.example.compensation_app.screens.deputyRanger






import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.compensation_app.Backend.RetrivalForm
import com.example.compensation_app.Backend.RetrivalFormShort
import com.example.compensation_app.Backend.emp
import com.example.compensation_app.Navigation.NavigationScreens
import com.example.compensation_app.Navigation.SecureStorage
import com.example.compensation_app.Navigation.clearLoginStatus
import com.example.compensation_app.Navigation.logoutUser
import com.example.compensation_app.components.DetailRow
import com.example.compensation_app.components.SignOut
import com.example.compensation_app.components.TokenCountdownDisplay
import com.example.compensation_app.components.TopAppBarOP
import com.example.compensation_app.screens.guard.InfoRow
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
            division = "", range_ = "", beat = ""))
    }
    LaunchedEffect (Unit){
        mainViewModel.GetGuard {
            if (it != null) {
                emp=it
            }
        }
    }

    var Forms by remember { mutableStateOf<List<RetrivalFormShort>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }  // Track loading state
    LaunchedEffect(Unit) {
        mainViewModel.getAllCompensationShortCache {
            isLoading=false
            Log.d("Forms from MainViewModel", "PrevApplicationScreen: $it")
            Forms=it
        }
    }

    var PendingForYou by remember { mutableStateOf<List<RetrivalFormShort>>(emptyList()) }
    var Pending by remember { mutableStateOf<List<RetrivalFormShort>>(emptyList()) }
    var Accepted by remember { mutableStateOf<List<RetrivalFormShort>>(emptyList()) }
    var Rejected by remember { mutableStateOf<List<RetrivalFormShort>>(emptyList()) }




    LaunchedEffect(Forms.isEmpty() && !isLoading) {
        if (emp.emp_id.isNotEmpty() && Forms.isEmpty() && !isLoading) {
            isLoading=true
            viewModel.getFormsByDeptRangerID(deptRangerId = emp.emp_id) { forms, message,code ->
                isLoading=false
                if (forms != null && forms.isNotEmpty()) {
                    mainViewModel.addCompensationShortCache(forms = forms)
                    Log.d("Initiasl", "PrevApplicationScreen: $forms")
                    Forms = forms

                    Log.d("Forms", "Fetched Forms: $Forms")
                } else {
                    if (code==401 || code ==403){
                        logoutUser(navController = navController, mainViewModel = mainViewModel, emp = emp)
                    }
                    Log.d("Forms", "No forms found or error: $message")
                }
            }
        }
    }
    PendingForYou = Forms.filter { it.status == "2" }
    Pending = Forms.filter { it.status in arrayOf("3", "4", "5") }
    Accepted = Forms.filter { it.status == "6" }
    Rejected = Forms.filter { it.status == "-1" }
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
                        InfoRow(label = "User ID", value = emp.emp_id)
                        InfoRow(label = "Mobile", value = emp.mobile_number)
                        InfoRow(label = "Circle", value = emp.Circle_CG)
                        InfoRow(label = "Division", value = emp.division)
                        InfoRow(label = "SubDivision", value = emp.subdivision)
                        InfoRow(label = "Range", value = emp.range_)
                        InfoRow(label = "Circle 1", value = emp.Circle1)
                        InfoRow(label = "Beat", value = emp.beat.toString())

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
                                    emp = response.employee!!
                                    response.token?.let { SecureStorage.saveToken(context, it) }
                                    mainViewModel.deleteCompensationShortCache()
                                    mainViewModel.deleteComplaintShortCache()



                                } else {
                                    if(code==401 || code ==403){
                                        logoutUser(
                                            navController = navController,
                                            mainViewModel = mainViewModel,
                                            emp = emp)
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
                                        emp = emp)
//                                    clearLoginStatus(context = context)
//                                    SecureStorage.clearToken(context)
//                                    mainViewModel.deleteEmp(emp = emp)
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
                greetings = "Welcome (स्वागत है)",
                onMenuClick = { // Open the drawer on menu click
                    scope.launch {
                        drawerState.open() // Open the drawer
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            TokenCountdownDisplay(context)
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    colors = ButtonDefaults.buttonColors(Color(0xFF0A66C2))
                ) {
                    Text(text = "Pending (For You)", color = Color.White)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { navController.navigate(NavigationScreens.PendingScreen.name + "/$encodedEmpJson/$encodedPending") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    colors = ButtonDefaults.buttonColors(Color(0xFF0A66C2))
                ) {
                    Text(text = "Pending", color = Color.White)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { navController.navigate(NavigationScreens.AcceptedScreen.name + "/$encodedEmpJson/$encodedAccepted") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    colors = ButtonDefaults.buttonColors(Color(0xFF0A66C2))
                ) {
                    Text(text = "Accepted", color = Color.White)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { navController.navigate(NavigationScreens.RejectedScreen.name + "/$encodedEmpJson/$encodedRejected") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    colors = ButtonDefaults.buttonColors(Color(0xFFFF4C4C))
                ) {
                    Text(text = "Rejected", color = Color.White)
                }
            }

        }
    }
}
