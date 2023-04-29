import Exceptions.IncorrectSyntaxException;
import java.util.List;

public class Buf {

    private int pos;

    private double rate;

    private List<Check> Checks;


    public Buf(List<Check> Checks, double rate) {
        this.Checks = Checks;
        this.rate = rate;
    }

    public Check next() {
        return Checks.get(pos++);
    }

    public void back() {
        pos--;
    }

    public int getPos() {
        return pos;
    }

    public static double syntaxAnalyzer(Buf Checks) throws IncorrectSyntaxException {
        Check check = Checks.next();
        switch (check.getType()){
            case Rub:
            case Tor:
                Checks.back();
                return plusR(Checks);
            case Doll:
            case Tod:
                Checks.back();
                return plusD(Checks);
            default:
                throw new IncorrectSyntaxException();

        }
    }
    public static double plusR(Buf Checks) throws IncorrectSyntaxException{
        double val = rubF(Checks);
        while (true){
            Check check = Checks.next();
            switch (check.getType()){
                case OpPlus:
                    val += rubF(Checks);
                    break;
                case OpMinus:
                    val -= rubF(Checks);
                    break;
                case Eof:
                    Checks.back();
                    return val;
                case RBracket:
                    Checks.back();
                    return val/ Checks.rate;
                default:
                    throw new IncorrectSyntaxException();
            }

        }
    }
    public static double plusD(Buf Checks) throws IncorrectSyntaxException{
        double val = dollF(Checks);
        while (true){
            Check check = Checks.next();
            switch (check.getType()){
                case OpPlus:
                    val += dollF(Checks);
                    break;
                case OpMinus:
                    val -= dollF(Checks);
                    break;
                case Eof:
                    Checks.back();
                    return val ;
                case RBracket:
                    Checks.back();
                    return val*Checks.rate;
                default:
                    throw new IncorrectSyntaxException();
            }

        }
    }
    public static double rubF(Buf Checks) throws IncorrectSyntaxException{
        Check check = Checks.next();
        switch (check.getType()){
            case Rub:
                return Double.parseDouble(check.getVal());
            case Tor:
                check = Checks.next();
                if (check.getType() == Check.Type.LBracket){
                    double val = plusD(Checks);
                    check = Checks.next();
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
    public static double dollF(Buf Checks) throws IncorrectSyntaxException{
        Check check = Checks.next();
        switch (check.getType()){
            case Doll:
                return Double.parseDouble(check.getVal());
            case Tod:
                check = Checks.next();
                if (check.getType() == Check.Type.LBracket){
                    double val = plusR(Checks);
                    check = Checks.next();
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