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
import android.opengl.*
import android.os.Build
import android.util.Log
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import androidx.annotation.RequiresApi

import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import javax.microedition.khronos.egl.EGL10

import mds.pcg1.aplicacion.AplicacionPCG
import mds.pcg1.utilidades.TAG
import mds.pcg1.utilidades.nfnd
import mds.pcg1.vec_mat.VecMatTest
import javax.microedition.khronos.egl.EGLContext
import javax.microedition.khronos.egl.EGLDisplay


// ------------------------------------------------------------------------------------------------

class SGListener : ScaleGestureDetector.SimpleOnScaleGestureListener() { }

//    override fun onScale( detector: ScaleGestureDetector ): Boolean {
//        //Log.v( TAG, "detectado gesto de escala (en progreso) scale f = ${detector.scaleFactor}")
//        return super.onScale(detector)
//    }
//
//    override fun onScaleBegin( detector: ScaleGestureDetector ): Boolean {
//        //Log.v( TAG, "detectado gesto de escala (inicio) scale f = ${detector.scaleFactor}")
//        return super.onScaleBegin(detector)
//    }
//
//    override fun onScaleEnd( detector: ScaleGestureDetector ) {
//        //Log.v( TAG, "detectado gesto de escala (fin) scale f = ${detector.scaleFactor}")
//        //return super.onScaleEnd( detector )
//    }

// ------------------------------------------------------------------------------------------------

// see: https://developer.android.com/develop/ui/views/graphics/opengl/about-opengl#version-check



/**
 * @brief Objeto 'surface view' para una superficie sobre la cual se dibuja con OpenGL
 */
class GLSurfaceViewPCG( p_context: Context ) : GLSurfaceView( p_context )
{
    private val renderer    : RendererPCG
    //private var aplicacion  : AplicacionPCG
    private var dg_escala : ScaleGestureDetector // detector de gestos de escala
    private var listener    : SGListener // hace algo cuando se detectan eventos de escala


    init {


        // Create an OpenGL ES 2.0 context
        setEGLContextClientVersion(2)

        renderer = RendererPCG()

        // Set the Renderer for drawing on the GLSurfaceView
        setRenderer( renderer )

        // Render the view only when there is a change in the drawing data
        renderMode = GLSurfaceView.RENDERMODE_WHEN_DIRTY

        // Eliminar la aplicación PCG anterior y crear una nueva.
        AplicacionPCG.instancia = null   // previene error en ctor de AplicacionPCG por doble creación
        AplicacionPCG( this )  // se registra como 'AplicacionPCG.instancia'

        // crear el detector de gestos de escala
        // ver: https://developer.android.com/reference/kotlin/android/view/ScaleGestureDetector
        listener = SGListener()
        dg_escala = ScaleGestureDetector( context, listener )
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onTouchEvent( me : MotionEvent ) : Boolean
    {
        dg_escala.onTouchEvent( me )  // detecta gesto de escala y actualiza su estado

        if ( dg_escala.isInProgress )
            AplicacionPCG.instancia?.mgePinchInOut( dg_escala.scaleFactor )
        //else
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
    override fun onSurfaceCreated( unused: GL10, config: EGLConfig )
    {
        val TAGF = "[${object {}.javaClass.enclosingMethod?.name?: nfnd}]"

        Log.v( TAGF, "$TAGF inicio")

        VecMatTest().let { it.run() } ;

        Log.v( TAGF, "$TAGF fin")
    }

    override fun onDrawFrame( unused: GL10 )
    {
        //val TAGF = "[${object {}.javaClass.enclosingMethod?.name?: nfnd}]"

        AplicacionPCG.instancia?.mgeVisualizarFrame()
    }

    override fun onSurfaceChanged(unused: GL10, width: Int, height: Int )
    {
        //val TAGF = "[${object {}.javaClass.enclosingMethod?.name?: nfnd}]"

        AplicacionPCG.instancia?.mgeCambioTamano( width, height )
    }
}
// -------------------------------------------------------------------------------------------------
