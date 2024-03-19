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
import mds.pcg1.objeto_visu.*
import mds.pcg1.utilidades.TAG
import mds.pcg1.vaos_vbos.*
import mds.pcg1.vec_mat.*


open class MallaInd : ObjetoVisualizable()
{
    // tablas de atributos
    protected val posiciones  : Array<Vec3> = emptyArray()  // tabla de coordenadas de las posiciones de los vértices
    protected val colores     : Array<Vec3> = emptyArray()  // tabla de colores de los vértices
    protected val normales_v  : Array<Vec3> = emptyArray()  // tabla de normales de los vértices
    protected val coords_text : Array<Vec2> = emptyArray()  // tabla de coordenadas de textura de los vértices

    // tabla de triángulos (tabla de índices)
    protected val triangulos : Array<UVec3> = emptyArray()


    // descriptor del VAO con la malla
    protected val dvao : DescrVAO? = null

    // descriptor del VAO con las aristas (se crea al llamar a 'crearVAOAristas')
    protected val dvao_aristas : DescrVAO? = null

    // descriptor del VAO con los segmentos de normales (se crea al llamar a 'crearVAONormales')
    protected val dvao_normales : DescrVAO? = null

    /**
     * Visualiza el objeto. este método debe ser redefinido en clases derivadas
     */
    override fun visualizar()
    {
        // FALTA: crear FloatArray desde Array<Vec3> de posiciones,
        // igualmente colores, normales, cctt (si no son nulos)
        // si el vao no está creado, crearlo
        // dibujar el VAO
    }

    /**
     * Visualiza las aristas del objeto. Este método puede ser redefinido en clases derivadas, si
     * no se hace, el método no hace nada (eso implica que ese objeto no tiene aristas que se pueden visualizar
     * o que no se ha implementado esto)
     */
    override fun visualizarAristas()
    {
        Log.v( TAG, "El objeto '${nombre}' no tiene método para visualizar aristas ('visualizarAristas')" )
    }

    /**
     * Visualiza las normales del objeto. Este método puede ser redefinido en clases derivadas, si
     * no se hace, el método no hace nada (eso implica que ese objeto no tiene normales que se pueden visualizar
     * o que no se ha implementado esto)
     */
    override fun visualizarNormales()
    {
        Log.v( TAG, "El objeto '${nombre}' no tiene método para visualizar normales ('visualizarNormales')." )
    }
}

