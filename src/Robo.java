public class Robo {
    protected int x;
    protected int y;
    protected int prevX;
    protected int prevY;
    protected String cor;
    protected int movimentosValidos = 0;
    protected int movimentosInvalidos = 0;
    protected boolean ativo = true;

    public Robo(String cor){
        this.cor = cor;
        this.x = 0;
        this.y = 0;
        this.prevX = 0;
        this.prevY = 0;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String getCor() {
        return cor;
    }

    public int getMovimentosValidos() {
        return movimentosValidos;
    }

    public int getMovimentosInvalidos() {
        return movimentosInvalidos;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void desativar() {
        this.ativo = false;
    }

    public void voltarPosicaoAnterior() {
        this.x = this.prevX;
        this.y = this.prevY;

        if (this.movimentosValidos > 0) {
            this.movimentosValidos--;
        }
        this.movimentosInvalidos++;
    }

    public void mover(String direcao) throws MovimentoInvalidoException {
        if (!ativo) return;

        this.prevX = this.x;
        this.prevY = this.y;

        int novoX = x;
        int novoY = y;

        switch (direcao.toLowerCase()) {
            case "up" -> novoY++;
            case "down" -> novoY--;
            case "right" -> novoX++;
            case "left" -> novoX--;
            default -> throw new IllegalArgumentException("Direção inválida: " + direcao);
        }


        if (novoX < 0 || novoY < 0) {
            movimentosInvalidos++;
            throw new MovimentoInvalidoException(direcao);
        }


        x = novoX;
        y = novoY;
        movimentosValidos++;

        System.out.println(cor + " moveu para (" + x + ", " + y + ")");
    }

    public void mover(int direcao) throws MovimentoInvalidoException {
        switch (direcao) {
            case 1 -> mover("up");
            case 2 -> mover("down");
            case 3 -> mover("right");
            case 4 -> mover("left");
            default -> throw new IllegalArgumentException("Código de direção inválido: " + direcao);
        }
    }

    public boolean encontrou(int fx, int fy) {
        return x == fx && y == fy;
    }
}

