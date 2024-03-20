package com.example.nytviewer.services.transport.utils

import com.example.nytviewer.services.transport.models.APIResponse
import retrofit2.Response

/**
 * This will return the full network response as the data in a TransportResponse object if the call was successful
 * (returning a normal HTTP success status code, no matter the server's response body).
 * */
fun <ResponseType : APIResponse<DataType>, DataType> Response<ResponseType>.getAPIResults(): TransportResponse<List<DataType>> {
    val body = this.body()
    val errorBody = this.errorBody()

    return if (this.isSuccessful && body != null) {
        TransportResponse.Success(body.results)
    } else if (errorBody != null) {
        TransportResponse.Error(this.code(), errorBody.string())
    } else {
        TransportResponse.Error(
            this.code(),
            "Received no error message from server."
        )
    }
}