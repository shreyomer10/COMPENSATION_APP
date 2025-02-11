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
    val beat: Int
)

data class VerifyGuardRequest(
    val emp_id: String,
    val mobile_number: String
)
data class StatusUpdate(
    val status: String,
    val comment: String,
    val timestamp: String,  // Store timestamp in ISO format (yyyy-MM-dd'T'HH:mm:ss'Z')
    val updatedBy: String   // Stores who changed the status (e.g., "SDO", "Ranger")
)
data class VerifyGuardResponse(
    val message: String,
    val employee: emp? // Use the Guard class to represent employee details if applicable
)

data class RetrivalForm(
    val formID: Int?,
    val submissionDateTime: String?,
    val forestGuardID: String?,
    val applicantName: String?,
    val age: Int?,
    val fatherSpouseName: String?,
    val mobile: String?,
    val animalName: String?,
    val incidentDate: String?,
    val additionalDetails: String?,
    val Circle_CG: String,
    val Circle1: String,
    val division: String,
    val subdivision: String,
    val range_: String,
    val beat: Int,
    val address: String?,
    val cropType: String?,
    val cerealCrop: String?,
    val cropDamageArea: Double?,
    var cropDamageAmount:Double?,
    val fullHouseDamage: String?,
    val partialHouseDamage: String?,
    var houseDamageAmount:Double?,
    val numberOfCattlesDied: Int?,
    val estimatedCattleAge: Int?,
    var catleInjuryAmount:Double?,
    val humanDeathVictimName: String?,
    val numberOfDeaths: Int?,
    val temporaryInjuryDetails: String?,
    val permanentInjuryDetails: String?,
    var humanDeathAmount:Double?,
    var humanInjuryAmount:Double?,
    val bankName: String?,
    val ifscCode: String?,
    val branchName: String?,
    val accountHolderName: String?,
    val accountNumber: String?,
    val panNumber: String?,
    val aadhaarNumber: String?,
    val totalCompensationAmount:Double?,
    val statusHistory: List<StatusUpdate>?,
    val status: String?,
    var documentURL: String?,
    val verifiedBy: String?,
    val paymentProcessedBy: String?,
    val comments: String?
)



@Entity(tableName = "DraftApplication")
data class FormData(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var forestGuardID: String = "",
    var name: String = "",
    var age: String = "1",
    var fatherOrSpouseName: String = "",
    var mobile: String = "",
    var animalList: String = "",
    var damageDate: String = "",
    var additionalDetails: String = "",

    var address: String = "",
    var cropType: String = "",
    var cerealCrop: String = "",
    var cropDamageArea: String = "",
    var cropDamageAmount:Double = 0.0,
    var fullHousesDamaged: String = "",
    var partialHousesDamaged: String = "",
    var houseDamageAmount:Double = 0.0,

    var cattleInjuryNumber: String = "",
    var cattleInjuryEstimatedAge: String = "",
    var catleInjuryAmount:Double = 0.0,

    var humanDeathVictimNames: String = "",
    var humanDeathNumber: String = "",
    var humanDeathAmount:Double = 0.0,
    var humanInjuryAmount:Double = 0.0,

    var temporaryInjuryDetails: String = "",
    var permanentInjuryDetails: String = "",
    var bankName: String = "",
    var ifscCode: String = "",
    var bankBranch: String = "",
    var bankHolderName: String = "",
    var bankAccountNumber: String = "",
    var pan: String = "",
    var adhar: String = "",
    var documentURL: String = ""
) {
    companion object {
        val Saver: Saver<FormData, Any> = object : Saver<FormData, Any> {
            override fun restore(value: Any): FormData {
                val list = value as List<String>
                return FormData(
                    id = list[0].toIntOrNull() ?: 0,
                    forestGuardID = list[1],
                    name = list[2],
                    age = list[3],
                    fatherOrSpouseName = list[4],
                    mobile = list[5],
                    animalList = list[6],
                    damageDate = list[7],
                    additionalDetails = list[8],

                    address = list[9],
                    cropType = list[10],
                    cerealCrop = list[11],
                    cropDamageArea = list[12],
                    fullHousesDamaged = list[13],
                    partialHousesDamaged = list[14],
                    cattleInjuryNumber = list[15],
                    cattleInjuryEstimatedAge = list[16],
                    humanDeathVictimNames = list[17],
                    humanDeathNumber = list[18],
                    temporaryInjuryDetails = list[19],
                    permanentInjuryDetails = list[20],
                    bankName = list[21],
                    ifscCode = list[22],
                    bankBranch = list[23],
                    bankHolderName = list[24],
                    bankAccountNumber = list[25],
                    pan = list[26],
                    adhar = list[27],
                    documentURL = list[28]
                )
            }

            override fun SaverScope.save(value: FormData): Any {
                return listOf(
                    value.id.toString(),
                    value.forestGuardID,
                    value.name,
                    value.age,
                    value.fatherOrSpouseName,
                    value.mobile,
                    value.animalList,
                    value.damageDate,
                    value.additionalDetails,

                    value.address,
                    value.cropType,
                    value.cerealCrop,
                    value.cropDamageArea,
                    value.fullHousesDamaged,
                    value.partialHousesDamaged,
                    value.cattleInjuryNumber,
                    value.cattleInjuryEstimatedAge,
                    value.humanDeathVictimNames,
                    value.humanDeathNumber,
                    value.temporaryInjuryDetails,
                    value.permanentInjuryDetails,
                    value.bankName,
                    value.ifscCode,
                    value.bankBranch,
                    value.bankHolderName,
                    value.bankAccountNumber,
                    value.pan,
                    value.adhar,
                    value.documentURL
                )
            }
        }
    }
}

data class CompensationForm(
    val forestGuardID: String,
    val applicantName: String,
    val age: Int=1,
    val fatherSpouseName: String,
    val mobile: String,
    val animalName: String,
    val incidentDate: String,
    val additionalDetails: String="",
    var circle_CG: String,
    var circle1: String,
    var division: String,
    var subdivision: String,
    var range_: String,
    var beat: Int,
    val address: String,
    val cropType: String,
    val cerealCrop: String,
    val cropDamageArea: Double=0.0,
    var cropDamageAmount:Double = 0.0,
    val fullHouseDamage: String,
    val partialHouseDamage: String,
    var houseDamageAmount:Double = 0.0,

    val numberOfCattlesDied: Int=0,
    val estimatedCattleAge: Int=0,
    var catleInjuryAmount:Double = 0.0,

    val humanDeathVictimName: String,
    val numberOfDeaths: Int=0,
    val temporaryInjuryDetails: String,
    val permanentInjuryDetails: String,
    var humanDeathAmount:Double = 0.0,
    var humanInjuryAmount:Double = 0.0,

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
    val documentURL: String,
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
