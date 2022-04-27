package org.climatechangemakers.parsecongress

import kotlinx.datetime.Clock
import kotlinx.serialization.Serializable

@Serializable class ClimateChangemakersMemberOfCongress(
  val bioguideId: String,
  val fullName: String,
  val legislativeRole: String,
  val state: String,
  val congressionalDistrict: Short?,
  val party: String,
  val dcPhoneNumber: String,
  val twitterHandle: String?,
  val cwcOfficeCode: String?,
)

fun combineCurrentLegislators(
  currentLegislators: List<UnitedStatesMemberOfCongress>,
  activeScwcOffices: Map<String, String>,
  twitterAccounts: Map<String, String>,
  clock: Clock,
): List<ClimateChangemakersMemberOfCongress> = currentLegislators.map { legislator ->
  combineLegislator(
    legislator = legislator,
    scwcOfficeCode = activeScwcOffices[legislator.id.bioguide],
    twitterAccount = twitterAccounts[legislator.id.bioguide],
    clock,
  )
}

private fun combineLegislator(
  legislator: UnitedStatesMemberOfCongress,
  scwcOfficeCode: String?,
  twitterAccount: String?,
  clock: Clock,
): ClimateChangemakersMemberOfCongress {
  val current = legislator.terms.current(clock)
  return ClimateChangemakersMemberOfCongress(
    bioguideId = legislator.id.bioguide,
    fullName = legislator.name.officialFullname,
    legislativeRole = current.representativeType,
    state = current.state,
    congressionalDistrict = current.district,
    party = current.party,
    dcPhoneNumber = current.phone!!,
    twitterHandle = twitterAccount,
    cwcOfficeCode = generateCWCOfficeCodeForLegislator(scwcOfficeCode, current),
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