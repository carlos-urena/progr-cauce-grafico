// *********************************************************************
// **
// ** Asignatura: PCG (Programación del Cauce Gráfico).
// **
// ** Clases relacionadas con VAOs y VBOs y tablas de atributos.
// ** Copyright (C) 2024 Carlos Ureña
// **
// ** Clases: Vec2, Vec3, Vec4, Mat4 (package vec_mat)
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

package mds.pcg1.vaos_vbos

import android.opengl.GLES20
import mds.pcg1.utilidades.*
import java.nio.Buffer
import java.nio.FloatBuffer
import java.nio.IntBuffer
import java.nio.IntBuffer.allocate





// ESTE ARCHIVO NO COMPILA, ESTA PENDIENTE DE ADAPTAR

/**
 * Estructura con las tablas de datos, se usa para dar los datos de entrada
 * necesarios para construir un VAO. Hay que inicializar al menos las posiciones
 */
@OptIn(ExperimentalUnsignedTypes::class) // opt-in para esta clase (por ahora, usar un array de unsigned es "experimental" en Kotlin)

class TablasAtributos
{
    // tablas de posiciones y resto de atributos
    var posiciones   : FloatArray ? = null
    var colores      : FloatArray ? = null
    var normales     : FloatArray ? = null
    var coords_text  : FloatArray ? = null

    // tabla de índices
    var indices      : UIntArray ?  = null
}

// ----------------------------------------------------------------------------
/**
 * Descriptor de un VBO (Vertex Buffer Object) con una tabla de un atributo de vértices
 *   - [p_index] índice de esta tabla de atributo
 *   - [p_size]  número de valores flotantes en cada tupla
 *   - [p_data]  valores flotantes para guardar en el VBO
 */
class DescrVBOAtrib( p_index : Int, p_size : Int, p_data : FloatArray )
{

    // índice de esta tabla de atributos
    private var index: Int = p_index

    // número de tuplas de valores en la tabla
    private var count: Int = p_data.size / p_size


    // tamaño de cada tupla en la tabla de datos
    private var size: Int = p_size

    // identificador del objeto buffer de OpenGL (VBO)
    // (0 antes de crearlo)
    private var buffer: Int = 0

    // tamaño de cada valor en bytes (por ahora usamos floats de 4 bytes)
    private var val_size : Int = 4

    // tamaño en bytes del buffer completo
    private var tot_size = count * size * val_size

    // referencia a los datos (únicamente para lectura).
    private val data: FloatArray = p_data

    // -------------------------------------------------------------------------------------------

    init {
        // Comprobar que está todo correcto al construir el VBO
        comprobar()
    }
    // -------------------------------------------------------------------------------------------

    fun get_count(): Int // ponerlo como getter de count
    {
        return count
    }

    fun get_index(): Int // ponerlo como getter de index
    {
        return index
    }

    // -------------------------------------------------------------------------------------------

    private fun comprobar()
    {
        val TAGF = "[${object {}.javaClass.enclosingMethod?.name ?: nfnd}]"

        assert(2 <= size && size <= 4) { "${TAGF} 'size' (== ${size}) must be between 2 and 4" }
        assert(1 <= data.size / size) { "${TAGF} 'size' (== ${size}) cannot be greater than 'data.length' (== ${data.size}) " }
        assert(data.size % size == 0) { "${TAGF}: número de valores en 'data' (${data.size}) no es múltiplo de 'size' (${size}) " }
        assert( tot_size == count*size*4 ) { "{TAGF} tamaño en bytes ($tot_size) es incoherente"}
    }
    // -------------------------------------------------------------------------------------------

    /**
     * Crea el VBO en la GPU y lo registra en el VAO activo, inicializa [buffer]
     * Usa los datos en [data] como fuentes.
     */
    fun crearVBO(  )
    {
        val TAGF = "[${object {}.javaClass.enclosingMethod?.name ?: nfnd}]"
        ComprErrorGL( "$TAGF : hay un error de OpenGL a la entrada")

        // crear el identificador de buffer en 'buffer'
        var ident_buffer : IntBuffer = allocate( 1 ) // array con un entero con id. de buff.
        GLES20.glGenBuffers( 1, ident_buffer )
        buffer = ident_buffer[0]
        assert( buffer != 0 ) { "${TAGF} no se ha podido crear el buffer" }

        // copiar datos hacia la GPU
        GLES20.glBindBuffer( GLES20.GL_ARRAY_BUFFER, buffer )
        GLES20.glBufferData( GLES20.GL_ARRAY_BUFFER, tot_size, FloatBuffer.wrap( data ),
                             GLES20.GL_STATIC_DRAW )
        assert( GLES20.glIsBuffer( buffer )) { "${TAGF} el buffer creado es inválido" }

        // registrar y activar el VBO en el VAO activo
        GLES20.glVertexAttribPointer( index, size, GLES20.GL_FLOAT, false, 0, 0  )
        GLES20.glBindBuffer( GLES20.GL_ARRAY_BUFFER, 0 )
        GLES20.glEnableVertexAttribArray( index )

        // ya está
        ComprErrorGL( "${TAGF}: hay un error de OpenGL a la salida")
    }

}  // final de DescrVBO


// -------------------------------------------------------------------------------------------------

/**
 * Descriptor de VBOs (Vertex Buffer Object) de índices
 */

//@OptIn(ExperimentalUnsignedTypes::class)

class DescrVBOInd( p_indices : IntArray )
{

    // -------------------------------------------------------------------------------------------
    // Variables de instancia

    // copia de los indices, propiedad de este objeto

    val indices : IntArray = p_indices

    // tipo de los valores (GL_UNSIGNED_BYTE, GL_UNSIGNED_SHORT, GL_UNSIGNED_INT)
    // (por ahora únicamente contemplamos arrays de unsigned, ya no existe la clase
    // UIntBuffer, pero sí IntBuffer)
    val type : Int = GLES20.GL_INT

    // cuenta de índices (>0)
    val count : Int = p_indices.size

    // tamaño de cada valor en bytes (el correspondiente a 'type')
    private var val_size : Int = 4

    // tamaño en bytes del buffer completo
    private var tot_size = count * val_size

    // VBO OpenGL, null hasta que se crea (con 'crearVVBO')
    var buffer : Int = 0

    // -------------------------------------------------------------------------------------------

    init {
        // Comprobar que está todo correcto al construir el VBO
        comprobar()
    }
    // -------------------------------------------------------------------------------------------

    private fun comprobar() {
        val TAGF = "[${object {}.javaClass.enclosingMethod?.name ?: nfnd}]"

        assert( count > 0 && count == indices.size ) { "$TAGF cuenta de índices es 0 o incoherente" }
        assert( tot_size == count*val_size ) { "$TAGF total de bytes incoherente"}
    }
    // -------------------------------------------------------------------------------------------

    fun get_count(): Int // ponerlo como getter de count
    {
        return count
    }
    // -------------------------------------------------------------------------------------------

    /**
     * Devuelve el tipo OpenGL de los índices
     * @returns variable de instancia 'type'
     */
    fun get_type() : Int
    {
        return this.type
    }
    // -------------------------------------------------------------------------------------------

    /**
     * Crea el VBO en la GPU, inicializa [buffer] con el identificador de buffer
     */
    fun crearVBO( )
    {
        val TAGF = "[${object {}.javaClass.enclosingMethod?.name ?: nfnd}]"

        ComprErrorGL( "${TAGF} error de OpenGL al inicio" )

        // crear y activar el buffer
        var ident_buffer  : IntBuffer = allocate( 1 ) // array de enteros con un entero con el identificador del buffer
        GLES20.glGenBuffers( 1, ident_buffer )
        buffer = ident_buffer[0]
        assert( buffer != 0 ) { "${TAGF} no se ha podido crear el buffer" }

        GLES20.glBindBuffer( GLES20.GL_ELEMENT_ARRAY_BUFFER, buffer )
        GLES20.glBufferData( GLES20.GL_ELEMENT_ARRAY_BUFFER, tot_size, IntBuffer.wrap( indices ),
                             GLES20.GL_STATIC_DRAW )
        assert( GLES20.glIsBuffer( buffer )) { "${TAGF} el buffer creado es inválido" }

        // ya está
        ComprErrorGL( "${TAGF} error de OpenGL al final")
    }

} // final DescrVBOInd

    /**
// Nota (YA HECHO)
//
// mejorar 'DescrVAO' usando como parámetro del constructor una instancia de un
// interfaz que contiene las tablas, obligatoriamente posiciones y opcionalmente
// indices y otros atributos.
//
// ver: https://www.typescriptlang.org/docs/handbook/interfaces.html

// -------------------------------------------------------------------------------------------------
/**
 *   Descriptor de VAO (Vertex Array Object)
 *
 *   Una clase para encapsular los VBOs de un VAO de WebGL, junto con el VAO en sí
 *   (https://developer.mozilla.org/en-US/docs/Web/API/WebGLVertexArrayObject)
 *
 */
export class DescrVAO
{
    // --------------------------------------------------------------------
    // Variables de instancia

    private nombre_vao : string = "no asignado"
    public get nombre() : string          { return this.nombre_vao }
    public set nombre( nombre : string )  { this.nombre_vao = nombre }

    // VAO en la GPU
    private array : WebGLVertexArrayObject | null = null

    // número de tablas de atributos que tiene el VAO (incluyendo las posicione)
    // como máximo será el numero total de atributos que gestiona el cauce
    private readonly num_atribs : number = 0

    // número de vértices en la tabla de posiciones de vértices
    private readonly count : number = 0

    // número de índices en la tabla de índices (si hay índices, en otro caso 0)
    private idxs_count : number = 0

    // si hay índices, tiene el tipo de los índices
    private idxs_type : number = 0

    // si la secuencia es indexada, VBO de attrs, en otro caso 'null'
    private dvbo_indices : DescrVBOInd | null  = null

    // array con los descriptores de VBOs de atributos
    private dvbo_atributo : Array<DescrVBOAtrib|null> = new Array<DescrVBOAtrib|null>

    // array que indica si cada tabla de atributos está habilitada o deshabilitada
    private atrib_habilitado : Array<Boolean> = new Array<Boolean>

    // 'objeto extensión' de WebGL (no tiene tipo definido en Typescript???)
    private ext_vao : any | null = null

    /**
     * Crea un descriptor de VAO, dando un descriptor del VBO de posiciones de vértices
     */
    constructor( tablas : TablasAtributos )
    {
        const nombref  = 'DescrVAO.constructor:'
        let gl = AplicacionPCG.instancia.gl

        this.num_atribs = Cauce.numero_atributos

        // si estamos en WebGL 1.0, exigir que esté disponible la extensión 'OES_vertex_array_object'  (revisar esto)
        if ( gl instanceof WebGLRenderingContext  )
        {
            this.ext_vao = gl.getExtension("OES_vertex_array_object")
            if ( this.ext_vao == null )
                throw Error(`${nombref} no encuentro la extensión 'OES_vertex_array_object'`)
        }

        // inicializar tablas de VBOs y de estado habilitado/deshabilitado
        for( let i = 0 ; i < this.num_atribs ; i++ )
        {
            this.dvbo_atributo.push( null )    // inicialmente no hay tabla de atributos (son null)
            this.atrib_habilitado.push( true ) // habilitado por defecto (si no es null)
        }

        // crear el VBO de posiciones (siempre tiene que estar)
        if ( ! tablas.hasOwnProperty("posiciones"))  // creo que no puede ocurrir ya que 'posiciones' no es opcional
            throw new Error(`${nombref} no hay tabla de posiciones en el conjunto de tablas  de entrada`)

        let dvbo_posiciones = new DescrVBOAtrib( Cauce.indice_atributo.posicion, 3, CrearFloat32ArrayV3( tablas.posiciones ) )
        this.dvbo_atributo[0] = dvbo_posiciones
        this.count = dvbo_posiciones.get_count

        // si hay tabla de índices, crear y añadir el correspondiente VBO
        if ( tablas.indices !== undefined )
            this.agregar_tabla_ind( tablas.indices )

        // si hay tabla de colores, crear y añadir el correspondiente VBO
        if ( tablas.colores !== undefined )
            this.agregar_tabla_atrib_v3( Cauce.indice_atributo.color, tablas.colores )

        // si hay tabla de normales, crear y añadir el correspondiente VBO
        if ( tablas.normales !== undefined )
            this.agregar_tabla_atrib_v3( Cauce.indice_atributo.normal, tablas.normales )

        // si hay tabla de coordenadas de textura, crear y añadir el correspondiente VBO
        if ( tablas.coords_text !== undefined )
            this.agregar_tabla_atrib_v2( Cauce.indice_atributo.coords_text, tablas.coords_text )
    }
    // -------------------------------------------------------------------------------------------------

    /**
     * Añade al VAO un descriptor de VBO de atributos, se llama desde 'agregar'
     * @param dvbo_atributo
     */
    public agregar_atrib( dvbo_atributo : DescrVBOAtrib ) : void
    {
        const nombref  = 'DescrVAO.agregar_atrib'

        let index = dvbo_atributo.get_index

        Assert( 0 < index && index < this.num_atribs, `${nombref} intro de agregar VBO con índice fuera de rango`)
        Assert( this.count == dvbo_atributo.get_count, `${nombref} intento de añadir un descriptor de atributo con un número de tuplas distinto al de vértices` );
        // registrar el descriptor de VBO en la tabla de descriptores de VBOs de atributos
        this.dvbo_atributo[index] = dvbo_atributo
    }
    // -------------------------------------------------------------------------------------------------

    /**
     * Crea un descriptor de VBO de atributos con tuplas de 2 elementos, y lo añade al VAO
     * @param dvbo_atributo
     */
    public agregar_tabla_atrib_v2( index : number, tabla : TablaFloatV2 ) : void
    {
        this.agregar_atrib( new DescrVBOAtrib( index, 2, CrearFloat32ArrayV2( tabla )))
    }
    // -------------------------------------------------------------------------------------------------

    /**
     * Crea un descriptor de VBO de atributos con tuplas de 3 elementos, y lo añade al VAO
     * @param dvbo_atributo
     */
    public agregar_tabla_atrib_v3( index : number, tabla : TablaFloatV3 ) : void
    {
        this.agregar_atrib( new DescrVBOAtrib( index, 3, CrearFloat32ArrayV3( tabla )))
    }
    // -------------------------------------------------------------------------------------------------

    /**
     * Añade al VAO un descriptor de VBO de índices, se llama desde 'agregar'
     * @param dvbo_atributo
     */
    public agregar_ind( dvbo_indices : DescrVBOInd ) : void
    {
        const nombref  = 'DescrVAO.agregar_ind:'

        this.idxs_count = dvbo_indices.get_count
        this.idxs_type  = dvbo_indices.get_type
        this.dvbo_indices = dvbo_indices
    }
    // -------------------------------------------------------------------------------------------------

    /**
     * Crea un descriptor de VBO de índices y lo añade al VAO
     * @param dvbo_atributo
     */
    public agregar_tabla_ind( tabla : TablaUnsigned ) : void
    {
        this.agregar_ind( new DescrVBOInd( CrearUint32Array( tabla )))
    }
    // -------------------------------------------------------------------------------------------------

    /**
     * Agrega un descriptor de VBO al VAO (puede ser un descriptor de VBO de atributo o de indice)
     * @param dvbo descriptor que se quiere agregar
     */
    public agregar( dvbo : DescrVBOAtrib | DescrVBOInd ) : void
    {
        const nombref : string = 'DescrVAO.agregar'

        if ( dvbo instanceof DescrVBOAtrib )
            this.agregar_atrib( dvbo )
        else if ( dvbo instanceof DescrVBOInd )
            this.agregar_ind( dvbo )
        else
            Assert( false, `${nombref} 'dvbo' no es un descriptor de VBO de atributos ni de índices`)
    }
    // -------------------------------------------------------------------------------------------------
    /**
     * Llama a 'createVertexArray' o 'createVertexArrayOES' en función de las características del
     * contexto WebGL y el objeto extensión ('this.gl' y 'this.ext_vao')
     * Asigna valor 'this.array'
     */
    private crearArray() : void
    {
        const nombref : string = `DescrVAO.crearArray`
        let gl = AplicacionPCG.instancia.gl

        if ( this.ext_vao == null && gl instanceof WebGL2RenderingContext )
        {
            this.array = gl.createVertexArray()
            Assert( gl != null , `${nombref} no se ha creado objeto VAO`)
        }
        else if ( this.ext_vao != null )
        {
            this.array = this.ext_vao.createVertexArrayOES()
            Assert( gl != null , `${nombref} no se ha creado objeto VAO`)
        }
        else
            throw Error(`${nombref} estado inconsistente`)
    }
    // -------------------------------------------------------------------------------------------------

    /**
     * Llama a 'bindVertexArray' o 'bindVertexArrayOES' en función de las características del
     * contexto WebGL y el objeto extensión ('this.gl' y 'this.ext_vao')
     */
    private activarVAO() : void
    {
        const nombref : string = `DescrVAO.activarVertexArray`

        let gl = AplicacionPCG.instancia.gl

        if ( gl == null )
            throw Error( `${nombref} 'gl' es nulo`)
        else if ( this.array == null )
            throw Error( `${nombref} 'array' es nulo`)
        else if ( this.ext_vao == null && gl instanceof WebGL2RenderingContext )
            gl.bindVertexArray( this.array )
        else if ( this.ext_vao != null )
            this.ext_vao.bindVertexArrayOES( this.array )
        else
            throw Error(`${nombref} estado inconsistente`)
    }

    /**
     * Llama a 'bindVertexArray(0)' o 'bindVertexArrayOES(0)' en función de las características del
     * contexto WebGL y el objeto extensión ('this.gl' y 'this.ext_vao')
     */
    private desactivarVAO() : void
    {
        const nombref : string = `DescrVAO.desactivarVAO`

        let gl = AplicacionPCG.instancia.gl

        if ( this.array == null )
            throw Error( `${nombref} 'array' es nulo`)
        else if ( this.ext_vao == null && gl instanceof WebGL2RenderingContext )
            gl.bindVertexArray( null )
        else if ( this.ext_vao != null )
            this.ext_vao.bindVertexArrayOES( null )
        else
            throw Error(`${nombref} estado inconsistente`)
    }
    // -------------------------------------------------------------------------------------------------

    /**
     * Crea el VAO en la GPU (queda 'binded')
     */
    private crearVAO() : void
    {
        const nombref : string = 'DescrVAO.crearVAO'
        let gl = AplicacionPCG.instancia.gl

        if ( this.dvbo_atributo[0] == null )
            throw Error("nunca pasa!")

        ComprErrorGL( gl, `${nombref} error de OpenGL a la entrada` )
        Assert( this.array == null, `${nombref}: el VAO ya se había creado antes` )

        // crear y activar el VAO
        this.crearArray()   // llama a 'gl.createVertexArray' o bien 'ext.createVertexArrayOES'
        this.activarVAO() // llama a 'gl.bindVertexArray' o bien 'ext.bindVertexArrayOES'

        // crear (y habilitar) los VBOs de posiciones y atributos en este VAO
        this.dvbo_atributo[0].crearVBO()

        for( let i = 1 ; i < this.num_atribs ; i++ )
        {
            let dvbo = this.dvbo_atributo[i]
            if ( dvbo != null )
                dvbo.crearVBO()
        }
        // si procede, crea el VBO de índices
        if ( this.dvbo_indices != null )
            this.dvbo_indices.crearVBO()

        // deshabilitar tablas que no estén habilitadas
        for( let i = 1 ; i < this.num_atribs ; i++ )
        {
            let dvbo = this.dvbo_atributo[i]
            if ( dvbo != null )
                if ( ! this.atrib_habilitado[i] )
                    gl.disableVertexAttribArray( i )
        }
        ComprErrorGL( gl, `${nombref} error de OpenGL a la salida` )
    }
    // -------------------------------------------------------------------------------------------------

    public habilitarAtrib( index : number, habilitar : Boolean ) : void
    {
        const nombref : string = 'DescrVAO.habilitarAtrib'
        // comprobar precondiciones
        Assert( 0 < index, `${nombref} índice es cero o negativo` )
        Assert( index < this.num_atribs, `${nombref} índice fuera de rango` )
        Assert( this.dvbo_atributo[index] != null, `${nombref} no se puede habilitar/deshab. un atributo sin tabla` )

        let gl = AplicacionPCG.instancia.gl

        // registrar el nuevo valor del flag
        this.atrib_habilitado[index] = habilitar

        // si el VAO ya se ha enviado a la GPU, actualizar estado del VAO en OpenGL
        if ( this.array != 0 )
        {
            ComprErrorGL( gl, `${nombref} pepe`)
            this.activarVAO()

            if ( habilitar )
                gl.enableVertexAttribArray( index );
            else
                gl.disableVertexAttribArray( index );
        }
    }
    // -------------------------------------------------------------------------------------------------

    /**
     * Visualiza el VAO, si no está creado en la GPU, lo crea.
     * (para los tipos de primitivas, ver: )
     *
     * @param mode (number) tipo de primitiva (gl.LINES, gl.TRIANGLES, etc...)
     */
    public draw( mode : number ) : void
    {
        const nombref : string = 'DescrVAO.draw:'
        let gl = AplicacionPCG.instancia.gl

        ComprErrorGL( gl, `${nombref} al inicio (vao==${this.nombre})`)

        if ( this.array == null )
            this.crearVAO()
        else
            this.activarVAO()

        ComprErrorGL( gl, `${nombref} vao creado`)

        if ( this.dvbo_indices == null )
            gl.drawArrays( mode, 0, this.count )
        else
            gl.drawElements( mode, this.idxs_count, this.idxs_type, 0 )


        this.desactivarVAO()
        ComprErrorGL( gl, `${nombref} al final (vao==${this.nombre})`)

    }
}



// -------------------------------------------------------------------------------------------------
/**
 * A simple indexed vertex sequence (with colors)
 */

export class CuadroXYColores extends ObjetoVisualizable
{
    private dvao : DescrVAO
    //private gl   : WebGL2RenderingContext | WebGLRenderingContext

    constructor(  )
    {
        super()
        let gl = AplicacionPCG.instancia.gl

        this.fijarNombre = "Cuadro colores"

        this.dvao = new DescrVAO
        ({
            posiciones:
            [
                -0.9, -0.9, 0.0,
                +0.9, -0.9, 0.0,
                -0.9, +0.9, 0.0,
                +0.9, +0.9, 0.0
            ],
            colores:
            [
                1.0,  0.0, 0.0,
                0.0,  1.0, 0.0,
                0.0,  0.0, 1.0,
                1.0,  1.0, 1.0
            ],
            normales:
            [
                0.0,  0.0, 1.0,
                0.0,  0.0, 1.0,
                0.0,  0.0, 1.0,
                0.0,  0.0, 1.0
            ],
            indices:
            [
                0,1,3,
                0,3,2
            ]
        })
    }

    public visualizar( ): void
    {
        let gl = AplicacionPCG.instancia.gl
        this.dvao.draw( gl.TRIANGLES )
    }
}
// -------------------------------------------------------------------------------------------------

 **/






