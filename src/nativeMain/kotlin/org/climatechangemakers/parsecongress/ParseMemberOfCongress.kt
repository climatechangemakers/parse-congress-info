package org.climatechangemakers.parsecongress

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.todayAt
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

fun readMemberOfCongressFile(
  rawJson: String,
  json: Json,
): List<MemberOfCongress> {
  return json.decodeFromString(rawJson)
}

@Serializable class MemberOfCongress(
  val id: Identifiers,
  val name: NameInfo,
  val terms: List<TermInfo>,
)

val MemberOfCongress.phoneNumber: String get() = terms.filter { term ->
  val currentDate = Clock.System.todayAt(TimeZone.UTC)
  term.start <= currentDate && currentDate <= term.end
}.firstNotNullOf { it.phone }

@Serializable class Identifiers(
  val bioguide: String,
)

@Serializable class NameInfo(
  @SerialName("first") val firstName: String,
  @SerialName("last") val lastName: String,
  @SerialName("official_full") val officialFullname: String,
)

@Serializable class TermInfo(
  @SerialName("type") val representativeType: String,
  val state: String,
  val district: Short?,
  val party: String,
  val phone: String?,
  val start: LocalDate,
  val end: LocalDate
)