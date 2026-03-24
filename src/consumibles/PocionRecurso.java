package consumibles;

import personajes.Personaje;

public class PocionRecurso {

    private String nombre = "Poción de Recurso";
    private int cantidad;

    public PocionRecurso(int cantidad) {
        this.cantidad = cantidad;
    }

    public void usar(Personaje usuario) {
        if (cantidad <= 0) {
            System.out.println("No quedan " + nombre + ".");
            return;
        }

        cantidad--;
        usuario.recuperarRecursos(40);
        System.out.println(usuario.getNombre() + " usa " + nombre + ". Restaura 40 SP/MP.");
    }

    public int getCantidad() {
        return cantidad;
    }
}
