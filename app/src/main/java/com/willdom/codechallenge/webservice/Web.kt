package com.willdom.codechallenge.webservice

import android.content.Context
import com.willdom.codechallenge.model.entity.BaseEntity


import android.net.ConnectivityManager
import android.net.ParseException
import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonSyntaxException
import com.willdom.codechallenge.tag
import com.willdom.codechallenge.webServiceToken
import com.willdom.codechallenge.webServices
import java.io.*
import java.lang.reflect.Type
import java.net.ConnectException
import java.net.HttpURLConnection
import java.net.URL
import java.util.ArrayList

abstract class Web(private val context: Context) {

    var isConnected = false

    protected lateinit var gson: Gson

    init {
        this.isConnected = checkMobile() || checkWifi()
        this.initGson()
    }

    fun checkWifi(): Boolean {
        try {
            val connectivity =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val info = connectivity.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
            if (info != null) {
                if (info.isConnected) {
                    return true
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return false
    }

    private fun checkMobile(): Boolean {
        try {
            val connectivity =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (connectivity != null) {
                val info = connectivity.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
                if (info != null) {
                    if (info.isConnected) {
                        return true
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return false
    }

    private fun initGson() {
        val builder = GsonBuilder()
        builder.setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        //builder.excludeFieldsWithoutExposeAnnotation();
        gson = builder.create()
    }

    @Throws(UnsupportedEncodingException::class)
    private fun prepareConnection(connection: HttpURLConnection): HttpURLConnection {
        connection.setRequestProperty("Accept", "application/json")
        connection.setRequestProperty("Content-type", "application/json")
        connection.connectTimeout = 25000
        connection.readTimeout = 25000
        return applySecurity(connection)
    }

    @Throws(UnsupportedEncodingException::class)
    private fun applySecurity(request: HttpURLConnection): HttpURLConnection {
        //request.setRequestProperty("Authorization", "Basic " + Base64.encodeToString((webServiceUser + ":" + webServicePassword).getBytes("UTF-8"), Base64.NO_WRAP));
        request.setRequestProperty("Authorization", "Bearer " + webServiceToken)
        Log.d(tag, request.getRequestProperty("Authorization"))
        return request
    }


    @Throws(Exception::class)
    private fun response(connection: HttpURLConnection): Response {
        val buffer = StringBuffer()
        var reader: BufferedReader? = null
        var code = 0
        try {
            if (connection.responseCode == -1) {
                buffer.append(MESSAGE_NO_CONEXION)
            } /*else if (connection.getResponseCode() == HttpURLConnection.HTTP_INTERNAL_ERROR) {
                buffer.append(MESSAGE_SERVER_ERROR);
            } else if (connection.getResponseCode() == HttpURLConnection.HTTP_NOT_FOUND) {
                buffer.append(MESSAGE_NOT_FOUND);
            }*/
            else {
                if (connection.responseCode == HttpURLConnection.HTTP_CREATED
                    || connection.responseCode == HttpURLConnection.HTTP_PARTIAL
                    || connection.responseCode == HttpURLConnection.HTTP_ACCEPTED
                    || connection.responseCode == HttpURLConnection.HTTP_OK
                ) {
                    reader = BufferedReader(InputStreamReader(connection.inputStream))
                } else {
                    reader = BufferedReader(InputStreamReader(connection.errorStream))
                }

                //var line: String? = null
                reader.forEachLine {
                    buffer.append(it)

                }
               /* while ((line = reader.readLine()) != null) {
                    buffer.append(line)
                }*/
            }
            code = connection.responseCode
        } catch (c: ConnectException) {
            buffer.append(MESSAGE_NO_CONEXION)
            code = HttpURLConnection.HTTP_CLIENT_TIMEOUT
        } catch (e: Exception) {
            Log.e(tag, e.message, e)
            buffer.append(connection.responseMessage)
            code = connection.responseCode
        } finally {
            reader?.close()
        }

        val obj = Response()
        obj.code = (code)
        obj.url = (connection.url.toString())
        obj.method = (connection.requestMethod)
        obj.message = (buffer.toString())
        obj.location = (connection.getHeaderField("Location"))
        if (obj.location  != null) {
            try {
                val parts = obj.location!!.split("/")
                val id = parts[parts.size - 1]
                obj.locationId  = (java.lang.Long.parseLong(id))
            } catch (e: Exception) {

            }

        }
        return obj
    }

    @Throws(Exception::class)
    fun getObject(method: String, type: Type): Any {
        Log.d(tag, webServices + method)
        val url = URL(webServices + method)
        try {
            val connection = url.openConnection() as HttpURLConnection
            prepareConnection(connection)
            connection.useCaches = false
            connection.doInput = true
            connection.disconnect()
            val response = response(connection)
            if (response.code == HttpURLConnection.HTTP_OK) {
                return gson.fromJson(response.message, type)
            }
            throw Exception(
                String.format(
                    "Error->GET Metodo: %s, Code: %s, Mensaje: %s",
                    method,
                    response.code,
                    response.message
                )
            )
        } catch (e: Exception) {
            Log.e(tag, e.message)
            throw e
        }

    }

    @Throws(Exception::class)
    operator fun <T : BaseEntity> get(method: String, type: Type): T {
        Log.d(tag, webServices + method)
        val url = URL(webServices + method)
        try {
            val connection = url.openConnection() as HttpURLConnection
            prepareConnection(connection)
            connection.useCaches = false
            connection.doInput = true
            connection.disconnect()
            val response = response(connection)
            if (response.code === HttpURLConnection.HTTP_OK || response.code === HttpURLConnection.HTTP_CREATED) {
                return gson.fromJson(response.message, type)
            }
            Log.e(
                tag,
                String.format(
                    "Error->GET Metodo: %s, Code: %s, Mensaje: %s",
                    method,
                    response.code,
                    response.message
                )
            )
            //throw new Exception(String.format("Error->GET Metodo: %s, Code: %s, Mensaje: %s", method, response.code, response.message));
            throw Exception(response.message)
        } catch (e: IllegalStateException) {
            Log.e(tag, e.message.toString(), e)
            throw Exception(MESSAGE_NO_CONEXION)
        } catch (e: ParseException) {
            Log.e(tag, e.message.toString(), e)
            throw Exception(MESSAGE_NO_CONEXION)
        } catch (e: JsonSyntaxException) {
            Log.e(tag, e.message.toString(), e)
            throw Exception(MESSAGE_NO_CONEXION)
        } catch (e: Exception) {
            Log.e(tag, e.message.toString(), e)
            throw e
        }

    }


    @Throws(Exception::class)
    operator fun get(method: String): String? {
        Log.d(tag, webServices + method)
        val url = URL(webServices + method)
        try {
            val connection = url.openConnection() as HttpURLConnection
            prepareConnection(connection)
            connection.useCaches = false
            connection.doInput = true
            connection.disconnect()
            val response = response(connection)
            if (response.code === HttpURLConnection.HTTP_OK || response.code === HttpURLConnection.HTTP_CREATED) {
                return response.message
            }
            Log.e(
                tag,
                String.format(
                    "Error->GET Metodo: %s, Code: %s, Mensaje: %s",
                    method,
                    response.code,
                    response.message
                )
            )
            //throw new Exception(String.format("Error->GET Metodo: %s, Code: %s, Mensaje: %s", method, response.code, response.message));
            throw Exception(response.message)
        } catch (e: IllegalStateException) {
            Log.e(tag, e.message.toString(), e)
            throw Exception(MESSAGE_NO_CONEXION)
        } catch (e: ParseException) {
            Log.e(tag, e.message.toString(), e)
            throw Exception(MESSAGE_NO_CONEXION)
        } catch (e: JsonSyntaxException) {
            Log.e(tag, e.message.toString(), e)
            throw Exception(MESSAGE_NO_CONEXION)
        } catch (e: Exception) {
            Log.e(tag, e.message.toString(), e)
            throw e
        }

    }

    @Throws(Exception::class)
    fun <T : BaseEntity> getList(method: String, type: Type ): List<T> {
        println(webServices + method)
        val url = URL(webServices + method)
        try {
            val connection = url.openConnection() as HttpURLConnection
            prepareConnection(connection)
            connection.useCaches = false
            connection.doInput = true
            connection.requestMethod = "GET"
            connection.disconnect()
            val response = response(connection)
            if (response.code == HttpURLConnection.HTTP_OK || response.code == HttpURLConnection.HTTP_CREATED) {
                Log.i(tag, "Result --> " + response.message)
                val entities = gson.fromJson<List<T>>(response.message, type)
                return entities
            }
            if (response.code == HttpURLConnection.HTTP_NOT_FOUND) {
                return ArrayList()
            }
            throw Exception(
                String.format(
                    "Error->GET Metodo: %s, Code: %s, Mensaje: %s",
                    method,
                    response.code,
                    response.message
                )
            )
        } catch (e: Exception) {
            Log.e(tag, e.message, e)
            throw e
        }

    }

    @Throws(Exception::class)
    fun <T : BaseEntity> post(method: String, obj: T): Long? {
        Log.d(tag, webServices + method)
        val url = URL(webServices + method)
        val connection = url.openConnection() as HttpURLConnection
        try {
            val postData = gson.toJson(obj)
            Log.d(tag, postData)
            prepareConnection(connection)
            connection.doOutput = true
            connection.doInput = true
            connection.requestMethod = "POST"
            val osw = DataOutputStream(connection.outputStream)
            osw.write(postData.toByteArray())
            osw.flush()
            val response = response(connection)
            if (response.code === HttpURLConnection.HTTP_CREATED) {
                return response.locationId
            }
            throw Exception(
                String.format(
                    "Error->POST Metodo: %s, Code: %s, Mensaje: %s",
                    method,
                    response.code,
                    response.message
                )
            )
        } catch (e: Exception) {
            Log.e(tag, e.message)
            throw e
        } finally {
            connection.disconnect()
        }
    }

    @Throws(Exception::class)
    fun <T : BaseEntity> post(method: String, obj: T, type: Type): T {
        Log.d(tag, webServices + method)
        val url = URL(webServices + method)
        val connection = url.openConnection() as HttpURLConnection
        try {
            val postData = gson.toJson(obj)
            Log.d(tag, "Sending --> $postData")
            prepareConnection(connection)
            connection.doOutput = true
            connection.doInput = true
            connection.requestMethod = "POST"
            val osw = DataOutputStream(connection.outputStream)
            osw.write(postData.toByteArray())
            osw.flush()
            val response = response(connection)
            if (response.code === HttpURLConnection.HTTP_CREATED || response.code === HttpURLConnection.HTTP_OK) {
                Log.d(tag, "Result --> " + response.message)
                return gson.fromJson(response.message, type)
            } else {
                if (response.code === HttpURLConnection.HTTP_BAD_REQUEST) {
                    Log.d(tag, "Result --> " + response.message)
                    //val error = gson.fromJson(response.message, ErrorHeader::class.java)
                    //throw Exception(processError(error))
                }
            }
            throw Exception(
                String.format(
                    "Error->POST Metodo: %s, Code: %s, Mensaje: %s",
                    method,
                    response.code,
                    response.message
                )
            )
        } catch (j: JsonSyntaxException) {
            throw Exception(MESSAGE_SERVER_ERROR)
        } catch (e: Exception) {
            Log.e(tag, e.message, e)
            throw e
        } finally {
            connection.disconnect()
        }
    }

    @Throws(Exception::class)
    fun <T : BaseEntity> put(method: String, obj: T): Long? {
        Log.d(tag, webServices + method)
        val url = URL(webServices + method)
        val connection = url.openConnection() as HttpURLConnection
        try {
            val postData = gson.toJson(obj)
            prepareConnection(connection)
            connection.doOutput = true
            connection.doInput = true
            connection.requestMethod = "PUT"
            val osw = DataOutputStream(connection.outputStream)
            osw.write(postData.toByteArray(charset("UTF-8")))
            osw.flush()
            val response = response(connection)
            if (response.code === HttpURLConnection.HTTP_ACCEPTED) {
                return response.locationId
            }
            throw Exception("Error PUT : " + response.code)
        } catch (e: Exception) {
            Log.e(tag, e.message)
            throw e
        } finally {
            connection.disconnect()
        }
    }

    @Throws(Exception::class)
    fun <T : BaseEntity> delete(method: String, obj: T): Boolean {
        Log.d(tag, webServices + method)
        val url = URL(webServices + method + "/" + obj.id)
        try {
            val connection = url.openConnection() as HttpURLConnection
            applySecurity(connection)
            connection.useCaches = false
            connection.doInput = true
            connection.requestMethod = "DELETE"
            connection.disconnect()
            val response = response(connection)
            if (response.code === HttpURLConnection.HTTP_OK) {
                return true
            }
            throw Exception("Error GET : " + response.code)
        } catch (e: Exception) {
            Log.e(tag, e.message)
            throw e
        }

    }





    /*private fun processError(errorResponse: ErrorHeader): String {
        val toReturn = StringBuilder()
        for (error in errorResponse.getErrors()) {
            toReturn.append(error.message).append("\n")
        }
        return toReturn.toString()
    }
*/
    companion object {


        private val MESSAGE_NO_CONEXION = "Error de conexión"
        private val MESSAGE_NOT_FOUND = "No se encontraron registros"
        private val MESSAGE_SERVER_ERROR = "Error en el servidor"
    }


}