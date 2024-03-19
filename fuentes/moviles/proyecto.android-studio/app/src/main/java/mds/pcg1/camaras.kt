package mds.pcg1.camaras

import android.util.Log
import mds.pcg1.cauce.*
import mds.pcg1.utilidades.*
import mds.pcg1.vec_mat.*
import kotlin.math.min

open class CamaraInteractiva( ancho_vp : Int, alto_vp : Int )
{
    // dimensiones del viewport sobre el cual se usa esta cámara
    protected var tam_vp_x_dcc  : Float = ancho_vp.toFloat()   // ancho del viewport en pixels
    protected var tam_vp_y_dcc  : Float = alto_vp.toFloat()    // alto del viewport en pixels

    // matrices de vista y proyección
    protected var mat_vista      = Mat4.ident()
    protected var mat_proyeccion = Mat4.ident()

    /**
     * Mueve la cámara tras un evento de movimiento con un desplazamiento
     * proporcional a [delta_x] y [delta_y]
     * (debe ser redefinido en clases derivadas)
     */
    open fun mover( delta_x : Float, delta_y : Float )
    {
        val TAGF = "[${object {}.javaClass.enclosingMethod?.name?: nfnd}]"
        Log.v( TAG, "este objeto cámara no tiene redefinido el método '$TAGF'")
    }

    /**
     * hacer 'zoom', usando un factor de escala relativo [factor_rel]
     * (debe ser redefinido en clases derivadas)
     */
    open fun zoom( factor_rel : Float )
    {
        val TAGF = "[${object {}.javaClass.enclosingMethod?.name?: nfnd}]"
        Log.v( TAG, "este objeto cámara no tiene redefinido el método '$TAGF'")
    }

    /**
     * Recalcular las matrices en función del estado actual
     * (debe ser redefinido en clases derivadas)
     */
    protected open fun recalcularMatrices( )
    {
        val TAGF = "[${object {}.javaClass.enclosingMethod?.name?: nfnd}]"
        Log.v( TAG, "este objeto cámara no tiene redefinido el método '$TAGF'")
    }

    /**
     * cambia las dimensiones del viewport (tiene efecto cuando se vuelve a activar)
     */
    fun fijarViewport( ancho_vp : Int, alto_vp : Int )
    {
        tam_vp_x_dcc  = ancho_vp.toFloat()   // ancho del viewport en pixels
        tam_vp_y_dcc  = alto_vp.toFloat()    // alto del viewport en pixels
    }

    /**
     * Activa esta cámara en este cauce: recalcula las matrices y fija uniforms
     */
    fun activar( cauce : CauceBase )
    {
        //val TAGF = "[${object {}.javaClass.enclosingMethod?.name?: nfnd}]"

        recalcularMatrices()
        cauce.fijarMatrizVista( mat_vista )
        cauce.fijarMatrizProyeccion( mat_proyeccion )
    }

}
// ----------------------------------------------------------------------------------------
/**
 * Cámaras para vistas 2D, el plano de visión es el plano XY
 * En el viewport se visualiza un cuadrado con centro en [cv_centro_wcc] y
 * lado [cv_lado_wcc]  (todos los valores en coordenadas de mundo)
 */
class CamaraVista2D( ancho_vp : Int, alto_vp : Int ) : CamaraInteractiva( ancho_vp, alto_vp )
{
    private var cv_centro_wcc : Vec3  = Vec3( 0.0f, 0.0f, 0.0f )
    private var cv_lado_wcc   : Float = 2.0f

    /**
     * Mueve la cámara tras un evento de movimiento con un desplazamiento
     * proporcional a [delta_x] y [delta_y]
     * Desplaza [cv_centro_wcc] usando [delta_x] y [delta_y]
     */
    override fun mover( delta_x : Float, delta_y : Float )
    {
        //val TAGF = "[${object {}.javaClass.enclosingMethod?.name?: nfnd}]"

        cv_centro_wcc = cv_centro_wcc - Vec3( delta_x, delta_y, 0.0f )
    }

    /**
     * hacer 'zoom', usando un factor de escala relativo [factor_rel]
     * cambia el lado del cuadrado visible ([cv_lado_wcc]
     */
    override fun zoom( factor_rel : Float )
    {
        //val TAGF = "[${object {}.javaClass.enclosingMethod?.name?: nfnd}]"
        cv_lado_wcc *= 1.0f/factor_rel
    }

    override fun recalcularMatrices()
    {
        val tam_pixel_wcc = cv_lado_wcc/ min( tam_vp_x_dcc, tam_vp_y_dcc ) // lado de un pixel en WCC
        val fx            = (2.0f/cv_lado_wcc)* min( 1.0f, tam_vp_y_dcc/tam_vp_x_dcc )
        val fy            = (2.0f/cv_lado_wcc)* min( 1.0f, tam_vp_x_dcc/tam_vp_y_dcc )
        //val dx_wcc        = touch_dx_dcc*tam_pixel_wcc
        //val dy_wcc        = touch_dy_dcc*tam_pixel_wcc
        mat_vista = Mat4.traslacion( (-tam_pixel_wcc)*cv_centro_wcc )
        mat_proyeccion = Mat4.escalado( Vec3( fx, fy, 1.0f  ))

    }
}
