package org.climatechangemakers.parsecongress

import kotlinx.serialization.json.Json
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer

fun parseUnitedStatedMemberOfCongressSocialMedia(
  rawJson: String,
  json: Json,
): List<UnitedStatesMemberOfCongressSocial> {
  return json.decodeFromString(
    deserializer = ListSerializer(UnitedStatesMemberOfCongressSocial.serializer()),
    string = rawJson,
  )
}

@Serializable class UnitedStatesMemberOfCongressSocial(
  val id: UnitedStatesIdentifiers,
  val social: UnitedStatesMemberOfCongressSocialAccounts,
)

@Serializable class UnitedStatesMemberOfCongressSocialAccounts(
  val twitter: String?,
)