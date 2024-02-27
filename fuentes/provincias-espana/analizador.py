import xml.etree.ElementTree as ET

input_file = 'provincias-espana.svg'
output_file = 'provincias-espana.txt'
try: 
    tree = ET.parse( input_file )
except:    
    ## error de tipo  xml.etree.ElementTree.ParseError (ver: https://docs.python.org/3/library/xml.etree.elementtree.html#exceptions)
    print("Error al analizar el fichero:",input_file)
    exit()


root = tree.getroot() 
print( "Árbol leído sin errores, root.tag:",root.tag)

if ( root.tag != "{http://www.w3.org/2000/svg}svg"):
    print( "El fichero no es un svg:", input_file )
    exit() 

min_x = 0.0
max_x = 0.0

min_y = 0.0
max_y = 0.0

n = 0

try:
    file = open( output_file, 'w') 
except:
    print("Error al crear el fichero de salida:", output_file )
    exit() 

for polygon_elem in root.iter('{http://www.w3.org/2000/svg}polygon'):

    n      = n+1
    id     = polygon_elem.get('id')
    points = polygon_elem.get('points').replace(",", " ")
            
    #print("-- polygon encontrado, id ==",id,"(número",n,"))")
    #print( points )
    
    # Write points string to a new line in the output text file
    file.write(points + '\n')
    
    # Create an array of float values
    points_array = [float(x) for x in points.split()]
    
    # Check if the number of floats in points is even
    if len(points_array) % 2 != 0:
        print("Error: el número de valores flotantes en el array no es par")
        exit()

    #print("    points array == ")
    #print( points_array )
    
    # Compute the min and max value of every float in an even position in the points_array
    min_value = min(points_array[::2])
    max_value = max(points_array[::2])

    #print("Minimum value (even positions):", min_value)
    #print("Maximum value (even positions):", max_value)
    
    # Compute the min and max value of every float in an odd position in the points_array
    min_value_odd = min(points_array[1::2])
    max_value_odd = max(points_array[1::2])

    #print("Minimum value (odd positions):", min_value_odd)
    #print("Maximum value (odd positions):", max_value_odd)

    if ( n == 1 or min_value < min_x ): 
        min_x = min_value
    if ( n == 1 or max_value > max_x ): 
        max_x = max_value
    if ( n == 1 or min_value_odd < min_y ):
        min_y = min_value_odd
    if ( n == 1 or max_value_odd > max_y ):
        max_y = max_value_odd
    
    #print("-- fin polygon")

print("Archivo de salida creado:",output_file)
print("Número de polígonos encontrados:",n)
print("Rango X:",min_x,"-",max_x)
print("Rango Y:",min_y,"-",max_y)


