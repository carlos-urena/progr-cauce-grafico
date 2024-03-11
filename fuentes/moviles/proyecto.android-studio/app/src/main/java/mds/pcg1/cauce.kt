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
import mds.pcg1.vec_mat.*


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
    """ 
        // uniforms: matrices 
        
        uniform mat4  u_mat_modelado ;    // matriz de modelado actual
        uniform mat4  u_mat_modelado_nor; // matriz de modelado para normales (traspuesta inversa de la anterior)
        uniform mat4  u_mat_vista ;       // matriz de vista (mundo --> camara)
        uniform mat4  u_mat_proyeccion ;  // matriz de proyeccion
        
        // uniforms: parámetros relativos a texturas
        
        uniform bool  u_eval_text ;       // false --> no evaluar texturas, true -> evaluar textura en FS, sustituye a (v_color)
        uniform int   u_tipo_gct ;        // tipo gen.cc.tt. (0=desact, 1=objeto, 2=camara)
        uniform vec4  u_coefs_s ;         // coefficientes para G.CC.TT. (coordenada 's')
        uniform vec4  u_coefs_t ;         // coefficientes para G.CC.TT. (coordenada 't')
        
        // atributos de vértice 
        
        attribute vec3 in_posicion_occ;   // 0 -> posicion (en coordenadas de objeto)
        attribute vec3 in_color;          // 1 -> color
        attribute vec3 in_normal_occ;     // 2 -> normal (en coordenadas de objeto)
        attribute vec2 in_coords_textura; // 3 -> coordenadas de textura
        
        // variables de salida hacia el fragment shader 
        
        varying vec4 v_posic_ecc ;   // posicion del punto (en coords de camara)
        varying vec4 v_color ;       // color del vértice (interpolado a los pixels)
        varying vec3 v_normal_ecc;   // normal  (en coords. de cámara)
        varying vec2 v_coord_text;   // coordenadas de textura
        varying vec3 v_vec_obs_ecc ; // vector hacia el observador (en coords de cámara)
       
        // ------------------------------------------------------------------------------
        // calculo de los parámetros de salida (io_... y gl_Position)
        
        vec2 CoordsTextura() // calcula las coordenadas de textura
        {
           if ( ! u_eval_text )            // si no se están evaluando las coordenadas de textura
              return vec2( 0.0, 0.0 );     //     devuelve las coordenadas (0.0,0.0)
           if ( u_tipo_gct == 0 )          // texturas activadas, generacion desactivada
              return in_coords_textura.st; //     devuelve las coords. de glTexCoords o tabla
        
           vec4 pos_ver ;
           if ( u_tipo_gct == 1 )                       // generacion en coordenadas de objeto
              pos_ver = vec4( in_posicion_occ, 1.0  ) ; //    usar las coords originales (objeto)
           else                                         // generacion en coords de cámara
              pos_ver = v_posic_ecc ;                   //    usar las coordenadas de cámara
        
           return vec2( dot(pos_ver,u_coefs_s), dot(pos_ver,u_coefs_t) );
        }
        
        // ------------------------------------------------------------------------------
        // devuelve el vector al obervador, a partir de pos. ECC, y en función de 
        // si la matriz de proyec. es ortogonal (a==0), o es perspectiva (a==-1)
        
        vec3 VectorObservadorVS( vec4 pos_ecc )
        {
           float pm23 = u_mat_proyeccion[2][3],   // -1 si es perspectiva, 0 si es ortográfica.
                 pm33 = u_mat_proyeccion[3][3];   // 0 si es perspectiva,  1 si es ortográfica.
        
           // código "largo" (para explicar lo que se hace)
           //
           // if ( pm23 == 0 &&  pm33 == 1.0 )    // si proyección es ortográfica
           //    return vec3( 0.0, 0.0, 1.0 );  //    devolver +Z
           // else                              // si es perspectiva
           //    return (-pos_ecc).xyz ;        //    devolver vector hacia el foco de la cámara (origen en ECC)
        
           // código "corto" (eficiente), equivalente
           return pm23*(pos_ecc.xyz) + vec3(0.0, 0.0, pm33 );
        }
        
        void main() 
        {
            vec4 posic_wcc  = u_mat_modelado * vec4( in_posicion_occ, 1.0 ) ; // posición del vértice en coords. de mundo
            vec3 normal_wcc = (u_mat_modelado_nor * vec4(in_normal_occ,0)).xyz ;

            // calcular las variables de salida
            
            v_posic_ecc    = u_mat_vista*posic_wcc ;
            v_normal_ecc   = (u_mat_vista*vec4(normal_wcc,0.0)).xyz ;
            v_vec_obs_ecc  = VectorObservadorVS( v_posic_ecc );  // ver la función arriba
            v_color        = vec4( in_color, 1 ) ;  // color fijado con in_color .....
            v_coord_text   = CoordsTextura();
   
            vec4 pos       = u_mat_proyeccion * v_posic_ecc ; 
            gl_Position    = pos ;
        }
    """

    // Cadena con el código de un fragment shader básico (preliminar?)

    private val fuente_fs_basico =
    """
        precision mediump float ;
        
        // uniforms: parámetros relativos evaluación de iluminación
        
        uniform bool  u_eval_mil ;        // evaluar el MIL sí (true) o no (false) --> si es que no, usar color plano actual
        uniform float u_mil_ka ;          // color de la componente ambiental del MIL (Ka)
        uniform float u_mil_kd ;          // color de la componente difusa del MIL (Kd)
        uniform float u_mil_ks ;          // color de la componente pseudo-especular del MIL (Ks)
        uniform float u_mil_exp ;         // exponente de la componente pseudo-especular del MIL (e)
        
        // variables de entrada desde el shader 
        
        varying vec4 v_posic_ecc ;   // posicion del punto (en coords de camara)
        varying vec4 v_color ;       // color del vértice (interpolado a los pixels)
        varying vec3 v_normal_ecc;   // normal  (en coords. de cámara)
        varying vec2 v_coord_text;   // coordenadas de textura
        varying vec3 v_vec_obs_ecc ; // vector hacia el observador (en coords de cámara)
        
        void main() 
        {
          gl_FragColor = v_color;
        }
    """

    // ---------------------------------------------------------------------------------------------

    private var mat_modelado     : Mat4 = Mat4.ident() // matriz de modelado
    private var mat_modelado_nor : Mat4 = Mat4.ident() // matriz de modelado para normales
    private var mat_vista        : Mat4 = Mat4.ident() // matriz de vista
    private var mat_proyeccion   : Mat4 = Mat4.ident() // matriz de proyección

    private var eval_mil          : Boolean = false // true -> evaluar MIL, false -> usar color plano
    private var usar_normales_tri : Boolean = false // true -> usar normal del triángulo, false -> usar interp. de normales de vértices
    private var eval_text         : Boolean = false // true -> eval textura, false -> usar glColor o glColorPointer

    private var tipo_gct : Int  = 0 ;     // tipo de generación de coordenadas de textura (0->desact, 1->c.obj, 2->c.mundo)
    private var color    : Vec3 = Vec3( 0.0f, 0.0f, 0.0f ) // color actual para visualización sin tabla de colores

    private var coefs_s  : Vec4 = Vec4( 1.0f,0.0f,0.0f,0.0f )  // coefs. GACT (s)
    private var coefs_t  : Vec4 = Vec4( 0.0f,1.0f,0.0f,0.0f )  // coefs. GACT (t)

    // pilas de colores y matrices guardadas (son MutableList para que permitan push/pop)

    private var pila_colores          : MutableList<Vec3> = mutableListOf()
    private var pila_mat_modelado     : MutableList<Mat4> = mutableListOf()
    private var pila_mat_modelado_nor : MutableList<Mat4> = mutableListOf()

    // locations de los uniforms

    private var loc_mat_modelado      : Int = -1
    private var loc_mat_modelado_nor  : Int = -1
    private var loc_mat_vista         : Int = -1
    private var loc_mat_proyeccion    : Int = -1
    private var loc_eval_mil          : Int = -1
    private var loc_usar_normales_tri : Int = -1
    private var loc_eval_text         : Int = -1
    private var loc_tipo_gct          : Int = -1
    private var loc_coefs_s           : Int = -1
    private var loc_coefs_t           : Int = -1
    private var loc_mil_ka            : Int = -1
    private var loc_mil_kd            : Int = -1
    private var loc_mil_ks            : Int = -1
    private var loc_mil_exp           : Int = -1
    private var loc_num_luces         : Int = -1
    private var loc_pos_dir_luz_ec    : Int = -1
    private var loc_color_luz         : Int = -1

    // Objeto programa y objetos shader

    private var programa        : Int = 0 // identificador o nombre del objeto programa
    private var vertex_shader   : Int = 0 // ident. del objeto shader con el VS
    private var fragment_shader : Int = 0 // ident. del objeto shader con el FS

    // Numero de atributos e identificadores de cada atributo.

    public val numero_atributos = 4

    // ---------------------------------------------------------------------------------------------

    /**
     * Activa este cauce, si el objeto programa no estaba creado, lo crea e inicializa aquí.
     * Es importante no usar funciones OpenGL fuera del MGE de revisualizar frame, por eso
     * el objeto programa debe inicializarse la primera vez que se activa (cuando se está
     * ejecutando mgeVisualizarFrame)
     */
    public fun activar()
    {
        val TAGF : String = "[CauceBase.activar]"

        if ( programa == 0 )
            inicializar()
        assert( programa>0 ) { "$TAGF intento de activar un CauceBase que no se ha inicializado (no debería pasar)"}
        GLES20.glUseProgram( programa )
    }
    // ---------------------------------------------------------------------------------------------

    private fun inicializar()
    {
        val TAGF : String = "[CauceBase.inicializar]"

        crearObjetoPrograma()
        inicializarUniforms()
        imprimeInfoUniforms()

        Log.v( TAGF, "${TAGF} shaders compilados y objeto 'CauceBase' creado con éxito.")
    }
    // ---------------------------------------------------------------------------

    private fun b2n( b : Boolean ) : Int  { return if (b) 1 else 0 }
    // ---------------------------------------------------------------------------

    /**
     *  Lee las 'locations' de todos los uniforms y les da un
     *  valor inicial por defecto. También inicializa algunas variables de estado.
     */
    private fun inicializarUniforms()
    {
        val TAGF : String = "Cauce.leerLocation"

        assert( programa > 0 ) { "$TAGF imposible inicializar uniforms: el objeto programa no se ha creado"}

        GLES20.glUseProgram( programa )

        // obtener las 'locations' de los parámetros uniform

        loc_mat_modelado      = leerLocation( "u_mat_modelado" )
        loc_mat_modelado_nor  = leerLocation( "u_mat_modelado_nor" )
        loc_mat_vista         = leerLocation( "u_mat_vista" )
        loc_eval_mil          = leerLocation( "u_eval_mil" )
        loc_mat_proyeccion    = leerLocation( "u_mat_proyeccion" )
        loc_eval_mil          = leerLocation( "u_eval_mil" )
        loc_usar_normales_tri = leerLocation( "u_usar_normales_tri" )
        loc_eval_text         = leerLocation( "u_eval_text" )
        loc_tipo_gct          = leerLocation( "u_tipo_gct" )
        loc_coefs_s           = leerLocation( "u_coefs_s" )
        loc_coefs_t           = leerLocation( "u_coefs_t" )
        loc_mil_ka            = leerLocation( "u_mil_ka" )
        loc_mil_kd            = leerLocation( "u_mil_kd" )
        loc_mil_ks            = leerLocation( "u_mil_ks" )
        loc_mil_exp           = leerLocation( "u_mil_exp" )
        loc_num_luces         = leerLocation( "u_num_luces" )
        loc_pos_dir_luz_ec    = leerLocation( "u_pos_dir_luz_ec" )
        loc_color_luz         = leerLocation( "u_color_luz" )

        // dar valores iniciales por defecto a los parámetros uniform
        GLES20.glUniformMatrix4fv( loc_mat_modelado,     1, false, mat_modelado.fb )
        GLES20.glUniformMatrix4fv( loc_mat_modelado_nor, 1, false, mat_modelado_nor.fb )
        GLES20.glUniformMatrix4fv( loc_mat_vista,        1, false, mat_vista.fb )
        GLES20.glUniformMatrix4fv( loc_mat_proyeccion,   1, false, mat_proyeccion.fb )

        GLES20.glUniform1i( loc_eval_mil,          if (eval_mil) 1 else 0 )
        GLES20.glUniform1i( loc_usar_normales_tri, if (usar_normales_tri) 1 else 0 )
        GLES20.glUniform1i( loc_eval_text,         if (eval_text) 1 else 0 )

        GLES20.glUniform1i( loc_tipo_gct, tipo_gct )

        GLES20.glUniform4fv( loc_coefs_s, 1, coefs_s.fb )
        GLES20.glUniform4fv( loc_coefs_t, 1, coefs_t.fb )

        GLES20.glUniform1f( loc_mil_ka,  0.2f )
        GLES20.glUniform1f( loc_mil_kd,  0.8f )
        GLES20.glUniform1f( loc_mil_ks,  0.0f )
        GLES20.glUniform1f( loc_mil_exp, 0.0f )

        GLES20.glUniform1i( loc_num_luces, 0 ) // por defecto: 0 fuentes de luz activas

        // desactivar objeto programa
        //gl.useProgram( null );
    }

    // ---------------------------------------------------------------------------------------------

    /**
     * obtiene el localizador único de un parámetro uniform dado su [nombre]
     * @return localizador (Int), o -1 si no se encuentra ese nombre como uniform o bien sí está pero no se usa en la salida
     */
    private fun leerLocation( nombre : String ) : Int
    {
        val TAGF : String = "[Cauce.leerLocation]"

        val loc =  GLES20.glGetUniformLocation( programa, nombre )
        if ( loc == -1 )
            Log.v( TAGF, "advertencia: el uniform de nombre '$nombre' no se encuentra en el fuente o no se usa en la salida")

        return loc
    }
    // ---------------------------------------------------------------------------

    private fun imprimeInfoUniforms()
    {

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
        val TAGF : String = "[${object {}.javaClass.enclosingMethod.name}]"

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