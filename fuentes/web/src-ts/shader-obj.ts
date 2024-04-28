import { Assert, ComprErrorGL, LeerArchivoTexto, Log } from "./utilidades.js"

type WGLContext = WebGLRenderingContext | WebGL2RenderingContext 

class ShaderObject 
{

    private tipo_shader    : GLenum 
    private url_fuente     : string
    private texto_fuente   : string 
    private gl             : WGLContext
    private nombre_fuente  : string
    private shader_object  : WebGLShader | null = null

    getObject() : WebGLShader 
    {
        if ( this.shader_object == null )
            throw new Error(`no se puede leer el shader object, es nulo`)
        return this.shader_object 
    }

    /**
     * Compilar un shader y, si va bien, adjuntarlo al objeto programa. Si hay errores se lanza 
     * una excepción cuyo texto tiene el log de errores.
     * 
     * @param gl   objeto de tipo WebGLRenderingContext o WebGL2RenderingContext)
     * @param tipo_shader  uno de: gl.FRAGMENT_SHADER, gl.VERTEX_SHADER, 
     * @param url_fuente (string) nombre del archivo que contenía el texto fuente
     * @param texto_fuente texto fuente del shader.
     */

    constructor( gl : WGLContext, tipo_shader : GLenum, url_fuente : string, texto_fuente : string )
    {
        const nombref : string = "ShaderObject.constructor:"

        // comprobar precondiciones
        Assert( tipo_shader == gl.VERTEX_SHADER || tipo_shader == gl.FRAGMENT_SHADER, 
                 `${nombref} valor de 'tipo_shader' es incorrecto` ) 

        if ( url_fuente != ""  && texto_fuente != "" )
            throw Error( `${nombref} 'url_fuente' y 'texto_fuente' no pueden ser ambos no vacíos`)
        if ( url_fuente == ""  && texto_fuente == "" )
            throw Error( `${nombref} 'url_fuente' y 'texto_fuente' no pueden ser ambos vacios`)

        // guardar datos
        this.gl           = gl 
        this.tipo_shader  = tipo_shader 
        this.url_fuente   = url_fuente 
        this.texto_fuente = texto_fuente 
        this.nombre_fuente = this.url_fuente != "" ? `'${this.url_fuente}'` : "cadena de texto"
    }

    /**
     * Compilar un shader y, si va bien, adjuntarlo al objeto programa. Si hay errores se lanza 
     * una excepción cuyo texto tiene el log de errores.
     * 
     
     */
    public async compilar(   ) 
    {
        const nombref : string = "Cauce.compilar:"
        let gl = this.gl 

        // comprobar precondiciones
        ComprErrorGL( gl, `${nombref} error OpenGL al inicio`)

        // si el textto fuente no ha sido cargado todavía, hacerlo ahora.
        if ( this.texto_fuente == "" )
            this.texto_fuente = await LeerArchivoTexto( this.url_fuente )
        
        

        // crear y compilar el shader
        this.shader_object = gl.createShader( this.tipo_shader )
        if ( this.shader_object == null )
                throw Error(`${nombref} no se ha podido crear el objeto shader`)

        gl.shaderSource( this.shader_object, this.texto_fuente )
        gl.compileShader( this.shader_object )

        // si ha habido error, lanzar error
        if ( ! gl.getShaderParameter( this.shader_object, gl.COMPILE_STATUS) ) 
        {
            const info = gl.getShaderInfoLog( this.shader_object )
            console.log(`${nombref} mensajes de error al compilar ${this.nombre_fuente} : \n\n${info}`)
            throw new Error(`${nombref} se han producido errores al COMPILAR (ver arriba)`);
        }

        ComprErrorGL( gl, `${nombref} error OpenGL al final`)
        Log(`${nombref} shader en ${this.nombre_fuente} compilado ok.`) 
    }

    /**
     * Compilar y adjuntar un shader y, si va bien, adjuntarlo al objeto programa. Si hay errores se lanza 
     * una excepción cuyo texto tiene el log de errores.
     * 
     * @param programa
     */
    public async compilarAdjuntarShader(  programa : WebGLProgram ) 
    {
        const nombref : string = "Cauce.compilarAdjuntarShader:"

        if ( this.shader_object == null )
            throw new Error(`${nombref} - el shader object es null`)
        
        this.compilar()
        this.gl.attachShader( programa, this.shader_object )
        ComprErrorGL( this.gl, `${nombref} error OpenGL al final`)
        Log(`${nombref} shader en '${this.nombre_fuente} adjuntado ok.`)
        
    }
}