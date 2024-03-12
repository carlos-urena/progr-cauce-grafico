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