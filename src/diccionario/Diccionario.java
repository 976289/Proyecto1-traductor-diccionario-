
package diccionario;

import arboles.Excepciones.ExcepcionOrdenInvalido;
import java.util.Scanner;
import negocio.AVL;
import negocio.ArbolBinarioBusqueda;
import negocio.ArbolMVias;
import negocio.IArbolBinario;

public class Diccionario {

    /**
     * @param args the command line arguments
     * @throws arboles.Excepciones.ExcepcionOrdenInvalido
     */
    public static void main(String[] args) throws ExcepcionOrdenInvalido {

        IArbolBinario<String, String> arbolPrueba = null;
        Scanner entrada = new Scanner(System.in);
        System.out.println("elija un tipo de arbol (ABB, AVL, AMV)");
        String tipoEntrada = entrada.next();
        switch (tipoEntrada) {
            case "ABB":
                arbolPrueba = new ArbolBinarioBusqueda<String, String>();
               
                break;
            case "AVL":
                arbolPrueba = new AVL<String, String>();
                break;
            case "AMV":
                arbolPrueba = new ArbolMVias<String, String>(5);
                break;
            default:
                System.out.println("Eror tipo de arbol");
                break;
        }
       arbolPrueba.cargarDatosDelTxt();
      //  arbolPrueba.insertar(Al, tipoEntrada);
       // arbolPrueba.insertar(Alex, tipoEntrada);
       // arbolPrueba.insertar(dd, tipoEntrada);
        System.out.println(arbolPrueba.recorridoEnInOrden());
          System.out.println(arbolPrueba.recorridoPorNiveles());
    }

}
