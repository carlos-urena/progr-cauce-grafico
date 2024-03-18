// *********************************************************************
// **
// ** Asignatura: PCG (Programación del Cauce Gráfico).
// **
// ** Clase singleton AplicacionPCG
// ** Copyright (C) 2024 Carlos Ureña
// **
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


import kotlin.math.*

import android.content.Context
import android.opengl.GLES30
import android.os.Build
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.annotation.RequiresApi

import mds.pcg1.utilidades.*
import mds.pcg1.vec_mat.*
import mds.pcg1.gl_surface.*
import mds.pcg1.cauce.*
import mds.pcg1.vaos_vbos.*

// -------------------------------------------------------------------------------------------------

/**
 * Clase que actua como controlador, contiene referencias a las vista GL, el cauce, etc...
 */
class AplicacionPCG( p_gls_view: GLSurfaceViewPCG )
{
    var gls_view : GLSurfaceViewPCG = p_gls_view

    private var ancho_vp : Int = 0 // ancho del viewport en pixels
    private var alto_vp  : Int = 0 // alto del viewport en pixels


    private var touch_ult_x : Float = 0.0f  // coordenada X (raw) del último evento touch
    private var touch_ult_y : Float = 0.0f  // coordenada Y (raw) del último evento touch

    private var cauce_opc : CauceBase? = null  ; // cauce en uso para hacer el render, se crea en la primera visualización

    private var dvao_hello_triangle = DescrVAOHelloTriangle()

    private var touch_dx_dcc = 0.0f
    private var touch_dy_dcc = 0.0f

    private var cv_lado_wcc   = 2.0f  // lado del cuadrado más grande visible en el viewport (se cambia con pinch in/out)

    init
    {
        // únicamente se puede crear una instancia de la clase 'AplicacionPCG'
        assert( instancia == null ) { "intento de crear una aplicación cuando ya hay otra creada" }

        // registrar la instancia ya creada
        instancia = this
    }

    companion object
    {
        var instancia : AplicacionPCG ? = null
    }
    // ---------------------------------------------------------------------------------------------

    /**
     * Método que procesa un evento tipo _touch_
     * @param [me] objeto tipo `MotionEvent` con los atributos del evento
     */
    @RequiresApi(Build.VERSION_CODES.Q)
    fun mgeTouch( me : MotionEvent )
    {
        val TAGF = "[${object {}.javaClass.enclosingMethod?.name?:nfnd}]"

        if ( me.action == MotionEvent.ACTION_DOWN )
        {
            Log.v( TAGF, "$TAGF touch down")
            touch_ult_x = me.rawX
            touch_ult_y = me.rawY
        }
        else if ( me.action == MotionEvent.ACTION_MOVE )
        {
            val incr_x_raw =  me.rawX - touch_ult_x
            val incr_y_raw =  touch_ult_y - me.rawY

            Log.v( TAGF, "$TAGF touch move, delta == ${incr_x_raw} , ${incr_y_raw}")

            touch_dx_dcc += incr_x_raw
            touch_dy_dcc += incr_y_raw

            touch_ult_x = me.rawX
            touch_ult_y = me.rawY
        }
        else if ( me.action == MotionEvent.ACTION_UP )
        {
            Log.v( TAGF, "$TAGF touch up")//, delta == ${me.rawX - touch_ini_x} , ${me.rawY-touch_ini_y}")
        }

        gls_view.requestRender()

    }
    // ---------------------------------------------------------------------------------------------


    /**
     * Método gestor de evento de un evento 'de escala' (_pinch in/out_)
     * @param [fe] (Float) factor de escala, >1 _pinch **out**_, <1 _pinch in_
     */
    fun mgePinchInOut( fe : Float  )
    {
        val TAGF = "[${object {}.javaClass.enclosingMethod?.name?:nfnd}]"

        Log.v( TAGF, "$TAGF factor escala == $fe")
        val k = 0.03f ;
        val fac = (1.0f/fe)*k + (1.0f-k)
        cv_lado_wcc *= (fac*fac)
        gls_view.requestRender()
    }
    // ---------------------------------------------------------------------------------------------


    /**
     * Método gestor de evento de cambio de tamaño,
     * @param [nuevo_ancho]  [nuevo_alto] (Int) - ancho y alto nuevos
     */
    fun mgeCambioTamano( nuevo_ancho : Int, nuevo_alto : Int )
    {
        val TAGF = "[${object {}.javaClass.enclosingMethod?.name?:nfnd}]"

        Log.v( TAGF, "$TAGF dimensiones de la superficie = $nuevo_alto x $nuevo_ancho")

        alto_vp  = nuevo_alto
        ancho_vp = nuevo_ancho

        //GLES20.glViewport(0, 0, alto_vp, ancho_vp )
    }
    // ---------------------------------------------------------------------------------------------


    /**
     * Método gestor de evento de visualización de un frame,
     * se debe llamar cuando cambia algo del modelo o los parámetros de visualización,
     * su ejecución se puede forzar con el método `requestRender` de [gls_view]
     */
    fun mgeVisualizarFrame()
    {
        val TAGF = "[${object {}.javaClass.enclosingMethod?.name?:nfnd}]"

        // Crear el cauce la 1a vez, y copiarlo en 'cauce' siempre
        if (cauce_opc == null)
           cauce_opc = CauceBase()

        val cauce : CauceBase = cauce_opc!!

        //Log.v(TAGF, "$TAGF inicio - viewport == $ancho_vp x $alto_vp")

        val tam_vp_x_dcc  = ancho_vp.toFloat()
        val tam_vp_y_dcc  = alto_vp.toFloat()

        val tam_pixel_wcc = cv_lado_wcc/min( tam_vp_x_dcc, tam_vp_y_dcc ) // lado de un pixel en WCC
        val ratio_xy      = tam_vp_x_dcc / tam_vp_y_dcc
        val fx            = (2.0f/cv_lado_wcc)*min( 1.0f, tam_vp_y_dcc/tam_vp_x_dcc )
        val fy            = (2.0f/cv_lado_wcc)*min( 1.0f, tam_vp_x_dcc/tam_vp_y_dcc )
        val dx_wcc        = touch_dx_dcc*tam_pixel_wcc
        val dy_wcc        = touch_dy_dcc*tam_pixel_wcc

        cauce.activar()
        cauce.fijarMatrizVista( Mat4.escalado( Vec3(  fx, fy, 1.0f )))
        cauce.resetMM()
        cauce.compMM( Mat4.traslacion( Vec3( dx_wcc, dy_wcc, 0.0f )))

        GLES30.glViewport(0, 0, ancho_vp, alto_vp )
        GLES30.glClearColor(0.1f, 0.3f, 0.3f, 1.0f)
        GLES30.glClear( GLES30.GL_COLOR_BUFFER_BIT or GLES30.GL_DEPTH_BUFFER_BIT )  // 'or' --> bitwise OR ?

        dvao_hello_triangle.draw( GLES30.GL_TRIANGLES )

        //Log.v(TAGF, "$TAGF fin")
    }
    // ---------------------------------------------------------------------------------------------


}

