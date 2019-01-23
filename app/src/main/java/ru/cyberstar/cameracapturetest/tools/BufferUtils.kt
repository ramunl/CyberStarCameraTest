package ru.cyberstar.cameracapturetest.tools

import java.nio.ByteBuffer

fun deepCopy(source: ByteBuffer): ByteBuffer {

    var target = ByteBuffer.allocate(source.remaining())

    val sourceP = source.position()
    val sourceL = source.limit()

    target!!.put(source)
    target!!.flip()

    source.position(sourceP)
    source.limit(sourceL)
    return target
}