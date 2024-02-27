#version 450 core


// *********************************************************************
// Tesselation Control Shader: 
//
// Este shader se invoca una vez por cada punto de control de un patch
// y se encarga de establecer los niveles de teselación para el patch
// y de copiar los valores (atributos de vértices) de entrada a los valores de salida
//
// https://www.khronos.org/opengl/wiki/Tessellation_Control_Shader
// https://ogldev.org/www/tutorial30/tutorial30.html
//
// ---------------------------------------------------------------------

layout( vertices = 3 ) out; // la salida son triangulos

// --------------------------------------------------------------------------------------------
// Uniforms usados en los tesselation shaders

uniform bool u_activar_ts ; // si es true se activa la teselación, en otro caso no se tesela (se hace 'passthrough')

// --------------------------------------------------------------------------------------------
// Valores de entrada desde el Vertex Shader

in vec4 v1_posic_ecc[] ;   // posicion del punto (en coords de camara)
in vec4 v1_color[] ;       // color del vértice (interpolado a los pixels)
in vec3 v1_normal_ecc[] ;   // normal  (en coords. de cámara)
in vec2 v1_coord_text[] ;   // coordenadas de textura
in vec3 v1_vec_obs_ecc[] ; // vector hacia el observador (en coords de cámara)

// --------------------------------------------------------------------------------------------
// Variables de salida hacia el Tesselation Evaluation Shader 

out vec4 v2_posic_ecc[] ;   // posicion del punto (en coords de camara)
out vec4 v2_color[] ;       // color del vértice (interpolado a los pixels)
out vec3 v2_normal_ecc[] ;   // normal  (en coords. de cámara)
out vec2 v2_coord_text[] ;   // coordenadas de textura
out vec3 v2_vec_obs_ecc[] ; // vector hacia el observador (en coords de cámara)

// --------------------------------------------------------------------------------------------
// 'main' se invoca una vez por cada punto de control del patch

void main(void)
{
   // copiamos los valores de entrada a los de salida.

   v2_posic_ecc[gl_InvocationID]    = v1_posic_ecc[gl_InvocationID] ;
   v2_color[gl_InvocationID]        = v1_color[gl_InvocationID] ;
   v2_normal_ecc[gl_InvocationID]   = v1_normal_ecc[gl_InvocationID] ;
   v2_coord_text[gl_InvocationID]   = v1_coord_text[gl_InvocationID] ;
   v2_vec_obs_ecc[gl_InvocationID]  = v1_vec_obs_ecc[gl_InvocationID] ;

   
   // calculamos la distancia del centro de cada arista al observador (al origen en ECC)
   float d0 = length( v2_posic_ecc[1] + v2_posic_ecc[0] ) / 2.0 ;
   float d1 = length( v2_posic_ecc[2] + v2_posic_ecc[1] ) / 2.0 ;
   float d2 = length( v2_posic_ecc[0] + v2_posic_ecc[2] ) / 2.0 ;



   // establecemos los niveles de teselación 
   // para el caso de triángulos se usan tres valores del array 'outer' y un valor del array 'inner'
   // los valores del array outer se interpretan como el numero de segmentos en los que se divide cada lado del triángulo externo
   // los valores del array inner se interpretan como el numero de triangulos internos que se generan en la teselación

   if ( u_activar_ts )
   {
      // si está activado, se usa el nivel de teselación basado en longitudes y áreas.
      gl_TessLevelOuter[0] = 10.0/d0;
      gl_TessLevelOuter[1] = 10.0/d1;
      gl_TessLevelOuter[2] = 10.0/d2;
      
      gl_TessLevelInner[0] = max( 10.0/d0, max( 10.0/d1, 10.0/d2 )); // el nivel de teselación interno es el máximo de los tres lados
   }
   else 
   {
      // si está desactivado se usa el nivel de teselación 1 (implica no teselar)
      gl_TessLevelOuter[0] = 1.0;
      gl_TessLevelOuter[1] = 1.0;
      gl_TessLevelOuter[2] = 1.0;

      gl_TessLevelInner[0] = 1.0;
   }
   

   // copiamos la posición del punto de control 

   gl_out[gl_InvocationID].gl_Position = gl_in[gl_InvocationID].gl_Position;
}