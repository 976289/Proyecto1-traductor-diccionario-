package negocio;

import arboles.Excepciones.ExcepcionOrdenInvalido;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class ArbolMVias< K extends Comparable<K>, V>
        implements IArbolBinario<K, V> {

    protected NodoMVias<K, V> raiz;
    protected int orden;
    protected int POSICION_INVALIDA = -1;
    private static final String UBICACION_ARCHIVO = "src\\Datos\\Diccionario.txt";

    @Override
    public void cargarDatosDelTxt() {
        try {

            BufferedReader lector = new BufferedReader(new FileReader(UBICACION_ARCHIVO));

            String linea;
            int i = 0;
            while ((linea = lector.readLine()) != null) {
                String[] palabras = linea.split("-");
                K clave = (K) palabras[0].trim();
                V valor = (V) palabras[1].trim();
                this.insertar(clave, valor);
            }

            lector.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArbolMVias() {
        this.orden = 3;
    }

    public ArbolMVias(int orden) throws ExcepcionOrdenInvalido {
        if (orden < 3) {
            throw new ExcepcionOrdenInvalido();
        }
        this.orden = orden;
    }

    @Override
    public void vaciar() {
        this.raiz = NodoMVias.nodoVacio();
    }

    @Override
    public boolean esArbolVacio() {
        return NodoMVias.esNodoVacio(raiz);
    }

    @Override
    public int size() {
        if (this.esArbolVacio()) {
            return 0;
        }
        Queue<NodoMVias<K, V>> colaNodos = new LinkedList<>();
        colaNodos.add(raiz);
        int cantidad = 0;
        while (!colaNodos.isEmpty()) {
            NodoMVias<K, V> nodoActual = colaNodos.poll();
            cantidad++;
            for (int i = 0; i < nodoActual.cantidadDeClavesNoVacios(); i++) {

                if (!nodoActual.esHijoVacio(i)) {
                    colaNodos.offer(nodoActual.getHijo(i));
                }

            }
            if (!nodoActual.esHijoVacio(nodoActual.cantidadDeClavesNoVacios())) {

                colaNodos.offer(nodoActual.getHijo(nodoActual.cantidadDeClavesNoVacios()));
            }
        }
        return cantidad;
    }

    @Override
    public int altura() {
        return altura(raiz);
    }

    protected int altura(NodoMVias<K, V> nodoActual) {
        if (NodoMVias.esNodoVacio(nodoActual)) {
            return 0;
        }
        int alturaMayor = 0;
        for (int i = 0; i < orden; i++) {
            int alturaHijo = altura(nodoActual.getHijo(i));
            if (alturaHijo > alturaMayor) {
                alturaMayor = alturaHijo;
            }
        }
        return alturaMayor + 1;
    }

    @Override
    public int nivel() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public K minimo() {
        if (this.esArbolVacio()) {
            return null;
        }
        NodoMVias<K, V> nodoActual = this.raiz;
        NodoMVias<K, V> nodoAnterior = nodoActual;
        while (!NodoMVias.esNodoVacio(nodoActual)) {
            nodoAnterior = nodoActual;
            nodoActual = nodoActual.getHijo(0);
        }
        return nodoAnterior.getClave(0);
    }

    @Override
    public K maximo() {
        if (this.esArbolVacio()) {
            return null;
        }
        NodoMVias<K, V> nodoActual = this.raiz;
        NodoMVias<K, V> nodoAnterior = nodoActual;
        while (!NodoMVias.esNodoVacio(nodoActual)) {
            nodoAnterior = nodoActual;
            nodoActual = nodoActual.getHijo(nodoActual.cantidadDeClavesNoVacios());
        }
        return nodoAnterior.getClave(nodoAnterior.cantidadDeClavesNoVacios());
    }

    @Override
    public void insertar(K claveAInsertar, V valorAInsertar) {
        if (claveAInsertar == null) {
            throw new RuntimeException("No se permite claves nulos");
        }
        if (valorAInsertar == null) {
            throw new RuntimeException("No se permite valores nulos");
        }
        if (this.esArbolVacio()) {
            this.raiz = new NodoMVias<>(this.orden, claveAInsertar, valorAInsertar);
            return;
        }
        NodoMVias<K, V> nodoActual = this.raiz;
        while (!NodoMVias.esNodoVacio(nodoActual)) {
            int posicionDeClave = this.existeClaveEnNodo(nodoActual, claveAInsertar);
            if (posicionDeClave != POSICION_INVALIDA) {
                nodoActual.setValor(posicionDeClave, valorAInsertar);
                nodoActual = NodoMVias.nodoVacio();
            } else {//insertar en un nodo hoja
                if (nodoActual.esHoja()) {
                    if (nodoActual.estanClavesLenas()) {
                        int posicionPorDondeBajar = this.posicionDondeBajar(nodoActual, claveAInsertar);
                        NodoMVias<K, V> nuevoHijo = new NodoMVias<>(this.orden, claveAInsertar, valorAInsertar);
                        nodoActual.setHijo(posicionPorDondeBajar, nuevoHijo);
                    } else {
                        this.insertarDatosEnOrden(nodoActual, claveAInsertar, valorAInsertar);
                    }
                    nodoActual = NodoMVias.nodoVacio();
                } else { //si no es hoja
                    int posicionPorDondeBajar = this.posicionDondeBajar(nodoActual, claveAInsertar);
                    if (nodoActual.esHijoVacio(posicionPorDondeBajar)) {
                        NodoMVias<K, V> nuevoHijo = new NodoMVias<>(this.orden, claveAInsertar, valorAInsertar);
                        nodoActual.setHijo(posicionPorDondeBajar, nuevoHijo);
                        nodoActual = NodoMVias.nodoVacio();
                    } else { // si donde bajamos existe nodo actualizamos nodoActual
                        nodoActual = nodoActual.getHijo(posicionPorDondeBajar);
                    }
                }
            }
        }
    }

    protected void insertarDatosEnOrden(NodoMVias<K, V> nodoActual, K claveAInsertar, V valorAInsertar) {
        int posicionAInsertar = this.posicionDondeBajar(nodoActual, claveAInsertar);
        for (int i = nodoActual.cantidadDeClavesNoVacios(); i > posicionAInsertar; i--) {
            nodoActual.setClave(i, nodoActual.getClave(i - 1));
            nodoActual.setValor(i, nodoActual.getValor(i - 1));
            //a probar
            nodoActual.setHijo(i + 1, nodoActual.getHijo(i));
        }
        nodoActual.setClave(posicionAInsertar, claveAInsertar);
        nodoActual.setValor(posicionAInsertar, valorAInsertar);
    }

    protected int posicionDondeBajar(NodoMVias<K, V> nodoActual, K claveABuscar) {

        for (int i = 0; i < nodoActual.cantidadDeClavesNoVacios(); i++) {
            K claveActual = nodoActual.getClave(i);
            if (claveABuscar.compareTo(claveActual) < 0) {
                return i;
            }
        }

        return nodoActual.cantidadDeClavesNoVacios();

    }

    protected int existeClaveEnNodo(NodoMVias<K, V> nodoActual, K claveABuscar) {
        for (int i = 0; i < nodoActual.cantidadDeClavesNoVacios(); i++) {
            K claveActual = nodoActual.getClave(i);
            if (claveABuscar.compareTo(claveActual) == 0) {
                return i;
            }
        }
        return POSICION_INVALIDA;
    }

    @Override
    public void eliminar(K clave) {
        if (clave == null) {
            throw new IllegalArgumentException("Clave a eliminar no puede ser nula");
        }
        this.raiz = eliminar(this.raiz, clave);
    }

    private NodoMVias<K, V> eliminar(NodoMVias<K, V> nodoActual, K claveAEliminar) {
        for (int i = 0; i < nodoActual.cantidadDeClavesNoVacios(); i++) {
            K claveActual = nodoActual.getClave(i);
            if (claveAEliminar.compareTo(claveActual) == 0) {
                if (nodoActual.esHoja()) { //si la clave se encuentra en un nodoHoja
                    this.eliminarClaveYValorDelNodo(nodoActual, i);
                    if (nodoActual.cantidadDeClavesNoVacios() == 0) {
                        return NodoMVias.nodoVacio();
                    }
                    return nodoActual;
                }//si no es hoja 
                K claveDeReemplazo;
                if (this.hayHijosMasAdelante(nodoActual, i)) {
                    claveDeReemplazo = this.buscarClaveSucesoraInOrden(nodoActual, claveAEliminar);
                } else {
                    claveDeReemplazo = this.buscarClavePredecesoraInOrden(nodoActual, claveAEliminar);
                }
                V valorDeReemplazo = this.buscar(claveDeReemplazo);
                nodoActual = eliminar(nodoActual, claveDeReemplazo);
                nodoActual.setClave(i, claveDeReemplazo);
                nodoActual.setValor(i, valorDeReemplazo);
                return nodoActual;
            }
            //si no esta en la posicion i bajamos
            if (claveAEliminar.compareTo(claveActual) < 0) {
                NodoMVias<K, V> supuestoNuevoHijo = this.eliminar(nodoActual.getHijo(i), claveAEliminar);
                nodoActual.setHijo(i, supuestoNuevoHijo);
                return nodoActual;
            }
        }
        //bajar al ultimo hijo 
        NodoMVias<K, V> supuestoNuevoHijo = this.eliminar(nodoActual.getHijo(nodoActual.cantidadDeClavesNoVacios()), claveAEliminar);
        nodoActual.setHijo(nodoActual.cantidadDeClavesNoVacios(), supuestoNuevoHijo);
        return nodoActual;
    }

    protected void eliminarClaveYValorDelNodo(NodoMVias<K, V> nodoActual, int posicionAEliminar) {
        for (int i = posicionAEliminar; i < nodoActual.cantidadDeClavesNoVacios() - 1; i++) {
            nodoActual.setClave(i, nodoActual.getClave(i + 1));
            nodoActual.setValor(i, nodoActual.getValor(i + 1));
        }
        int n = nodoActual.cantidadDeClavesNoVacios() - 1;
        nodoActual.setClave(n, (K) NodoMVias.datoVacio());
        nodoActual.setValor(n, (V) NodoMVias.datoVacio());
    }

    //metodo pa casa 
    private boolean hayHijosMasAdelante(NodoMVias<K, V> nodoActual, int posicion) {//dudas
        for (int i = posicion; i < nodoActual.cantidadDeClavesNoVacios(); i++) {
            if (!nodoActual.esHijoVacio(i)) {
                return true;
            }
        }
        if (!nodoActual.esHijoVacio(nodoActual.cantidadDeClavesNoVacios())) {
            return true;
        }
        return false;
    }

    //metodo pa casa
    private K buscarClaveSucesoraInOrden(NodoMVias<K, V> nodoActual, K claveABuscar) {
        /*K claveSucesora = (K) NodoMVias.datoVacio();
        int posicion = this.obtenerPosicionClaveEnNodo(nodoActual, claveABuscar);
        if (nodoActual.esHijoVacio(posicion + 1)) {
            claveSucesora = nodoActual.getClave(posicion + 1);
            return claveSucesora;
        } else {
            nodoActual = nodoActual.getHijo(posicion + 1);
            while (!nodoActual.esHijoVacio(0)) {
                nodoActual = nodoActual.getHijo(0);
            }
            claveSucesora = nodoActual.getClave(0);
        }
        return claveSucesora;*/
        List<K> listaDeClavesInOrden = this.recorridoEnInOrden();
        K claveDeRetorno = (K) NodoMVias.datoVacio();
        for (int i = 0; i < listaDeClavesInOrden.size(); i++) {
            if (claveABuscar.compareTo(listaDeClavesInOrden.get(i)) == 0) {
                claveDeRetorno = listaDeClavesInOrden.get(i + 1);
                break;
            }
        }
        return claveDeRetorno;
    }

    //metodo pa casa
    private K buscarClavePredecesoraInOrden(NodoMVias<K, V> nodoActual, K claveABuscar) {
        /*K claveSucesora = (K) NodoMVias.datoVacio();
        int posicion = this.obtenerPosicionClaveEnNodo(nodoActual, claveABuscar);
        if (nodoActual.esHijoVacio(posicion)) {
            claveSucesora = nodoActual.getClave(posicion - 1);
            return claveSucesora;
        } else {
            nodoActual = nodoActual.getHijo(posicion);
            while (!nodoActual.esHijoVacio(nodoActual.cantidadDeClavesNoVacios())) {
                nodoActual = nodoActual.getHijo(nodoActual.cantidadDeClavesNoVacios());
            }
            claveSucesora = nodoActual.getClave(nodoActual.cantidadDeClavesNoVacios());
        }
        return claveSucesora;*/
        List<K> listaDeClavesInOrden = this.recorridoEnInOrden();
        K claveDeRetorno = (K) NodoMVias.datoVacio();
        for (int i = 0; i < listaDeClavesInOrden.size(); i++) {
            if (claveABuscar.compareTo(listaDeClavesInOrden.get(i)) == 0) {
                claveDeRetorno = listaDeClavesInOrden.get(i - 1);
                break;
            }
        }
        return claveDeRetorno;
    }

    protected int obtenerPosicionClaveEnNodo(NodoMVias<K, V> nodoActual, K claveABuscar) {
        for (int i = 0; i < nodoActual.cantidadDeClavesNoVacios(); i++) {
            K claveActual = nodoActual.getClave(i);
            if (claveABuscar.compareTo(claveActual) == 0) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public boolean contiene(K clave) {
        return buscar(clave) != null;
    }

    @Override
    public V buscar(K claveABuscar) {
        NodoMVias<K, V> nodoActual = this.raiz;
        while (!NodoMVias.esNodoVacio(nodoActual)) {
            boolean huboCambioNodoActual = false;
            for (int i = 0; i < nodoActual.cantidadDeClavesNoVacios()
                    && !huboCambioNodoActual; i++) {
                K claveActual = nodoActual.getClave(i);
                if (claveActual.compareTo(claveABuscar) == 0) {
                    return nodoActual.getValor(i);
                }
                if (claveABuscar.compareTo(claveActual) < 0) {
                    if (nodoActual.esHijoVacio(i)) {
                        return (V) NodoMVias.datoVacio();
                    }
                    nodoActual = nodoActual.getHijo(i);
                    huboCambioNodoActual = true;
                }

            }
            if (!huboCambioNodoActual) {
                nodoActual = nodoActual.getHijo(nodoActual.cantidadDeClavesNoVacios());
            }
        }
        return (V) NodoMVias.datoVacio();
    }

    @Override
    public List<K> recorridoEnInOrden() {
        List<K> recorrido = new ArrayList<>();
        recorridoEnInOrden(raiz, recorrido);
        return recorrido;
    }

    private void recorridoEnInOrden(NodoMVias<K, V> nodoActual, List<K> recorrido) {
        if (NodoMVias.esNodoVacio(nodoActual)) {
            return;
        }
        for (int i = 0; i < nodoActual.cantidadDeClavesNoVacios(); i++) {
            recorridoEnInOrden(nodoActual.getHijo(i), recorrido);
            recorrido.add(nodoActual.getClave(0));
        }
        recorridoEnInOrden(nodoActual.getHijo(nodoActual.cantidadDeClavesNoVacios()), recorrido);

    }

    @Override
    public List<K> recorridoEnPosOrden() {
        List<K> recorrido = new ArrayList<>();
        recorridoEnPosOrden(raiz, recorrido);
        return recorrido;
    }

    private void recorridoEnPosOrden(NodoMVias<K, V> nodoActual, List<K> recorrido) {
        if (NodoMVias.esNodoVacio(nodoActual)) {
            return;
        }
        recorridoEnPosOrden(nodoActual.getHijo(0), recorrido);
        for (int i = 0; i < nodoActual.cantidadDeClavesNoVacios(); i++) {
            recorridoEnPosOrden(nodoActual.getHijo(i + 1), recorrido);
            recorrido.add(nodoActual.getClave(i));
        }

    }

    @Override
    public List<K> recorridoEnPreOrden() {
        List<K> recorrido = new ArrayList<>();
        recorridoEnPreOrden(raiz, recorrido);
        return recorrido;
    }

    void recorridoEnPreOrden(NodoMVias<K, V> nodoActual, List<K> recorrido) {
        if (NodoMVias.esNodoVacio(nodoActual)) {
            return;
        }
        for (int i = 0; i < nodoActual.cantidadDeClavesNoVacios(); i++) {
            recorrido.add(nodoActual.getClave(i));
            recorridoEnPreOrden(nodoActual.getHijo(i), recorrido);
        }
        recorridoEnPreOrden(nodoActual.getHijo(nodoActual.cantidadDeClavesNoVacios()), recorrido);
    }

    @Override
    public List<K> recorridoPorNiveles() {
        List<K> listaElementos = new ArrayList<>();
        if (this.esArbolVacio()) {
            return listaElementos;
        }
        Queue<NodoMVias<K, V>> colaNodos = new LinkedList<>();
        colaNodos.add(raiz);
        while (!colaNodos.isEmpty()) {
            NodoMVias<K, V> nodoActual = colaNodos.poll();
            for (int i = 0; i < nodoActual.cantidadDeClavesNoVacios(); i++) {
                listaElementos.add(nodoActual.getClave(i));

                if (!nodoActual.esHijoVacio(i)) {
                    colaNodos.offer(nodoActual.getHijo(i));
                }

            }
            if (!nodoActual.esHijoVacio(nodoActual.cantidadDeClavesNoVacios())) {

                colaNodos.offer(nodoActual.getHijo(nodoActual.cantidadDeClavesNoVacios()));
            }
        }
        return listaElementos;
    }
     protected void insertarClaveYValorOrdenadoEnNodo(NodoMVias<K,V> nodoActual, K claveAInsertar, V valorAInsertar){
        int posicionAInsertar = this.posicionDondeBajar(nodoActual, claveAInsertar);
        for (int i = nodoActual.cantidadDeClavesNoVacios(); i > posicionAInsertar; i--){
            nodoActual.setClave(i, nodoActual.getClave(i-1));
            nodoActual.setValor(i, nodoActual.getValor(i-1));
            //a probar
            nodoActual.setHijo(i + 1 , nodoActual.getHijo(i));
        }
        nodoActual.setClave(posicionAInsertar, claveAInsertar);
        nodoActual.setValor(posicionAInsertar, valorAInsertar);
    }

    @Override
    public String toString() {
        return generarCadenaDeArbol(this.raiz, "", true, 0); //raiz, prefijo "", ponerCodo true
    }

    private String generarCadenaDeArbol(NodoMVias<K, V> nodoActual, String prefijo, boolean ponerCodo, int num) {
        StringBuilder cadena = new StringBuilder();
        cadena.append(prefijo);
        if (prefijo.length() == 0) {
            cadena.append("|---( RAIZ ) |");// └ alt+192 +196 , ├ alt+195 +196
        } else {
            cadena.append(ponerCodo ? "  |__( " : " |---( ");
            cadena.append(num);
            cadena.append(" ) | ");
        }
        if (NodoMVias.esNodoVacio(nodoActual)) {
            cadena.append("----||\n");// ╣ alt+185
            return cadena.toString();
        }

        for (int i = 0; i < nodoActual.cantidadDeClavesNoVacios(); i++) {
            cadena.append(nodoActual.getClave(i).toString());
            cadena.append("  | ");
        }
        cadena.append("\n");

        for (int i = 0; i < this.orden - 1; i++) {
            NodoMVias<K, V> nodoHijo = nodoActual.getHijo(i);
            String prefijoAux = prefijo + (ponerCodo ? "     " : " |     ");// │ alt+179
            cadena.append(generarCadenaDeArbol(nodoHijo, prefijoAux, false, i + 1));
        }

        NodoMVias<K, V> nodoHijo = nodoActual.getHijo(this.orden - 1);
        String prefijoAux = prefijo + (ponerCodo ? "     " : " |    ");// │ alt+179
        cadena.append(generarCadenaDeArbol(nodoHijo, prefijoAux, true, this.orden));

        return cadena.toString();
    }
}
