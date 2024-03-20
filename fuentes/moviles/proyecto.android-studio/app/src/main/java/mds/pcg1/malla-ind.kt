// *********************************************************************
// **
// ** Asignatura: PCG (Programación del Cauce Gráfico).
// **
// ** Clases relacionadas con mallas indexadas
// ** Copyright (C) 2024 Carlos Ureña
// **
// ** Clases: MallaInd
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

package mds.pcg1.malla_ind


import android.util.Log
import android.opengl.GLES30

import mds.pcg1.aplicacion.AplicacionPCG
import mds.pcg1.objeto_visu.*
import mds.pcg1.texturas.Textura
import mds.pcg1.utilidades.*
import mds.pcg1.vaos_vbos.*
import mds.pcg1.vec_mat.*


open class MallaInd : ObjetoVisualizable()
{
    // tablas de atributos
    protected var posiciones  : Array<Vec3> = emptyArray()  // tabla de coordenadas de las posiciones de los vértices
    protected var colores     : Array<Vec3> = emptyArray()  // tabla de colores de los vértices
    protected var normales    : Array<Vec3> = emptyArray()  // tabla de normales de los vértices
    protected var coords_text : Array<Vec2> = emptyArray()  // tabla de coordenadas de textura de los vértices

    // tabla de triángulos (tabla de índices)
    protected var triangulos : Array<UVec3> = emptyArray()


    // descriptor del VAO con la malla
    protected var dvao : DescrVAO? = null

    // descriptor del VAO con las aristas (se crea al llamar a 'crearVAOAristas')
    protected var dvao_aristas : DescrVAO? = null

    // descriptor del VAO con los segmentos de normales (se crea al llamar a 'crearVAONormales')
    protected var dvao_normales : DescrVAO? = null

    // ---------------------------------------------------------------------------------------------

    /**
     * Visualiza la malla indexada
     */
    override fun visualizar()
    {
        val TAGF = "[${object {}.javaClass.enclosingMethod?.name?: nfnd}]"

        val cauce = AplicacionPCG.instancia?.leer_cauce ?: throw Error("$TAGF no se ha podido obtener el cauce")

        guardarCambiarEstado( cauce )

        if ( dvao == null )
            crearInicializarVAO()

        val dvao_nn = dvao ?: throw Error("$TAGF - el descriptor de VAO es nulo")
        dvao_nn.draw( GLES30.GL_TRIANGLES )

        restaurarEstado( cauce )

    }
    // ---------------------------------------------------------------------------------------------

    /**
     * Visualiza las aristas del objeto. Este método puede ser redefinido en clases derivadas, si
     * no se hace, el método no hace nada (eso implica que ese objeto no tiene aristas que se pueden visualizar
     * o que no se ha implementado esto)
     */
    override fun visualizarAristas()
    {
        Log.v( TAG, "$TAG las mallas indexadas no tienen todavía definido el método de visualizar aristas" )
    }
    // ---------------------------------------------------------------------------------------------

    /**
     * Visualiza las normales del objeto. Este método puede ser redefinido en clases derivadas, si
     * no se hace, el método no hace nada (eso implica que ese objeto no tiene normales que se pueden visualizar
     * o que no se ha implementado esto)
     */
    override fun visualizarNormales()
    {
        Log.v( TAG, "$TAG las mallas indexadas no tienen todavía definido el método de visualizar normales" )
    }
    // ---------------------------------------------------------------------------------------------

    /**
     * Comprueba que una malla indexada está correctamente inicializada
     * (se debe llamar al final del constructor de las clases derivadas)
     * se llama antes de visualizar la primera vez.
     */
    fun comprobar( nombref : String )
    {
        assert( posiciones.size > 0 ) { "${nombref} malla indexada con tabla de posiciones de vértices vacía (${nombre})" }
        assert( triangulos.size > 0 )  { "${nombref} malla indexada con tabla de triángulos vacía (${nombre})" }

        if ( colores.size > 0 )
            assert( posiciones.size == colores.size ) { "${nombref} tabla de colores no vacía pero con tamaño distinto a la de vértices (${nombre})" }
        if ( normales.size > 0 )
            assert( posiciones.size == normales.size ) { "${nombref} tabla de normales no vacía pero con tamaño distinto a la de vértices (${this.nombre}) " }
        if ( coords_text.size > 0 )
            assert( posiciones.size == coords_text.size ) { "${nombref} tabla de coordenadas de textura no vacía pero con tamaño distinto a la de vértices (${nombre}) " }

    }
    // ---------------------------------------------------------------------------------------------

    /**
     * Crea e inicializa el VAO (crea [dvao])
     */
    fun crearInicializarVAO()
    {
        val TAGF = "[${object {}.javaClass.enclosingMethod?.name?: nfnd}]"

        Log.v( TAGF, "${TAGF} inicio, creando VAO de ${nombre}" )

        assert( dvao == null ) { "${TAGF} el vao ya está creado."  }
        comprobar( TAGF )

        var tablas = TablasAtributos( ConvFloatArray( posiciones ))

        tablas.indices = ConvIntArray( triangulos )

        if ( colores.size > 0 )
            tablas.colores = ConvFloatArray( colores )
        if ( normales.size > 0 )
            tablas.normales = ConvFloatArray( normales )
        if ( coords_text.size > 0 )
            tablas.coords_text = ConvFloatArray( coords_text )

        dvao = DescrVAO( tablas )

        //Log(`${nombref} fin`)
    }

} // fin de MallaInd

// ---------------------------------------------------------------------------------------------



/**
 * Una clase de pruebas para un rectangulo en 3D
 */
class MallaIndHelloRectangleXY() : MallaInd()
{
    init {
        nombre = "MallaInd Hello Rectangle"

        posiciones = arrayOf(
            Vec3( 0.0f, 0.0f, 0.0f  ),
            Vec3( 1.0f, 0.0f, 0.0f  ),
            Vec3( 1.0f, 1.0f, 0.0f  ),
            Vec3( 0.0f, 1.0f, 0.0f  )
        )
        coords_text = arrayOf(
            Vec2( 0.0f, 0.0f ),
            Vec2( 1.0f, 0.0f ),
            Vec2( 1.0f, 1.0f ),
            Vec2( 0.0f, 1.0f )
        )
        triangulos = arrayOf(
            UVec3( 0u, 1u, 2u ),
            UVec3( 0u, 2u, 3u )
        )

        textura = Textura("imgs/madera2.png")

    }
}

/**
 * Una clase de pruebas para un rectangulo en 3D
 */
class MallaIndHelloRectangleXZ() : MallaInd()
{
    init {
        nombre = "MallaInd Hello Rectangle"

        posiciones = arrayOf(
            Vec3( 0.0f, 0.0f, 0.0f  ),
            Vec3( 1.0f, 0.0f, 0.0f  ),
            Vec3( 1.0f, 0.0f, 1.0f  ),
            Vec3( 0.0f, 0.0f, 1.0f  )
        )

        colores = arrayOf(
            Vec3( 1.0f, 0.0f, 0.0f  ),
            Vec3( 0.0f, 1.0f, 0.0f  ),
            Vec3( 0.0f, 0.0f, 1.0f  ),
            Vec3( 0.0f, 1.0f, 1.0f  )
        )

        triangulos = arrayOf(
            UVec3( 0u, 1u, 2u ),
            UVec3( 0u, 2u, 3u )
        )

    }
}

// *************************************************************************************************

/** Un ob jeto visualizable con los dos rectangulos de arriba **/

open class ObjetoVisCompuesto() : ObjetoVisualizable()
{

    protected var sub_objetos : MutableList<ObjetoVisualizable> = mutableListOf()

    /**
     * Visualiza el objeto compuesto
     */
    override fun visualizar()
    {
        val TAGF = "[${object {}.javaClass.enclosingMethod?.name?: nfnd}]"
        val cauce = AplicacionPCG.instancia?.leer_cauce ?: throw Error("$TAGF no se ha podido obtener el cauce")

        assert( sub_objetos.size > 0 ) {"$TAGF el array de subobjetos está vacío"}

        guardarCambiarEstado( cauce )

        for( obj in sub_objetos )
            obj.visualizar()

        if ( color != null )
            cauce.popColor()

        restaurarEstado( cauce )
    }
}

class DosCuadrados : ObjetoVisCompuesto()
{
    init {
        nombre = "Dos cuadrados"
        sub_objetos.add( MallaIndHelloRectangleXY() )
        sub_objetos.add( MallaIndHelloRectangleXZ() )
    }
}

