// *********************************************************************
// **
// ** Asignatura: INFORMÁTICA GRÁFICA
// ** 
// ** Gestión de grafos de escena (declaraciones)
// ** Copyright (C) 2016-2023 Carlos Ureña
// **
// ** Declaraciones de: 
// **     + Clase 'NodoGrafoEscena' (derivada de 'Objeto3D')
// **     + Clase 'EntradaNGE' (una entrada de un nodo del grafo de escena)
// **     + Tipo enumerado 'TipoEntNGE' (tipo de entradas del nodo del grafo de escena)
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

#pragma once

#include "vaos-vbos.h"
#include "objeto-visu.h"

// ---------------------------------------------------------------------
///
/// @brief Clase para polígonos en 2D, 
/// @brief Cada objeto es una malla de triángulos (relleno) delimitados 
/// @brief por una secuencia de vértices que forma el contorno. 
///
class Poligono2D : public ObjetoVisu2D
{
   public:

   Poligono2D(  ) ;
   ~Poligono2D() ;

   // visualizar el objeto con OpenGL, métodos virtuales 
      
   virtual void visualizarGL(  ) override ;
   virtual void visualizarSegmentosGL() override ;
   virtual void visualizarModoSeleccionGL() override;

   // --------------------------------------------------------------------- 
   protected:

   void crearVAOtriangulos();
   void crearVAOcontorno();
    
   std::vector<glm::vec2>  vertices ; // vector de coordenadas de vértices 
   std::vector<glm::vec3>  colores_ver ; // vector de colores de vértices
   std::vector<glm::vec2>  cct_ver ; // vector de coordenadas de textura de 
   std::vector<glm::uvec3> triangulos ; // vector de triángulos (índices de vértices)
   std::vector<unsigned>   contorno ; // vector de índices de vértices del contorno

   DescrVAO * dvao_tris = nullptr ; // vao de triángulos (para visualizarGL)
   DescrVAO * dvao_cont = nullptr ; // vao de lineas (para contorno, en visualizarSegmentosGL)
   
} ;

// ---------------------------------------------------------------------
///
/// @brief Clase para polígonos en 2D, con la secuencia de vértices leídos de un polígono 
/// @brief Cada objeto es una malla de triángulos (relleno) delimitados 
/// @brief por una secuencia de vértices que forma el contorno. 
///
class PoligonosDeTXT : public ObjetoVisu2D
{ 
   public:
   PoligonosDeTXT( const std::string & nombre_fichero ) ;

   virtual void visualizarGL(  ) override ;
   virtual void visualizarSegmentosGL() override ;
   virtual void visualizarModoSeleccionGL() override ;

   private:
   std::vector< std::vector<glm::vec2> >  vertices ; // vector de vectores de coordenadas de vértices
   std::vector<DescrVAO *> dvaos ; 
   bool dvaos_creados = false ;
   void crearVAOs();
   glm::vec2 centro, tamano ;
} ;

// ---------------------------------------------------------------------
///
/// @brief Clase para un polígono 2D con forma de estrella de N puntas
///
class Estrella : public Poligono2D 
{
   public: 
   Estrella( const unsigned n_puntas, const float radio1, const float radio2 ) ;
} ;



