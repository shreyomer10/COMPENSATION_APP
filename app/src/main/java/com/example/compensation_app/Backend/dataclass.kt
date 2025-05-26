package com.example.compensation_app.Backend

import android.util.Log
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.SaverScope
@Entity(tableName = "emp")
data class emp(
    @PrimaryKey
    val emp_id: String,
    val Circle_CG: String,
    val Name: String,
    val roll: String,
    val mobile_number: String,
    val Circle1: String,
    val division: String,
    val subdivision: String,
    val range_: String,
    val beat: String
){
    companion object {
        fun default() = emp(emp_id = "",
            mobile_number = "",
            Name = "",
            Circle_CG = "",
            Circle1 = "",
            roll = "guard",
            subdivision = "",
            division = "", range_ = "", beat = "")
    }
}
data class LoginRequest(
    val emp_id: String,
    val roll: String,
    val mobile_number: String,
    val password: String,
    val is_app:Boolean=true
)

data class LoginResponse(
    val message: String,
    val token: String?,
    val employee:emp?
)



data class VerifyGuardRequest(
    val emp_id: String,
    val mobile_number: String,
    val roll:String,
    val changePass:String
)
data class StatusUpdate(
    val status: String,
    val comment: String,
    val timestamp: String,  // Store timestamp in ISO format (yyyy-MM-dd'T'HH:mm:ss'Z')
    val updatedBy: String   // Stores who changed the status (e.g., "SDO", "Ranger")
)
data class VerifyGuardResponse(
    val message: String,
   // val employee: emp? // Use the Guard class to represent employee details if applicable
)
@Entity(tableName = "CompensationShortCache")
data class RetrivalFormShort(
    @PrimaryKey
    val formID: Int?,
    val submissionDateTime: String?,
    val forestGuardID: String?,
    val complaint_id: String?,
    val applicantName: String?,
    val mobile: String?,
    val incidentDate: String?,
    val statusHistory: List<StatusUpdate>?,
    val status: String?,

)
data class RetrivalForm(
    val formID: Int?,
    val submissionDateTime: String?,
    val forestGuardID: String?,
    val complaint_id: String?,
    val applicantName: String?,
    val age: Int?,
    val fatherSpouseName: String?,
    val mobile: String?,
    var email: String?,
    val animalName: String?,
    val incidentDate: String?,
    val additionalDetails: String?,
    val Circle_CG: String,
    val Circle1: String,
    val division: String,
    val subdivision: String,
    val range_: String,
    val beat: String,
    val address: String?,
    val cropType: String?,
    val cerealCrop: String?,
    val cropDamageArea: Double?,
    var cropDamageAmount: Double?,

    var cropdamagePhoto: String?,
    var nocReport: String?,
    var landOwnershipReport: String?,
    var rorReport: String?,

    val fullHouseDamage: String?,
    val partialHouseDamage: String?,
    var houseDamageAmount: Double?,

    var housedamagePhoto1: String?,
    var housedamagePhoto2: String?,
    var propertyOwnerReport: String?,

    val numberOfCattlesDied: Int?,
    val estimatedCattleAge: Int?,
    var catleInjuryAmount: Double?,
    var cattlePhoto: String?,
    var vasCertificate: String?,
    val humanDeathVictimName: String?,
    val numberOfDeaths: Int?,
    val temporaryInjuryDetails: String?,
    val permanentInjuryDetails: String?,
    var humanDeathAmount: Double?,
    var humanPhoto1: String?,
    var humanPhoto2: String?,
    var deathCertificate: String?,
    var sarpanchReport: String?,
    var pmReport: String?,

    var humanInjuryAmount: Double?,

    var humanInjuryPhoto: String?,
    var medicalCertificate: String?,
    val bankName: String?,
    val ifscCode: String?,
    val branchName: String?,
    val accountHolderName: String?,
    val accountNumber: String?,
    val panNumber: String?,
    val aadhaarNumber: String?,
    val totalCompensationAmount: Double?,
    val statusHistory: List<StatusUpdate>?,
    val status: String?,
    var idProof: String?,
    var photoUrl: String?,
    var eSignUrl: String?,
    val verifiedBy: String?,
    val paymentProcessedBy: String?,
    val comments: String?
) {
    companion object {
        fun default() = RetrivalForm(
            formID = 0,
            submissionDateTime = "",
            forestGuardID = "",
            complaint_id = "",
            applicantName = "",
            age = 0,
            fatherSpouseName = "",
            mobile = "",
            email = "",
            animalName = "",
            incidentDate = "",
            additionalDetails = "",
            Circle_CG = "",
            Circle1 = "",
            division = "",
            subdivision = "",
            range_ = "",
            beat = "",
            address = "",
            cropType = "",
            cerealCrop = "",
            cropDamageArea = 0.0,
            cropDamageAmount = 0.0,
            cropdamagePhoto = "",
            nocReport = "",
            landOwnershipReport = "",
            rorReport = "",
            fullHouseDamage = "",
            partialHouseDamage = "",
            houseDamageAmount = 0.0,
            housedamagePhoto1 = "",
            housedamagePhoto2 = "",
            propertyOwnerReport = "",
            numberOfCattlesDied = 0,
            estimatedCattleAge = 0,
            catleInjuryAmount = 0.0,
            cattlePhoto = "",
            vasCertificate = "",
            humanDeathVictimName = "",
            numberOfDeaths = 0,
            temporaryInjuryDetails = "",
            permanentInjuryDetails = "",
            humanDeathAmount = 0.0,
            humanPhoto1 = "",
            humanPhoto2 = "",
            deathCertificate = "",
            sarpanchReport = "",
            pmReport = "",
            humanInjuryAmount = 0.0,
            humanInjuryPhoto = "",
            medicalCertificate = "",
            bankName = "",
            ifscCode = "",
            branchName = "",
            accountHolderName = "",
            accountNumber = "",
            panNumber = "",
            aadhaarNumber = "",
            totalCompensationAmount = 0.0,
            statusHistory = emptyList(),
            status = "",
            idProof = "",
            photoUrl = "",
            eSignUrl = "",
            verifiedBy = "",
            paymentProcessedBy = "",
            comments = ""
        )
    }
}

data class EmailRequest(
    val email: String,
    val message: String
)

data class EmailResponse(
    val success: Boolean,
    val response: Any?
)
data class SubmissionComplaintResponse(
    val message: String,
    val complaint_id: Int
)
data class SearchComplaintRequest(
    val complaint_id: String?,
    val mobile: String?
)
data class FullComplaintResponse(
    val found: String?, // "yes" or "no"
    val complaint: UserComplaintRetrievalForm?, // If found == "yes", this contains data
    val error: String? // If there's an error, this contains an error message
)
data class GuardComplaintResponse(
    val found: String,  // Always "yes" or "no", so non-nullable
    val complaints: List<UserComplaintRetrievalFormShort> ?
)

data class UserComplaintRetrievalForm(
    var complaint_id: String?,
    var SubmissionDateTime: String?,
    var name: String?,
    var age: String?,
    var fatherOrSpouseName: String?,
    var mobile: String?,
    var email: String?,
    var adhaar: String?,

    var animalList: String?,
    var damageDate: String?,
    var additionalDetails: String?,
    var address: String?,
    var Circle_CG: String?,

    var division: String?,
    var subdivision: String?,
    var range_: String?,
    var circle1: String?,
    var beat: String?,
    var cropType: String?,
    var cerealCrop: String?,
    var fullHousesDamaged: String?,
    var partialHousesDamaged: String?,
    var cattleInjuryNumber: String?,
    var cattleInjuryEstimatedAge: String?,
    var humanDeathVictimNames: String?,
    var humanDeathNumber: String?,
    var temporaryInjuryDetails: String?,
    var permanentInjuryDetails: String?,
    var photoUrl: String?,
    var eSignUrl: String?,
    var incidentUrl1: String?,
    var incidentUrl2: String?,
    var incidentUrl3: String?,
    val status: String?,
    val statusHistory: MutableList<StatusUpdate>?
) {
    companion object {
        fun default() = UserComplaintRetrievalForm(
            complaint_id = 0.toString(),
            SubmissionDateTime = "",
            name = "",
            age = "",
            fatherOrSpouseName = "",
            mobile = "",
            email = "",
            adhaar = "",
            animalList = "",
            damageDate = "",
            additionalDetails = "",
            address = "",
            Circle_CG = "",
            division = "",
            subdivision = "",
            range_ = "",
            circle1 = "",
            beat = "",
            cropType = "",
            cerealCrop = "",
            fullHousesDamaged = "",
            partialHousesDamaged = "",
            cattleInjuryNumber = "",
            cattleInjuryEstimatedAge = "",
            humanDeathVictimNames = "",
            humanDeathNumber = "",
            temporaryInjuryDetails = "",
            permanentInjuryDetails = "",
            photoUrl = "",
            eSignUrl = "",
            incidentUrl1 = "",
            incidentUrl2 = "",
            incidentUrl3 = "",
            status = "",
            statusHistory = mutableListOf()
        )
    }
}
@Entity(tableName = "ComplaintShortCache")
data class UserComplaintRetrievalFormShort(
    @PrimaryKey
    var complaint_id: String="",
    var SubmissionDateTime:String?,
    var name: String?,
    var mobile: String?,
    var damageDate: String?,
    val status: String?,
    val statusHistory: MutableList<StatusUpdate>?,

    // The latest status (for quick access)

)
data class UserComplaintForm(

    var name: String = "",
    var age: String = "1",
    var fatherOrSpouseName: String = "",
    var mobile: String = "",
    var email:String="",
    var adhaar:String="",
    var animalList: String = "",
    var damageDate: String = "",
    var additionalDetails: String = "",

    var address: String = "",
    var Circle_CG: String="",

    var division: String="",
    var subdivision: String="",
    var range_: String="",
    var circle1: String="",
    var beat: String="",
    var cropType: String = "",
    var cerealCrop: String = "",

    var fullHousesDamaged: String = "",
    var partialHousesDamaged: String = "",

    var cattleInjuryNumber: String = "",
    var cattleInjuryEstimatedAge: String = "",

    var humanDeathVictimNames: String = "",
    var humanDeathNumber: String = "",


    var temporaryInjuryDetails: String = "",
    var permanentInjuryDetails: String = "",


    // The latest status (for quick access)


    var photoUrl:String="",
    var eSignUrl:String="",
    var incidentUrl1:String="",
    var incidentUrl2:String="",
    var incidentUrl3:String="",
    var status: String="0",
    var statusHistory: MutableList<StatusUpdate> = mutableListOf()

    )

fun validateComplaint(Form:UserComplaintForm): Boolean {


    return Form.name.isNotEmpty() &&
            Form.age!="0" &&
            Form.age.isNotEmpty() &&
            Form.fatherOrSpouseName.isNotEmpty() &&
            Form.mobile.isNotEmpty() &&
            Form.email.isNotEmpty() &&
            Form.adhaar.isNotEmpty() &&
            Form.Circle_CG.isNotEmpty() &&
            Form.animalList.isNotEmpty() &&
            Form.damageDate.isNotEmpty() &&
            Form.address.isNotEmpty() &&

            Form.division.isNotEmpty() &&
            Form.subdivision.isNotEmpty() &&
            Form.range_.isNotEmpty() &&
            Form.circle1.isNotEmpty() &&
            Form.beat.isNotEmpty()


}

@Entity(tableName = "DraftApplication")
data class FormData(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var forestGuardID: String = "",
    var complaint_id: String?=null,
    var name: String = "",
    var age: String = "1",
    var fatherOrSpouseName: String = "",
    var mobile: String = "",
    var email:String="",

    var animalList: String = "",
    var damageDate: String = "",
    var additionalDetails: String = "",

    var address: String = "",
    var cropType: String = "",
    var cerealCrop: String = "",
    var cropDamageArea: String = "",
    var cropDamageAmount:Double = 0.0,

    var cropdamagePhoto: String = "",
    var nocReport: String = "",
    var landOwnershipReport: String = "",
    var rorReport: String = "",

    var fullHousesDamaged: String = "",
    var partialHousesDamaged: String = "",
    var houseDamageAmount:Double = 0.0,

    var housedamagePhoto1: String = "",
    var housedamagePhoto2: String = "",
    var propertyOwnerReport: String = "",


    var cattleInjuryNumber: String = "",
    var cattleInjuryEstimatedAge: String = "",
    var catleInjuryAmount:Double = 0.0,

    var cattlePhoto: String = "",
    var vasCertificate: String = "",

    var humanDeathVictimNames: String = "",
    var humanDeathNumber: String = "",
    var humanDeathAmount:Double = 0.0,
    var humanPhoto1: String = "",
    var humanPhoto2: String = "",
    var deathCertificate: String = "",
    var sarpanchReport: String = "",
    var pmReport: String = "",


    var humanInjuryAmount: Double = 0.0,
    var humanInjuryPhoto: String = "",
    var medicalCertificate: String = "",

    var temporaryInjuryDetails: String = "",
    var permanentInjuryDetails: String = "",
    var bankName: String = "",
    var ifscCode: String = "",
    var bankBranch: String = "",
    var bankHolderName: String = "",
    var bankAccountNumber: String = "",
    var pan: String = "",
    var adhar: String = "",
    var idProof: String = "",
    var photoUrl:String="",
    var eSignUrl:String="",
)


data class CompensationForm(
    val forestGuardID: String,
    val complaint_id: String?=null,
    val applicantName: String,
    val age: Int=1,
    val fatherSpouseName: String,
    val mobile: String,
    var email:String,

    val animalName: String,
    val incidentDate: String,
    val additionalDetails: String="",
    var circle_CG: String,
    var circle1: String,
    var division: String,
    var subdivision: String,
    var range_: String,
    var beat: String,
    val address: String,
    val cropType: String,
    val cerealCrop: String,
    val cropDamageArea: Double=0.0,
    var cropDamageAmount:Double = 0.0,

    var cropdamagePhoto: String,
    var nocReport: String,
    var landOwnershipReport: String,
    var rorReport: String,

    var fullHouseDamage: String,
    var partialHouseDamage: String,
    var houseDamageAmount:Double = 0.0,

    var housedamagePhoto1: String,
    var housedamagePhoto2: String,
    var propertyOwnerReport: String,

    val numberOfCattlesDied: Int=0,
    val estimatedCattleAge: Int=0,
    var catleInjuryAmount:Double = 0.0,

    var cattlePhoto: String,
    var vasCertificate: String,


    val humanDeathVictimName: String,
    val numberOfDeaths: Int=0,
    val temporaryInjuryDetails: String,
    val permanentInjuryDetails: String,
    var humanDeathAmount:Double = 0.0,

    var humanPhoto1: String = "",
    var humanPhoto2: String = "",
    var deathCertificate: String = "",
    var sarpanchReport: String = "",
    var pmReport: String = "",

    var humanInjuryAmount:Double = 0.0,

    var humanInjuryPhoto: String = "",
    var medicalCertificate: String = "",

    val bankName: String,
    val ifscCode: String,
    val branchName: String,
    val accountHolderName: String,
    val accountNumber: String,
    val panNumber: String,
    val aadhaarNumber: String,
    val totalCompensationAmount:Double=0.0,
    val statusHistory: MutableList<StatusUpdate> = mutableListOf(),

    // The latest status (for quick access)
    val status: String,
    val idProof: String,
    var photoUrl:String,
    var eSignUrl:String,

    val verifiedBy: String,
    val paymentProcessedBy: String,
    val comments: String,
)

fun validate(Form:CompensationForm): Boolean {
    Log.d("FORM DOC URL", "validate: ${Form.forestGuardID.isNotEmpty()} &&\n" +
            "            ${Form.applicantName.isNotEmpty()} &&\n" +
            "${Form.age!=0} &&\n" +
            "            ${Form.fatherSpouseName.isNotEmpty()} &&\n" +
            "            ${Form.mobile.isNotEmpty()} &&\n" +
            "           ${ Form.animalName.isNotEmpty()} &&\n" +
            "            ${Form.incidentDate.isNotEmpty()} &&\n" +
            "            ${Form.address.isNotEmpty()} &&\n" +
            "\n" +
            "           ${ Form.bankName.isNotEmpty()} &&\n" +
            "           ${ Form.ifscCode.isNotEmpty()} &&\n" +
            "            ${Form.branchName.isNotEmpty()} &&\n" +
            "            ${Form.accountHolderName.isNotEmpty()} &&\n" +
            "            ${Form.accountNumber.isNotEmpty()} &&\n" +
            "           ${ Form.panNumber.isNotEmpty()} &&\n" +
            "            ${Form.aadhaarNumber.isNotEmpty()} ")
    return Form.forestGuardID.isNotEmpty() &&
            Form.applicantName.isNotEmpty() &&
            Form.age!=0 &&
            Form.fatherSpouseName.isNotEmpty() &&
            Form.mobile.isNotEmpty() &&
            Form.email.isNotEmpty() &&
            Form.animalName.isNotEmpty() &&
            Form.incidentDate.isNotEmpty() &&
            Form.address.isNotEmpty() &&

            Form.bankName.isNotEmpty() &&
            Form.ifscCode.isNotEmpty() &&
            Form.branchName.isNotEmpty() &&
            Form.accountHolderName.isNotEmpty() &&
            Form.accountNumber.isNotEmpty() &&
            Form.panNumber.isNotEmpty() &&
            Form.aadhaarNumber.isNotEmpty()


}

data class UpdateFormStatusRequest(
    val emp_id: String,
    val action: String,
    val comments: String? = null
)

data class UpdateFormStatusResponse(
    val message: String,
    val new_status: String,
    val verified_by: String
)
data class PdfRequest(
    val mobile: String,
    val username: String,
    val forestguardId: String?, // Nullable, required only if is_compensation is true
    val form_id: String,
    val is_compensation: Boolean
)
data class PdfResponse(
    val download_url: String?,
    val error:String?
)

