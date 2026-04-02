/*
 * Copyright (c) 2025-26 by Fred George
 * @author Fred George  fredgeorge@acm.org
 * Licensed under the MIT License; see LICENSE file in root.
 */

package com.nrkei.project.issue

import com.nrkei.project.issue.IssueSet.IssueSetDto
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import java.util.*


val baseIssueSerializers = SerializersModule {
    polymorphic(IssueDto::class) {
        // register only Core DTO's here
    }
}
internal val baseJson = Json {
    serializersModule = baseIssueSerializers
    prettyPrint = false
    ignoreUnknownKeys = true
    classDiscriminator = "type"
}

internal inline fun <reified T : Any> toBase64(dto: T, json: Json = baseJson) =
    Base64.getEncoder().encodeToString(
            toJson(dto, json).toByteArray(Charsets.UTF_8)
        ) //.also { println(it) }

internal inline fun <reified T : Any> toJson(dto: T, json: Json) =
    json.encodeToString(dto) //.also { println(it) }

internal fun issueSetDtoFromBase64(base64: String, json: Json = baseJson): IssueSetDto =
    fromJson(fromBase64ToJson(base64), json)

internal inline fun <reified T> fromJson(jsonText: String, json: Json): T =
    json.decodeFromString(jsonText)

internal fun fromBase64ToJson(base64: String)=
    String(Base64.getDecoder().decode(base64), Charsets.UTF_8)

