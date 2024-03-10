# Edición, compilación y ejecución del proyecto Android Studio.


Para ejecutar Android Studio con lanzar dicha aplicación y abrir el proyecto que está en esta carpeta 

``` 
App_Android_PCG_v1.proyecto_AS
``` 

Para abrir ese proyecto se puede seleccionar de la lista de proyectos abiertos en el pasado, o bien (la primera vez) navegar hasta la carpeta que contiene esta carpeta `.proyecto_AS` y seleccionarla.

## Edición

Los fuentes del proyecto se pueden editar con el editor de Android Studio, son archivos de extensión `.kt` (fuentes Kotlin). El editor señala todos los posibles errores, si bien a veces pueden ocurrir errores al ejecutar que no han sido señalados por el editor ni el compilador

## Compilación 

El proyecto de puede compilar usando el botón _build_ (es un icono de un martillo). Los posibles errores o warnings aparecerán en el panel correspondiente (el que tiene el símbolo de un martillo). Si hubiera errores en tiempo de ejecución, se ven en el panel _logcat_ (ver más abajo).

## Ejecución (en el simulador)

El proyecto se puede compilar y ejecutar con el botón _run_ (es un triángulo verde apuntando a la derecha, que aparece en la parte superior de la ventana). Para terminar la ejecución, hay que pulsar el botón del recuadro rojo. Durante la ejecución los mensajes de _log_ (emitidos desde el código con la función `Log.v`) aparecerán en el panel _logcat_, que puede mostrarse pulsando en el botón con un icono de un gato. 

Durante la ejecución aparecerá a la derecha una imagen de la pantalla del dispositivo móvil, cuyo modelo es el que selecciones de entre los disponibles (por defecto es un Google Pixel 3A).

## Ejecución (en un dispositivo Android conectado al ordenador)

Pendiente.











