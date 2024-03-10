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
import android.util.Log
import android.view.MotionEvent
import android.view.View
import mds.pcg1.utilidades.*
import mds.pcg1.vec_mat.*
import mds.pcg1.gl_surface.*



/**
 * Clase que actua como controlador, contiene referencias a las vista GL, el cauce, etc...
 */
class AplicacionPCG( p_gls_view: GLSurfaceViewPCG )
{
    var gls_view : GLSurfaceViewPCG = p_gls_view

    private var ancho_vp : Int = 0
    private var alto_vp  : Int = 0

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

    /**
     * @brief Método gestor de eventos 'touch'
     * @return 'true' si es necesario redibujar, 'false' si nada ha cambiado
     */
    fun mgeTouch( event: MotionEvent )
    {
        Log.v( TAG, "ON TOUCH!")
        //Log.v( TAG, "classification == ${event.getClassification()} ")

        Log.v( TAG, "event x y == ${event.rawX} - ${event.rawY}")

        gls_view.requestRender()
        return
    }

    /**
     * @brief M.G.E. cambio de tamaño
     */
    fun mgeCambioTamano( nuevo_ancho : Int, nuevo_alto : Int )
    {
        Log.v( TAG, "!! nuevas dimensiones de la superficie: $nuevo_alto x $nuevo_ancho")

        alto_vp  = nuevo_alto
        ancho_vp = nuevo_ancho

        GLES20.glViewport(0, 0, alto_vp, ancho_vp )
    }

    /**
     * @brief M.G.E. de revisualización de un frame, se llama cuando cambia algo del modelo (o en animaciones?)
     */
    fun mgeVisualizarFrame()
    {
        Log.v( TAG, "Comienza 'mgeVisualizarFrame' viewport == $ancho_vp x $alto_vp")

        GLES20.glClearColor(0.1f, 0.3f, 0.3f, 1.0f)
        GLES20.glClear( GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT )  // 'or' --> bitwise OR

        Log.v( TAG, "Acaba 'mgeVisualizarFrame'")
    }


}

