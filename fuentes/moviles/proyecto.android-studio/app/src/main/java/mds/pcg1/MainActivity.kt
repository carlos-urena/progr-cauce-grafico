// *********************************************************************
// **
// ** Asignatura: PCG (Programación del Cauce Gráfico).
// **
// ** Implementación de la actividad principal
// ** Copyright (C) 2024 Carlos Ureña
// **
// ** Clase: MainActivity
// **
// ** This program is free software: you can redistribute it and/or modify
// ** it under the terms of the GNU General Public License as published by
// ** the Free Software Foundation, either version 3 of the License, or
// ** (at your option) any later version.
// **
// ** This program is distributed in the hope that it will be useful,
// ** but WITHOUT ANY WARRANTY; without even the implied warranty of
// ** MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// ** GNU General Public License for more details.
// **
// ** You should have received a copy of the GNU General Public License
// ** along with this program.  If not, see <http://www.gnu.org/licenses/>.
// **
// *********************************************************************

package mds.pcg1

// ---------------------------------------------------------------------------

import android.os.Bundle

// import creados al crear la aplicación

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import mds.pcg1.ui.theme.AppAndroidPCGV1Theme

// imports necesarios para el código OpenGL

import android.app.Activity
import android.content.Context
import android.opengl.GLSurfaceView

import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

import android.opengl.GLES20

// imports añadidos por mi

import android.util.Log

import mds.pcg1.utilidades.*
import mds.pcg1.vec_mat.*


// ---------------------------------------------------------------------------

/** código obtenido de :
 *  @see: https://developer.android.com/develop/ui/views/graphics/opengl/environment?authuser=1&hl=es-419
 *
 *  Requiere añadir esto al manifiesto (para decir que queremos OpenGL ES 2.0:
 *
 *  <uses-feature android:glEsVersion="0x00020000" android:required="true" />
 *
 *  Se debe añadir en app/manifests/AndroidManifest.xml, como hijo directo del nodo 'Manifest'
 */

// -------------------------------------------------------------------------------------------------

class OpenGLES20Activity : Activity() {

    private lateinit var gLView: GLSurfaceView

    public override fun onCreate( savedInstanceState: Bundle? )
    {
        super.onCreate(savedInstanceState)

        // Create a GLSurfaceView instance and set it
        // as the ContentView for this Activity.
        gLView = MyGLSurfaceView(this )
        setContentView(gLView)
    }
}
// -------------------------------------------------------------------------------------------------


class MyGLSurfaceView( context: Context) : GLSurfaceView(context) {

    private val renderer: MyGLRenderer

    init {

        // Create an OpenGL ES 2.0 context
        setEGLContextClientVersion(2)

        renderer = MyGLRenderer()

        // Set the Renderer for drawing on the GLSurfaceView
        setRenderer(renderer)

        // Render the view only when there is a change in the drawing data
        renderMode = GLSurfaceView.RENDERMODE_WHEN_DIRTY
    }
}
// -------------------------------------------------------------------------------------------------

class MyGLRenderer : GLSurfaceView.Renderer {

    override fun onSurfaceCreated( unused: GL10, config: EGLConfig) {
        // Set the background frame color
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
    }

    override fun onDrawFrame( unused: GL10 ) {
        // Redraw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
    }

    override fun onSurfaceChanged(unused: GL10, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
    }
}
// -------------------------------------------------------------------------------------------------

// De aquí en adelante hay código que se generó al crear la aplicación.
// (con algún añadido, como lo del test del vectores)

class MainActivity : ComponentActivity()
{
    override fun onCreate( savedInstanceState: Bundle? ) {

        super.onCreate( savedInstanceState )

        setContent {
            AppAndroidPCGV1Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("App PCG Android v1")
                }
            }
        }
        Log.v( TAG, "Inicialización de la aplicación finalizada - va test")

        var test = VecMatTest() ;
        test.run()
    }
}
// ----------------------------------------------------------------------------

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hola desde $name!",
        modifier = modifier
    )
}
// ----------------------------------------------------------------------------

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AppAndroidPCGV1Theme {
        Greeting("App PCG Android")
    }
}

