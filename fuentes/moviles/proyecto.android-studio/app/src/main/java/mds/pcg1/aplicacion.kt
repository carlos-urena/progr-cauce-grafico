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


import android.opengl.GLES20
import android.os.Build
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.annotation.RequiresApi
import mds.pcg1.utilidades.*
import mds.pcg1.vec_mat.*
import mds.pcg1.gl_surface.*
import mds.pcg1.cauce.*

// -------------------------------------------------------------------------------------------------

/**
 * Clase que actua como controlador, contiene referencias a las vista GL, el cauce, etc...
 */
class AplicacionPCG( p_gls_view: GLSurfaceViewPCG )
{
    var gls_view : GLSurfaceViewPCG = p_gls_view

    private var ancho_vp : Int = 0 // ancho del viewport en pixels
    private var alto_vp  : Int = 0 // alto del viewport en pixels


    private var touch_ini_x : Float = 0.0f  // @brief coordenada X de inicio de un evento touch
    private var touch_ini_y : Float = 0.0f  // coordenada Y de inicio de un evento touch

    private var cauce : CauceBase ; // cauce en uso para hacer el render

    init
    {
        // únicamente se puede crear una instancia de la clase 'AplicacionPCG'
        assert( instancia == null ) { "intento de crear una aplicación cuando ya hay otra creada" }

        // crear el cauce (compila shaders, en pruebas)
        cauce = CauceBase()

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
        if ( me.action == MotionEvent.ACTION_DOWN )
        {
            Log.v( TAG, "Touch down")
            touch_ini_x = me.rawX
            touch_ini_y = me.rawY
        }
        else if ( me.action == MotionEvent.ACTION_MOVE )
        {
            Log.v( TAG, "Touch move, delta == ${me.rawX - touch_ini_x} , ${me.rawY-touch_ini_y}")
        }
        else if ( me.action == MotionEvent.ACTION_UP )
        {
            Log.v( TAG, "Touch up")//, delta == ${me.rawX - touch_ini_x} , ${me.rawY-touch_ini_y}")
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
        Log.v( TAG, "Pinch in/out: factor escala == $fe")
        gls_view.requestRender()
    }
    // ---------------------------------------------------------------------------------------------


    /**
     * Método gestor de evento de cambio de tamaño,
     * @param [nuevo_ancho]  [nuevo_alto] (Int) - ancho y alto nuevos
     */
    fun mgeCambioTamano( nuevo_ancho : Int, nuevo_alto : Int )
    {
        Log.v( TAG, "Nuevas dimensiones de la superficie: $nuevo_alto x $nuevo_ancho")

        alto_vp  = nuevo_alto
        ancho_vp = nuevo_ancho

        GLES20.glViewport(0, 0, alto_vp, ancho_vp )
    }
    // ---------------------------------------------------------------------------------------------


    /**
     * Método gestor de evento de visualización de un frame,
     * se debe llamar cuando cambia algo del modelo o los parámetros de visualización,
     * su ejecución se puede forzar con el método `requestRender` de [gls_view]
     */
    fun mgeVisualizarFrame()
    {
        //Log.v( TAG, "Comienza 'mgeVisualizarFrame' viewport == $ancho_vp x $alto_vp")

        GLES20.glClearColor(0.1f, 0.3f, 0.3f, 1.0f)
        GLES20.glClear( GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT )  // 'or' --> bitwise OR

        //Log.v( TAG, "Acaba 'mgeVisualizarFrame'")
    }
    // ---------------------------------------------------------------------------------------------


}

