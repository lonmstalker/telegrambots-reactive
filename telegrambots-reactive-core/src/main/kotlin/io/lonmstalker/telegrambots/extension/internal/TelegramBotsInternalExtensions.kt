package io.lonmstalker.telegrambots.extension.internal

import io.lonmstalker.telegrambots.constants.MediaTypeConstants.octetStreamType
import io.lonmstalker.telegrambots.constants.MediaTypeConstants.textType
import io.lonmstalker.telegrambots.serde.InputStreamRequestBody
import io.lonmstalker.telegrambots.serde.SerializeApi
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.telegram.telegrambots.meta.api.objects.InputFile

internal fun MultipartBody.Builder.addPart(name: String, value: String?) =
    value?.let { this.addFormDataPart(name, name, it.toRequestBody(textType)) } ?: this

internal fun MultipartBody.Builder.addRelatedPart(name1: String, value1: String?, name2: String, value2: String?) =
    value1
        ?.let { this.addPart(name1, it).addPart(name2, value2) }
        ?: this

internal fun MultipartBody.Builder.addPart(name: String, value: Long?) =
    value?.let { this.addPart(name, it.toString()) } ?: this

internal fun MultipartBody.Builder.addPart(name: String, value: Int?) =
    value?.let { this.addPart(name, it.toString()) } ?: this

internal fun MultipartBody.Builder.addPart(name: String, value: Boolean?) =
    value?.let { this.addPart(name, it.toString()) } ?: this

internal fun MultipartBody.Builder.addSerializePart(name: String, value: Any?, serializeApi: SerializeApi) =
    value
        ?.let {
            this.addFormDataPart(
                name,
                name,
                serializeApi.serialize(value).toRequestBody(textType)
            )
        } ?: this

internal fun MultipartBody.Builder.addFile(
    fileField: String,
    file: InputFile,
    addField: Boolean = true
): MultipartBody.Builder {
    if (file.isNew) {
        if (file.newMediaFile != null) {
            this.addFormDataPart(
                fileField,
                fileField,
                file.newMediaFile.asRequestBody(octetStreamType)
            )
        } else if (file.newMediaStream != null) {
            this.addFormDataPart(
                fileField,
                fileField,
                InputStreamRequestBody(octetStreamType, file.newMediaStream)
            )
        }
    }
    if (addField) {
        return this.addPart(fileField, file.attachName)
    }
    return this
}