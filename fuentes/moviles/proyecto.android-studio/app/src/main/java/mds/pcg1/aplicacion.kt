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

import android.opengl.GLES30
import android.util.Log

import mds.pcg1.utilidades.*
import mds.pcg1.vec_mat.*
import mds.pcg1.gls_view.*
import mds.pcg1.cauce.*
import mds.pcg1.vaos_vbos.*
import mds.pcg1.camaras.*
import mds.pcg1.malla_ind.*
import mds.pcg1.objeto_visu.ObjetoVisualizable
import mds.pcg1.texturas.*

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

    private var pinch_ult_fe = 1.0f

    private var ejes = ObjetoEjes()

    // color por defecto inicial
    private val color_ini = Vec3( 0.95f, 0.95f, 1.0f )

    // colección de objetos visualizables y sus correspondientes cámaras interactivas (una por objeto)
    var objetos  : MutableList<ObjetoVisualizable> = mutableListOf()
    var camaras  : MutableList<CamaraInteractiva>  = mutableListOf()

    // índice del objeto actual
    var ind_objeto_act = 0

    // objeto actual, va cambiando con el indice
    private var objeto_act : ObjetoVisualizable

    // cámara actual, va cambiando con el objeto actual
    private var camara_act : CamaraInteractiva

    // ---------------------------------------------------------------------------------------------

    init
    {
        // únicamente se puede crear una instancia de la clase 'AplicacionPCG'
        assert( instancia == null ) { "intento de crear una aplicación cuando ya hay otra creada" }


        objetos.add( DosCuadrados() )
        camaras.add( CamaraOrbital3D( 512, 512 ) )

        objetos.add( HelloTriangle() )
        camaras.add( CamaraVista2D( 512, 512) )

        assert( objetos.size > 0 ) {"no se han creado objetos en el ctor de la aplic."}
        assert( objetos.size == camaras.size ) {"tamaño de 'objetos' y 'camaras' difieren en el ctor de la aplic."}

        // registrar la camara actual como la camara del objeto actual
        camara_act = camaras[ ind_objeto_act ]

        // registrar el objeto actual
        objeto_act = objetos[ ind_objeto_act ]

        // registrar la instancia ya creada, esto debe ser lo ultimo pues a partir de aquí se puede usar.
        instancia = this
    }

    companion object
    {
        var instancia : AplicacionPCG ? = null
    }
    // ---------------------------------------------------------------------------------------------

    /**
     * Método gestor de evento de visualización de un frame,
     * se debe llamar cuando cambia algo del modelo o los parámetros de visualización,
     * su ejecución se puede forzar con el método `requestRender` de [gls_view]
     */
    fun mgeVisualizarFrame()
    {
        //val TAGF = "[${object {}.javaClass.enclosingMethod?.name?:nfnd}]"

        // obtener el cauce de esta instancia
        val cauce = leer_cauce

        //Log.v(TAGF, "$TAGF inicio - viewport == $ancho_vp x $alto_vp")

        assert( objetos.size > 0 )
        assert( camaras.size == objetos.size )
        assert( 0 <= ind_objeto_act && ind_objeto_act < objetos.size  )




        // activar e inicializar el estado del cauce
        cauce.activar()
        cauce.resetMM()
        cauce.fijarEvalText( false, 0 )
        cauce.fijarColor( color_ini )

        camara_act.fijarViewport( ancho_vp, alto_vp )
        camara_act.activar( cauce )

        // inicializar OpenGL y el framebuffer
        GLES30.glViewport(0, 0, ancho_vp, alto_vp )
        GLES30.glClearColor(0.04f, 0.10f, 0.13f, 1.0f)
        GLES30.glClear( GLES30.GL_COLOR_BUFFER_BIT or GLES30.GL_DEPTH_BUFFER_BIT )  // 'or' --> bitwise OR ?
        GLES30.glEnable( GLES30.GL_DEPTH_TEST )

        // visualizar los ejes
        ejes.visualizar()

        // visualizar el par de mallas de test
        objeto_act.visualizar()

        // ya está.

        //Log.v(TAGF, "$TAGF fin")
    }
    // ---------------------------------------------------------------------------------------------

    /**
     * Método que procesa un evento tipo _touch_
     * @param [me] objeto tipo `MotionEvent` con los atributos del evento
     */
    //@RequiresApi(Build.VERSION_CODES.Q)
    fun mgeInicioMover( rawX : Float, rawY : Float  )
    {
        //val TAGF = "[${object {}.javaClass.enclosingMethod?.name?:nfnd}]"
        //Log.v( TAGF, "$TAGF rawX == $rawX - rawY == $rawY")

        touch_ult_x = rawX
        touch_ult_y = rawY
    }
    /**
     * Método que procesa un evento tipo _touch_
     * @param [me] objeto tipo `MotionEvent` con los atributos del evento
     */
    fun mgeMover( rawX : Float, rawY : Float  )
    {
        //val TAGF = "[${object {}.javaClass.enclosingMethod?.name?:nfnd}]"

        val incr_x_raw =  rawX - touch_ult_x
        val incr_y_raw =  touch_ult_y - rawY

        //Log.v( TAGF, "$TAGF touch move, delta == ${incr_x_raw} , ${incr_y_raw}")

        camara_act.mover( incr_x_raw, incr_y_raw )

        touch_ult_x = rawX
        touch_ult_y = rawY

        gls_view.requestRender()
    }
    // ---------------------------------------------------------------------------------------------

    /**
     * Método gestor de inicio de evento de un evento 'de escala' (_pinch in/out_)
     * @param [fe] (Float) factor de escala inicial (es siempre 1)
     */
    fun mgeInicioPinchInOut( fe : Float )
    {
        //val TAGF = "[${object {}.javaClass.enclosingMethod?.name?:nfnd}]"
        //Log.v( TAGF, "$TAGF INICIO escala: fe == $fe")

        pinch_ult_fe = fe
    }
    // ---------------------------------------------------------------------------------------------

    /**
     * Método gestor de evento de un evento 'de escala' (_pinch in/out_)
     * @param [fe] (Float) factor de escala, >1 _pinch **out**_, <1 _pinch in_
     */
    fun mgePinchInOut( fe : Float  )
    {
        //val TAGF = "[${object {}.javaClass.enclosingMethod?.name?:nfnd}]"

        val factor_relativo = fe/pinch_ult_fe // factor relativo, respecto del inicio o del anterior
        pinch_ult_fe = fe

        //Log.v( TAGF, "$TAGF escala: fe == $fe - factor.rel == $factor_relativo ")

        camara_act.zoom( factor_relativo )
        gls_view.requestRender()
    }
    // ---------------------------------------------------------------------------------------------


    /**
     * Método gestor de evento de cambio de tamaño,
     * Recibe el nuevo tamaño [nuevo_ancho]  [nuevo_alto]
     * Simplemente registra el tamaño (lo usa al visualizar).
     */
    fun mgeCambioTamano( nuevo_ancho : Int, nuevo_alto : Int )
    {
        val TAGF = "[${object {}.javaClass.enclosingMethod?.name?:nfnd}]"
        Log.v( TAGF, "$TAGF dimensiones de la superficie = $nuevo_alto x $nuevo_ancho")

        alto_vp  = nuevo_alto
        ancho_vp = nuevo_ancho
    }
    // ---------------------------------------------------------------------------------------------

    /**
     * Devuelve el cauce en uso en la aplicación (nunca nulo)
     * Si el cauce no existe, se crea
     */
    val leer_cauce : CauceBase get()
        {
            val TAGF = "[${object {}.javaClass.enclosingMethod?.name?:nfnd}]"

            if ( cauce_opc == null )
                cauce_opc = CauceBase()

            return cauce_opc ?: throw Error( "$TAGF el cauce es nulo, esto no debería pasar")
        }

    // ---------------------------------------------------------------------------------------------


}

