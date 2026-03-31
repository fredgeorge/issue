/*
 * Copyright (c) 2025-26 by Fred George
 * @author Fred George  fredgeorge@acm.org
 * Licensed under the MIT License; see LICENSE file in root.
 */

package com.nrkei.project.issue

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue

// Understands a four-sided polygon with sides at right angles
data class RectangleJson(val length: Double, val width: Double) {

    fun area() = length * width

    fun toJson(mapper: ObjectMapper = jacksonObjectMapper()): String =
        mapper.writeValueAsString(this)

    companion object {
        fun fromJson(json: String, mapper: ObjectMapper = jacksonObjectMapper()): RectangleJson =
            mapper.readValue(json)
    }
}