public class NumeroSecreto {
    private int num;

    public NumeroSecreto(int n) {
        piensa(n);
    }

    public void piensa(int max) {
        setNumero((int) ((Math.random()*max)+1));
        System.out.println("El numero es: " + getNumero());
    }

    public int comprueba(int n) {
            if (num == n) return 0;
            else if (num < n) return 1;
            else return 2;
    }

    public int getNumero() {
        return num;
    }

    private void setNumero(int num) {
        this.num = num;
    }


}
