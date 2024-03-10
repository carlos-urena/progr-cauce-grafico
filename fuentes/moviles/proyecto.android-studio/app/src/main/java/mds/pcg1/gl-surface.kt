// *********************************************************************
// **
// ** Asignatura: PCG (Programación del Cauce Gráfico).
// **
// ** Clases relacionadas con la vista de la superficie OpenGL ES
// ** Copyright (C) 2024 Carlos Ureña
// **
// ** Clases: GLSurfaceViewPCG, RendererPCG
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

package mds.pcg1.gl_surface

import android.content.Context
import android.opengl.GLSurfaceView
import android.util.Log
import android.view.MotionEvent
import android.view.View

import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

import mds.pcg1.aplicacion.AplicacionPCG
import mds.pcg1.utilidades.TAG
import mds.pcg1.vec_mat.VecMatTest

// ------------------------------------------------------------------------------------------------

/**
 * @brief Objeto 'surface view' para una superficie sobre la cual se dibuja con OpenGL
 */
class GLSurfaceViewPCG( context: Context) : GLSurfaceView( context )
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
    }

    override fun onTouchEvent( me : MotionEvent ) : Boolean
    {
        AplicacionPCG.instancia?.mgeTouch( me )
        return true
    }


}

// -------------------------------------------------------------------------------------------------

/**
 * @brief Objeto de tipo 'Renderer'
 */
class RendererPCG( ) : GLSurfaceView.Renderer
{
    override fun onSurfaceCreated(unused: GL10, config: EGLConfig)
    {
        Log.v( TAG, "INI TEST de vec-mat")
        var test = VecMatTest() ;
        test.run()
        Log.v( TAG, "FIN TEST de vec-mat")
    }

    override fun onDrawFrame( unused: GL10)
    {
        AplicacionPCG.instancia?.mgeVisualizarFrame()
    }

    override fun onSurfaceChanged(unused: GL10, width: Int, height: Int )
    {
        AplicacionPCG.instancia?.mgeCambioTamano( width, height )
    }
}
// -------------------------------------------------------------------------------------------------
