import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;
import java.util.HashMap;

class NextToYou {
    
    public static  HashMap<String, localidad> lecturaTXT(Scanner sc) {
        HashMap<String, localidad> localidades = new HashMap<String, localidad>();
        sc.useDelimiter(",|(\\n)|(\r\n)");
    
        while(sc.hasNextLine()) {
            String local1 = sc.next();
            String local2 = sc.next();
            
            try {
                if (localidades.containsKey(local1)) {
                    localidad local = localidades.get(local1);
                    if (!local.conectado(local2)) {
                        local.addVias(local2);
                    } 
                } else {
                    localidad local = new localidad(local1, local2);
                    localidades.put(local1, local);
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Ha ocurrido un error leyendo el nombre");
                e.printStackTrace();
                System.exit(1);
            }
        }
        return localidades;
    }

    public static class localidad {
        private String nombre;
        private ArrayList<String> vias = new ArrayList<>();

        public String getNombre() {
            return this.nombre;
        }
        
        public localidad(String local1, String local2) {
            this.nombre = local1;
            vias.add(local2);
        }

        public boolean conectado(String local) {
            if (this.vias.contains(local)) {
                return true;
            } else {
                return false;
            }
        }

        public void addVias(String local) {
            this.vias.add(local);
        }

        public String getVias() {
            return this.vias.toString();
        }
    }

    static int repartidores(int comercios) {
        if (comercios <= 2) {
            return 10;
        } else if (comercios > 2 && comercios <= 5) {
            return 20;
        } else {
            return 30;
        }
    }

    public static void main(String[] args) {
        try {

            File file = new File("Caracas.txt");
            Scanner sc = new Scanner(file); 
            HashMap<String, localidad> comercios = lecturaTXT(sc);
            System.out.println(repartidores(comercios.size()));
            sc.close();

        } catch (FileNotFoundException e) {
            System.out.println("Hubo un error al buscar el documento");
            System.exit(1);
        }
    }
}