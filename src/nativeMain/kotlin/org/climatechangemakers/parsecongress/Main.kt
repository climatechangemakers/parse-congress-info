package org.climatechangemakers.parsecongress

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.convert
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.github.ajalt.clikt.parameters.types.choice
import okio.Path

fun main(args: Array<String>) = Parse().main(args)

class Parse : CliktCommand() {

  private val inputType: InputJsonType by option("-i", "--input-type").choice(
    InputJsonType.DistrictOffices.cliValue,
    InputJsonType.CurrentMembersOfCongress.cliValue,
  ).convert { value -> InputJsonType.values().first { value == it.cliValue } }.required()

  private val inputFilePath: Path by argument(
    name = "input",
    help = "input JSON file",
  ).path(mustExist = true)

  private val outputFilePath: Path by argument(
    name = "output",
    help = "output CSV file",
  ).path(mustExist = false)

  override fun run() = println(
    """
      inputFilePath: $inputFilePath
      outputFilePath: $outputFilePath
      inputType: $inputType
    """.trimIndent()
  )
}