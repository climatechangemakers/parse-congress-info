package org.climatechangemakers.parsecongress

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
): List<ClimateChangemakersMemberOfCongress> = currentLegislators.map { legislator ->
  combineLegislator(legislator = legislator, scwcOfficeCode  = activeScwcOffices[legislator.id.bioguide])
}

private fun combineLegislator(
  legislator: UnitedStatesMemberOfCongress,
  scwcOfficeCode: String?,
) = ClimateChangemakersMemberOfCongress(
  bioguideId = legislator.id.bioguide,
  fullName = legislator.name.officialFullname,
  legislativeRole = legislator.terms.current.representativeType,
  state = legislator.terms.current.state,
  congressionalDistrict = legislator.terms.current.district,
  party = legislator.terms.current.party,
  dcPhoneNumber = legislator.phoneNumber,
  twitterHandle = "TODO",
  cwcOfficeCode = generateCWCOfficeCodeForLegislator(legislator, scwcOfficeCode),
)

private fun generateCWCOfficeCodeForLegislator(
  legislator: UnitedStatesMemberOfCongress,
  scwcOfficeCode: String?,
): String? = when (val type = legislator.terms.current.representativeType) {
  "sen" -> scwcOfficeCode
  "rep" -> generateHWCWOfficeCodeForLegislator(legislator)
  else -> throw IllegalArgumentException("Wrong representative type $type")
}

private fun generateHWCWOfficeCodeForLegislator(
  legislator: UnitedStatesMemberOfCongress,
): String {
  val term = legislator.terms.current
  return when (term.state) {
    "AS" -> "AQ00" // American Somoa is a special case conflict.
    else -> "${term.state}${term.district!!.toString().padStart(2, '0')}"
  }
}