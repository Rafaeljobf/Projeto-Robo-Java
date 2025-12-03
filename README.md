# Projeto Robô Java

Este é um projeto acadêmico em Java que simula o movimento de robôs em um grid cartesiano 4x4. O objetivo é implementar conceitos de Programação Orientada a Objetos, como herança, polimorfismo, classes abstratas e tratamento de exceções.

O projeto inclui tanto executáveis baseados em console quanto interfaces gráficas (GUI) usando Java Swing para cada um dos cenários propostos.

## Funcionalidades Principais

* **Movimentação Básica:** A classe `Robo` pode se mover em quatro direções (`up`, `down`, `left`, `right`).
* **Tratamento de Exceções:** O movimento é restrito a um grid de 4x4 (posições 0 a 3). Qualquer tentativa de movimento para fora dessa área (ex: -1 ou 4) lança uma `MovimentoInvalidoException`.
* **Robô Inteligente:** Uma subclasse `RoboInteligente` que, ao invés de lançar uma exceção por movimento inválido, captura a falha internamente e garante tentar um movimento *diferente* no lugar, até ter sucesso.
* **Obstáculos:** Implementação de uma classe abstrata `Obstaculo` com duas classes concretas:
    * `Bomba`: Desativa o robô ao colidir.
    * `Rocha`: Força o robô a voltar para a posição anterior.
* **Interfaces Gráficas (Swing):** Todos os cenários do trabalho foram implementados também com GUIs, que mostram o tabuleiro visualmente e possuem um log de eventos.
* **Logging:** O sistema de log foi implementado usando um `Consumer` (injeção de dependência) para que as classes `Robo` possam enviar mensagens de status (movimentos válidos e inválidos) diretamente para a `JTextArea` da GUI.

## Estrutura do Projeto

O código-fonte está organizado nos seguintes pacotes:

* `src/br/com/projetorobo/classesrobo/`
    * Contém as classes de lógica de negócio (Robo, RoboInteligente, Obstaculo, Bomba, Rocha, MovimentoInvalidoException).
* `src/br/com/projetorobo/executaveisconsole/`
    * Contém os arquivos `main` originais para execução via terminal de console.
* `src/br/com/projetorobo/interfacesgraficas/`
    * Contém os arquivos `main` que executam as interfaces gráficas (GUI) em Swing.

## Como Executar

Você pode compilar e executar qualquer um dos arquivos `main` localizados nos pacotes `executaveisconsole` ou `interfacesgraficas`.

### Interfaces Gráficas (GUI)

* **Questão 1 (Controle Manual):** `br.com.projetorobo.interfacesgraficas.MainControleUsuarioGUI.java`
* **Questão 2 (Competição Aleatória):** `br.com.projetorobo.interfacesgraficas.MainCompeticaoAleatoriaGUI.java`
* **Questão 3 (Normal vs. Inteligente):** `br.com.projetorobo.interfacesgraficas.MainGUI.java`
* **Questão 4 (Com Obstáculos):** `br.com.projetorobo.interfacesgraficas.MainComObstaculosGUI.java`

### Executáveis de Console

* **Questão 1 (Controle Manual):** `br.com.projetorobo.executaveisconsole.MainControleUsuario.java`
* **Questão 2 (Competição Aleatória):** `br.com.projetorobo.executaveisconsole.MainCompeticaoAleatoria.java`
* **Questão 3 (Normal vs. Inteligente):** `br.com.projetorobo.executaveisconsole.Main.java`
* **Questão 4 (Com Obstáculos):** `br.com.projetorobo.executaveisconsole.MainComObstaculos.java`
