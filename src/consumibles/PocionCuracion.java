package consumibles;

import personajes.Personaje;

public class PocionCuracion {

    private String nombre = "Poción de Curación";
    private int cantidad;

    public PocionCuracion(int cantidad) {
        this.cantidad = cantidad;
    }

    public void usar(Personaje usuario) {
        if (cantidad <= 0) {
            System.out.println("No quedan " + nombre + ".");
            return;
        }

        cantidad--;
        usuario.curar(50);
        System.out.println(usuario.getNombre() + " usa " + nombre + ". Restaura 50 HP.");
    }

    public int getCantidad() {
        return cantidad;
    }
}
