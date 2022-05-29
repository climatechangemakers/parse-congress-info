package org.climatechangemakers.parsecongress

import kotlinx.serialization.SerialName
import kotlinx.serialization.json.Json
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer

fun parseActiveCwcOffices(
  rawJson: String,
  json: Json,
): List<ActiveOffice> {
  return json.decodeFromString(
    deserializer = ListSerializer(ActiveOffice.serializer()),
    string = rawJson,
  )
}

private val bioguideRegex = Regex("""[A-Z][0-9]+""")

@Serializable class ActiveOffice(
  private val name: String,
  @SerialName("office_code") val officeCode: String,
) {
  val bioguide: String by lazy {
    bioguideRegex.findAll(name).first().value
  }
}
