public class MovimentoInvalidoException extends Exception {
    public MovimentoInvalidoException(String movimento){
        super("Movimento inválido:"+ movimento + "(Coordenada negativa)");
    }
}
