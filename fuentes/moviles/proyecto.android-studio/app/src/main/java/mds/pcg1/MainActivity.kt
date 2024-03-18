// *********************************************************************
// **
// ** Asignatura: PCG (Programación del Cauce Gráfico).
// **
// ** Implementación de la actividad principal
// ** Copyright (C) 2024 Carlos Ureña
// **
// ** Clase: MainActivity
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

package mds.pcg1

import android.os.Bundle
import android.app.Activity
import mds.pcg1.gl_surface.*

/**
 * Ver el OpenGL Dahsboard: el 93% de los móviles soportan OpenGL ES 3.0 (Marzo 24)
 * @see https://developer.android.com/about/dashboards#OpenGL
 *   ---> decido usar GLES30 para esta aplicación
 *
 * Código basado en:
 * @see: https://developer.android.com/develop/ui/views/graphics/opengl/environment?authuser=1&hl=es-419
 *
 * pendiente de apaptar a lo que se dice aquí (en el apartado "Detect All Suported Gestures")
 * https://developer.android.com/develop/ui/views/touch-and-input/gestures/detector
 *
 */

class OpenGLES30Activity : Activity() {

    private lateinit var gl_view : GLSurfaceViewPCG

    /**
     * Función que se ejecuta cada vez que se crea la actividad, eso
     * ocurre cuando se lanza la aplicación, pero también cuando se gira el dispositivo.
     * (se crea una surface, un contexto y una aplicación nuevas)
     */
    public override fun onCreate( savedInstanceState: Bundle? )
    {
        instancia = this
        super.onCreate( savedInstanceState )

        // Create a GLSurfaceView instance and set it
        // as the ContentView for this Activity.

        gl_view = GLSurfaceViewPCG( this )
        setContentView( gl_view )

    }

    companion object {
        public var instancia : OpenGLES30Activity? = null
    }
}

