package com.madmensoftware.sips.util

import android.util.Log
import java.io.*


object IOUtils {
    val BUFFER_SIZE = 1024 * 2

    /** Copies file to a temporary file. **/
    @Throws(IOException::class)
    fun copy(input: InputStream, output: OutputStream): Int {
        val buffer = ByteArray(BUFFER_SIZE)
        val inputIn = BufferedInputStream(input, BUFFER_SIZE)
        val inputOut = BufferedOutputStream(output, BUFFER_SIZE)
        var count = 0
        var n = 0
        try {
            while ({n = inputIn.read(buffer, 0, BUFFER_SIZE); n}() != -1) {
                inputOut.write(buffer, 0, n)
                count += n
            }
            inputOut.flush()
        }
        finally {
            try {
                inputOut.close()
            }
            catch (e: IOException) {
                Log.e("IOUtils: ", e.message)
            }
            try {
                inputIn.close()
            }
            catch (e: IOException) {
                Log.e("IOUtil: ", e.message)
            }
        }
        return count
    }

}
