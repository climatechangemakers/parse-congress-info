package org.climatechangemakers.parsecongress.extensions

import okio.BufferedSource
import okio.FileSystem
import okio.Path

fun Path.readContents(): String = FileSystem.SYSTEM.read(this, BufferedSource::readUtf8)
