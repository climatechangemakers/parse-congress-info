package org.climatechangemakers.parsecongress

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.convert
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.github.ajalt.clikt.parameters.types.choice

fun main(args: Array<String>) = Parse().main(args)

class Parse : CliktCommand() {

  private val inputType: InputJsonType by option("-i", "--input-type").choice(
    InputJsonType.DistrictOffices.cliValue,
    InputJsonType.CurrentMembersOfCongress.cliValue,
  ).convert { value -> InputJsonType.values().first { value == it.cliValue } }.required()

  private val inputFilePath: String by argument(
    name = "input",
    help = "input JSON file",
  )

  private val outputFilePath: String by argument(
    name = "output",
    help = "output CSV file",
  )

  override fun run() = println(
    """
      inputFilePath: $inputFilePath
      outputFilePath: $outputFilePath
      inputType: $inputType
    """.trimIndent()
  )
}