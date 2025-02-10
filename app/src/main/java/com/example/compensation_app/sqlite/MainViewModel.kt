package com.example.compensation_app.sqlite


import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.compensation_app.Backend.FormData
import com.example.compensation_app.Backend.emp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class MainViewModel@Inject constructor(private val offlineRepsitory: OfflineRepsitory ,
                                       private val database: AppDatabase ): ViewModel() {


    // Fetch a single student by ID




    fun getAllForms(callback: (List<FormData>) -> Unit) {
        viewModelScope.launch {
            try {
                val forms = offlineRepsitory.getAllForms()
                callback(forms)
            } catch (e: Exception) {
                callback(emptyList())
            }

            // Pass the list of teachers to the callback
        }
    }
    fun updateForm(entity: FormData){
        viewModelScope.launch {
            try{

                offlineRepsitory.update(entity)
                Log.d("DONE", "updateForm: DOne")
            }
            catch (e:Exception){
                Log.d("Sorry", "updateTT: C")
            }

        }
    }



    // Fetch all subjects

    fun insertEntry(form: FormData) = viewModelScope.launch(Dispatchers.IO) {
        val dao = offlineRepsitory.insertEntry(form)
    }
    fun deleteAll(){
        viewModelScope.launch {
            try {
                offlineRepsitory.deleteAll()
            }
            catch(e:Exception){
                Log.d("Sorry", "updateTT: Can not delete")

            }

        }
    }
    fun deleteForm(id:Int){
        viewModelScope.launch {
            try{
                offlineRepsitory.delete(id=id)
            }
            catch (e:Exception){
                Log.d("Sorry", "updateTT: Can not delete")
            }

        }
    }
    fun GuardDetails(emp: emp){
        viewModelScope.launch {
            try{
                offlineRepsitory.insertGuard(emp)
                Log.d("Guard added", "GuardDetails: Added ")
            }
            catch (e:Exception){
                Log.d("Sorry", "$e")
            }

        }
    }
    fun GetGuard(callback: (emp?) -> Unit){
        viewModelScope.launch {
            try{
               val guard= offlineRepsitory.getGuard()
                callback(guard)
            }
            catch (e:Exception){
                callback(null)
                Log.d("Sorry", "updateTT: Can not delete")
            }

        }
    }
    fun deleteEmp(emp: emp){
        viewModelScope.launch {
            try{
                offlineRepsitory.deleteEmp(emp)
            }
            catch (e:Exception){
                Log.d("Sorry", "Delete: Can not delete")
            }

        }
    }

}