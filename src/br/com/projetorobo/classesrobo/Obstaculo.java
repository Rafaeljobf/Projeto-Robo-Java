package br.com.projetorobo.classesrobo;

public abstract class Obstaculo {
    protected int x;
    protected int y;
    protected char simbolo;

    public Obstaculo(int x, int y, char simbolo) {
        this.x = x;
        this.y = y;
        this.simbolo = simbolo;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public char getSimbolo() { return simbolo; }

    public abstract void bater(Robo robo);
}