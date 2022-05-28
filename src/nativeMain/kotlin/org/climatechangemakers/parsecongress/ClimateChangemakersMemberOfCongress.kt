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

fun combineLegislators(
  legislators: List<UnitedStatesMemberOfCongress>,
  activeScwcOffices: Map<String, String>,
  twitterAccounts: Map<String, String>,
  today: LocalDate,
): List<ClimateChangemakersMemberOfCongress> = legislators.map { legislator ->
  combineLegislator(
    legislator = legislator,
    scwcOfficeCode = activeScwcOffices[legislator.id.bioguide],
    twitterAccount = twitterAccounts[legislator.id.bioguide],
    today = today,
  )
}

private fun combineLegislator(
  legislator: UnitedStatesMemberOfCongress,
  scwcOfficeCode: String?,
  twitterAccount: String?,
  today: LocalDate,
): ClimateChangemakersMemberOfCongress {
  val current = legislator.terms.mostRecent()
  return ClimateChangemakersMemberOfCongress(
    bioguideId = legislator.id.bioguide,
    fullName = checkNotNull(legislator.name.officialFullname) { "$legislator did not have an official full name." },
    firstName = legislator.name.firstName,
    lastName = legislator.name.lastName,
    legislativeRole = current.representativeType,
    state = current.state,
    congressionalDistrict = current.district,
    party = checkNotNull(current.party) { "$legislator did not have a most recent party." },
    dcPhoneNumber = checkNotNull(current.phone) { "$legislator did not have a most recent DC phone." },
    twitterHandle = twitterAccount,
    cwcOfficeCode = if (current.isActiveForDate(today)) {
      generateCWCOfficeCodeForLegislator(scwcOfficeCode, current)
    } else {
      null
    },
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