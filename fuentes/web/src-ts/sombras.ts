import { glsl }
from "./utilidades.js"

import { CauceBase }
from "./cauce-base.js"

import { ProgramObject } 
from "./program-obj.js"

import { FramebufferObject } 
from "./framebuffer-obj.js"

import { ShaderObject, VertexShaderObject, FragmentShaderObject } 
from "./shader-obj.js"

import { ComprErrorGL, Log, ContextoWebGL }
from   "./utilidades.js"

// -------------------------------------------------------------------------

const src_vertex :string = 
glsl`#version 300 es
    precision highp float;

    uniform mat4 u_mat_modelado;
    uniform mat4 u_mat_modelado_nor;
    uniform mat4 u_mat_vista;
    uniform mat4 u_mat_proyeccion;

    layout( location = 0 ) in vec3 pos;
    layout( location = 2 ) in vec3 nor;

    void main()
    {
        mat4 m = u_mat_proyeccion * u_mat_vista * u_mat_modelado;
        gl_Position = m*vec4( pos, 1.0 );
    }
`
// -------------------------------------------------------------------------

const src_fragment : string = 
glsl`#version 300 es
    precision highp float;

    uniform mat4 u_mat_modelado;
    uniform mat4 u_mat_modelado_nor;
    uniform mat4 u_mat_vista;
    uniform mat4 u_mat_proyeccion;

    in vec3 pos;
    in vec3 nor;

    out vec4 color;
    
    void main()
    {
        color = vec4( 1.0, 0.0, 0.0, 1.0 );
    }
`

// -------------------------------------------------------------------------

export class CauceSombras extends CauceBase
{
    private fbo_opc : FramebufferObject | null = null

    public get fbo() : FramebufferObject
    {
        if ( this.fbo_opc == null )
            throw new Error("CauceSombras.fbo: todavía no se ha creado el objeto FBO")
        return this.fbo_opc
    }
    
    // ------------------------------------------------------------------------- 

    constructor( gl : ContextoWebGL )
    {
        super( gl )
        
    }

    public async inicializar( sizex : number, sizey : number)
    {
        const fname : string = "CauceSombras.inicializar:"
        let gl = this.gl

        ComprErrorGL( gl, `${fname} al inicio`)

        this.objeto_programa = new ProgramObject( gl )

        this.objeto_programa.agregar( new VertexShaderObject( gl, null, src_vertex ) )
        this.objeto_programa.agregar( new FragmentShaderObject( gl, null, src_fragment ))
        
        await this.objeto_programa.compilarEnlazar()
        this.objeto_programa.usar()
        this.inicializarUniformsBase()

        this.fbo_opc = new FramebufferObject( this.gl, sizex, sizey )
        ComprErrorGL( gl, `${fname} al final`)
    }

    /**
     * 
     * @param gl Función que crea el cauce de sombras (asíncrono)
     * @param sizex number
     * @param sizey number
     * @returns promesa resuelta con el cauce de sombras nuevo
     */
    public static async crear( gl: ContextoWebGL, sizex : number, sizey : number ) : Promise<CauceSombras>
    {
        let cs = new CauceSombras(gl)
        await cs.inicializar( sizex, sizey )
        return cs 
    }

    // --------------------------------------------------------------------------

    public fijarDimensionesFBO( nuevo_tamX : number, nuevo_tamY : number ) : void
    {
        if ( nuevo_tamX != this.fbo.tamX || nuevo_tamY != this.fbo.tamY )
            this.fbo_opc = new FramebufferObject( this.gl, nuevo_tamX, nuevo_tamY )
    }
    // --------------------------------------------------------------------------

    /**
     * Activa el cauce y el FBO
     */
    public inicioRender()
    {
        let gl = this.gl 

        this.programa.usar()
        this.fbo.activar()

        gl.viewport( 0, 0, this.fbo.tamX, this.fbo.tamY )
        gl.clearColor( 0.0, 0.0, 0.0, 1.0 )
        gl.clear( gl.COLOR_BUFFER_BIT | gl.DEPTH_BUFFER_BIT )
    }
    // --------------------------------------------------------------------------


    /**
     * Desactiva el cauce y el FBO
     */
    public finRender()
    {
        this.gl.useProgram( null )
        this.gl.bindFramebuffer( this.gl.FRAMEBUFFER, null )
    }
    // --------------------------------------------------------------------------


}