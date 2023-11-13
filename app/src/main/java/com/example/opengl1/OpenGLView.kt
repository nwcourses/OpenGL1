package com.example.opengl1

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.util.AttributeSet
import android.util.Log
import freemap.openglwrapper.GPUInterface
import freemap.openglwrapper.OpenGLUtils
import java.io.IOException
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

// Our GLSurfaceView for rendering the 3D world.

class OpenGLView(ctx: Context, aSet:AttributeSet): GLSurfaceView(ctx, aSet), GLSurfaceView.Renderer {

    init {
        setEGLContextClientVersion(2) // use GL ES 2
        setRenderer(this)
    }

    val gpu = GPUInterface("default shader")


    var fbuf: FloatBuffer? = null

    val red = floatArrayOf(1f, 0f, 0f, 1f)
    val yellow = floatArrayOf(1f, 1f, 0f, 1f)
    val blue = floatArrayOf(0f, 0f, 1f, 0f)

    // Setup code to run when the OpenGL view is first created.
    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        // Sets the background colour
        GLES20.glClearColor(0f, 0f, 0f, 1f)

        // Enable depth testing
        GLES20.glClearDepthf(1.0f)
        GLES20.glEnable(GLES20.GL_DEPTH_TEST)

        try {
            val success = gpu.loadShaders(context.assets, "vertex.glsl", "fragment.glsl")
            if(success == false) {
                Log.d("opengl01", gpu.lastShaderError)
            }
            fbuf = OpenGLUtils.makeFloatBuffer(
                floatArrayOf(
                    0f, 0f, 0f,
                    1f, 0f, 0f,
                    0f, 1f, 0f,
                    0f, 0f, 0f,
                    -1f, 0f, 0f,
                    0f, -1f, 0f
                )
            )
            // Selects this shader program
            gpu.select()
        } catch(e: IOException) {
            Log.d("opengl01", e.stackTraceToString())
        }
    }

    // Draws the current frame
    // Is called multiple times per second
    // You actual scene drawing should go in here.

    override fun onDrawFrame(gl: GL10?) {
        // Clear any previous settings from the previous frame
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        val ref_aVertex = gpu.getAttribLocation("aVertex")
        val ref_uColour = gpu.getUniformLocation("uColour")

        // Only run code below if buffer is not null
        fbuf?.apply {
            gpu.setUniform4FloatArray(ref_uColour, blue)
            gpu.specifyBufferedDataFormat(ref_aVertex, this, 0 )
            gpu.drawBufferedTriangles(0, 3)
            gpu.setUniform4FloatArray(ref_uColour, yellow)
            gpu.drawBufferedTriangles(3, 3)
        }

    }

    // Is called whenever the resolution changes (on a mobile device this will occur
    // when the device is rotated)
    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
    }

}