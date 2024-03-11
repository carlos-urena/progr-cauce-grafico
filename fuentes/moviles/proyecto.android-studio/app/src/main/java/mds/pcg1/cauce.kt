// *********************************************************************
// **
// ** Asignatura: PCG (Programación del Cauce Gráfico).
// **
// ** Clases relacionadas con el Cauce
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

package mds.pcg1.cauce

import java.nio.*
import android.util.Log
import android.opengl.GLES20
import mds.pcg1.utilidades.*


// -------------------------------------------------------------------------------------------------
// Tipo enumerado para los enteros que son indices de atributos (del 0 al 3)

class ind_atributo
{
    companion object
    {
        val posicion      get() = 0
        val color         get() = 1
        val normal        get() = 2
        val coords_text   get() = 3
        val numero_atribs get() = 4
    }
}

// -------------------------------------------------------------------------------------------------

/** Clase que encapsula un objeto programa de OpenGL y los valores de sus parámetros uniform
 *
 *  Para información básica de referencia, ver lo relativo a compilar shaders en el primer enlace
 *  Se deberían guardar los códigos fuentes de los 'assets' del proyecto y leerlos con el _assets manager_, ver segundo enlace
 *
 *  @see developer.android.com/develop/ui/views/graphics/opengl/draw
 *  @see stackoverflow.com/questions/27574136/where-to-store-shader-code-in-android-app
 */
class CauceBase()
{

    // Cadena con el código de un vertex shader básico (preliminar?)

    private val fuente_vs_basico =
        "attribute vec4 vPosition; " +
                "void main() {" +
                "  gl_Position = vPosition;" +
                "}"

    // Cadena con el código de un fragment shader básico (preliminar?)

    private val fuente_fs_basico =
        "precision mediump float;" +
                "uniform vec4 vColor;" +
                "void main() {" +
                "  gl_FragColor = vColor;" +
                "}"

    // Objeto programa y objetos shader

    private var programa        : Int = 0 // identificador o nombre del objeto programa
    private var vertex_shader   : Int = 0 // ident. del objeto shader con el VS
    private var fragment_shader : Int = 0 // ident. del objeto shader con el FS

    // Numero de atributos e identificadores de cada atributo.

    public val numero_atributos = 4

    // ---------------------------------------------------------------------------------------------
    // inicialización
    // se inicializa la primera vez que se activa, dentro de 'activar()'

    // ---------------------------------------------------------------------------------------------

    /**
     * Activa este cauce, si el objeto programa no estaba creado, lo crea ahora.
     */
    public fun activar()
    {
        if ( programa == 0 )
            crearObjetoPrograma()
        assert( programa>0 ) { "intento de activar un CauceBase que no se ha inicializado (no debería pasar)"}
        GLES20.glUseProgram( programa )
    }

    // ---------------------------------------------------------------------------------------------
    /**
     * Compilar un shader y, si va bien, adjuntarlo al objeto programa.
     * Se muestra el log de errores o warnings.
     * Si hay errores se lanza una excepción.
     *
     * TODO: gestionar esto cuando se da un nombre de archivo en lugar de una cadena
     *
     *
     * @param [tipo_shader]  uno de: GLES20.GL_FRAGMENT_SHADER, GLES20.GL_VERTEX_SHADER,
     * @param [nombre_archivo] (string) nombre del archivo que contiene el texto fuente
     * @param [texto_fuente] texto fuente del shader.
     */
    private fun compilarAdjuntarShader( tipo_shader : Int, nombre_archivo : String, texto_fuente : String ) : Int
    {
        val nombref : String = "Función ${object {}.javaClass.enclosingMethod.name}:"
        Log.v( TAG, "Entra función [$nombref] ")

        // Comprobar precondiciones

        ComprErrorGL( "${nombref}: error de OpenGL al inicio")

        assert( programa > 0 )
            { "$nombref: el objeto programa no está creado" }

        assert( tipo_shader == GLES20.GL_VERTEX_SHADER || tipo_shader == GLES20.GL_FRAGMENT_SHADER )
            { "$nombref: El valor de 'tipo_shader' ($tipo_shader) es incorrecto" }

        // Crear y compilar el shader

        val shader = GLES20.glCreateShader( tipo_shader )
        assert( shader > 0 ) { "${nombref}: no se ha podido crear el objeto shader " }

        GLES20.glShaderSource( shader, texto_fuente )
        GLES20.glCompileShader( shader )

        // Mostrar errores o warnings

        val info = GLES20.glGetShaderInfoLog( shader )
        if ( info != "" )
        {   Log.v(TAG, "Resultado de compilar el shader:")
            Log.v(TAG, "---------------------- ")
            Log.v(TAG, info)
            Log.v(TAG, "---------------------- ")
        }
        // Si ha habido error, lanzar excepción de error

        var sin_errores = IntBuffer.allocate( 1 )
        GLES20.glGetShaderiv( shader, GLES20.GL_COMPILE_STATUS, sin_errores )
        if ( sin_errores[0] != GLES20.GL_TRUE )
        {
            val msg = "$nombref Errores al compilar un shader (ver log). Aborto."
            Log.v( TAG, msg )
            throw Error( msg )
        }

        // Adjuntar objeto shader al objeto programa

        GLES20.glAttachShader( programa, shader )
        ComprErrorGL( "${nombref} error OpenGL al final" )

        // Devolver el identificador del objeto programa creado

        Log.v( TAG, "${nombref} shader compilado ok.")
        return shader
    }
    // ---------------------------------------------------------------------------------------------

    /**
     *  Compila y enlaza el objeto programa
     * (deja nombre en 'id_prog', debe ser 0 antes)
     */
    private fun crearObjetoPrograma()
    {
        val TAGF : String = object {}.javaClass.enclosingMethod.name

        Log.v( TAGF, "inicio")

        // Comprobar precondiciones

        ComprErrorGL( "${TAGF} error de OpenGL al inicio")

        assert( programa == 0 )
            { "$TAGF: el objeto programa ya está creado" }

        assert( fuente_vs_basico != "" ) { "No hay fuente del VS ¿?"}
        assert( fuente_fs_basico != "" ) { "No hay fuente del FS ¿?"}

        //const nombre_archivo_vs = "/glsl/cauce_3_00_vertex_shader.glsl"
        //const nombre_archivo_fs = "/glsl/cauce_3_00_fragment_shader.glsl"

        // Crear el objeto programa

        programa = GLES20.glCreateProgram() ;
        assert( programa > 0 ) {"$TAGF no se ha podido crear el objeto programa" }


        // Adjuntarle los shaders al objeto programa
        vertex_shader   = compilarAdjuntarShader( GLES20.GL_VERTEX_SHADER,   "", fuente_vs_basico )
        fragment_shader = compilarAdjuntarShader( GLES20.GL_FRAGMENT_SHADER, "", fuente_fs_basico )

        Log.v( TAGF, "shader compilados y adjuntados")

        // Asociar los índices de atributos con las correspondientes variables de entrada ("in")
        // del vertex shader (hay que hacerlo antes de enlazar)
        // (esto es necesario para asegurarnos que conocemos el índice de cada atributo específico

        ComprErrorGL( "$TAGF antes de bind de atributos " )

        assert( numero_atributos >= 4 ) { "El cauce no gestiona al menos 4 atributos" }

        GLES20.glBindAttribLocation( programa, ind_atributo.posicion,    "in_posicion_occ" )
        GLES20.glBindAttribLocation( programa, ind_atributo.color,       "in_color" )
        GLES20.glBindAttribLocation( programa, ind_atributo.normal,      "in_normal_occ" )
        GLES20.glBindAttribLocation( programa, ind_atributo.coords_text, "in_coords_textura" )

        ComprErrorGL( "$TAGF después de bind de atributos" )


        // Enlazar programa y ver errores

        GLES20.glLinkProgram( programa )

        // Mostrar errores o warnings

        val info = GLES20.glGetProgramInfoLog( programa )
        if ( info != "")
        {   Log.v( TAGF, "$TAGF resultado de enlazar el programa:")
            Log.v( TAGF, "---------------------- ")
            Log.v( TAGF, info )
            Log.v( TAGF, "---------------------- ")
        }

        // Si ha habido errores al enlazar, o el objeto programa es inválido, abortar

        var sin_errores = IntBuffer.allocate( 1 )
        GLES20.glGetProgramiv( programa, GLES20.GL_LINK_STATUS, sin_errores )
        if ( sin_errores[0] != GLES20.GL_TRUE )
        {
            val msg = "$TAGF errores al enlazar un programa (ver log). Aborto."
            Log.v( TAGF, msg )
            throw Error( msg )
        }
        if ( ! GLES20.glIsProgram( programa ) )
        {
            val msg = "$TAGF el objeto programa creado no es válido. Aborto."
            Log.v( TAGF, msg )
            throw Error( msg )
        }

        // ya está

        ComprErrorGL( "$TAGF error OpenGL al final" )
        Log.v( TAGF, "$TAGF programa compilado y enlazado ok." )
    }


    // ---------------------------------------------------------------------------------------------
}