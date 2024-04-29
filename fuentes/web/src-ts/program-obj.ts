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
    private program_object   : WebGLProgram | null = null // objeto programa encapsulado (null si todavía no compilado)
    
    // --------------------------------------------------------------------------------------------

    constructor( gl : ContextoWebGL )
    {
        const nombref : string = "ProgramObject.constructor:"
        this.gl = gl
    }
    // --------------------------------------------------------------------------------------------
    
    public get program() : WebGLProgram
    {
        if ( this.program_object == null )
            throw new Error(`no se puede leer el programa, es nulo (no se ha compilado todavía)`)
        return this.program_object
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
    public usar() : void
    {
        const nombref : string = "ProgramObject.usar:"
        let gl = this.gl

        if ( this.program_object == null )
            throw new Error(`${nombref} no se ha compilado y enlazado el objeto programa webgl`)
        
        gl.useProgram( this.program_object )
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

        if ( this.program_object != null )
            return 

        if ( this.fragment_shader == null )
            throw new Error(`${nombref} no hay fragment shaders agregados al objeto programa`)
        if ( this.vertex_shader == null  )
            throw new Error(`${nombref} no hay vertex shaders agregados al objeto programa`)

        let gl = this.gl
        
        // compilar los shaders        
        
        ComprErrorGL( gl, `${nombref} error OpenGL al inicio`)
    

        // Compilar los dos shaders (si no lo estaban ya)

        this.fragment_shader.compilar()
        this.vertex_shader.compilar()
        Log(`${nombref} shaders compilados ok.`)

        // Crear el objeto programa 
        this.program_object = gl.createProgram()
        if ( this.program_object == null )
            throw Error(`${nombref} no se ha podido crear el objeto programa`)
        Log(`${nombref} objeto programa webgl creado`)
        
        // Adjuntarle los shaders al objeto programa
        gl.attachShader( this.program_object, this.fragment_shader.shader_wgl )
        gl.attachShader( this.program_object, this.vertex_shader.shader_wgl )
        Log(`${nombref} shaders adjuntados al programa`)

        // Asociar los índices de atributos con las correspondientes variables de entrada ("in")
        // del vertex shader (hay que hacerlo antes de enlazar)
        // (esto es necesario para asegurarnos que conocemos el índice de cada atributo específico)
        // ¿ver si se puede quitar? -- ¿fuerza a que el cauce tenga estos atribs ?
        
        ComprErrorGL( gl, `antes de bind de atributos`)
        Assert( Cauce.numero_atributos >= 4, `${nombref} el cauce no gestiona al menos 4 atributos`)
        gl.bindAttribLocation( this.program_object, Cauce.indice_atributo.posicion,    "in_posicion_occ" )
        gl.bindAttribLocation( this.program_object, Cauce.indice_atributo.color,       "in_color" )
        gl.bindAttribLocation( this.program_object, Cauce.indice_atributo.normal,      "in_normal_occ" )
        gl.bindAttribLocation( this.program_object, Cauce.indice_atributo.coords_text, "in_coords_textura" )
        ComprErrorGL( gl, `después de bind de atributos`)
        
        
        // enlazar programa y ver errores
        gl.linkProgram( this.program_object )

        if ( ! gl.getProgramParameter( this.program_object, gl.LINK_STATUS) ) 
        {
            const info = gl.getProgramInfoLog( this.program_object )
            console.log(`Se han producido errores al ENLAZAR. Mensajes: \n\n${info}`)
                throw new Error(`${nombref} Se han producido errores al ENLAZAR. Mensajes: \n\n${info}`);
        }

        if ( ! gl.isProgram( this.program_object ))
        {
            console.log(`${nombref} no se ha creado un objeto programa válido`)
            throw new Error(`${nombref} no se ha creado un objeto programa válido`);
        }
        
        ComprErrorGL( gl, `${nombref} error OpenGL al final`)
        Log(`${nombref} programa compilado y enlazado ok.`)
    }


    
    // --------------------------------------------------------------------------------------------

}