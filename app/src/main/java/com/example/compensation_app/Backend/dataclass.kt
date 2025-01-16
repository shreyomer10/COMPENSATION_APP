package com.example.compensation_app.Backend

data class Guard(
    val emp_id: String,
    val name: String,
    val mobile_number: String,
    val division: String,
    val range_: String,
    val beat: Int
)
data class VerifyGuardRequest(
    val emp_id: String,
    val mobile_number: String
)

data class VerifyGuardResponse(
    val message: String,
    val employee: Guard? // Use the Guard class to represent employee details if applicable
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
    val address: String?,
    val cropType: String?,
    val cerealCrop: String?,
    val cropDamageArea: Double?,
    val fullHouseDamage: String?,
    val partialHouseDamage: String?,
    val numberOfCattlesDied: Int?,
    val estimatedCattleAge: Int?,
    val humanDeathVictimName: String?,
    val numberOfDeaths: Int?,
    val temporaryInjuryDetails: String?,
    val permanentInjuryDetails: String?,
    val bankName: String?,
    val ifscCode: String?,
    val branchName: String?,
    val accountHolderName: String?,
    val accountNumber: String?,
    val panNumber: String?,
    val aadhaarNumber: String?,
    val status: String?,
    val verifiedBy: String?,
    val paymentProcessedBy: String?
)

data class FormData(
    var forestGuardID: String="",
    var name: String = "",
    var age: String = "",
    var fatherOrSpouseName: String = "",
    var mobile: String = "",
    var animalList: String = "",
    var damageDate: String = "",
    var additionalDetails: String = "",
    var address: String = "",
    var cropType: String = "",
    var cerealCrop: String = "",
    var cropDamageArea: String = "",
    var fullHousesDamaged: String = "",
    var partialHousesDamaged: String = "",
    var cattleInjuryNumber: String = "",
    var cattleInjuryEstimatedAge: String = "",
    var humanDeathVictimNames: String = "",
    var humanDeathNumber: String = "",
    var temporaryInjuryDetails: String = "",
    var permanentInjuryDetails: String = "",
    var bankName: String = "",
    var ifscCode: String = "",
    var bankBranch: String = "",
    var bankHolderName: String = "",
    var bankAccountNumber: String = "",
    var pan:String="",
    var adhar:String="",

)
data class CompensationForm(
    val forestGuardID: String,
    val applicantName: String,
    val age: Int,
    val fatherSpouseName: String,
    val mobile: String,
    val animalName: String,
    val incidentDate: String,
    val additionalDetails: String="",
    val address: String,
    val cropType: String,
    val cerealCrop: String,
    val cropDamageArea: Double=0.0,
    val fullHouseDamage: String,
    val partialHouseDamage: String,
    val numberOfCattlesDied: Int=0,
    val estimatedCattleAge: Int=0,
    val humanDeathVictimName: String,
    val numberOfDeaths: Int=0,
    val temporaryInjuryDetails: String,
    val permanentInjuryDetails: String,
    val bankName: String,
    val ifscCode: String,
    val branchName: String,
    val accountHolderName: String,
    val accountNumber: String,
    val panNumber: String,
    val aadhaarNumber: String,
    val status: String,
    val verifiedBy: String,
    val paymentProcessedBy: String
){

}
fun validate(Form:CompensationForm): Boolean {
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
