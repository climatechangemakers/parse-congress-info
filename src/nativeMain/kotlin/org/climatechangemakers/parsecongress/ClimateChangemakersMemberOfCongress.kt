package org.climatechangemakers.parsecongress

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable class ClimateChangemakersMemberOfCongress(
  val bioguideId: String,
  val fullName: String,
  val firstName: String,
  val lastName: String,
  val legislativeRole: String,
  val state: String,
  val congressionalDistrict: Short?,
  val party: String,
  val dcPhoneNumber: String,
  val twitterHandle: String?,
  val cwcOfficeCode: String?,
  val termEndDate: LocalDate,
)

fun combineCurrentLegislators(
  currentLegislators: List<UnitedStatesMemberOfCongress>,
  activeScwcOffices: Map<String, String>,
  twitterAccounts: Map<String, String>,
): List<ClimateChangemakersMemberOfCongress> = currentLegislators.map { legislator ->
  combineLegislator(
    legislator = legislator,
    scwcOfficeCode = activeScwcOffices[legislator.id.bioguide],
    twitterAccount = twitterAccounts[legislator.id.bioguide],
  )
}

private fun combineLegislator(
  legislator: UnitedStatesMemberOfCongress,
  scwcOfficeCode: String?,
  twitterAccount: String?,
): ClimateChangemakersMemberOfCongress {
  val current = legislator.terms.mostRecent()
  return ClimateChangemakersMemberOfCongress(
    bioguideId = legislator.id.bioguide,
    fullName = legislator.name.officialFullname,
    firstName = legislator.name.firstName,
    lastName = legislator.name.lastName,
    legislativeRole = current.representativeType,
    state = current.state,
    congressionalDistrict = current.district,
    party = current.party,
    dcPhoneNumber = current.phone!!,
    twitterHandle = twitterAccount,
    cwcOfficeCode = generateCWCOfficeCodeForLegislator(scwcOfficeCode, current),
    termEndDate = current.end,
  )
}

private fun generateCWCOfficeCodeForLegislator(
  scwcOfficeCode: String?,
  currentTerm: UnitedStatesTermInfo,
): String? = when (val type = currentTerm.representativeType) {
  "sen" -> scwcOfficeCode
  "rep" -> generateHWCWOfficeCodeForLegislator(currentTerm)
  else -> throw IllegalArgumentException("Wrong representative type $type")
}

private fun generateHWCWOfficeCodeForLegislator(
  currentTerm: UnitedStatesTermInfo,
): String {
  val stateCode = when (currentTerm.state) {
    "AS" -> "AQ"
    else -> currentTerm.state
  }

  return "H$stateCode${currentTerm.district!!.toString().padStart(2, '0')}"
}