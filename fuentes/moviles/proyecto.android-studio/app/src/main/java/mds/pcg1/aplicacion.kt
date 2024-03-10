// *********************************************************************
// **
// ** Asignatura: PCG (Programación del Cauce Gráfico).
// **
// ** Clases singleton (superficie OpenGL, renderer, Aplicación)
// ** Copyright (C) 2024 Carlos Ureña
// **
// ** Clases: TouchListenerPCG, GLSurfaceViewPCG, RendererPCG, AplicacionPCG
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

package mds.pcg1.aplicacion

import android.content.Context
import android.opengl.GLSurfaceView
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import android.opengl.GLES20
import android.util.Log
import android.view.MotionEvent
import android.view.View

import mds.pcg1.utilidades.*
import mds.pcg1.vec_mat.*

// ------------------------------------------------------------------------------------------------

/**
 * @brief Objeto que escucha los eventos tipo 'touch' y los redirige a la aplicación
 */
class TouchListenerPCG( p_surface : GLSurfaceView ) : View.OnTouchListener
{
    var gl_surface_view : GLSurfaceView = p_surface

    override fun onTouch(v: View, event: MotionEvent) : Boolean
    {
        Log.v( TAG, "ON TOUCH!")
        //Log.v( TAG, "classification == ${event.getClassification()} ")

        Log.v( TAG, "event x y == ${event.rawX} - ${event.rawY}")
        gl_surface_view.requestRender()
        return true
    }
}
// -------------------------------------------------------------------------------------------------

/**
 * @brief Objeto 'surface view' para una superficie sobre la cual se dibuja con OpenGL
 */
class GLSurfaceViewPCG( context: Context ) : GLSurfaceView( context )
{
    private val renderer: RendererPCG
    private var aplicacion : AplicacionPCG

    init {

        // Create an OpenGL ES 2.0 context
        setEGLContextClientVersion(2)

        renderer = RendererPCG()

        // Set the Renderer for drawing on the GLSurfaceView
        setRenderer( renderer )

        // Render the view only when there is a change in the drawing data
        renderMode = GLSurfaceView.RENDERMODE_WHEN_DIRTY

        // Crear la aplicación PCG
        aplicacion = AplicacionPCG( this )

        // probando....
        this.setOnTouchListener( TouchListenerPCG( this ) )

    }
}
// -------------------------------------------------------------------------------------------------

/**
 * @brief Objeto de tipo 'Renderer'
 */
class RendererPCG : GLSurfaceView.Renderer
{

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

/**
 * Clase que actua como controlador, contiene referencias a las vista GL, el cauce, etc...
 */
class AplicacionPCG( p_gls_view : GLSurfaceViewPCG )
{
    var gls_view : GLSurfaceViewPCG = p_gls_view
}

