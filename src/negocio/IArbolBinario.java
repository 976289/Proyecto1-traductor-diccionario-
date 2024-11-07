
package negocio;

import java.util.List;


public interface IArbolBinario<K extends Comparable<K>, V> {
    void vaciar();
    boolean esArbolVacio();
    int size();
    int altura();
    int nivel();
    K minimo();
    K maximo();
    void insertar(K clave, V valor);
    void eliminar(K clave);
    boolean contiene(K clave);
    V buscar(K clave);
    List<K> recorridoEnInOrden();
    List<K> recorridoEnPosOrden();
    List<K> recorridoEnPreOrden();
    List<K> recorridoPorNiveles();

    public void cargarDatosDelTxt();
}
