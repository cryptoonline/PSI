import flanagan.math.Polynomial;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;

public class ClientPhase1 {

    public static void main(String[] args) {
        String filename = args[0];

        Paillier paillier = new Paillier();

        Inputs inputs = new Inputs(filename);
        BigInteger[] roots = inputs.getInputs();

        double[] rootsDouble = new double[roots.length];
        for(int i = 0; i < roots.length; i++) rootsDouble[i] = roots[i].doubleValue();

        Polynomial poly = Polynomial.rootsToPoly(rootsDouble);

        double[] coefficients = poly.coefficientsCopy();
        BigInteger[] coefficientsBigInteger = new BigInteger[coefficients.length];

        int j = 0;
        for(double coefficient : coefficients){
            coefficientsBigInteger[j++] = new BigDecimal(coefficient).toBigInteger();
        }

        BigInteger[] encryptedCoefficients = new BigInteger[coefficients.length];

        for(int k = 0; k < coefficients.length; k++){
            encryptedCoefficients[k] = paillier.Encryption(coefficientsBigInteger[k]);
        }

        write(encryptedCoefficients, "Client_To_Server.out");
        write(paillier.getPublicKey(), "ClientPK.out");
        write(paillier.getSecretKey(), "ClientSK.out");

    }

    public static void write(Object object, String filename){
        ObjectOutputStream oos;
        try {
            oos = new ObjectOutputStream(new FileOutputStream(filename));
            oos.writeObject(object);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
