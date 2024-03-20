// *********************************************************************
// **
// ** Asignatura: PCG (Programación del Cauce Gráfico).
// **
// ** Clases para vectores de 2, 3 y 4 floats, y matrices.
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

package mds.pcg1.vec_mat


import android.util.Log
import mds.pcg1.utilidades.*
import java.nio.FloatBuffer
import kotlin.math.*

// -------------------------------------------------------------------------------------------------

/**
 * Clase con tests para vectores y matrices
 */
class VecMatTest
{
    public fun run()
    {
        val v2a = Vec2(1.0f, 2.0f)
        val v2b = Vec2(3.0f, 4.0f)
        val v2c : Vec2 = 2.0f*v2a + v2b*7.0f

        val d : Float = v2a.dot( v2b )

        val v3a = Vec3(3.0f, 4.0f, 5.0f )
        val v3b = Vec3(3.0f, 4.0f, 6.0f )
        val v3c : Vec3 = 2.0f*v3a + v3b*7.0f

        val v4a = Vec4(3.0f, 4.0f, 5.0f, 6.0f )
        val v4b = Vec4(3.0f, 4.0f, 6.0f, 7.0f )
        val v4c : Vec4 = 2.0f*v4a - v4b*7.0f

        Log.v( TAG, "d   == ${d}")
        Log.v( TAG, "v2c == ${v2c}")
        Log.v( TAG, "v3c == ${v3c}")
        Log.v( TAG, "v4c == ${v4c}")

        val m1 : Mat4 = Mat4.escalado( Vec3( 2.0f, 1.0f/2.0f, 3.0f ))
        val m2 : Mat4 = Mat4.escalado( Vec3( 1.0f/2.0f, 2.0f, 1.0f/3.0f ))
        val mid = Mat4.ident()
        val mce = Mat4.ceros()
        val m3  = m1*m2

        Log.v( TAG, "cosas del test: $mid $mce $m3")

    }
}

// -------------------------------------------------------------------------------------------------

/**
 * Clase para vectores de 2 Float
 */
class Vec2 : VecGen<Longitud2>
{
    constructor( v0 : Float, v1 : Float ) : super( floatArrayOf( v0, v1 ))
    //constructor( v : VecGen<Longitud2> ) : super( v.array )

    // Añadir métodos de consulta de las componentes de los vectores

    val x get() = this[0]
    val y get() = this[1]

    val s get() = this[0]
    val t get() = this[1]

    operator fun plus ( v : Vec2 )  : Vec2 { return Vec2( this[0]+v[0], this[1]+v[1] )  }
    operator fun minus( v : Vec2 )  : Vec2 { return Vec2( this[0]-v[0], this[1]-v[1] )  }
    operator fun times( a : Float ) : Vec2 { return Vec2( this[0]*a, this[1]*a  ) }
    operator fun div  ( a : Float ) : Vec2 { return Vec2( this[0]*a, this[1]*a  ) }
    //fun dot( v : Vec2 ) : Vec2 { return this.op_dot( v ) }
}

operator fun Float.times( v : Vec2 ) : Vec2 { return v*this }

// -------------------------------------------------------------------------------------------------

/**
 * Clase para vectores de 3 Float
 */
class Vec3 : VecGen<Longitud3>
{
    constructor( v0 : Float, v1 : Float, v2 : Float )  : super( floatArrayOf( v0, v1, v2 ))
    //constructor( v : VecGen<Longitud3> ) : super( v.array )

    val x get() = this[0]
    val y get() = this[1]
    val z get() = this[2]

    val r get() = this[0]
    val g get() = this[1]
    val b get() = this[2]

    operator fun plus ( v : Vec3 )  : Vec3 { return Vec3( this[0]+v[0], this[1]+v[1], this[2]+v[2] )   }
    operator fun minus( v : Vec3 )  : Vec3 { return Vec3( this[0]-v[0], this[1]-v[1], this[2]-v[2] )  }
    operator fun times( a : Float ) : Vec3 { return Vec3( this[0]*a, this[1]*a, this[2]*a )  }
    operator fun div  ( a : Float ) : Vec3 { return Vec3( this[0]/a, this[1]/a, this[2]/a )  }

    /**
     * Calcula el producto vectorial (cross product) entre este vector y otro
     * @param that (Vec3) el otro vector
     * @returns (Vec3) producto vectorial
     */
    public fun cross( that : Vec3 ) : Vec3
    {
        return Vec3(
            this.y*that.z - this.z*that.y,  // x = y1*z2 - z1*y2
            this.z*that.x - this.x*that.z,  // y = z1*x2 - x1*z2
            this.x*that.y - this.y*that.x   // z = x1*y2 - x2*y1
        )
    }

}

operator fun Float.times( v : Vec3 ) : Vec3 { return v*this }

// -------------------------------------------------------------------------------------------------

/**
 * Clase para vectores de 4 Float
 */
class Vec4 : VecGen<Longitud4>
{
    constructor( v0 : Float, v1 : Float, v2 : Float, v3 : Float  ) : super( floatArrayOf( v0, v1, v2, v3 )) { }
    //constructor( v : VecGen<Longitud4> ) : super( v.array )

    val x get() = this[0]
    val y get() = this[1]
    val z get() = this[2]
    val w get() = this[3]

    val r get() = this[0]
    val g get() = this[1]
    val b get() = this[2]
    val a get() = this[3]

    operator fun plus ( v : Vec4 )  : Vec4 { return Vec4( this[0]+v[0], this[1]+v[1], this[2]+v[2], this[3]+v[3] )}
    operator fun minus( v : Vec4 )  : Vec4 { return Vec4( this[0]-v[0], this[1]-v[1], this[2]-v[2], this[3]-v[3] )}
    operator fun times( a : Float ) : Vec4 { return Vec4( this[0]*a, this[1]*a, this[2]*a, this[3]*a )}
    operator fun div  ( a : Float ) : Vec4 { return Vec4( this[0]/a, this[1]/a, this[2]/a, this[3]/a )}
}

operator fun Float.times( v : Vec4 ) : Vec4 { return v*this }

// ---------------------------------------------------------------------------------------

/**
 * Vectores de 3 números enteros sin signo de 4 bytes cada uno (UInt)
 */
@OptIn(ExperimentalUnsignedTypes::class)
class UVec3( u0 : UInt, u1 : UInt, u2 : UInt )
{
    private var valores : UIntArray = uintArrayOf( u0, u1, u2 )

    operator fun get( i : Int ) : UInt
    {
        compruebaIndice( i )
        return valores[ i ]
    }
    operator fun set( i : Int, nuevo_valor : UInt )
    {
        compruebaIndice( i )
        valores[ i ] = nuevo_valor
    }

    private fun compruebaIndice( i : Int )
    {
        assert( i in 0..<3 ) { "Acceso fuera de rango a un UVec3 (i==$i, debe ser 0, 1 o 2)" }
    }
}

// ---------------------------------------------------------------------------------------

/**
 * Clase base para clases que contienen un entero con una longitud
 */
open class Longitud( pn : Int )
{
    public val n = pn
}

/** Clases concretas con longitudes concretas
 *
 */
class Longitud2() : Longitud( 2 ) {}
class Longitud3() : Longitud( 3 ) {}
class Longitud4() : Longitud( 4 ) {}




// -------------------------------------------------------------------------------------------------

/**
 * Clase base (plantilla genérica) para vectores de flotantes de longitud L, donde L es una clase
 * que implementa el interfaz Longitud.
 * Para valuir una instancia, usar: VectorFlotantes( Longitud2() ), por ejemplo
 * Se instancia usando una ongitud L concreta (una clase que implementa el interfaz 'Longitud')
 */
open class VecGen<L> where L : Longitud
{
    /**
     * Array de L.n flotantes
     */
    private var valores : FloatArray

    /**
     * Devuelve el array de valores del vector
     */
    public val array get() = valores

    /**
     * devuelve un 'FloatBuffer' que es un alias del array de valores
     */
    public val fb : FloatBuffer get() = FloatBuffer.wrap( valores )  // https://developer.android.com/reference/kotlin/java/nio/FloatBuffer#wrap(kotlin.FloatArray,%20kotlin.Int,%20kotlin.Int)


    /**
     * Devuelve la longitud de este vector (Int) (la longitud del array
     */
    public val long : Int get() =  valores.size

    // constructores: son 'internal' ya que permiten valuir un VecGen<Longitud3>, p.ej., usando 2 floats.

    /**
     * construye un vector dando su longitud (los valores se ponen a 0)
     */
    internal constructor( n : Int )
    {
        valores = FloatArray( n, {0.0f} )
    }

    /**
     * construye un vector dando su longitud y un array de flotantes (que debe ser de dicha longitud)
     */
    internal constructor( arr : FloatArray )
    {
        valores = arr
    }

    /**
     * Comprueba si un índice está en el rango de índices del array
     * @param i - indice
     */
    private fun compruebaIndice( i : Int )
    {
        assert( 0 <= i && i < long )
        {   "Acceso de lectura fuera de rango a un 'ArrayFlotantes', el índice es $i pero el tamaño es $valores.size"
        }
    }

    /**
     * Comprueba si un índice (sin signo) está en el rango de índices del array
     * @param i - indice (sin signo)
     */
    private fun compruebaIndice( i : UInt )
    {
        assert( i.toInt() < long )
        {   "Acceso de lectura fuera de rango a un 'ArrayFlotantes', el índice es $i pero el tamaño es $valores.size"
        }
    }

    /**
     * Devuelve el valor de la entrada, si el índice está fuera de rango, produce un error
     * @param i - índice
     */
    operator fun get( i : Int ) : Float
    {
        compruebaIndice( i )
        return valores[ i ]
    }
    operator fun get( iu : UInt ) : Float
    {
        compruebaIndice( iu )
        return valores[ iu.toInt() ]
    }

    /**
     * Escribe en una entrda,  si el índice está fuera de rango, produce un error
     * @param i - índice
     */
    operator fun set( i : Int, a : Float )
    {
        compruebaIndice( i )
        valores[i] = a
    }
    operator fun set( iu : UInt, a : Float )
    {
        compruebaIndice( iu )
        valores[iu.toInt()] = a
    }
    override fun toString() : String {
        var s : String = "("
        for( i in 0..<long )
            s = s + valores[i] + if (i<long-1) "," else ""
        return s + ")"
    }

    fun dot( v : VecGen<L> ) : Float
    {
        var d = 0.0f
        for( i in 0..<long )
            d += this[i]*v[i]
        return d
    }

    val longitud : Float get()
    {
        var sqs = 0.0f
        for( i in 0..<valores.size )
            sqs += valores[i]*valores[i]
        return sqrt( sqs )
    }

}
// -------------------------------------------------------------------------------------------------

/**
 * Clase para matrices de 4x4 flotantes
 *
 */

class Mat4
{
    /**
     * Array de 16 flotantes, son los valores reales de la matriz
     * (dispuestos POR FILAS)
     */
    private var valores : FloatArray

    /**
     * devuelve un 'FloatBuffer' que es un alias del array de valores
     */
    public val fb : FloatBuffer get() = FloatBuffer.wrap( valores )  // https://developer.android.com/reference/kotlin/java/nio/FloatBuffer#wrap(kotlin.FloatArray,%20kotlin.Int,%20kotlin.Int)

    // ---------------------------------------------------------------------------------------------

    constructor( arr : FloatArray )
    {
        assert( arr.size == 16 ) { "Intento de valruir una matriz (Mat4) con un array de longitud distinta de 16" }
        valores = arr
    }
    // ---------------------------------------------------------------------------------------------
    /**
     * Lee un valor en una fila y una columna (ambas entre 0 y 3)
     * @param f índice de fila
     * @param c índice de columna
     * @returns  valor en la celda
     */
    operator fun get( f : Int, c : Int ) : Float
    {
        return valores[ ind(f,c) ]
    }
    // ---------------------------------------------------------------------------------------------

    /**
     * Cambia el valor en una fila y una columna (ambas entre 0 y 3)
     * @param f índice de fila
     * @param c índice de columna
     */
    operator fun set( f : Int, c : Int, a : Float )
    {
        valores[ ind(f,c) ] = a
    }
    // ---------------------------------------------------------------------------------------------

    /**
     * Operador de multiplicación (composición) de esta matriz y otra por la derecha
     *
     * @param   m (Mat4)
     * @returns (Mat4) matriz resultado
     */
    operator fun times( m : Mat4 ) : Mat4
    {
        var res = Mat4.ceros()

        for( fila in 0..3  )
            for( colu in 0..3 )
                for( i in 0..3 )
                    res[fila,colu] = res[fila,colu] + this[fila,i] * m[i,colu]

        return res
    }
    // ---------------------------------------------------------------------------------------------

    /**
     * Aplicar esta matriz a un vector de 4 entradas y devolver el vector 4 resultado
     *
     * @param   v (Vec4) vector original
     * @returns (Vec4) vector resultado
     */
    operator fun times( v : Vec4 ) : Vec4
    {
        var res = Vec4( 0.0f, 0.0f, 0.0f, 0.0f )

        for( fila in 0..3  )
            for( colu in 0..3 )
                res[fila] = res[fila] + this[fila,colu] * v[colu]

        return res
    }



    // -----------------------------------------------------------------------------
    // Métodos estáticos de la clase (miembros del objeto singleton 'companion')
    // Principalmente son constructores de matrices

    companion object
    {
        /**
         * Para una matriz 4x4 dispuesta POR FILAS, devuelve el índice del elemento en una
         * fila y columna determinadas.
         *
         * @param f índice de fila
         * @param c índice de columna
         * @returns  índice de la celda
         */
        fun ind( f : Int, c : Int ) : Int
        {
            assert( 0 <= f && f < 4 ) { "índice de fila (${f}) fuera de rango (accediendo a Mat4)" }
            assert( 0 <= c && c < 4 ) { "índice de columna (${c}) fuera de rango (accediento a Mat4)" }
            return 4*f + c
        }

        /**
         * Produce una matriz con todas las entradas puesta a cero
         * @return (Mat4) matriz con ceros
         */
        fun ceros() : Mat4
        {
            return Mat4( floatArrayOf(
                0.0f, 0.0f, 0.0f, 0.0f,
                0.0f, 0.0f, 0.0f, 0.0f,
                0.0f, 0.0f, 0.0f, 0.0f,
                0.0f, 0.0f, 0.0f, 0.0f
            ))
        }

        /**
         * Produce la matriz identidad
         * @return (Mat4) matriz identidad 4x4
         */
        fun ident() : Mat4
        {
            return Mat4( floatArrayOf(
                1.0f, 0.0f, 0.0f, 0.0f,
                0.0f, 1.0f, 0.0f, 0.0f,
                0.0f, 0.0f, 1.0f, 0.0f,
                0.0f, 0.0f, 0.0f, 1.0f
            ))
        }

        /**
         * construye una matriz 4x4 de traslación a partir de un vector de traslación
         *
         * @param v (Vec3) vector de traslación
         * @return (Mat4) matriz de traslación
         */
        fun traslacion( v : Vec3 ) : Mat4
        {
            return Mat4( floatArrayOf(
                1.0f, 0.0f, 0.0f, v.x,
                0.0f, 1.0f, 0.0f, v.y,
                0.0f, 0.0f, 1.0f, v.z,
                0.0f, 0.0f, 0.0f, 1.0f
            ))
        }

        /**
         * construye una matriz 4x4 de escalado a partir de un vector con los factores de escala
         *
         * @param v (Vec3) vector con los factores de escala en X, Y y Z
         * @return (Mat4) matriz de escalado
         */
        fun escalado( v : Vec3 ) : Mat4
        {
            return Mat4( floatArrayOf(
                v.x,  0.0f,  0.0f,   0.0f,
                0.0f, v.y,   0.0f,   0.0f,
                0.0f, 0.0f,  v.z,    0.0f,
                0.0f, 0.0f,  0.0f,   1.0f
            ))
        }

        /**
         * construye una matriz de rotación entorno al eje X, dado un ángulo (en grados)
         * @param angulo_grad (Float) ángulo en grados
         * @return (Mat4) matriz de rotación entorno a X
         */
        fun rotacionXgrad( angulo_grad : Float ) : Mat4
        {
            val ar : Float = (angulo_grad*PI.toFloat())/180.0f
            val c  : Float = cos( ar )
            val s  : Float = sin( ar )

            return Mat4( floatArrayOf(
                1.0f,  0.0f,  0.0f,  0.0f ,
                0.0f,  c,     -s,    0.0f ,
                0.0f,  s,     c,     0.0f ,
                0.0f,  0.0f,  0.0f,  1.0f
            ))
        }

        /**
         * construye una matriz de rotación entorno al eje Y, dado un ángulo (en grados)
         * @param angulo_grad (Float) ángulo en grados
         * @return (Mat4) matriz de rotación entorno a Y
         */
        fun rotacionYgrad( angulo_grad : Float ) : Mat4
        {
            val ar : Float = (angulo_grad*PI.toFloat())/180.0f
            val c  : Float = cos( ar )
            val s  : Float = sin( ar )

            return Mat4( floatArrayOf(
                c,    0.0f, s,    0.0f,
                0.0f, 1.0f, 0.0f, 0.0f,
                -s,   0.0f, c,    0.0f,
                0.0f, 0.0f, 0.0f, 1.0f
            ))
        }

        /**
         * construye una matriz de rotación entorno al eje Z, dado un ángulo (en grados)
         * @param angulo_grad (Float) ángulo en grados
         * @return (Mat4) matriz de rotación entorno a Y
         */
        fun rotacionZgrad( angulo_grad : Float ) : Mat4
        {
            val ar : Float = (angulo_grad*PI.toFloat())/180.0f
            val c  : Float = cos( ar )
            val s  : Float = sin( ar )

            return Mat4( floatArrayOf(
                c,    -s,   0.0f, 0.0f ,
                s,    c,    0.0f, 0.0f ,
                0.0f, 1.0f, c,    0.0f ,
                0.0f, 0.0f, 0.0f, 1.0f
            ))
        }

        // ------------------------------------------------------------------------------------------------
        /**
         * construye una matriz de proyección perspectiva a partir de los límites del view-frustum
         * (según: http://docs.gl/gl2/glFrustum)
         *
         * @param l (Float) limite izquierdo del frustum en X (en el plano z=near)
         * @param r (Float) limite derecho del frustum en X (en el plano z=near)
         * @param b (Float) limite inferior del frustum en Y (en el plano z=near)
         * @param t (Float) limite superior del frustum en Y (en el plano z=near)
         * @param n (Float) distancia en Z entre observador y plano de recorte delantero (>0)
         * @param f (Float) distancia en Z entre observador y plano de recorte trasero (>n)
         * @returns {Mat4}   (Float) matriz de proyeccion perspectiva
         */
        fun frustum( l : Float, r : Float, b : Float, t : Float, n : Float, f : Float ) : Mat4
        {
            val eps = 1e-6
            assert( eps < n && eps < f )  { "'n' o 'f' son casi cero o negativos" }
            assert( n < f ) { "'n' no es menor que 'f'" }
            assert( Math.abs(r-l) > eps && Math.abs(t-b) > eps  && Math.abs(n-f) > eps ) { "parámetros incorrectos (el tamaño en X, Y o Z es casi cero)" }

            val a00 : Float = (2.0f*n)/(r-l) ; val a02 : Float = (r+l)/(r-l)
            val a11 : Float = (2.0f*n)/(t-b) ; val a12 : Float = (t+b)/(t-b)
            val a22 : Float = -(n+f)/(f-n)   ; val a23 : Float = -(2.0f*f*n)/(f-n)

            return Mat4( floatArrayOf(
                a00,   0.0f,   a02,   0.0f,
                0.0f,   a11,   a12,   0.0f,
                0.0f,   0.0f,  a22,   a23,
                0.0f,   0.0f,  -1.0f, 0.0f
            ))
        }

        // ------------------------------------------------------------------------------------------------
        /**
         * construye una matriz de proyección perspectiva a partir de un conjunto alternativo de parámetros
         * (según: https://registry.khronos.org/OpenGL-Refpages/gl2.1/xhtml/gluPerspective.xml)
         *
         * @param fovy_grad (Float) amplitud de campo en vertical (un ángulo en grados, entre 0 y 180.0f, sin incluir)
         * @param aspect_xy (Float) relación de aspecto (ancho del viewport dividido por el alto)
         * @param near      (Float) distancia en Z entre observador y plano de recorte delantero (>0)
         * @param far       (Float) distancia en Z entre observador y plano de recorte trasero (>n)
         * @returns
         */
        fun perspective( fovy_grad : Float, aspect_xy : Float, near : Float, far : Float ) : Mat4
        {
            val fovy_rad : Float = (fovy_grad*PI.toFloat())/180.0f
            val h        : Float = near * tan( fovy_rad/2.0f )
            val w        : Float = h*aspect_xy

            return frustum( -w, +w, -h, +h, near, far )
        }

    } // fin del 'companion object'

} // fin de la clase 'Mat4'









/** COMENTADO DESDE AQUI HASTA EL FINAL

// ---------------------------------------------------------------------------------------

/**
 * Vectores de 3 números reales en simple precision
*/
export class Vec3 extends Float32Array
{
constructor( arr : Array<Float> )
{
Assert( arr.length == 3, "Vec3.constructor - se deben dar 16 valores reales" )
super( arr )
}
public get x() { return this[0] }
public get y() { return this[1] }
public get z() { return this[2] }

public get r() { return this[0] }
public get g() { return this[1] }
public get b() { return this[2] }

/**
 * Convierte un Vec3 codificando un color con valores entre 0 y 1 en una cadena con porcentajes
 * @returns (string)
*/

public toStringPercent() : string
{
val opts = { maximumFractionDigits : 0 }
val sr : string = (100.0f*this[0]).toLocaleString( "en", opts )
val sg : string = (100.0f*this[1]).toLocaleString( "en", opts )
val sb : string = (100.0f*this[2]).toLocaleString( "en", opts )

return `( ${sr}%, ${sg}%, ${sb}% )`
}

/**
 * Calcula la suma de este vector y otro
 * @param otro (Vec3) el otro vector
 * @returns (Vec3) vector suma
*/
public mas( otro : Vec3 ) : Vec3
{
return new Vec3([ this.x+otro.x, this.y+otro.y, this.z+otro.z ])
}

/**
 * Devuelve una cadena con un color (con componentes entre 0 y 1)
 * codificado en hexadecimal con formato CSS (cadena de # seguido de 6 dígitos).
 * Produce excepción si las componentes no están entre 0 y 1.
 * @returns (string) representación del color en la forma '#rrggbb'
*/
public hexColorStr() : string
{
return `#${Hex2( this.r )}${Hex2( this.g )}${Hex2( this.b )}`
}

/**
 * Calcula la resta de este vector y otro
 * @param otro (Vec3) el otro vector
 * @returns (Vec3) vector resta
*/
public menos( otro : Vec3 ) : Vec3
{
return new Vec3([ this.x-otro.x, this.y-otro.y, this.z-otro.z ])
}

/**
 * Calcula el producto de este vector y un valor escalar (real)
 * @param a (Float) valor a multiplicar
 * @returns (Vec3) vector multiplicado
*/
public mult( a : Float ) : Vec3
{
return new Vec3([ a*this.x, a*this.y, a*this.z ])
}

/**
 * Divide cada componentes de este vector por un valor escalar (real)
 * @param a (Float) valor a dividir (>0)
 * @returns (Vec3) vector multiplicado
*/
public div( a : Float ) : Vec3
{
return new Vec3([ this.x/a, this.y/a, this.z/a ])
}

/**
 * Calcula la longitud de un vector
 * @returns (Float) longitud de este vector
*/
public get longitud() : Float
{
return Math.sqrt( this.x*this.x + this.y*this.y + this.z*this.z )
}

/**
 * Produce una copia normalizada de este vector
 * @returns (Vec3) vector normmalizado
*/
public get normalizado() : Vec3
{
val l : Float = this.longitud
return new Vec3([ this.x/l, this.y/l, this.z/l ])
}

/**
 * Calcula el producto escalar (dot product) entre este vector y otro
 * @param otro (Vec3) el otro vector
 * @returns (Float) producto escalar
*/
public dot( otro : Vec3 ) : Float
{
return this.x*otro.x + this.y*otro.y +  this.z*otro.z
}

/**
 * Calcula el producto vectorial (cross product) entre este vector y otro
 * @param otro (Vec3) el otro vector
 * @returns (Vec3) producto vectorial
*/
public cross( otro : Vec3 ) : Vec3
{
return new Vec3
([
this.y*otro.z - this.z*otro.y, // x = y1*z2 - z1*y2
this.z*otro.x - this.x*otro.z, // y = z1*x2 - x1*z2
this.x*otro.y - this.y*otro.x  // z = x1*y2 - x2*y1
])
}

}

// ------------------------------------------------------------------------------------

/**
 * Convierte una cadena con un color codificado en hexadecimal en un Vec3 con valores entre 0 y 1
 * @param s (string) cadena de entrada, de 7 caracteres comenzando en '#'
 * @return (Vec3) vector codificando un color con componentes entre 0 y 1.
*/
fun Vec3DesdeColorHex( s : string ) : Vec3
{
val nombref : string = 'ColorStrAVec3'
Assert( s.length == 7 && s[0] == '#', `${nombref} la cadena '${s}' no tiene formato de color en hexadecimal `)

val r : Float = NumbDesdeHex2(`${s[1]}${s[2]}`)
val g : Float = NumbDesdeHex2(`${s[3]}${s[4]}`)
val b : Float = NumbDesdeHex2(`${s[5]}${s[6]}`)
return new Vec3([ r, g, b ])

}
// ------------------------------------------------------------------------------------

/**
 * Vectores de 4 números reales en simple precision
*/
export class Vec4 extends Float32Array
{
constructor( arr : Array<Float> )
{
Assert( arr.length == 4, "Vec4.constructor - se deben dar 16 valores reales" )
super( arr )
}
public get x() { return this[0] }
public get y() { return this[1] }
public get z() { return this[2] }
public get w() { return this[3] }

public get r() { return this[0] }
public get g() { return this[1] }
public get b() { return this[2] }
public get a() { return this[3] }

/**
 * Calcula la longitud de un vector
 * @returns (Float) longitud de este vector
*/
public get longitud() : Float
{
return Math.sqrt( this.x*this.x + this.y*this.y + this.z*this.z )
}
}

// ------------------------------------------------------------------------------------

/**
 * Vectores de 3 números enteros sin signo de 4 bytes cada uno.
*/
export class UVec3 extends Uint32Array
{
constructor( arr : Array<Float> )
{
Assert( arr.length == 3, "UVec3.constructor - se deben dar exactamente 3 valores enteros sin signo" )
super( arr )
}
}

// ----------------------------------------------------------------------------------

/**
 * Para una matriz 4x4 dispuesta por filas, devuelve el índice del elemento en una
 * fila y columna determinadas.
 *
 * @param f índice de fila
 * @param c índice de columna
 * @returns  índice de la celda
*/
function ind( f : Float, c : Float ) : Float
{
Assert( 0 <= f && f < 4, `Mat4.indice: fila (${f}) fuera de rango`)
Assert( 0 <= c && c < 4, `Mat4.indice: columna (${c}) fuera de rango`)
return 4*f + c
}


/**
 * Matriz de 4x4 valores reales (Float), se guarda por filas como un 'Float32Array'
 *                                                  ---------
*/
export class Mat4 extends Float32Array
{
constructor( arr : Array<Float> )
{
Assert( arr.length == 16, "Mat4.constructor - se deben dar 16 valores reales" )
super( arr )
}

/**
 * Lee un valor en una fila y una columna (ambas entre 0 y 3)
 * @param f índice de fila
 * @param c índice de columna
 * @returns  valor en la celda
*/
val( f : Float, c : Float ) : Float
{
return this[ ind(f,c) ]
}


public get _00() : Float { return this.val(0,0) }
public get _01() : Float { return this.val(0,1) }
public get _02() : Float { return this.val(0,2) }
public get _03() : Float { return this.val(0,3) }

public get _10() : Float { return this.val(1,0) }
public get _11() : Float { return this.val(1,1) }
public get _12() : Float { return this.val(1,2) }
public get _13() : Float { return this.val(1,3) }

public get _20() : Float { return this.val(2,0) }
public get _21() : Float { return this.val(2,1) }
public get _22() : Float { return this.val(2,2) }
public get _23() : Float { return this.val(2,3) }

public get _30() : Float { return this.val(3,0) }
public get _31() : Float { return this.val(3,1) }
public get _32() : Float { return this.val(3,2) }
public get _33() : Float { return this.val(3,3) }



/**
 * Aplica esta matriz a un vector de 3 entradas y devolver el vector 3 resultado
 *
 * @param   v  (Vec3) vector original
 * @param   w  (Float) coordenada W (suele ser 0 o 1, pero no necesariamente)
 * @returns (Vec3) vector resultado
*/
aplica( v : Vec3, w : Float ) : Vec3
{
let res = new Vec3([ 0.0f, 0.0f, 0.0f ])

for( let fila = 0 ; fila < 3 ; fila++ )
for( let colu  = 0 ; colu < 4 ; colu++ )
res[fila]  +=  this.val( fila, colu ) * ( (colu < 3) ? v[colu] : w )
return res
}

/**
 * Aplica esta matriz a un vector de 4 entradas y devolver el vector 4 resultado
 *
 * @param   v  (Vec4) vector original
 * @returns (Vec4) vector resultado
*/
aplica_v4( v : Vec4 ) : Vec4
{
let res = new Vec4([ 0.0f, 0.0f, 0.0f, 0.0f ])

for( let fila = 0 ; fila < 3 ; fila++ )
for( let colu = 0 ; colu < 4 ; colu++ )
res[fila]  +=  this.val( fila, colu )* v[colu]
return res
}

/**
 * Compone esta matriz con otra y devuelve la matriz resultado
 * (la otra matriz se compone por la derecha)
 * @param otra (Mat4) la otra matriz
 * @returns (Mat4) la matriz resultado
*/
componer( otra : Mat4 ) : Mat4
{
let res = CMat4.cero()

for( let fila = 0 ; fila<4 ; fila++ )
for( let colu = 0 ; colu<4 ; colu++ )
for( let k = 0 ; k<4 ; k++ )
res[ ind(fila,colu) ] += this.val( fila, k ) * otra.val( k, colu )
// ==> res(fila,colu) += this(fila,k) * m(k,colu)
return res
}

/**
 * calcula el determinante de la submatriz 3x3
 * @returns determinante
*/
determinante3x3(  )
{
return + this._00 * this._11 * this._22
+ this._01 * this._12 * this._20
+ this._02 * this._10 * this._21

- this._00 * this._12 * this._21
- this._01 * this._10 * this._22
- this._02 * this._11 * this._20
}

// calcula la matriz transpuesta de la submatriz 3x3, ignorando desplazamientos
traspuesta3x3() : Mat4
{
let tras : Mat4 = CMat4.ident()

for( let f = 0 ; f < 3 ; f++ )
for( let c = 0 ; c < 3 ; c++ )
{
tras[ ind(f,c) ] = this.val( c,f )
}
return tras
}




/**
 * Calcula la matriz inversa de una matriz 4x4 afín, es decir, en la cual la
 * última fila es 0 0 0 1. El determinante no puede ser 0. Solo considera la matriz 3x3 sin los términos de traslación.
 * @returns matriz 4x4 inversa
*/
inversa3x3(  ) : Mat4
{
val nombref : string = "Mat4.inversa"
Assert( this._30 == 0.0f && this._31 == 0 && this._32 == 0.0f && this._33 == 1.0, `${nombref} la matriz no es afín` )

// 1. calcular matriz de cofactores ('cofac')

let cofac : Mat4 = CMat4.ident()

for( let i = 0 ; i < 3 ; i++ )
for( let j = 0 ; j < 3 ; j++ )
{
val i1 = (i+1)%3, i2 = (i+2)%3
val j1 = (j+1)%3, j2 = (j+2)%3

cofac[ ind(i,j) ] = this.val(i1,j1)*this.val(i2,j2) - this.val(i1,j2)*this.val(i2,j1) ;
}

// 2. calcular determinante (det) (usando la primera fila de 'm' y de 'cofac')

val det : Float = this._00*cofac._00 + this._01*cofac._01 + this._02*cofac._02

Assert( 1e-6 < Math.abs(det), `${nombref} el determinante es cero o casi cero (matriz singular)` )

// 3. calcular la matriz inversa de la sub-matrix 3x3 (inv3x3) como la
// adjunta (transpuesta de los cofactores), dividida por el determinante:

let inv3x3 : Mat4 = CMat4.ident()
for( let i = 0 ; i < 3 ; i++ )
for( let j = 0 ; j < 3 ; j++ )
inv3x3[ ind(i,j) ] = cofac.val(j,i) / det

// ya está
return inv3x3
}

/**
 * Calcula la matriz inversa de una matriz 4x4 afín, es decir, en la cual la
 * última fila es 0 0 0 1. El determinante no puede ser 0.
 * @returns matriz 4x4 inversa
*/
inversa(  ) : Mat4
{
val nombref : string = "Mat4.inversa"
Assert( this._30 == 0.0f && this._31 == 0 && this._32 == 0.0f && this._33 == 1.0, `${nombref} la matriz no es afín` )

let inv3x3 : Mat4 = this.inversa3x3()

// 4. calcular la matriz de traslación inversa

let trasl_inv : Mat4 = CMat4.ident()
for( let i = 0 ; i < 3 ; i++ )
trasl_inv[ ind(i,3) ] = -this.val(i,3)

// 5. calcular (y devolver) la inversa completa
// (traslación inversa seguida de la inversa 3x3)

return inv3x3.componer( trasl_inv )
}

/**
 * Convierte una matriz en una cadena (incluye varias líneas)
 * @returns (string) cadena
*/
toString() : string
{
let str : string = '\n'
val n = 3

for( let f = 0 ; f<4 ; f++ )
{
str = str + ` | ${N2S(this.val(f,0))}, ${N2S(this.val(f,1))}, ${N2S(this.val(f,2))}, ${N2S(this.val(f,3))} |\n`
}
return str
}
}
// ----------------------------------------------------------------------------------

/**
 * namespace que incluye las funciones que crean matrices 4x4
*/
export namespace CMat4
{
/**
 * Produce la matriz con todas las entradas a cero
 * @return (Mat4) matriz con entradas a cero
*/
fun cero() : Mat4
{
return new Mat4
([
0, 0, 0, 0,
0, 0, 0, 0,
0, 0, 0, 0,
0, 0, 0, 0
])
}

/**
 * Produce la matriz identidad
 * @return (Mat4) matriz identidad 4x4
*/
fun ident() : Mat4
{
return new Mat4
([
1, 0, 0, 0,
0, 1, 0, 0,
0, 0, 1, 0,
0, 0, 0, 1
])
}


// ------------------------------------------------------------------------------------------------
/**
 * construye una matriz 4x4 de traslación a partir de un vector de traslación
 *
 * @param v (Vec3) vector de traslación
 * @return (Mat4) matriz de traslación
*/
fun traslacion( v : Vec3 ) : Mat4
{
return new Mat4
([ 1, 0, 0, v.x,
0, 1, 0, v.y,
0, 0, 1, v.z,
0, 0, 0, 1
])
}

// ------------------------------------------------------------------------------------------------
/**
 * construye una matriz 4x4 de escalado a partir de un vector con los factores de escala
 *
 * @param v (Vec3) vector con los factores de escala en X, Y y Z
 * @return (Mat4) matriz de escalado
*/
fun escalado( v : Vec3 ) : Mat4
{
return new Mat4
([ v.x, 0,   0,   0,
0,   v.y, 0,   0,
0,    0,  v.z, 0,
0,    0,  0,   1
])
}

/**
 * construye una matriz de rotación entorno al eje X, dado un ángulo (en grados)
 * @param angulo_grad (Float) ángulo en grados
 * @return (Mat4) matriz de rotación entorno a X
*/
fun rotacionXgrad( angulo_grad : Float )
{
val ar : Float = (angulo_grad*Math.PI)/180.0f ,
c  : Float = Math.cos( ar ),
s  : Float = Math.sin( ar )

return new Mat4
([ 1,  0,  0,  0 ,
0,  c, -s,  0 ,
0,  s,  c,  0 ,
0,  0,  0,  1
])
}

// ------------------------------------------------------------------------------------------------
/**
 * construye una matriz de rotación entorno al eje Y, dado un ángulo (en grados)
 * @param angulo_grad (Float) ángulo en grados
 * @return (Mat4) matriz de rotación entorno a Y
*/
fun rotacionYgrad( angulo_grad : Float )
{
val ar : Float = (angulo_grad*Math.PI)/180.0f ,
c  : Float = Math.cos( ar ),
s  : Float = Math.sin( ar )

return new Mat4
([ c,  0,  s,  0,
0,  1,  0,  0,
-s, 0,  c,  0,
0,  0,  0,  1
])
}

// ------------------------------------------------------------------------------------------------
/**
 * construye una matriz de rotación entorno al eje Z, dado un ángulo (en grados)
 * @param angulo_grad (Float) ángulo en grados
 * @return (Mat4) matriz de rotación entorno a Z
*/
fun rotacionZgrad( angulo_grad : Float )
{
val ar : Float = (angulo_grad*Math.PI)/180.0f ,
c  : Float = Math.cos( ar ),
s  : Float = Math.sin( ar )

return new Mat4
([ c, -s,  0,  0,
s,  c,  0,  0,
0,  0,  1,  0,
0,  0,  0,  1
])
}

// ------------------------------------------------------------------------------------------------
/**
 * construye una matriz de proyección perspectiva a partir de los límites del view-frustum
 * (según: http://docs.gl/gl2/glFrustum)
 *
 * @param l (Float) limite izquierdo del frustum en X (en el plano z=near)
 * @param r (Float) limite derecho del frustum en X (en el plano z=near)
 * @param b (Float) limite inferior del frustum en Y (en el plano z=near)
 * @param t (Float) limite superior del frustum en Y (en el plano z=near)
 * @param n (Float) distancia en Z entre observador y plano de recorte delantero (>0)
 * @param f (Float) distancia en Z entre observador y plano de recorte trasero (>n)
 * @returns {Mat4}   (Float) matriz de proyeccion perspectiva
*/
fun frustum( l : Float, r : Float, b : Float, t : Float, n : Float, f : Float ) : Mat4
{
val nombref = "CMat4.frustum"
val eps = 1e-6
Assert( eps < n && eps < f, `${nombref} 'n' o 'f' son casi cero o negativos` )
Assert( n < f, `${nombref} 'n' no es menor que 'f'`)
Assert( Math.abs(r-l) > eps && Math.abs(t-b) > eps  && Math.abs(n-f) > eps, `${nombref} parámetros incorrectos (el tamaño en X, Y o Z es casi cero)` )

val
a00 : Float = (2.0*n)/(r-l),   a02 : Float = (r+l)/(r-l),
a11 : Float = (2.0*n)/(t-b),   a12 : Float = (t+b)/(t-b) ,
a22 : Float = -(n+f)/(f-n),    a23 : Float = -(2.0*f*n)/(f-n)

return new Mat4
([  a00,   0.0f,   a02,  0.0f,
0.0f,   a11,   a12,  0.0f,
0.0f,   0.0f,   a22,  a23,
0.0f,   0.0f,  -1.0,  0.0f
])
}

// ------------------------------------------------------------------------------------------------
/**
 * construye una matriz de proyección perspectiva a partir de un conjunto alternativo de parámetros
 * (según: https://registry.khronos.org/OpenGL-Refpages/gl2.1/xhtml/gluPerspective.xml)
 *
 * @param fovy_grad (Float) amplitud de campo en vertical (un ángulo en grados, entre 0 y 180.0f, sin incluir)
 * @param aspect_xy (Float) relación de aspecto (ancho del viewport dividido por el alto)
 * @param near      (Float) distancia en Z entre observador y plano de recorte delantero (>0)
 * @param far       (Float) distancia en Z entre observador y plano de recorte trasero (>n)
 * @returns
*/
fun perspective( fovy_grad : Float, aspect_xy : Float, near : Float, far : Float ) : Mat4
{
val
fovy_rad : Float = (fovy_grad*Math.PI)/180.0f,
h        : Float = near * Math.tan( fovy_rad/2.0 ),
w        : Float = h*aspect_xy

return frustum( -w, +w, -h, +h, near, far )
}


}

 **/
