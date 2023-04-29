import java.util.List;
import java.util.Scanner;
import java.io.*;
import Exceptions.*;


public class Main {
    public static void main(String[] args) {
        File input  = new File("Dollar rate.txt");
        try{
            Scanner file = new Scanner(input);
            double rate = Double.parseDouble(file.nextLine());
            System.out.print("Введите выражение: ");
            Scanner in = new Scanner(System.in);

            String text = in.nextLine();

            List<Check> Checks = Check.ChecksAnalyze(text);

            Check first = Checks.get(0);
            char f = ' ';
            if (first.getType() == Check.Type.Rub || first.getType() == Check.Type.Tor){
                f = 'r';
            } else if (first.getType() == Check.Type.Doll || first.getType() == Check.Type.Tod){
                f = 'd';
            }

            Buf Buf = new Buf(Checks, rate);

            double result = Buf.syntaxAnalyzer(Buf);

            String res = String.format("%.2f",result);
            if (f == 'r'){
                System.out.print(res+"p");
            } else{
                System.out.print("$"+res);
            }
        }

        catch (FileNotFoundException e){
            System.out.println("Файл отсутствует");
        }
        catch (NumberFormatException e){
            System.out.println("Неверный курс доллара");
        }
        catch (WrongWordsException e){
            System.out.println("Неожиданный характер в выражении");
        }
        catch (IncorrectSyntaxException e){
            System.out.println("Неправильный синтаксис");
        }

    }
}
