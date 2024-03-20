package mds.pcg1.malla_ply

import android.util.Log
import mds.pcg1.malla_ind.*
import mds.pcg1.utilidades.*
import mds.pcg1.vec_mat.UVec3
import mds.pcg1.vec_mat.Vec3
import java.lang.Float.parseFloat
import java.lang.Integer.parseInt

fun Tokenize( linea : String ) : List<String>
{
    val tks_in = linea.trim().trim().split(" " )
    var tks_out : MutableList<String> = mutableListOf()

    for( tk in tks_in )
        if ( tk != "" )
            tks_out.add( tk )
    return tks_out
}


class MallaPLY( p_nombre_arch : String ) : MallaInd()
{
    val nombre_arch: String = p_nombre_arch

    init {
        analizarPLY()
    }

    /** analiza un archivo de texto con un modelo codificado en PLY
     * escribe en las tablas de posiciones y triangulos
     */
    fun analizarPLY()
    {
        val TAGF: String = "[${object {}.javaClass.enclosingMethod?.name ?: nfnd}]"
        val info: String = "${TAGF} leyendo ply '${nombre_arch}': "

        Log.v( TAGF, "$TAGF INICIO INICIO - nombref == $nombre_arch")

        assert(posiciones.size == 0) { "${info} la tabla de posiciones de vértices no estaba vacía" }
        assert(triangulos.size == 0) { "${info} la tabla de triángulos no estaba vacía" }

        val texto_ply_bruto: String = LeerArchivoDeTexto("plys/${nombre_arch}")
        val lineas: List<String> = texto_ply_bruto.split("\n")

        Log.v( TAGF, "$TAGF INICIO INICIO - lineas.size == ${lineas.size}")

        var nl: Int = 0  // número de línea que se va a procesar

        var nv: Int = 0 // número de vértices según la cabecera
        var nt: Int = 0 // numero de caras (triángulos) según la cabecera

        var tokens: List<String> //= arrayListOf()  // tokens de la linea actual
        var linea: String // linea actual
        var estado: Int = 0 // estado: 0 --> en cabecera, 1 leyendo vértices, 2 leyendo caras

        // Parser de PLYs: lee línea a línea y puede estar en alguno de estos 3 estados:
        //
        //   0 -> en la cabecera (antes de leer vértices) (estado inicial).
        //   1 -> leyendo la tabla de vértices
        //   2 -> leyendo la tabla de caras

        // leer y procesar todas las líneas en un bucle
        while (nl < lineas.size) {
            val info_linea = "${info} línea ${nl+1}:"

            // hacer cambio de estado 1 a 2 si procede

            if ( estado == 1 && posiciones.size == nv)
                estado = 2

            // obtener siguiente línea (se busca hasta línea no vacía o el final del archivo)

            linea = lineas[nl]
            tokens = Tokenize( linea )

            while (tokens.size == 0 && nl < lineas.size - 1) {
                nl = nl + 1
                linea = lineas[nl]
                tokens = Tokenize( linea )
            }

            // debug de los tokens
            var dbg = ""
            for( t in tokens )
                dbg = "$dbg [$t]"
            Log.v( TAGF, "$TAGF linea: tokens == $dbg")

            // salir si se ha llegado al final de las lineas sin encontrar ninguna no vacía
            if (nl == lineas.size - 1)
                break

            // procesar línea
            if ( estado == 0 && tokens[0] != "comment")  // si estamos en la cabecera
            {
                if (tokens[0] == "element" && tokens[1] == "vertex")
                {
                    nv = parseInt(tokens[2])
                    assert(nv > 0) { "${info_linea} el número de vértices indicado en la cabecera es 0 o no es un entero" }
                }
                else if (tokens[0] == "element" && tokens[1] == "face")
                {
                    nt = parseInt(tokens[2])
                    assert(nt > 0) { "${info_linea} el número de caras indicado en la cabecera es 0 o no es un entero" }
                }
                else if (tokens[0] == "end_header") // pasar al estado 1 al acabar la cabecera
                {
                    assert(nv > 0) { "${info_linea} no se encuentra el número de vértices en la cabecera" }
                    assert(nt > 0) { "${info_linea} no se encuentra el número de caras en la cabecera" }
                    estado = 1
                }
                else
                    throw Error("$info_linea token '${tokens[0]}' no reconocido (estado == $estado)")
            }
            else if ( estado == 1 && tokens[0] != "comment") // leyendo vértices
            {
                assert(3 <= tokens.size) { "${info_linea} línea de vértice con menos de tres valores" }
                posiciones.add(
                    Vec3(
                        parseFloat(tokens[0]),
                        parseFloat(tokens[1]),
                        parseFloat(tokens[2])
                    )
                )
            }
            else if ( estado == 2 && tokens[0] != "comment") // estado == 2 (leyendo caras)
            {
                assert(tokens.size == 4) { "${info_linea} se esperaban 4 números, leyendo caras \n(línea == [${linea}])" }
                assert(parseInt(tokens[0]) == 3) { "${info_linea} encontrada cara con un número de vértices distinto de 3 o que no es un entero." }
                triangulos.add(
                    UVec3(
                        parseInt(tokens[1]).toUInt(),
                        parseInt(tokens[2]).toUInt(),
                        parseInt(tokens[3]).toUInt()
                    )
                )
            }
            else if ( tokens[0] == "comment")
                Log.v(TAGF, "${info} ${linea}")

            nl = nl + 1
        }
    }
}

/**

        // leer y procesar todas las líneas en un bucle
        while( nl < lineas.length )
        {
            const info_linea = `${info} línea ${nl+1}: `

                    // hacer cambio de estado 1 a 2 si procede
                    if ( estado == 1 && this.posiciones.length == nv )
                        estado = 2

            // obtener siguiente línea (se busca hasta línea no vacía o el final del archivo)
            linea = lineas[nl]
            tokens = lineas[nl].trim().split(/\s+/) // separar la línea en tokens
            while( tokens.length == 0 && nl < lineas.length-1 )
            {
                nl = nl+1
                linea = lineas[nl]
                tokens = linea.trim().split(/\s+/) // separar la línea en tokens
            }
            // salir si se ha llegado al final de las lineas sin encontrar ninguna no vacía
            if ( nl == lineas.length-1 )
                break

            // procesar línea
            if ( estado == 0 && tokens[0] != "comment" )  // si estamos en la cabecera
            {
                if ( tokens[0] == 'element' && tokens[1] == 'vertex' )
                {
                    nv = parseInt( tokens[2] )
                    assert( nv > 0 ) { "${info_linea} el número de vértices indicado en la cabecera es 0 o no es un entero" }
                }
                else if ( tokens[0] == 'element' && tokens[1] == 'face' )
                {
                    nt = parseInt( tokens[2] )
                    assert( nt > 0 ) { "${info_linea} el número de caras indicado en la cabecera es 0 o no es un entero" }
                }
                else if ( tokens[0] == 'end_header' ) // pasar al estado 1 al acabar la cabecera
                {
                    assert( nv > 0 ) { "${info_linea} no se encuentra el número de vértices en la cabecera" }
                    assert( nt > 0 ) { "${info_linea} no se encuentra el número de caras en la cabecera" }
                    estado = 1
                }
            }
            else if ( estado == 1 && tokens[0] != "comment" ) // leyendo vértices
            {
                assert( 3 <= tokens.length ) { "${info_linea} línea de vértice con menos de tres valores" }
                this.posiciones.push( new Vec3([ parseFloat(tokens[0]), parseFloat(tokens[1]), parseFloat(tokens[2]) ]))
            }
            else if ( estado == 2 && tokens[0] != "comment") // estado == 2 (leyendo caras)
            {
                assert( tokens.length == 4 ) { "${info_linea} se esperaban 4 números, leyendo caras \n(línea == [${linea}])`)
                assert( parseInt( tokens[0] ) == 3 ) { "${info_linea} encontrada cara con un número de vértices distinto de 3 o que no es un entero." }
                triangulos.push( new UVec3([ parseInt(tokens[1]), parseInt(tokens[2]), parseInt(tokens[3]) ]))
            }
            else if ( tokens[0] == "comment" )
                Log(`${info} ${linea}`)

            nl = nl+1

        }

        // ver si ha ido todo bien
        if ( nl == 0 ) throw Error(`${info} el archivo está vacío (no tiene líneas )`)
        if ( this.posiciones.length != nv ) throw Error(`${info} encontrados ${this.posiciones.length} vértices, pero en la cabecera se dice que debe de haber ${nv}`)
        if ( this.triangulos.length != nt ) throw Error(`${info} encontrados ${this.posiciones.length} triángulos, pero en la cabecera se dice que debe de haber ${nt})`)

        Log(`${info} encontrados ${nv} vértices y ${nt} triángulos.`)

        // calcular las normales
        this.calcularNormales()
    }
}

**/