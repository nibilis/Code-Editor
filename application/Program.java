// Made by:
// Marco Antonio de Camargo - RA 10418309
// Natan Moreira Passos - RA 10417916
// Nicolas Henriques de Almeida - RA 10418357

//references: 
// - Java Create and Write To Files - https://www.w3schools.com/java/java_files_create.asp
// - https://sentry.io/answers/how-do-i-convert-a-string-to-an-int-in-java/
// - https://www.w3schools.com/java/ref_string_compareto.asp
// - https://javarush.com/en/groups/posts/en.1412.formatting-number-output-in-java
// - https://www.w3schools.com/java/ref_string_contains.asp

package application;

import ed1.LinkedList;
import java.io.File; // Import the File class
import java.io.FileNotFoundException; // Import this class to handle errors
import java.io.IOException;
import java.util.Scanner; // Import the Scanner class to read text files
import java.io.FileWriter; // Import the FileWriter class

public class Program {
  static boolean unsaved = false;

  public static void main(String[] args) {
    LinkedList code = new LinkedList();
    // Ask user for input
    Scanner sc = new Scanner(System.in);

    LinkedList clipboard = new LinkedList();
    int clipBoardStart = 0;
    int clipBoardEnd = 0;

    String fileName = "";

    while (true) {

      String input = sc.nextLine();

      // :e NomeArq.ext - Abrir o arquivo de nome NomeArq.ext e armazenar cada linha
      // em um nó da lista
      if (input.startsWith(":e")) {
        int len = input.length(); // verifica se esta no formato correto :e nomeArq.ext
        if (len > 3) { // se estiver:
          fileName = "";

          fileName = input.substring(3); // filename será tudo que vem depois de :e

          openFile(code, fileName); // chama a operação de abrir o arquivo
        } else {
          System.out.println("Enter the name of the file! Use the format :e fileName.txt");
        }
      }

      // :w - Salvar a lista no arquivo atualmente aberto.
      // :w NomeArq.ext - Salvar a lista no arquivo de nome NomeArq.ext
      else if (input.startsWith(":w")) {
        if (input.length() > 3) { // verifica se esta no formato :w ou :w nomeArq.ext
          int i = 3;
          String newFileName = ""; // inicializar onde o nome do novo arquivo sera armazenado
          //receber o nome do novo arquivo
          while (i < input.length() && input.charAt(i) != ' ') { 
            newFileName += input.charAt(i);
            i++;
          }
          saveAs(code, newFileName);
        } else { // se não quiser salvar em outro arquivo:
          saveAs(code, fileName); // salva no mesmo arquivo
        }
      }

      // :q! - Encerrar o editor. Caso existam modificações não salvas na lista,
      // o programa deve solicitar confirmação se a pessoa usuária do
      // editor deseja salvar as alterações em arquivo antes de encerrar o
      // editor.
      else if (input.compareTo(":q!") == 0) {
        if (unsaved) { // verificar se existem alterações não salvas
          char choice = ' ';
          while (choice != 'N' || choice != 'Y') {
            //pede ao usuario confirmação caso existam alterações não salvas
            System.out.println("There are unsaved changes, do you want to save before exiting? (Y/N)");
            choice = sc.next().charAt(0);
            if (choice == 'Y') { // se quiser salvar -> salve antes de sair
              saveAs(code, fileName);
              return;
            } else if (choice == 'N') { // se não quiser -> apenas saia
              return;
            } else { // se a resposta for invalida
              System.out.println("Invalid answer - please type Y or N");
              continue;
            }
          }
        } else {
          break;
        }
      }

      // :v LinIni LinFim Marcar um texto da lista (para cópia ou recorte – “área de
      // transferência”) da LinIni até LinFim. Deve ser verificado se o
      // intervalo [LinIni, LinFim] é válido.
      else if (input.startsWith(":v")) {
        if (input.length() > 3) { // verifica se esta no formato :v X Y
          String[] parts = input.substring(3).split(" "); // separa X de Y e coloca-os em um array
          if (parts.length == 2) {
            try {
              clipBoardStart = Integer.parseInt(parts[0]); // transforma X em um int
              clipBoardEnd = Integer.parseInt(parts[1]); // transforma Y em um int
              int size = code.getCount(); 
              if (clipBoardStart <= 0 || clipBoardEnd <= 0 || clipBoardEnd > size || clipBoardStart > size) { // se o input de X ou Y for invalido
                System.out.println("Invalid. Please use valid numbers");
                clipBoardStart = 0;
                clipBoardEnd = 0;
                continue;
              }
              if (clipBoardStart > clipBoardEnd) { // X não pode ser maior que Y
                System.out.println("Invalid. The start can't be after the finish ");
                clipBoardStart = 0;
                clipBoardEnd = 0;
                continue;
              }
              //se passar em todas as verificações, a lista é marcada 
              System.out.println("Successfully selected lines " + clipBoardStart + "-" + clipBoardEnd);
            } catch (NumberFormatException e) { // caso o user use um numero invalido
              System.out.println("Please, use only natural numbers");
            }
          } else { // caso o user use um formato invalido
            System.out.println("Invalid - please use this format -> :v X Y");
          }
        } else { // caso o user use um formato invalido
          System.out.println("Invalid - please use this format -> :v X Y");
        }
      }

      // :y Copiar o texto marcado (ver comando anterior) para uma lista
      // usada como área de transferência.
      else if (input.compareTo(":y") == 0) {  //se nada estiver marcado, mostre um erro
        if (clipBoardStart == 0 && clipBoardEnd == 0) { //se nada estiver marcado, mostre um erro
          System.out.println("There is othing selected!");
          continue;
        }
        var start = code.getNode(clipBoardStart); // pega o nodo de linIni 
        var end = code.getNode(clipBoardEnd); // pega o nodo de linFim
        if (start == null || end == null) {
          System.out.println("Please select valid lines."); // se as linhas não forem validas
          continue;
        }
        clipboard = new LinkedList();
        code.copy(start, end, clipboard); // chama o metodo de copiar
        System.out
            .println("Content of the lines " + clipBoardStart + "-" + clipBoardEnd + " was copied to the clipboard"); 
      }

      // :c Recortar o texto marcado para a lista de área de transferência
      else if (input.compareTo(":c") == 0) {
        if (clipBoardStart == 0 && clipBoardEnd == 0) { //se nada estiver marcado, mostre um erro
          System.out.println("There is nothing selected!");
          continue;
        }
        var start = code.getNode(clipBoardStart); // pega o nodo de linIni
        var end = code.getNode(clipBoardEnd); // pega o nodo de linFim

        if (start == null || end == null) {
          System.out.println("Please select valid lines."); // se as linhas não forem validas
          continue;
        }
        clipboard = new LinkedList(); 
        code.cut(start, end, clipboard); // chama o metodo de corte
        unsaved = true; // o arquivo agora esta com alterações não salvas
        System.out.println("Content of the lines " + clipBoardStart + "-" + clipBoardEnd + " was cut to the clipboard");
      }

      // :p LinIniColar Colar o conteúdo da área de transferência na lista, a partir
      // da linha
      // indicada em LinIniColar. Deve ser verificado se LinIniColar é válido.
      else if (input.startsWith(":p")) {
        if (clipboard.isEmpty()) { //impedir o user de realizar as operações de colar se a area de transferencia estiver vazia
          System.out.println("The clipboard is empty!");
          continue;
        }
        if (input.length() > 3) {
          input = input.substring(3);
          try {
            int number = Integer.valueOf(input); //transforma o numero digitado pelo user em um int

            code.paste(number, clipboard); // realiza a operação
          } catch (NumberFormatException e) {
            System.out.println("Invalid input"); // retorna um erro se o input do user não for valido
          }
        } else {
          System.out.println("Invalid - use this format -> :p X"); // retorna um erro se o user usar o formato errado
          continue;
        }
      }

      // :s - Exibir em tela o conteúdo completo do código-fonte que consta na
      // lista, de 20 em 20 linhas
      else if (input.startsWith(":s")) {
        if (code.isEmpty()) {
          System.out.println("The code is empty.");
          continue;
        }
        if (input.length() > 3) {
          String[] parts = input.substring(3).split(" ");

          if (parts.length == 2) {
            try {
              // converter os dois parametros em ints
              int start = Integer.parseInt(parts[0]);
              int finish = Integer.parseInt(parts[1]);
              if (start <= 0 || finish <= 0) { //impedir o user de inserir numeros negativos ou 0
                System.out.println("Please, use only natural numbers");
                continue;
              }
              // chama o metodo com os numeros digitados pelo user
              showScreen(start, finish, code, sc);
            } catch (NumberFormatException e) {
              System.out.println("Please, use only natural numbers"); // impede que o user use numeros que não sejam naturais
            }
          } else {
            System.out.println("Invalid. Please use this format -> :s X Y"); // impede que o user use o formato errado
          }
        }

        else {
          showScreen(1, code.getCount(), code, sc); //se o user digitar apenas :s
        }

      }

      // :xG Lin Apagar o conteúdo a partir da linha Lin até o final da lista.
      else if (input.startsWith(":xG")) {
        if (input.length() > 4) {
          input = input.substring(4);
          try {
            int number = Integer.valueOf(input);
            if (code.removeAfter(number)) {
              System.out.println("Removed lines from " + number + " and forward.");
            } else {
              System.out.println("The line " + number + " was not found.");
            }
          } catch (NumberFormatException e) {
            System.out.println("Invalid input. Use the format :xG lineNumber");
          }
        }
      }

      // :XG - Lin Apagar o conteúdo da linha Lin até o início da lista.
      else if (input.startsWith(":XG")) {
        if (input.length() > 4) {
          input = input.substring(4);
          try {
            int number = Integer.valueOf(input);
            if (code.removeBefore(number)) {
              System.out.println("Removed lines from " + number + " and back.");
            } else {
              System.out.println("The line " + number + " was not found.");
            }
          } catch (NumberFormatException e) {
            System.out.println("Invalid input");
          }
        }
      }

      // :x Lin - Apagar a linha de posição Lin da lista.
      else if (input.startsWith(":x")) {
        if (input.length() > 3) {
          input = input.substring(3);
          try {
            int number = Integer.valueOf(input);
            if (code.removeIndex(number)) {
              System.out.println("Line " + number + " was successfully removed!");
            } else {
              System.out.println("Line " + number + " was not found");
            }

          } catch (NumberFormatException e) {
            System.out.println("Invalid input");
          }
        }
      }

      // :/ Elemento - Percorrer a lista, localizar as linhas que contém Elemento e
      // exibir
      // o conteúdo das linhas por completo.
      // :/ Elem ElemTroca - Percorrer a lista e realizar a troca de Elem por
      // ElemTroca em todas
      // as linhas do código-fonte
      // :/ Elem ElemTroca Linha - Realizar a troca de Elem por ElemTroca na linha
      // Linha do códigofonte.
      else if (input.startsWith(":/")) {
        if (input.length() > 3) {
          String[] parts = input.split(" ");
          if (parts.length == 2) {
            String search = parts[1];
            int found = code.searchElement(search);
            if (found == -1) {
              System.out.println("There are no lines of code");
            } else {
              System.out.println("There are " + found + " lines with the element '" + search + "'.");
            }
          }
          if (parts.length == 3) {
            String search = parts[1];
            String replace = parts[2];
            int replacedLines = code.replaceElement(search, replace);
            if (replacedLines == -1) {
              System.out.println("No lines were modified");
            } else {
              System.out.println(
                  "'" + search + "' replaced with '" + replace + "' in " + replacedLines + " different line(s).");
              unsaved = true;
            }
          }
          if (parts.length == 4) {
            String search = parts[1];
            String replace = parts[2];
            try {
              int line = Integer.valueOf(parts[3]);
              if (line < 1 || line > code.getCount()) {
                System.out.println("Invalid line number!");
              } else {
                if (code.replaceElementAtLine(search, replace, line)) {
                  System.out.println("'" + search + "' was replaced with '" + replace + "' at the line " + line + ".");
                } else {
                  System.out.println("'" + search + "' was not found at line " + line + ".");
                }
              }

            } catch (NumberFormatException e) {
              System.out.println("The last argument should be a number! Use the format :/ str strToReplace lineNumber");
            }
          }
        }
      }

      // :a - PosLin Permitir a inserção de uma ou mais linhas e inserir na lista
      // depois da posição PosLin. O término da entrada do novo conteúdo é
      // dado por um :a em uma linha vazia.
      else if (input.startsWith(":a")) {
        if (input.length() > 3) {
          input = input.substring(3);
          try {
            int number = Integer.valueOf(input);
            appendAfter(number, code);
          } catch (NumberFormatException e) {
            System.out.println("Invalid input");
          }
        }
      }

      // :i - PosLin Permitir a inserção de uma ou mais linhas e inserir na lista
      // antes
      // da posição PosLin. O término da entrada do novo conteúdo é
      // dado por um :i em uma linha vazia.
      else if (input.startsWith(":i")) {
        if (input.length() > 3) {
          input = input.substring(3);
          try {
            int number = Integer.valueOf(input);
            insertBefore(number, code);
          } catch (NumberFormatException e) {
            System.out.println("Invalid input");
          }
        }
      }

      // :help - Apresentar na tela todas as operações permitidas no editor.
      else if (input.compareTo(":help") == 0) {
        help();
      }

      // Default option
      else {
        System.out.println("Invalid option! Use :help to see commands.");
      }
    }
    sc.close();
  }

  // operação para adicionar novo conteudo após um nodo especifico da linked list
  private static void appendAfter(int number, LinkedList code) {
    if (number > code.getCount() || number < 1) { // caso o numero seja valido
      System.out.println("Invalid line number!");
      return;
    }
    Scanner sc = new Scanner(System.in);
    LinkedList newContent = new LinkedList(); // cria a lista que conterá o novo conteudo
    String newLine = "";
    do {
      newLine = sc.nextLine(); // recebe o dado de um nodo da nova lista 
      newContent.append(newLine); // adiciona esse nodo à nova lista
    } while (newLine.compareTo(":a") != 0); // faz isso até encontrar o indicador de parada
    newContent.removeTail(); // o indicador de para é adiocionado à lista - essa operação remove-o
    if (code.appendList(number, newContent)) { // verifica se a adição do novo conteudo foi bem sucedida
      System.out.println("Code successfully appended after line " + number);
      unsaved = true; // agora existem alterações não salvas na lista original
    } else { // caso não tenha sido:
      System.out.println("Invalid line number!");
    }
  }
  
  // operação para inserir mais conteudo antes de um nodo da linked list
  private static void insertBefore(int number, LinkedList code) { // recebe um int (o indice ao qual inserir antes) e a linked list sendo usada
    if (number > code.getCount() || number < 1) { // caso o numero seja invalido
      System.out.println("Invalid line number!");
      return;
    }
    Scanner sc = new Scanner(System.in);
    LinkedList newContent = new LinkedList(); // cria a lista que conterá o novo conteudo
    String newLine = "";
    do {
      newLine = sc.nextLine(); // recebe o dado de um nodo da nova lista
      newContent.append(newLine); // adiciona esse nodo à nova lista
    } while (newLine.compareTo(":i") != 0); // faz isso até encontrar o indicador de parada
    newContent.removeTail(); // o indicador de parada é adicionado à lista - essa operação remove-o
    if (code.insertList(number, newContent)) { // verifica se a inserção do novo conteudo foi bem sucedida
      System.out.println("Code successfully inserted before line " + number); 
      unsaved = true; // agora existem alterações não salvas na lista original
    } else { // caso não tenha sido:
      System.out.println("Invalid line number!");
    }
  }
  // operação para abrir o arquivo e colocar cada linha dele em um nodo de linked List
  private static void openFile(LinkedList code, String fileName) { // recebe uma linked list e o nome do arquivo a ser aberto
    try {
      //abrir o arquivo
      File file = new File(fileName);
      Scanner scanFile = new Scanner(file);
      // transferir cada linha para um nodo de linked List
      while (scanFile.hasNextLine()) {
        String data = scanFile.nextLine();
        code.append(data);
      }
      // retorno ao usuario
      scanFile.close();
      System.out.println("The file " + fileName + " was opened successfully");
      unsaved = true;
    } catch (FileNotFoundException e) { // caso o arquivo não seja encontrado:
      System.out.println("The file was not found.");
    }
  }
  // operação para mostrar as linhas do arquivo na tela de 20 em 20
  private static void showScreen(int start, int finish, LinkedList code, Scanner sc) {
    var linhaIni = code.getNode(start); // recebe nodo de linIni
    var linhaFim = code.getNode(finish); // recebe nodo de linFim
    char choice = ' ';
    boolean keepPrinting = true; // variavel que mantem registro se o user quer continuar imprimindo
    int partialFinish = (finish >= 20 ? 20 : finish); // contador de quais linhas estão sendo impressas
    while (keepPrinting) {
      linhaIni = code.printList(linhaIni, linhaFim, start); // chama a operação de imprimir de 20 em 20 linhas
      System.out.println("Showing lines from " + start + "-" + partialFinish + ". Lines total: " + code.getCount()); // mostra quais linhas estão sendo impressas
      if (linhaIni == null) { // se a linha inicial for null - encerra
        return;
      }
      System.out.print("Do you wish to see more? (Y/N): "); // pergunta ao user se quer ver as proximas 20 linhas
      while (true) {
        choice = sc.next().charAt(0);
        sc.nextLine();

        if (choice == 'N') { // se não quiser - encerra
          return;
        } else if (choice == 'Y') { // se quiser - continue a operação
          break;
        } else { // se a resposta for invalida - peça novamente
          System.out.println("Invalid answer - please type Y or N: ");
        }
      }
      // atualiza os contadores
      partialFinish = (partialFinish + 20 > finish ? finish : partialFinish + 20); 
      start = start + 20;
    }
  }

  // salvar no arquivo nome.txt
  private static void saveAs(LinkedList code, String fileName) {
    try {
      FileWriter obj = new FileWriter(fileName);
      code.writeData(obj);
      obj.close();
      System.out.println("The content was successfully saved on " + fileName);
      unsaved = false;
    } catch (IOException e) {
      System.out.println("You don't have an open file. If you want to save on a different file, use :w fileName.txt");
    }
  }

  private static void help() {
    System.out.println(
        "### Menu de Ajuda: ###\n :e NomeArq.ext --> Abrir o arquivo de nome NomeArq.ext e armazenar cada linha em um nó da lista\n :w --> Salvar a lista no arquivo atualmente aberto.\n :w NomeArq.ext --> Salvar a lista no arquivo de nome NomeArq.ext.\n :q! --> Encerrar o editor. Caso existam modificações não salvas na lista, o programa deve solicitar confirmação se a pessoa usuária do editor deseja salvar as alterações em arquivo antes de encerrar o editor\n :v LinIni LinFim --> Marcar um texto da lista (para cópia ou recorte – “área de transferência”) da LinIni até LinFim. Deve ser verificado se o intervalo [LinIni,LinFim] é válido.\n :y --> Copiar o texto marcado (ver comando anterior) para uma lista usada como área de transferência.\n :c --> Recortar o texto marcado para a lista de área de transferência.\n :p LinIniColar --> Colar o conteúdo da área de transferência na lista, a partir da linha indicada em LinIniColar. Deve ser verificado se LinIniColar é válido\n :s --> Exibir em tela o conteúdo completo do código-fonte que consta na lista, de 20 em 20 linhas.\n :s LinIni LinFim --> Exibir na tela o conteúdo do código-fonte que consta na lista, da linha inicial LinIni até a linha final LinFim, de 20 em 20 linhas.\n :x Lin --> Apagar a linha de posição Lin da lista.\n :xG Lin --> Apagar o conteúdo a partir da linha Lin até o final da lista.\n :XG Lin --> Apagar o conteúdo da linha Lin até o início da lista.\n :/ Elemento --> Percorrer a lista, localizar as linhas que contém Elemento e exibir o conteúdo das linhas por completo.\n :/ Elem ElemTroca --> Percorrer a lista e realizar a troca de Elem por ElemTroca em todas as linhas do código-fonte.\n :/ Elem ElemTroca Linha --> Realizar a troca de Elem por ElemTroca na linha Linha do código fonte.\n :a PosLin --> Permitir a inserção de uma ou mais linhas e inserir na lista depois da posição PosLin. O término da entrada do novo conteúdo é dado por um :a em uma linha vazia.\n :i PosLin --> Permitir a inserção de uma ou mais linhas e inserir na lista antes da posição PosLin. O término da entrada do novo conteúdo é dado por um :i em uma linha vazia.\n :help --> Apresentar na tela todas as operações permitidas no editor.");
  }
}