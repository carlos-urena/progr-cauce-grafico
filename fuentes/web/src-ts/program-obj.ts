import { Assert, ComprErrorGL, LeerArchivoTexto, Log, ContextoWebGL } from "./utilidades.js"
import { ShaderObject, FragmentShaderObject, VertexShaderObject } from "./shader-obj.js"
import { Cauce } from "./cauce.js"

/**
 * Clase que encapsula un "program object"
 */
export class ProgramObject 
{
    private fragment_shader  : ShaderObject | null = null
    private vertex_shader    : ShaderObject | null = null 
    private gl               : ContextoWebGL      // contexto usado para crear este programa
    private programa_wgl_act   : WebGLProgram | null = null // objeto programa encapsulado (null si todavía no compilado)
    
    // --------------------------------------------------------------------------------------------

    constructor( gl : ContextoWebGL )
    {
        const nombref : string = "ProgramObject.constructor:"
        this.gl = gl
    }
    // --------------------------------------------------------------------------------------------
    
    public get programa_wgl() : WebGLProgram
    {
        if ( this.programa_wgl_act == null )
            throw new Error(`no se puede leer el programa, es nulo (no se ha compilado todavía)`)
        return this.programa_wgl_act
    }
    // --------------------------------------------------------------------------------------------
    public leerLocation( nombre: string ) : WebGLUniformLocation | null 
    {
        const nombref : string = 'ObjetoPrograma.leerLocation:'
        return this.gl.getUniformLocation( this.programa_wgl, nombre )
    }
    // --------------------------------------------------------------------------------------------

    public get fragment() : FragmentShaderObject
    {
        let nombref = "ProgramObject.fragment:"
        if ( this.fragment_shader == null )
            throw new Error(`no se puede leer el fragment shader, es nulo (no se ha agregado todavía)`)
        return this.fragment_shader
    }
    // --------------------------------------------------------------------------------------------

    public get vertex() : VertexShaderObject
    {
        let nombref = "ProgramObject.vertex:"
        if ( this.vertex_shader == null )
            throw new Error(`no se puede leer el vertex shader, es nulo (no se ha agregado todavía)`)
        return this.vertex_shader
    }
    // --------------------------------------------------------------------------------------------
    
    /**
     * Ejecute 'UseProgram' para usar este objeto programa 
     * (si está creado, en otro caso se produce un error)
     */
    public usar() : void
    {
        const nombref : string = "ProgramObject.usar:"
        this.gl.useProgram( this.programa_wgl )
    }
    // --------------------------------------------------------------------------------------------

    /**
     * Agrega un shader al objeto programa. Si ya hay un shader del mismo tipo se lanza una excepción.
     * @param shader shader a agregar
     */
    public agregar( shader : FragmentShaderObject | VertexShaderObject ) 
    {
        const nombref : string = "ProgramObject.agregar:"
        
        if ( shader instanceof FragmentShaderObject )
        {
            if ( this.fragment_shader != null )
                throw new Error(`${nombref} ya hay un fragment shader agregado`)
            this.fragment_shader = shader 
        }
        else if ( shader instanceof VertexShaderObject )
        {
            if ( this.vertex_shader != null )
                throw new Error(`${nombref} ya hay un vertex shader agregado`)
            this.vertex_shader = shader 
        }
    }
    // --------------------------------------------------------------------------------------------
    
    /**
     *  Compila los shaders (si no lo estaban ya) y enlaza el objeto programa 
     * (si no estaba ya enlazado).
     */
    public async compilarEnlazar() : Promise<void>
    {
        const nombref : string = 'ProgramObject.crearObjetoPrograma:'

        Log(`${nombref} inicio`)

        if ( this.programa_wgl_act != null )
            return 

        if ( this.fragment_shader == null )
            throw new Error(`${nombref} no hay fragment shaders agregados al objeto programa`)
        if ( this.vertex_shader == null  )
            throw new Error(`${nombref} no hay vertex shaders agregados al objeto programa`)

        let gl = this.gl
        
        // compilar los shaders        
        
        ComprErrorGL( gl, `${nombref} error OpenGL al inicio`)
    

        // Compilar los dos shaders (si no lo estaban ya)

        await this.fragment_shader.compilar()
        await this.vertex_shader.compilar()
        Log(`${nombref} shaders compilados ok.`)

        // Crear el objeto programa 
        this.programa_wgl_act = gl.createProgram()
        if ( this.programa_wgl_act == null )
            throw Error(`${nombref} no se ha podido crear el objeto programa`)
        Log(`${nombref} objeto programa webgl creado`)
        
        // Adjuntarle los shaders al objeto programa
        gl.attachShader( this.programa_wgl_act, this.fragment_shader.shader_wgl )
        gl.attachShader( this.programa_wgl_act, this.vertex_shader.shader_wgl )
        Log(`${nombref} shaders adjuntados al programa`)

        // enlazar programa y ver errores
        gl.linkProgram( this.programa_wgl_act )

        if ( ! gl.getProgramParameter( this.programa_wgl_act, gl.LINK_STATUS) ) 
        {
            const info = gl.getProgramInfoLog( this.programa_wgl_act )
            console.log(`Se han producido errores al ENLAZAR. Mensajes: \n\n${info}`)
                throw new Error(`${nombref} Se han producido errores al ENLAZAR. Mensajes: \n\n${info}`);
        }

        if ( ! gl.isProgram( this.programa_wgl_act ))
        {
            console.log(`${nombref} no se ha creado un objeto programa válido`)
            throw new Error(`${nombref} no se ha creado un objeto programa válido`);
        }
        
        ComprErrorGL( gl, `${nombref} error OpenGL al final`)
        Log(`${nombref} programa compilado y enlazado ok.`)
    }

    // --------------------------------------------------------------------------------------------

}