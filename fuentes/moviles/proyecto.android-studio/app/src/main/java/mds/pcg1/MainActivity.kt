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

import mds.pcg1.utilidades.* // para TAG y otras utilidades...
import mds.pcg1.vec_mat.* // para clases Vec2, Vec3, Mat4,
import android.content.*  // para clase Intent (lanza la actividad OpenGL)
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener


/** código obtenido de :
 *  @see: https://developer.android.com/develop/ui/views/graphics/opengl/environment?authuser=1&hl=es-419
 *
 *  Requiere añadir esto al manifiesto (para decir que queremos OpenGL ES 2.0:
 *
 *  <uses-feature android:glEsVersion="0x00020000" android:required="true" />
 *
 *  Se debe añadir en 'app/manifests/AndroidManifest.xml', como hijo directo del nodo 'Manifest'
 *
 *  Se requiere añadir "OpenGLES20Activity" al manifiesto y quitar las actividades que no se usan
 *  Cada actividad es un nodo XML de tipo <activity>
 *  Se pueden poner varias, pero una de ellas es la que se lanza al inicio (ponerle category "LAUNCHER")
 *  Una vez que se ha creado una actividad, en el 'onCreate' se puede lanzar otra, ver:
 *    https://stackoverflow.com/questions/45518139/kotlin-android-start-new-activity
 *  (he probado la respuesta de Gowtham y funciona perfectamente)
 *  Pero no es necesario lanzar otra actividad por ahora, ya que la actividad opengl se puede ejecutar
 *  al lanzar la aplicación como primera yúnica actividad).
 *
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
class CUATouchListener() : OnTouchListener
{
    override fun onTouch(v: View, event: MotionEvent ) : Boolean
    {
        Log.v( TAG, "ON TOUCH!")
        //Log.v( TAG, "classification == ${event.getClassification()} ")
        Log.v( TAG, "event x y == ${event.getRawX()} - ${event.getRawY()}")
        return true
    }
}
// --------------------
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

        // probando....
        this.setOnTouchListener( CUATouchListener() )

    }
}
// -------------------------------------------------------------------------------------------------

class MyGLRenderer : GLSurfaceView.Renderer {

    override fun onSurfaceCreated( unused: GL10, config: EGLConfig) {
        // Set the background frame color
        GLES20.glClearColor(0.1f, 0.3f, 0.3f, 1.0f)

        Log.v( TAG, "INI TEST de vec-mat")
        var test = VecMatTest() ;
        test.run()
        Log.v( TAG, "FIN TEST de vec-mat")
    }

    override fun onDrawFrame( unused: GL10 ) {
        // Redraw background color
        Log.v( TAG, "Comienza 'onDrawFrame'")
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
        Log.v( TAG, "Acaba 'onDrawFrame'")
    }

    override fun onSurfaceChanged(unused: GL10, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
    }



}
// -------------------------------------------------------------------------------------------------
