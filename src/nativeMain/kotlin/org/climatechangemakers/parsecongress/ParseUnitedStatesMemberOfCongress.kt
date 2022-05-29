package org.climatechangemakers.parsecongress

import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json

fun parseUnitedStatesMemberOfCongressFile(
  rawJson: String,
  json: Json,
): List<UnitedStatesMemberOfCongress> {
  return json.decodeFromString(
    deserializer = ListSerializer(UnitedStatesMemberOfCongress.serializer()),
    string = rawJson
  )
}

@Serializable class UnitedStatesMemberOfCongress(
  val id: UnitedStatesIdentifiers,
  val name: UnitedStatesNameInfo,
  val terms: List<UnitedStatesTermInfo>,
)

fun List<UnitedStatesTermInfo>.mostRecent(): UnitedStatesTermInfo = checkNotNull(
  maxByOrNull { term -> term.end }
) { "Terms $this had no max." }

@Serializable class UnitedStatesIdentifiers(
  val bioguide: String,
)

@Serializable class UnitedStatesNameInfo(
  @SerialName("first") val firstName: String,
  @SerialName("last") val lastName: String,
  @SerialName("official_full") val officialFullname: String?,
)

@Serializable class UnitedStatesTermInfo(
  @SerialName("type") val representativeType: String,
  val state: String,
  val district: Short?,
  val party: String?,
  val phone: String?,
  val start: LocalDate,
  val end: LocalDate
) {
  init {
    if (representativeType == "rep") {
      requireNotNull(district)
    } else {
      require(district == null)
    }
  }

  fun isActiveForDate(date: LocalDate): Boolean {
    return date in start..end
  }
}