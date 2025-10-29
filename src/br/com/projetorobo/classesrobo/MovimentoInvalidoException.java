package br.com.projetorobo.classesrobo;


public class MovimentoInvalidoException extends Exception {
    public MovimentoInvalidoException(String mensagemDeErro){
        super(mensagemDeErro);
    }
}