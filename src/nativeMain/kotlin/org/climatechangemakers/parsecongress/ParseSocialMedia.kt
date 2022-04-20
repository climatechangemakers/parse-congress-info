package org.climatechangemakers.parsecongress

import kotlinx.serialization.json.Json
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString

fun parseUnitedStatedMemberOfCongressSocialMedia(
  rawJson: String,
  json: Json,
): List<UnitedStatesMemberOfCongressSocial> {
  return json.decodeFromString(rawJson)
}

@Serializable class UnitedStatesMemberOfCongressSocial(
  val id: UnitedStatesIdentifiers,
  val social: UnitedStatesMemberOfCongressSocialAccounts,
)

@Serializable class UnitedStatesMemberOfCongressSocialAccounts(
  val twitter: String?,
)