## Uso del proyecto _Android Studio_


### Abrir el proyecto en AS 

Lanzar la aplicación _Android Studio_, y abrir el proyecto que está en esta carpeta 

``` 
proyecto.android-studio
``` 

Para abrir ese proyecto se puede seleccionar directamente de la lista de proyectos abiertos en el pasado, o bien (la primera vez) nseleccionar pulsar el botón _open_ y navegar hasta la carpeta que contiene `proyecto.android-studio` y seleccionarla.

### Edición

Los fuentes del proyecto se pueden editar con el editor de Android Studio, son archivos de extensión `.kt` (fuentes Kotlin). El editor señala todos los posibles errores, si bien a veces pueden ocurrir errores al ejecutar que no han sido señalados por el editor ni el compilador

### Compilación 

El proyecto de puede compilar usando el botón _build_ (es un icono de un martillo). Los posibles errores o warnings aparecerán en el panel correspondiente (el que tiene el símbolo de un martillo). Si hubiera errores en tiempo de ejecución, se ven en el panel _logcat_ (ver más abajo).

### Ejecución (en el simulador)

El proyecto se puede compilar y ejecutar con el botón _run_ (es un triángulo apuntando a la derecha, que aparece en la parte superior de la ventana, cuando la aplicación se puede ejecutar, aparecerá de color verde). Para terminar la ejecución, hay que pulsar el botón _stop_ (es un botón con un recuadro rojo que aparece a la derecha del botón _run_). 

Durante la ejecución los mensajes de _log_ (emitidos desde el código con la función `Log.v`) aparecerán en el panel _logcat_, que puede mostrarse pulsando en el botón con un icono de un gato. 

Durante la ejecución aparecerá a la derecha una imagen de la pantalla del dispositivo móvil, cuyo modelo es el que selecciones de entre los disponibles (por defecto es un Google Pixel 3A).

### Ejecución (en un dispositivo Android conectado al ordenador)

Pendiente.











