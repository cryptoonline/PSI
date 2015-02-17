import java.io.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * Created by naveed on 2/15/15.
 */
public class Server {
    public static void main(String[] args) {

        String filename = args[0];
	String clientFilename = args[1];
	
        Inputs inputs = new Inputs(filename);

        BigInteger[] serverInputs = inputs.getInputs();

        BigInteger[] encryptedPoly = (BigInteger[]) read(clientFilename);
        BigInteger publicKey = (BigInteger) read("ClientPK.out");

        Paillier paillier = new Paillier();
        paillier.setPublicKey(publicKey);

        BigInteger[] encryptedPolyEval = new BigInteger[serverInputs.length];
        int j = 0;
        for (BigInteger serverInput : serverInputs) {
            encryptedPolyEval[j] = paillier.Encryption(BigInteger.ZERO);
            for (int i = encryptedPoly.length - 1; i >= 0; i--) {
                encryptedPolyEval[j] = paillier.add(encryptedPoly[i], paillier.const_mul(encryptedPolyEval[j], serverInput));
            }
            encryptedPolyEval[j] = paillier.const_mul(encryptedPolyEval[j], nextRandomBigInteger(BigInteger.valueOf(2^32)));
            encryptedPolyEval[j] = paillier.add(encryptedPolyEval[j], paillier.Encryption(serverInput));
            j++;
        }

        write(encryptedPolyEval, clientFilename+".out");
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

    public static Object read(String filename){
        ObjectInputStream ois;
        Object object = null;
        try {
            ois = new ObjectInputStream(new FileInputStream(filename));
            object = ois.readObject();
            ois.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return  object;
    }


    //This is not cryptographically secure random number.
    public static BigInteger nextRandomBigInteger(BigInteger n) {
        Random rand = new Random();
        BigInteger result = new BigInteger(n.bitLength(), rand);
        while( result.compareTo(n) >= 0 ) {
            result = new BigInteger(n.bitLength(), rand);
        }
        return result;
    }
}
