package com.android.flickphoto.requests.responses

import android.util.Log
import retrofit2.Response


sealed class ApiResponse <T> {
companion object {

        private const val TAG = "ApiResponse"


    //if the response fails
    fun <T> create(error: Throwable): ApiErrorResponse<T> {
        Log.d(TAG, "errormsg:${error.message?:"unknown error"} ")
        return ApiErrorResponse(error.message ?: "unknown error")
    }



    fun <T> create(response: Response<T>): ApiResponse<T> {
        return  if (response.isSuccessful) {
            val body = response.body()

            //check if the response is empty
            if (body == null || response.code() == 204) {
                 ApiEmptyResponse()
            } else {
                 ApiSuccessResponse<T>(body)
            }

        } else {
           val msg = response.errorBody()?.string()
            val errorMsg = if (msg.isNullOrEmpty()){
                response.message()
            }else{
                msg
            }
            Log.d(TAG, "errormsg:${errorMsg?:"unknown error"} ")
            ApiErrorResponse(errorMsg ?:"Unknown Error")

        }
    }
}


    class ApiSuccessResponse<T>(val body:T):ApiResponse<T>()
    class ApiErrorResponse<T>(val errorMessage:String):ApiResponse<T>()
    class ApiEmptyResponse<T>:ApiResponse<T>()
}