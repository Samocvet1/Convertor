import Exceptions.IncorrectSyntaxException;
import java.util.List;

public class Buf {

    private int pos;

    private double rate;

    private List<Check> lexemes;


    public Buf(List<Check> lexemes, double rate) {
        this.lexemes = lexemes;
        this.rate = rate;
    }

    public Check next() {
        return lexemes.get(pos++);
    }

    public void back() {
        pos--;
    }

    public int getPos() {
        return pos;
    }

    public static double syntaxAnalyzer(Buf lexemes) throws IncorrectSyntaxException {
        Check check = lexemes.next();
        switch (check.getType()){
            case Rub:
            case Tor:
                lexemes.back();
                return plusR(lexemes);
            case Doll:
            case Tod:
                lexemes.back();
                return plusD(lexemes);
            default:
                throw new IncorrectSyntaxException();

        }
    }
    public static double plusR(Buf lexemes) throws IncorrectSyntaxException{
        double val = rubF(lexemes);
        while (true){
            Check check = lexemes.next();
            switch (check.getType()){
                case OpPlus:
                    val += rubF(lexemes);
                    break;
                case OpMinus:
                    val -= rubF(lexemes);
                    break;
                case Eof:
                    lexemes.back();
                    return val;
                case RBracket:
                    lexemes.back();
                    return val/ lexemes.rate;
                default:
                    throw new IncorrectSyntaxException();
            }

        }
    }
    public static double plusD(Buf lexemes) throws IncorrectSyntaxException{
        double val = dollF(lexemes);
        while (true){
            Check check = lexemes.next();
            switch (check.getType()){
                case OpPlus:
                    val += dollF(lexemes);
                    break;
                case OpMinus:
                    val -= dollF(lexemes);
                    break;
                case Eof:
                    lexemes.back();
                    return val ;
                case RBracket:
                    lexemes.back();
                    return val*lexemes.rate;
                default:
                    throw new IncorrectSyntaxException();
            }

        }
    }
    public static double rubF(Buf lexemes) throws IncorrectSyntaxException{
        Check check = lexemes.next();
        switch (check.getType()){
            case Rub:
                return Double.parseDouble(check.getVal());
            case Tor:
                check = lexemes.next();
                if (check.getType() == Check.Type.LBracket){
                    double val = plusD(lexemes);
                    check = lexemes.next();
                    if (check.getType() != Check.Type.RBracket){
                        throw new IncorrectSyntaxException();
                    }
                    return val;
                }else {
                    throw new IncorrectSyntaxException();
                }
            default:
                throw new IncorrectSyntaxException();
        }
    }
    public static double dollF(Buf lexemes) throws IncorrectSyntaxException{
        Check check = lexemes.next();
        switch (check.getType()){
            case Doll:
                return Double.parseDouble(check.getVal());
            case Tod:
                check = lexemes.next();
                if (check.getType() == Check.Type.LBracket){
                    double val = plusR(lexemes);
                    check = lexemes.next();
                    if (check.getType() != Check.Type.RBracket){
                        throw new IncorrectSyntaxException();
                    }
                    return val;
                }
            default:
                throw new IncorrectSyntaxException();
        }
    }
}