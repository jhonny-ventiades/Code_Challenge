package com.willdom.codechallenge.webservice

import java.io.Serializable

class Response : Serializable {

    var code: Int = 0
    var url: String? = null
    var method: String? = null
    var location: String? = null
    var locationId: Long? = null
    var message: String? = null

    override fun toString(): String {
        return "Response{" +
                "code=" + code +
                ", url='" + url + '\''.toString() +
                ", method='" + method + '\''.toString() +
                ", location='" + location + '\''.toString() +
                ", locationId=" + locationId +
                ", message='" + message + '\''.toString() +
                '}'.toString()
    }
}
