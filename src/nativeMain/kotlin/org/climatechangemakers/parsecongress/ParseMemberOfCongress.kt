package org.climatechangemakers.parsecongress

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayAt
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

fun readMemberOfCongressFile(
  rawJson: String,
  json: Json,
): List<UnitedStatesMemberOfCongress> {
  return json.decodeFromString(rawJson)
}

@Serializable class UnitedStatesMemberOfCongress(
  val id: UnitedStatesIdentifiers,
  val name: UnitedStatesNameInfo,
  val terms: List<UnitedStatesTermInfo>,
)

val UnitedStatesMemberOfCongress.phoneNumber: String get() = terms.filter { term ->
  val currentDate = Clock.System.todayAt(TimeZone.UTC)
  term.start <= currentDate && currentDate <= term.end
}.firstNotNullOf { it.phone }

@Serializable class UnitedStatesIdentifiers(
  val bioguide: String,
)

@Serializable class UnitedStatesNameInfo(
  @SerialName("first") val firstName: String,
  @SerialName("last") val lastName: String,
  @SerialName("official_full") val officialFullname: String,
)

@Serializable class UnitedStatesTermInfo(
  @SerialName("type") val representativeType: String,
  val state: String,
  val district: Short?,
  val party: String,
  val phone: String?,
  val start: LocalDate,
  val end: LocalDate
)