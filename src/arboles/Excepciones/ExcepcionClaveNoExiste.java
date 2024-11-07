
package arboles.Excepciones;


public class ExcepcionClaveNoExiste extends Exception {

    /**
     * Creates a new instance of <code>ExcepcionClaveNoExiste</code> without
     * detail message.
     */
    public ExcepcionClaveNoExiste() {
        super("Orden del arbol debe ser mayor o igual a 3");
    }

    /**
     * Constructs an instance of <code>ExcepcionClaveNoExiste</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public ExcepcionClaveNoExiste(String msg) {
        super(msg);
    }
}
