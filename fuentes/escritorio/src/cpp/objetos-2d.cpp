// *********************************************************************
// **
// ** Asignatura: PCG (Programación del cauce gráfico) (MDS)
// ** 
// ** Gestión de grafos de escena (implementación)
// ** Copyright (C) 2016-2023 Carlos Ureña
// **
// ** Implementación de objetos 2D
// **     + Clase 'Poligono2D' (derivada de 'ObjetoVisu')
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

#include <string>
#include <sstream>  // istringstream
#include "utilidades.h"
#include "vaos-vbos.h"  
#include "cauce-2d.h"
#include "aplic-2d.h"
#include "objetos-2d.h"

// ---------------------------------------------------------------------

Poligono2D::Poligono2D(  )
{

}

// -----------------------------------------------------------------------------------------------

Poligono2D::~Poligono2D()
{
    delete dvao_tris ;
    dvao_tris = nullptr ;

    vertices.clear();
    colores_ver.clear();
    cct_ver.clear();
    triangulos.clear();
    contorno.clear();
}
// ---------------------------------------------------------------------

void Poligono2D::crearVAOtriangulos() 
{
    using namespace std ;

    assert( dvao_tris == nullptr ); 
    assert( vertices.size() > 0 ); 
    assert( triangulos.size() > 0 );
    
    // dvao_tris = new DescrVAO( numero_atributos_cauce_base, 
    //                           new DescrVBOAtribs( 0, vertices ));  
    // assert( dvao_tris != nullptr );
    // dvao_tris->agregar( new DescrVBOInds( triangulos ) );

    // if ( 0 < colores_ver.size() )   
    //     dvao_tris->agregar( new DescrVBOAtribs( ind_atrib_colores, colores_ver ) );
    // if ( 0 < cct_ver.size() ) 
    //     dvao_tris->agregar( new DescrVBOAtribs( ind_atrib_coord_text, cct_ver ) );

    dvao_tris = new DescrVAO({ .posiciones_2d = vertices, 
                               .colores       = colores_ver, 
                               .coord_text    = cct_ver, 
                               .triangulos    = triangulos });

    CError();
}
// ---------------------------------------------------------------------

void Poligono2D::crearVAOcontorno() 
{
    using namespace std ;

    assert( dvao_cont == nullptr );
    assert( vertices.size() > 0 ); 
    assert( contorno.size() > 0 );

    dvao_cont = new DescrVAO({ .posiciones_2d = vertices, 
                               .indices       = contorno });
}

// ---------------------------------------------------------------------

void Poligono2D::visualizarGL(  ) 
{
    using namespace std ;

    Cauce2D * cauce = Aplicacion2D::instancia()->cauce2D() ;

    if ( triangulos.size() == 0 || vertices.size() == 0 )
    {  
        cout << "advertencia: intentando dibujar un polígono vacío '" << leerNombre() << "'" << endl << flush ;
        return ;
    }

    if ( dvao_tris == nullptr ) 
        crearVAOtriangulos();

    if ( tieneColor() )
    {
      cauce->pushColor();
      cauce->fijarColor( leerColor() );
    }

    // dibujar relleno
    dvao_tris->draw( GL_TRIANGLES );

    if ( tieneColor() )
      cauce->popColor();

    CError();
}
// ---------------------------------------------------------------------

void Poligono2D::visualizarSegmentosGL(  ) 
{
    using namespace std ;
    //cout << "Invocado " << __FUNCTION__ << " para un Poligono2D" << endl ;

    if ( dvao_cont == nullptr ) 
        crearVAOcontorno();

    Cauce2DLineas * cauce2d_lineas = Aplicacion2D::instancia()->cauce2DLineas() ;
    assert( cauce2d_lineas != nullptr );

    
    // visualizar las lineas de color azul
    cauce2d_lineas->fijarColor( 0.0f, 0.0f, 0.8f ) ; 
    cauce2d_lineas->fijarVisualizarPuntos( false );
    cauce2d_lineas->fijarVisualizarLineas( true );
    dvao_cont->draw( GL_LINE_LOOP );  

    // visualizar los puntos de color rojo
    cauce2d_lineas->fijarColor( 0.8f, 0.0f, 0.0f ) ; 
    cauce2d_lineas->fijarVisualizarPuntos( true );
    cauce2d_lineas->fijarVisualizarLineas( false );
    dvao_cont->draw( GL_LINE_LOOP );       

          
}
// ---------------------------------------------------------------------

void Poligono2D::visualizarModoSeleccionGL()
{
    using namespace std ;
    cout << "Invocado " << __FUNCTION__ << " para un Poligono2D" << endl ;
}


// ---------------------------------------------------------------------

Estrella::Estrella( const unsigned n_puntas, const float radio1, const float radio2 ) 
{
    ponerNombre( "Estrella (" + std::to_string( n_puntas ) + " puntas)." );
    using namespace glm ;
    const unsigned nv  = 2*n_puntas+1 ;
    const unsigned ivc = nv-1 ; // índice del vértice central

    vertices.resize( nv );
    triangulos.resize( 2*n_puntas );

    // vértices y triángulos
    for( unsigned i = 0 ; i < n_puntas ; i++ )
    {
        const float ang   = 2.0f * M_PI * float(i) / n_puntas ,
                    ang2  = 2.0f * M_PI * float(i+0.5f) / n_puntas ; 

        vertices[ 2*i ]    = radio1 * vec2( cosf( ang ),  sinf( ang ) );
        vertices[ 2*i +1 ] = radio2 * vec2( cosf( ang2 ), sinf( ang2 ) );

        triangulos[2*i]   = uvec3( ivc, 2*i,   2*i+1 );
        triangulos[2*i+1] = uvec3( ivc, 2*i+1, (2*i+2)%(2*n_puntas) );
    }

    // añadir vértice central
    vertices[ ivc ] = vec2( 0.0f, 0.0f ) ;

    // crear la lista de indices de vértices del contorno 
    contorno.resize( 2*n_puntas );
    for( unsigned i = 0 ; i < 2*n_puntas ; i++ )
        contorno[i] = i ; 

}
// ---------------------------------------------------------------------

PoligonosDeTXT::PoligonosDeTXT( const std::string& nombre_fichero )
{
    using namespace std;

    string nombre_fichero_completo = PathCarpetaMateriales() + "/varios/" + nombre_fichero;
    ifstream file(nombre_fichero_completo);

    if (!file.is_open())
    {
        std::cout << "Error al intentar abrir archivo" << nombre_fichero_completo << std::endl;
        exit(1);
    }
    string line;

    ponerNombre( "polígonos del archivos txt '" + nombre_fichero + "'" );

    unsigned n_lineas = 0, n_vertices = 0 ;
    float minX = std::numeric_limits<float>::max();
    float maxX = std::numeric_limits<float>::min();
    float minY = std::numeric_limits<float>::max();
    float maxY = std::numeric_limits<float>::min();

    while (std::getline(file, line))
    {
        istringstream iss(line);
        float x, y;
        vector<glm::vec2> array_vec2;

        while (iss >> x >> y)
        {
            array_vec2.push_back( glm::vec2(x, y) );
            minX = std::min(minX, x); maxX = std::max(maxX, x);
            minY = std::min(minY, y); maxY = std::max(maxY, y);
            n_vertices++ ;
        }

        vertices.push_back(array_vec2);
        
        n_lineas++;
        //cout << "  leída línea " << n_lineas << " con " << array_vec2.size() << " vértices" << endl;
    }
    cout << "Archivo '" << nombre_fichero << "' leído con " << n_lineas << " líneas y " << n_vertices << " vértices." << endl;

    centro = glm::vec2((minX + maxX) / 2.0f, (minY + maxY) / 2.0f); 
    tamano = glm::vec2(maxX - minX, maxY - minY);

    cout << "Centro == " << to_string(centro) << ", tamano == " << to_string(tamano) << endl ;
}

// ---------------------------------------------------------------------

void PoligonosDeTXT::visualizarGL(  ) 
{
    // no hace nada (por ahora), ya que solo tenemos el line_loop del contorno, en 'visualizarSegmentosGL'
}
// ---------------------------------------------------------------------

void PoligonosDeTXT::visualizarSegmentosGL() 
{
    using namespace std ;
    using namespace glm ;

    if ( !dvaos_creados )
        crearVAOs();   

    auto * cauce = Aplicacion2D::instancia()->cauce2DLineas() ;
    assert( cauce != nullptr );

    const float f = 1.8f/std::max( tamano.x, tamano.y ) ;

    cauce->fijarVisualizarPuntos( true );
    cauce->fijarVisualizarLineas( true );
    cauce->fijarGrosorLineasWCC( 0.002f );
    cauce->fijarRadioPuntosWCC( 0.001f );
    cauce->fijarColor( 0.3f, 0.0f, 0.0f );

    cauce->pushMM();
        cauce->compMM( scale( vec3( f, -f, 1.0f ) ));
        cauce->compMM( translate( vec3( -centro, 0.0f ) ));
        for( unsigned i = 0 ; i < dvaos.size() ; i++ )
            dvaos[i]->draw( GL_LINE_STRIP );

    cauce->popMM();
}
// ---------------------------------------------------------------------

void PoligonosDeTXT::visualizarModoSeleccionGL() 
{
    using namespace std ;
    cout << "por ahora no se visualizan en modo selección los segmentos de los polígonos de TXT" << endl ;
    
}
// ---------------------------------------------------------------------

void PoligonosDeTXT::crearVAOs()
{
    using namespace std ;

    for( unsigned i = 0 ; i < vertices.size() ; i++ )
    {
        assert( vertices[i].size() > 2 );
        dvaos.push_back( new DescrVAO( numero_atributos_cauce_base, 
                                        new DescrVBOAtribs( ind_atrib_posiciones, 
                                                            vertices[i] )));  
    }
    dvaos_creados = true ;
    cout << "Creados VAOs para " << vertices.size() << " polígonos de TXT" << endl ;
} 