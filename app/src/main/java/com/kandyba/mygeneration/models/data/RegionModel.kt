package com.kandyba.mygeneration.models.data

import java.io.IOException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.Serializable


data class RegionModel(
    var code: String? = null,
    var name: String? = null
) : Serializable {

    @Throws(ClassNotFoundException::class, IOException::class)
    private fun readObject(inputStream: ObjectInputStream) {
        code = inputStream.readUTF()
        name = inputStream.readUTF()
    }

    @Throws(IOException::class)
    private fun writeObject(outputStream: ObjectOutputStream) {
        outputStream.writeUTF(code)
        outputStream.writeUTF(name)
    }
}
