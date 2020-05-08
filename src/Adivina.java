public class Adivina {
    String palabra;
    String palabraAdivinadaHastaElMomento;
    boolean letraCorrecta = false;

    String[] diccionario = {"ABEJA", "AEROPUERTO", "COMPUTADOR", "OSO",
            "JAVA", "NEVERA", "PROGRAMA", "INFORMATICA", "COMPUTACION", "COMPUTADOR","CORAZON","BANANO","PLATANO",
            "AUTOMOVIL", "PERRO", "COLOMBIA", "LONDRES", "CEPILLO", "BRAZO", "CABEZA", "CUERPO","DEPORTE","SALUD",
            "ANONYMOUS", "CUADERNO", "PANTALLA", "UBUNTU", "SEMAFORO", "LINUX", "LOBO","AMOR","MOSCA","ZANAHORIA",
            "PINGUINO", "HACKER", "SISTEMA", "ELEFANTE", "CASCADA", "JUEGOS","LARGO","BONITO","IMPOSIBLE","UNIDOS","ZOMBIE",
            "MATEMATICAS", "CALCULO", "ALGEBRA", "DICCIONARIO", "BIBLIOTECA", "COCINA","PELICULA","COMERCIAL","GRANDE","PEQUEÑO",
            "GATO", "SAPO", "JIRAFA", "FERROCARRIL", "FACEBOOK", "PERSONA","BICICLETA","CONTROL","PANTALON","AEROSOL",
            "ERROR", "COPA", "COPA", "PROGRAMADOR", "LICENCIA", "NUEVE", "PROCESADOR","GARAJE","MODERNO","TABLA","DISCOTECA",
            "LENGUAJE", "PROGRAMACION", "HERRAMIENTAS", "INTERNET", "EJECUTAR", "PROYECTO","PERIODICO","COCODRILO","TORTUGA","CABALLO",
            "APLICACION", "PERA", "SOFTWARE", "ADMINISTRACION", "VENTANA", "MANTENIMIENTO","INFORMACION","PRESIDENTE","PERSONA","GENTE",
            "NARANJA", "PRUEBA", "MANZANA", "JARRA", "CELULAR", "TELEFONO","CONTAMINACION","COLOR","ROMANO","ADIVINAR","MARCADOR",
            "INSTRUCCION", "CUADERNO", "CASA", "PALA", "ARBOL", "PUENTE", "PAPEL", "HOJA","HELICOPTERO","BARCO","GOLF","CARRERA",
            "TUBERIA", "PLOMERO", "FUTBOL", "BALONCESTO", "ESTADIO", "JEAN", "FUENTE", "LEOPARDO","REGLA","PRIMERO","SEGUNDO",
            "BLUSA", "CAMISA", "AGUA", "FUEGO", "INDUSTRIA", "AIRE","TIERRA","NATURALEZA","MIERCOLES","FOTOGRAFIA","LEON",
            "TIGRE"};

    public Adivina() {
        int num = (int)(Math.random()*(diccionario.length));
        palabra = diccionario[num].toLowerCase();
        palabraAdivinadaHastaElMomento = letraAdivinadaHastaElMomento();
    }

    public int comprueba() {
        if (palabra.equals(palabraAdivinadaHastaElMomento)) return 0;
        else return 2;
    }

    public String letraAdivinadaHastaElMomento(){
        String resultado = "";
        for (int i = 0; i < palabra.length() ; i++) {
            resultado += "-";
        }
        return resultado;
    }

    public String actualizarLetraAdivinadaHastaElMomento(char letra) {
        String resultado = "";
        for (int i = 0; i < palabra.length() ; i++) {
            char l = palabra.charAt(i);
            if(letra == l || l == palabraAdivinadaHastaElMomento.charAt(i)){
                resultado += palabra.charAt(i);
                letraCorrecta = true;
            }
            else {
                resultado += "-";
            };
        }
        return resultado;
    }
}
