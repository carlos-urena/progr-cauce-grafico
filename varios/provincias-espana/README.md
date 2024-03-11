Carpeta con un fuente python para analizar el SVG de las provincias de España, produce en el archivo de salida una línea por cada elemento 'polygon' del SVG. 
Crear el archivo de salida con 'make', requiere 'python3'

Archivo SVG 'provncias-espana.svg' fuente creado por Emilio Gómez Fernández y Javi CS, disponible aquí:

https://es.m.wikipedia.org/wiki/Archivo:Provincias_de_Espa%C3%B1a.svg

El anterior tiene ciertos defectos en los elementos 'polygon', me descargo 'provincias-espana-v2.svg' de aquí:

https://commons.wikimedia.org/wiki/File:Provinciasesp.svg

en el makefile se puede construir el v1 o el v2, por defecto se construye el v2 (ver makefile)
