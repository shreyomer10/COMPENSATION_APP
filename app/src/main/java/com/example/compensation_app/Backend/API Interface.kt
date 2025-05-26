package com.example.compensation_app.Backend

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    @POST("/register")
    fun register(@Body emp: LoginRequest): Call<ApiResponse>

    @POST("/login")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>


    @POST("/refresh")
    fun refreshToken(): Call<LoginResponse>
    @POST("/updatePassword")
    fun updatePass(@Body emp: LoginRequest): Call<ApiResponse>
    @GET("/guards")
    fun getGuards(): Call<List<emp>>

    @POST("/guards/add")
    fun addGuard(@Body emp: emp): Call<Void>

    @POST("/verify/verify_guard")
    fun verifyGuard(@Body request: VerifyGuardRequest): Call<VerifyGuardResponse>

    @POST("/email/send_email")
    fun sendEmail(@Body request: EmailRequest): Call<EmailResponse>
    @POST("/compensationform")
    fun submitCompensationForm(@Body form: CompensationForm): Call<Void>
    @POST("/complaints/get_complaint")
    fun getComplaint(@Body request: SearchComplaintRequest): Call<FullComplaintResponse>
    @POST("/complaints/submit_complaint")
    fun submitComplaintForm(@Body form: UserComplaintForm): Call<SubmissionComplaintResponse>

    // ApiService.kt


    @GET("/guards/{mobile_number}")
    fun getGuardByMobileNumber(@retrofit2.http.Path("mobile_number") mobileNumber: String): Call<emp>
//    @GET("/compensationform/{forest_guard_id}")
//    fun getCompensationFormsByGuardId(@Path("forest_guard_id") forestGuardId: String): Call<List<RetrivalForm>>
    @GET("/compensationform/forestguard/{forest_guard_id}")
    fun getCompensationFormsByGuardId(@Path("forest_guard_id") forestGuardId: String): Call<List<RetrivalFormShort>>

    @GET("/compensationform/deputyranger/{dept_ranger_id}")
    fun getCompensationFormsByDeptRangerID(@Path("dept_ranger_id") deptRangerId: String): Call<List<RetrivalFormShort>>
    @POST("/complaints/get_guard_complaints")  // Update with your actual endpoint
    fun getGuardComplaints(@Body request: GuardRepository.guardComplaintRequest): Call<GuardComplaintResponse>

    @POST("/update_form_status/{form_id}")
    fun updateFormStatus(
        @Path("form_id") formId: String,
        @Body request: UpdateFormStatusRequest
    ): Call<UpdateFormStatusResponse>
    @POST("/complaints/reject_complaint")
    fun rejectComplaint(@Body request: RejectComplaintRequest): Call<ApiResponse>

    @GET("/compensationform/one/{form_id}")
    fun getEachCompensationForm(
        @Path("form_id") formId: String
    ): Call<RetrivalForm>

    @GET("/complaints/one/{form_id}")  // Update with your actual endpoint
    fun getEachComplaint( @Path("form_id") formId: String): Call<FullComplaintResponse>

    @POST("/pdf")
    fun getOrCreatePdf(@Body request: PdfRequest): Call<PdfResponse>








}

data class RejectComplaintRequest(
    @SerializedName("complaint_id") val complaintId: String,
    @SerializedName("guardId") val guardId: String,
    @SerializedName("comment") val comment: String
)

data class ApiResponse(
    @SerializedName("message") val message: String?,
    @SerializedName("error") val error: String?
)
