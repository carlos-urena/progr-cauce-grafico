# El Cauce Gráfico en dispositivos móviles 

En esta carpeta se encuentra una subcarpeta (con extensión `.proyecto_AS` con los fuentes _Kotlin_ y demás archivos del proyecto de _Android Studio_ para la asignatura. En este README están las instrucciones para 

## Instalación de Android Studio 


Para instalar Android Studio se accede aquí: 

 
 [developer.android.com/studio](https://developer.android.com/studio)
 
 Hay versiones para Linux, Windows y macOS.

### Linux

 Los pasos siguientes de instalación se han probado en Linux (Ubuntu 22.04 LTS) con la versión <i>Android Studio Hedgehog</i> - 2023.1.1 Patch 2 para Linux.


Las instrucciones de instalación están aquí:

[developer.android.com/studio/install](https://developer.android.com/studio/install)

Ahí e dice que en Linux de 64 bits hay que instalar unas librerías adicionales. Para comprobar si Ubuntu es de 32 bits o no, se puede acceder a la ventana de configuración de Ubuntu y mirar, en la sección _Acerca de_ (al final), el tipo de SO (64 o 32 bits). En mi caso efectivamente se trata de un Ubuntu de 64 bits, así que es necesario hacer:

``` 
sudo apt-get install libc6:i386 libncurses5:i386 libstdc++6:i386 lib32z1 libbz2-1.0:i386
``` 

El siguiente paso es descargar de la Web el archivo con Android Studio, en Linux es un `.tar.gz`. Lo descargamos a una carpeta nueva vacía. En esa carpeta, extraemos el archivo. Se puede extraer con el administrador de archivos o en la línea de órdenes con:

```
tar zxvf archivo.tar.gz
```

Al abrirlo se crea una subcarpeta, de nombre `android-studio`, a su vez tiene una subcarpeta de nombre `bin`. Hacemos `cd` a la subcarpeta `bin` y lanzamos Android Studio con:

```
./studio.sh
```

Al lanzarlo la primera vez se ejecuta el _Setup Wizard_ para instalar el SDK de Android (conjunto de herramientas y librerías de desarrollo de Google para Android). Durante la instalación se pregunta:

  -  La carpeta de instalación (<i>config or installation directory</i>), lo dejo en blanco para que se instale en la ubicación por defecto, que será 
  en `~/Android/Sdk` 
  - Si se quiere hacer una instalación  _Standard_ o _Custom_, elijo _Standard_ 

En el recuadro para aceptar el acuerdo de licencia hay que pulsar _accept__ **para cada una**  de las componentes que se instalan (es decir, hay que pulsarlo varias veces). La instalación tarda un poco (son 5,5 Gb en Ubuntu).

Una vez que se ha completado la instalación, aparece un recuadro para abrir un proyecto o crear uno nuevo. En ese momento se puede salir de la aplicación y mover la carpeta `android-studio` a su ubicación definitiva, que puede ser, por ejemplo, dentro de la carpeta `~/Android`, junto con el SDK. 

Finalmente para lanzar la aplicación hay que volver a ejecutar el script `studio.sh`. Para poder hacer esto fácilmente desde la línea de órdenes, se puede definir un _alias_ de Linux (de nombre, por ejemplo, `studio`) que lance la aplicación.

### macOS

La instalación en el SO de Apple es similar a la de Linux, excepto que en este caso se decarga un archivo ¿`.dmg`?. Después de descargar, la aplicación Android Studio se debe mover a la carpeta `Aplicaciones`, como es habitual en macOs. En la primera ejecución se instalará el SDK, al igual que en Linux. Para lanzar el programa fácilmente, un _alias_, igul que en Linux.

### Windows 

Pendiente.

## Creación de un proyecto para OpenGL 

Aunque en esta asignatura los estudiantes abrirán un proyecto ya creado, en esta subcarpeta se dan detalles acerca de como se ha creado dicho proyecto: 

[Creacion del proyecto](README-crea.md)

## Uso del proyecto _Android Studio_


### Abrir el proyecto en AS 

Lanzar la aplicación _Android Studio_, y abrir el proyecto que está en esta carpeta 

``` 
proyecto.android-studio
``` 

Para abrir ese proyecto se puede seleccionar directamente de la lista de proyectos abiertos en el pasado, o bien (la primera vez) pulsar el botón _open_ y navegar hasta la carpeta que contiene `proyecto.android-studio` y seleccionarla.

### Edición

Los fuentes del proyecto se pueden editar con el editor de Android Studio, son archivos de extensión `.kt` (fuentes Kotlin). El editor señala todos los posibles errores, si bien a veces pueden ocurrir errores al ejecutar que no han sido señalados por el editor ni el compilador

### Compilación 

El proyecto de puede compilar usando el botón _build_ (es un icono de un martillo). Los posibles errores o warnings aparecerán en el panel correspondiente (el que tiene el símbolo de un martillo). Si hubiera errores en tiempo de ejecución, se ven en el panel _logcat_ (ver más abajo).

### Ejecución (en el simulador)

El proyecto se puede compilar y ejecutar con el botón _Run_, es un triángulo apuntando a la derecha, que aparece en la parte superior de la ventana, cuando la aplicación se puede ejecutar, aparecerá de color verde. Para terminar la ejecución, hay que pulsar el botón _Stop_, es un botón con un recuadro rojo que aparece a la derecha del botón _Run_. 

Durante la ejecución, los mensajes de _log_ (emitidos desde el código con la función `Log.v`) aparecerán en el panel _logcat_, que puede mostrarse pulsando en el botón con un icono de un gato. 

Durante la ejecución aparecerá a la derecha una imagen de la pantalla del dispositivo móvil, cuyo modelo es el que selecciones de entre los disponibles (por defecto es un Google Pixel 3A).

### Ejecución en un dispositivo Android 

Aquí están las instrucciones para ejecutar y depurar la aplicación usando un dispositivo Android conectado al ordenador (probado con un cable USB).
Estas instrucciones se han probado en un Google Pixel en Marzo de 2024. Se pueden consultar estas páginas del sitio Web _Android Developer_: 

[https://developer.android.com/studio/run/device](https://developer.android.com/studio/run/device)
[https://developer.android.com/codelabs/basic-android-kotlin-compose-connect-device](https://developer.android.com/codelabs/basic-android-kotlin-compose-connect-device)


A continuación los pasos que he dado:

**Añadir usuario al grupo _plugdev_** 

```
sudo usermod -aG plugdev $LOGNAME
``` 

Verificar que ha ido bien (el grupo `plugdev` debe estar en la salida de la orden `groups`, que lista los grupos del usuario)

**Añadir package de Linux con reglas _udev_**

```
sudo apt install android-sdk-platform-tools-common
```

(tener en cuenta el _sudo_, no aparece en la página de _Android Developer_)

**Habilitar ejecución y depuración en el dispositivo** 

En el dispositivo Android es necesario permitir que se ejecuten aplicaciones enviadas desde Android Studio, se hace con:

  1. Ir a _Ajustes_ /_Información del Teléfono_  en esa pantalla pulsar 7 veces sobre _Numero de compilación_
  2. Ir a  _Ajustes_/_Sistema_/_Opciones para desarrolladores_:  activar _usar opciones para desarrolladores_, activar _depuración USB_


**Ejecutar en el dispositivo**

Lanzar Android Studio, si el dispositivo físico no aparece ya seleccionado en la barra superior, seleccionarlo. 
A partir de este momento se puede ejecutar y parar la aplicación en el dispositivo, al tiempo que se pueden ver los mensajes de _log_ en la ventana _logcat_ de Android Studio en el ordenador.














