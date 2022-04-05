package org.climatechangemakers.parsecongress.extensions

import com.github.ajalt.clikt.completion.CompletionCandidates
import com.github.ajalt.clikt.parameters.arguments.ProcessedArgument
import com.github.ajalt.clikt.parameters.arguments.RawArgument
import com.github.ajalt.clikt.parameters.arguments.convert
import com.github.ajalt.clikt.parameters.options.NullableOption
import com.github.ajalt.clikt.parameters.options.RawOption
import com.github.ajalt.clikt.parameters.options.convert
import okio.FileSystem
import okio.Path
import okio.Path.Companion.toPath

fun RawOption.path(
  mustExist: Boolean = false,
): NullableOption<Path, Path> {
  return convert({ localization.pathMetavar() }, CompletionCandidates.Path) { str ->
    str.toPath().also { path ->
      if (mustExist) require(FileSystem.SYSTEM.exists(path)) { "$path does not exist." }
    }
  }
}

fun RawArgument.path(
  mustExist: Boolean = false,
): ProcessedArgument<Path, Path> {
  return convert(CompletionCandidates.Path) { str ->
    str.toPath().also { path ->
      if (mustExist) require(FileSystem.SYSTEM.exists(path)) { "$path does not exist." }
    }
  }
}
