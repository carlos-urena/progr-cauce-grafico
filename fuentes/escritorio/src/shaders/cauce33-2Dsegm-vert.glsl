#version 330 core

// uniforms
uniform mat4  u_mat_modelado ;    // matriz de modelado actual
uniform mat4  u_mat_vista ;       // matriz de vista (mundo --> camara)
uniform mat4  u_mat_proyeccion ;  // matriz de proyeccion
uniform float u_ancho_wcc ;       // ancho de las líneas en WCC

// entradas
layout( location = 0 ) in vec3 in_posicion_occ ;   // posición del vértice en coordenadas de objeto
layout( location = 1 ) in vec3 in_color ;          // color del vértice

// salidas (adicionales a gl_Position)
out vec4 v1_color ;  // color del vértice 

// ------------------------------------------------------------------------------

void main()
{
   vec4 pos_wcc    =  // posición del vértice en coords. de mundo
   v1_color        = vec4( in_color, 1.0 ) ;
   //gl_Position   = u_mat_proyeccion * u_mat_vista * u_mat_modelado * vec4( in_posicion_occ, 1.0 ) ;  
   gl_Position     = u_mat_vista * u_mat_modelado * vec4( in_posicion_occ, 1.0 ) ;  

}
