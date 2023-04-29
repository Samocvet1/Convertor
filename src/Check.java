import Exceptions.WrongWordsException;
import java.util.ArrayList;
import java.util.List;

public class Check {

    public enum Type {
        LBracket, RBracket, OpPlus, OpMinus, Eof, Doll, Rub, Tod, Tor;
    }


    private Type type;

    private String val;

    public Type getType(){
        return type;
    }
    public String getVal(){
        return val;
    }
    public Check(Type type, String val) {
        this.type = type;
        this.val = val;
    }
    public Check(Type type, Character val) {
        this.type = type;
        this.val = val.toString();
    }
    public static List<Check> ChecksAnalyze(String text) throws WrongWordsException {
        ArrayList<Check> Checks = new ArrayList<>();
        int pos = 0;
        String toD = "toDollars";
        String toR = "toRubles";
        while (pos< text.length()) {
            char c = text.charAt(pos);
            switch (c) {
                case '(':
                    Checks.add(new Check(Type.LBracket, c));
                    pos++;
                    continue;
                case ')':
                    Checks.add(new Check(Type.RBracket, c));
                    pos++;
                    continue;
                case '+':
                    Checks.add(new Check(Type.OpPlus, c));
                    pos++;
                    continue;
                case '-':
                    Checks.add(new Check(Type.OpMinus, c));
                    pos++;
                    continue;
                default:
                    char f = '0';
                    if (c == '$'){
                        f = 'd';
                        pos++;
                        c = text.charAt(pos);
                    }
                    if (c <= '9' && c >= '0') {
                        StringBuilder sb = new StringBuilder();
                        int flag = 0;
                        do {
                            sb.append(c);
                            pos++;
                            if (pos >= text.length()) {
                                break;
                            }
                            c = text.charAt(pos);
                            if (c == '.'){
                                flag ++;
                            }
                            if (flag>1){
                                throw new RuntimeException("2 ,,");
                            }
                        } while ((c <= '9' && c >= '0') || c == '.');
                        if (f == 'd') {
                            Checks.add(new Check(Type.Doll, sb.toString()));
                            f = '0';
                            continue;
                        }
                        char a = text.charAt(pos);
                        pos++;
                        if (a == 'p' || a == 'р'){
                            Checks.add(new Check(Type.Rub, sb.toString()));
                            continue;
                        }
                    }else if(toD.contains(Character.toString(c)) || toR.contains(Character.toString(c))) {
                        StringBuilder sb = new StringBuilder();
                        sb.append(c);
                        while (toD.contains(sb.toString()) || toR.contains(sb.toString())){
                            pos++;
                            if (pos >= text.length()) {
                                break;
                            }
                            c = text.charAt(pos);
                            sb.append(c);
                        }
                        if (sb.substring(0,sb.length()-1).toString().equals(toD)){
                            Checks.add(new Check(Type.Tod, sb.substring(0,sb.length()-1).toString()));
                        }
                        else if (sb.substring(0,sb.length()-1).toString().equals(toR)){
                            Checks.add(new Check(Type.Tor, sb.substring(0,sb.length()-1).toString()));
                        }
                        else{
                            throw new WrongWordsException();
                        }
                    } else{
                        if (c != ' ') {
                            throw new WrongWordsException();
                        }
                        pos++;
                    }
            }
        }
        Checks.add(new Check(Type.Eof, ""));
        return Checks;
    }
}